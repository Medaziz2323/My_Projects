import pickle
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB

X = [
    # Normal
    "GET /login",
    "POST /login username=admin",
    "GET /index.html",
    "search=hello",
    "user profile page",

    # SQL Injection
    "admin' OR 1=1 --",
    "' UNION SELECT * FROM users --",
    "1 OR 1=1",
    "SELECT password FROM users",
    "username=' OR '1'='1",

    # XSS
    "<script>alert(1)</script>",
    "<img src=x onerror=alert(1)>",
    "javascript:alert(1)",
    "<svg onload=alert(1)>",

    # Command Injection
    "ping 127.0.0.1; ls",
    "cat /etc/passwd",
    "whoami && id",
    "8.8.8.8 | nc attacker",
]

y = [
    "normal","normal","normal","normal","normal",
    "sql_injection","sql_injection","sql_injection","sql_injection","sql_injection",
    "xss","xss","xss","xss",
    "command_injection","command_injection","command_injection","command_injection"
]


vectorizer = CountVectorizer()
X_vec = vectorizer.fit_transform(X)

model = MultinomialNB()
model.fit(X_vec, y)

with open("model.pkl", "wb") as f:
    pickle.dump((vectorizer, model), f)

print("Model trained and saved as model.pkl")
