import time
from signatures import SIGNATURES

def analyze_payload(payload, meta):
    findings = []
    data = payload.lower()

    for attack, sig in SIGNATURES.items():
        for pattern in sig["patterns"]:
            if pattern in data:
                findings.append({
                    "timestamp": time.time(),
                    "attack": attack,
                    "severity": sig["severity"],
                    "impact": sig["impact"],
                    "evidence": pattern,
                    **meta
                })
    return findings
