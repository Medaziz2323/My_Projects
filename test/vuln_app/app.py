from flask import Flask, request, render_template_string, session
import sqlite3

app = Flask(__name__)
app.secret_key = 'supersecret'  # Insecure, but for demo sessions

DB_NAME = "users.db"

def init_db():
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute("""
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE,
            password TEXT
        )
    """)
    c.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('admin', 'admin123')")
    c.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('Aziz', 'aziz123')")
    conn.commit()
    conn.close()

init_db()

# Ultra-pretty index template
INDEX_TEMPLATE = '''
<!DOCTYPE html>
<html lang="en"> 
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vulnerable Login Lab</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { 
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab); 
            background-size: 400% 400%; animation: gradientShift 15s ease infinite; 
            min-height: 100vh; display: flex; align-items: center; justify-content: center; 
        }
        @keyframes gradientShift { 0%{background-position:0% 50%} 50%{background-position:100% 50%} 100%{background-position:0% 50%} }
        .glass-card { 
            background: rgba(255,255,255,0.1); backdrop-filter: blur(20px); border-radius: 20px; 
            box-shadow: 0 25px 45px rgba(0,0,0,0.1); border: 1px solid rgba(255,255,255,0.2); 
            transition: all 0.3s ease; 
        }
        .glass-card:hover { transform: translateY(-10px); box-shadow: 0 35px 60px rgba(0,0,0,0.2); }
        .form-control { background: rgba(255,255,255,0.9); border-radius: 12px; border: none; }
        .form-control:focus { background: rgba(255,255,255,1); box-shadow: 0 0 0 0.2rem rgba(102,126,234,0.25); }
        .btn-login { 
            background: linear-gradient(45deg, #667eea, #764ba2); border: none; border-radius: 12px; 
            font-weight: 600; padding: 12px; transition: all 0.3s; 
        }
        .btn-login:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(102,126,234,0.4); }
        .floating { animation: float 3s ease-in-out infinite; }
        @keyframes float { 0%,100%{transform:translateY(0);} 50%{transform:translateY(-10px);} }
        .users-link { color: #fff; text-decoration: none; opacity: 0.9; }
        .users-link:hover { opacity: 1; text-shadow: 0 0 10px rgba(255,255,255,0.5); }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 col-sm-8">
                <div class="glass-card p-5 floating text-center">
                    <div class="mb-4">
                        <i class="bi bi-shield-lock-fill text-white" style="font-size: 4rem; opacity: 0.8;"></i>
                        <h2 class="text-white mt-3 mb-4 fw-bold">Login</h2>
                    </div>
                    <form method="POST" action="/login">
                        <div class="mb-4">
                            <input name="username" type="text" class="form-control form-control-lg" placeholder="Username" required>
                        </div>
                        <div class="mb-4">
                            <input name="password" type="password" class="form-control form-control-lg" placeholder="Password" required>
                        </div>
                        <button type="submit" class="btn btn-login btn-lg w-100 text-white">
                            <i class="bi bi-box-arrow-in-right me-2"></i>Sign In
                        </button>
                    </form>
                  
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
'''

ACCESS_DENIED_TEMPLATE = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { 
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab); 
            background-size: 400% 400%; animation: gradientShift 15s ease infinite; 
            min-height: 100vh; display: flex; align-items: center; justify-content: center; 
        }
        @keyframes gradientShift { 0%{background-position:0% 50%} 50%{background-position:100% 50%} 100%{background-position:0% 50%} }
        .glass-card { 
            background: rgba(255,255,255,0.1); backdrop-filter: blur(20px); border-radius: 20px; 
            box-shadow: 0 25px 45px rgba(0,0,0,0.1); border: 1px solid rgba(255,255,255,0.2); 
            transition: all 0.3s ease; max-width: 450px; width: 100%; 
        }
        .glass-card:hover { transform: translateY(-5px); box-shadow: 0 35px 60px rgba(0,0,0,0.2); }
        .btn-back { 
            background: linear-gradient(45deg, #ff6b6b, #ee5a52); border: none; border-radius: 12px; 
            font-weight: 600; padding: 12px 30px; transition: all 0.3s; 
        }
        .btn-back:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(255,107,107,0.4); }
        .floating { animation: float 3s ease-in-out infinite; }
        @keyframes float { 0%,100%{transform:translateY(0);} 50%{transform:translateY(-10px);} }
        .lock-icon { font-size: 6rem; opacity: 0.8; animation: pulse 2s infinite; }
        @keyframes pulse { 0%,100%{transform:scale(1);} 50%{transform:scale(1.05);} }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-sm-8">
                <div class="glass-card p-5 text-center floating">
                    <div class="mb-4">
                        <i class="bi bi-shield-lock text-warning lock-icon"></i>
                        <h2 class="text-white mt-4 mb-3 fw-bold">🔒 Access Denied</h2>
                        <p class="text-white-50 lead mb-1">Admin privileges required</p>
                        <p class="text-white-75">Only administrators can view user data</p>
                    </div>
                   
                    <a href="/" class="btn btn-back text-white btn-lg w-100 mb-3">
                        <i class="bi bi-arrow-left me-2"></i>Back to Login
                    </a>

                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
