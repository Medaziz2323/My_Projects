#!/usr/bin/env python3
import sys
import logging
import argparse
import time
import hashlib
from scapy.all import *
from scapy.layers.inet import IP, TCP

from analyzer import analyze_payload
from reporter import record_finding, generate_reports
from stream_reassembly import reassemble
from ml_detector import predict_attack

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[
        logging.StreamHandler(sys.stdout),
        logging.FileHandler("packet_analyzer.log")
    ]
)

logger = logging.getLogger("PacketAnalyzer")
conf.verb = 0

SEEN_PAYLOADS = set()
WEB_PORT = 5000


class PacketAnalyzer:
    def __init__(self, interface, filter_exp=None):
        self.interface = interface
        self.filter_exp = filter_exp

    def start_capture(self):
        logger.info(f"Starting capture on {self.interface}")
        if self.filter_exp:
            logger.info(f"Active filter: {self.filter_exp}")

        try:
            sniff(
                iface=self.interface,
                filter=self.filter_exp,
                prn=self.process_packet,
                store=False
            )
        except KeyboardInterrupt:
            logger.info("Stopping capture...")
        finally:
            generate_reports()

    def process_packet(self, packet):
        if packet.haslayer(IP) and packet.haslayer(TCP):
            # Only analyze traffic going to the web app
            if packet[TCP].dport == WEB_PORT:
                self.analyze_http(packet)

    def analyze_http(self, packet):
        stream = reassemble(packet)
        if not stream:
            return

        # Only analyze full HTTP request
        if b"\r\n\r\n" not in stream:
            return

        try:
            full_payload = stream.decode(errors="ignore")
        except:
            return

        # Extract only request line + body (ignore headers)
        lines = full_payload.split("\r\n")
        request_line = lines[0] if lines else ""
        body = lines[-1] if len(lines) > 1 else ""
        payload = f"{request_line} {body}"

        # Deduplicate same request
        payload_hash = hashlib.md5(payload.encode()).hexdigest()
        if payload_hash in SEEN_PAYLOADS:
            return
        SEEN_PAYLOADS.add(payload_hash)

        meta = {
            "src_ip": packet[IP].src,
            "dst_ip": packet[IP].dst,
            "src_port": packet[TCP].sport,
            "dst_port": packet[TCP].dport,
            "protocol": "HTTP"
        }

        # Signature detection
        findings = analyze_payload(payload, meta)
        for f in findings:
            record_finding(f)

        # AI detection only if signature didn't trigger
        if not findings:
            ai = predict_attack(payload)
            if ai and ai["ai_label"] != "normal":
                record_finding({
                    "timestamp": time.time(),
                    "attack": f"AI:{ai['ai_label']}",
                    "severity": 7,
                    "impact": "Detected by ML model",
                    "evidence": payload,
                    "confidence": ai["confidence"],
                    **meta
                })


def main():
    parser = argparse.ArgumentParser(description="Hybrid IDS (Signature + AI)")
    parser.add_argument("-i", "--interface", required=True)
    parser.add_argument("--ip", help="IP filter")
    parser.add_argument("-f", "--filter", help="BPF filter")

    args = parser.parse_args()

    bpf = None
    if args.ip and args.filter:
        bpf = f"host {args.ip} and {args.filter}"
    elif args.ip:
        bpf = f"host {args.ip}"
    elif args.filter:
        bpf = args.filter

    PacketAnalyzer(args.interface, bpf).start_capture()


if __name__ == "__main__":
    main()
