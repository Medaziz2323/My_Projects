🔍 Traffic Analysis & Intrusion Detection Module (IDS)

This module implements a passive network traffic analysis system designed to complement the Web Application Firewall (WAF).
Unlike the WAF, which actively blocks malicious requests, this component operates as an Intrusion Detection System (IDS), observing traffic in real time without interfering with it.

✨ Key Features

Passive packet capture using Scapy

TCP stream reconstruction to analyze complete HTTP requests

Signature-based detection for common web attacks:

SQL Injection

Cross-Site Scripting (XSS)

Command Injection

Machine Learning–based detection to identify obfuscated or unknown attack patterns

Behavior correlation to detect repeated or automated attack campaigns

Structured audit reporting in CSV and JSON formats

🧠 How It Works

Network packets are captured from a specified interface and filtered by IP and port.

TCP packets belonging to the same connection are grouped and reconstructed into complete data streams.

HTTP requests are extracted and analyzed using known attack signatures.

If no signature matches, a trained Machine Learning model classifies the request and assigns a confidence score.

Detected events are correlated over time to identify suspicious behavior.

All alerts are logged and exported for audit and analysis.

🤖 Artificial Intelligence Integration

The IDS integrates a supervised Machine Learning model trained on labeled HTTP requests.
Incoming requests are vectorized and classified in real time.
The trained model is serialized using pickle and dynamically loaded at runtime, enabling efficient and scalable detection.

📊 Reporting

All detections are stored in structured reports containing:

Attack type

Source and destination information

Timestamp

Severity

AI confidence score

Payload evidence

Reports are exported in CSV (Excel-compatible) and JSON formats for auditing and investigation.

🔐 Security Approach

This module illustrates a defense-in-depth strategy, combining:

Active prevention (WAF)

Passive detection (IDS)

Intelligent analysis (AI)