'''

@app.route("/")
def index():
    return render_template_string(INDEX_TEMPLATE)

@app.route("/login", methods=["POST"])
def login():
    username = request.form.get("username", "")
    password = request.form.get("password", "")

    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    query = f"SELECT * FROM users WHERE username = '{username}' AND password = '{password}'"
    print("DEBUG QUERY:", query)
    c.execute(query)
    user = c.fetchone()
    conn.close()

    # XSS vuln: Unsanitized input
    if user:
        is_admin = (user[1] == 'admin')
        session['logged_in'] = True
        session['username'] = username
        session['is_admin'] = is_admin
        welcome_msg = f"<div class='alert alert-success shadow'><h3>✅ Welcome, <strong>{username}</strong>!</h3><p>You are logged in successfully. ID: {user[0]}</p>"
        if is_admin:
            welcome_msg += "<p class='mt-3'><i class='bi bi-star-fill text-warning'></i> Admin access granted! <a href='/users' class='btn btn-warning mt-2'>View Users</a></p>"
        else:
            welcome_msg += "<p class='mt-3 text-muted'>Regular user</p>"
        welcome_msg += "</div>"
    else:
        welcome_msg = f"<div class='alert alert-danger shadow'><h3>❌ Login failed</h3><p>Tried: <strong>{username}</strong></p><p>Invalid credentials.</p></div>"

    return f"""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Login Result</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    </head>
    <body class="bg-light d-flex align-items-center justify-content-center min-vh-100 p-3">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow-lg border-0 glass-card p-5 text-center">
                        {welcome_msg}
                        <div class="mt-4">
                            <a href="/" class="btn btn-primary me-2"><i class="bi bi-arrow-left"></i> Back</a>
                            <a href="/users" class="btn btn-outline-secondary">Users</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    </html>
    """


@app.route("/users")
def users():
    if not session.get('logged_in') or not session.get('is_admin'):
        return render_template_string(ACCESS_DENIED_TEMPLATE)

    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute("SELECT id, username, password FROM users")
    rows = c.fetchall()
    conn.close()

    return render_template_string(USERS_TABLE_TEMPLATE, users=rows)

USERS_TABLE_TEMPLATE = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { 
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab); 
            background-size: 400% 400%; animation: gradientShift 15s ease infinite; 
            min-height: 100vh; padding: 2rem 0;
        }
        @keyframes gradientShift { 0%{background-position:0% 50%} 50%{background-position:100% 50%} 100%{background-position:0% 50%} }
        .glass-card { 
            background: rgba(255,255,255,0.15); backdrop-filter: blur(20px); 
            border-radius: 20px; border: 1px solid rgba(255,255,255,0.3);
            box-shadow: 0 25px 45px rgba(0,0,0,0.1);
        }
        .table-transparent {
            background: rgba(255,255,255,0.1) !important;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255,255,255,0.2);
            color: white;
        }
        .table-transparent th {
            background: rgba(255,255,255,0.2) !important;
            color: white;
            border: none;
            font-weight: 600;
            text-shadow: 0 1px 2px rgba(0,0,0,0.3);
        }
        .table-transparent td {
            background: transparent;
            color: white;
            border-color: rgba(255,255,255,0.1);
        }
        .table-transparent tr:hover {
            background: rgba(255,255,255,0.2) !important;
            backdrop-filter: blur(5px);
        }
        .table-transparent code {
            background: rgba(0,0,0,0.3);
            color: #ffeb3b;
            padding: 0.25rem 0.5rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="glass-card p-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="text-white mb-0"><i class="bi bi-people me-2"></i>All Users</h2>
                <a href="/" class="btn btn-light">← Back</a>
            </div>
            
            <div class="table-responsive">
                <table class="table table-transparent table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Password</th>
                        </tr>
                    </thead>
                    <tbody>
                        {% for user in users %}
                        <tr>
                            <td><strong>{{ user[0] }}</strong></td>
                            <td>{{ user[1] }}</td>
                            <td><code>{{ user[2] }}</code></td>
                        </tr>
                        {% endfor %}
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
'''



