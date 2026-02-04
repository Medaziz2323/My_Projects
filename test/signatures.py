SIGNATURES = {
    "SQL Injection": {
        "patterns": ["or 1=1", "union select", "'--", "select * from"],
        "severity": 8,
        "impact": "Authentication bypass / data leakage"
    },
    "XSS": {
        "patterns": ["<script>", "javascript:", "alert("],
        "severity": 6,
        "impact": "Client-side code execution"
    },
    "Command Injection": {
        "patterns": [
            "&&", "|", "`",
            "ping ", "cat ", "whoami", "ls ", "nc ", "bash"
        ],
        "severity": 9,
        "impact": "Remote command execution"
    }
}
