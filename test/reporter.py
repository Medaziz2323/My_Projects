import json
import csv
import logging
import os
from datetime import datetime
from correlator import correlate

logger = logging.getLogger("PacketAnalyzer")
FINDINGS = []

BASE = os.path.dirname(os.path.abspath(__file__))
REPORTS = os.path.join(BASE, "reports")
os.makedirs(REPORTS, exist_ok=True)

JSON_FILE = os.path.join(REPORTS, "audit_report.json")
CSV_FILE = os.path.join(REPORTS, "audit_report.csv")

def record_finding(finding):
    FINDINGS.append(finding)

    logger.warning(
        f"[ALERT] {finding['attack']} | "
        f"{finding['src_ip']} → {finding['dst_ip']}:{finding['dst_port']} | "
        f"Evidence: {finding.get('evidence','')}"
    )

    esc = correlate(finding)
    if esc:
        logger.warning(
            f"[ESCALATION] Attack campaign from {esc['ip']} "
            f"({esc['count']} events)"
        )

def generate_json_report():
    with open(JSON_FILE, "w") as f:
        json.dump({
            "generated_at": datetime.utcnow().isoformat(),
            "events": FINDINGS
        }, f, indent=4)

def generate_csv_report():
    if not FINDINGS:
        return

    headers = [
        "Date","Time","Attack","Severity","AI Confidence",
        "Impact","Evidence","Source IP","Source Port",
        "Destination IP","Destination Port","Protocol"
    ]

    with open(CSV_FILE, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(headers)

        for e in FINDINGS:
            t = datetime.fromtimestamp(e["timestamp"])
            w.writerow([
                t.date(), t.time(),
                e.get("attack",""),
                e.get("severity",""),
                e.get("confidence",""),
                e.get("impact",""),
                e.get("evidence",""),
                e.get("src_ip",""),
                e.get("src_port",""),
                e.get("dst_ip",""),
                e.get("dst_port",""),
                e.get("protocol","")
            ])

def generate_reports():
    generate_json_report()
    generate_csv_report()