USERS_TEMPLATE = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { 
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab); 
            background-size: 400% 400%; animation: gradientShift 15s ease infinite; 
            min-height: 100vh; padding: 2rem 0;
        }
        @keyframes gradientShift { 0%{background-position:0% 50%} 50%{background-position:100% 50%} 100%{background-position:0% 50%} }
        
        .glass-header { 
            background: rgba(255,255,255,0.15); backdrop-filter: blur(20px); 
            border-radius: 20px; border: 1px solid rgba(255,255,255,0.2);
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
        }
        
        .user-card { 
            background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); 
            border-radius: 16px; border: none; transition: all 0.3s ease;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            height: 100%; overflow: hidden;
        }
        .user-card:hover { 
            transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .user-header { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
            color: white; padding: 1.5rem; 
        }
        .admin-badge { 
            background: linear-gradient(45deg, #ffd700, #ffed4a); 
            color: #333; padding: 0.25rem 0.75rem; border-radius: 20px;
            font-size: 0.8rem; font-weight: 700; box-shadow: 0 2px 10px rgba(255,215,0,0.4);
        }
        .password-code { 
            background: #f8f9fa; border-radius: 8px; padding: 0.5rem 1rem; 
            font-family: 'Courier New', monospace; font-size: 0.9rem;
            border-left: 4px solid #667eea;
        }
        .floating { animation: float 3s ease-in-out infinite; }
        @keyframes float { 0%,100%{transform:translateY(0);} 50%{transform:translateY(-5px);} }
        .btn-home { 
            background: linear-gradient(45deg, #4ecdc4, #44a08d); border: none; 
            border-radius: 12px; font-weight: 600; padding: 1rem 2rem;
        }
        .btn-home:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(78,205,196,0.4); }
        .stats-card {
            background: rgba(255,255,255,0.2); backdrop-filter: blur(15px);
            border-radius: 16px; border: 1px solid rgba(255,255,255,0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="glass-header p-4 mb-5 text-center floating">
            <h1 class="text-white mb-2 fw-bold">
                <i class="bi bi-people-fill me-3"></i>Admin Dashboard
            </h1>
            <p class="text-white-50 mb-0 lead">User Management Panel ({{ users|length }} users)</p>
        </div>

        <!-- Stats Cards -->
        <div class="row g-4 mb-5">
            <div class="col-md-3 col-sm-6">
                <div class="stats-card p-4 text-center text-white">
                    <i class="bi bi-people display-4 opacity-75"></i>
                    <h3 class="fw-bold mt-2">{{ users|length }}</h3>
                    <p class="mb-0">Total Users</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="stats-card p-4 text-center text-white">
                    <i class="bi bi-person-check-fill display-4 opacity-75 text-success"></i>
                    <h3 class="fw-bold mt-2">1</h3>
                    <p class="mb-0">Admins</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="stats-card p-4 text-center text-white">
                    <i class="bi bi-shield-exclamation display-4 opacity-75 text-warning"></i>
                    <h3 class="fw-bold mt-2">2</h3>
                    <p class="mb-0">Vulnerable</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="stats-card p-4 text-center text-white">
                    <i class="bi bi-bug display-4 opacity-75 text-danger"></i>
                    <h3 class="fw-bold mt-2">4</h3>
                    <p class="mb-0">Vulns</p>
                </div>
            </div>
        </div>

        <!-- Users Grid -->
        <div class="row g-4">
            {% for user in users %}
            <div class="col-xl-4 col-lg-6 col-md-6">
                <div class="user-card h-100">
                    <div class="user-header">
                        <div class="d-flex justify-content-between align-items-center">
                            <h4 class="mb-0 fw-bold">{{ user[1] }}</h4>
                            {% if user[1] == 'admin' %}
                                <span class="admin-badge">ADMIN</span>
                            {% endif %}
                        </div>
                        <small class="opacity-75">ID: {{ user[0] }}</small>
                    </div>
                    <div class="card-body p-4">
                        <div class="password-code mb-3">
                            <i class="bi bi-key me-2"></i>Password: {{ user[2] }}
                        </div>
                        <div class="d-flex gap-2">
                            <span class="badge bg-primary">SQLi Vulnerable</span>
                            <span class="badge bg-danger">XSS Vulnerable</span>
                        </div>
                    </div>
                </div>
            </div>
            {% endfor %}
        </div>

        <!-- Footer Button -->
        <div class="text-center mt-5">
            <a href="/" class="btn btn-home text-white btn-lg px-5">
                <i class="bi bi-house-door me-2"></i>Back to Login
            </a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
'''

if __name__ == "__main__":
    app.run(debug=True)
