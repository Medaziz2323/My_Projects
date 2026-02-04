from collections import defaultdict
import time

HISTORY = defaultdict(list)
LAST_ALERT = {}

def correlate(finding):
    ip = finding["src_ip"]
    now = time.time()

    HISTORY[ip].append(now)
    recent = [t for t in HISTORY[ip] if now - t < 60]

    if len(recent) >= 3:
        last = LAST_ALERT.get(ip, 0)
        if now - last > 60:
            LAST_ALERT[ip] = now
            return {
                "ip": ip,
                "count": len(recent),
                "risk": "High"
            }
    return None
