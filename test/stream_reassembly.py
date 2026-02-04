from collections import defaultdict

STREAMS = defaultdict(bytes)

def get_stream_id(packet):
    ip = packet["IP"]
    tcp = packet["TCP"]
    return (ip.src, ip.dst, tcp.sport, tcp.dport)

def reassemble(packet):
    if not packet.haslayer("TCP"):
        return None

    sid = get_stream_id(packet)
    payload = bytes(packet["TCP"].payload)
    if payload:
        STREAMS[sid] += payload

    return STREAMS[sid]
