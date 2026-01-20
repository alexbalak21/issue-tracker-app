# 🔐 Authentication System (JWT Access + Refresh Tokens)

This application uses a secure, modern JWT authentication flow based on short‑lived access tokens and long‑lived refresh tokens.  
All users are created internally by Admins or Managers — there is no public registration.

---

## 👤 User Login Flow

### 1. User sends credentials  
POST /auth/login

Request:
{
  "email": "user@example.com",
  "password": "secret123"
}

### 2. Backend validates credentials  
If valid, the server generates:
- access_token (short lifespan)
- refresh_token (long lifespan)

Response:
{
  "access_token": "jwt-access-token",
  "refresh_token": "jwt-refresh-token"
}

---

## 🔁 Refresh Token Flow

POST /auth/refresh-token

Request:
{
  "refresh_token": "jwt-refresh-token"
}

Response:
{
  "access_token": "new-access-token"
}

---

## 🧱 Token Lifetimes

| Token Type     | Lifetime     | Purpose                  |
|----------------|--------------|--------------------------|
| Access Token   | 10–20 min    | Auth for API requests    |
| Refresh Token  | 7–30 days    | Silent re‑authentication |

---

## 🛡️ Storage Strategy

### Access Token  
- Stored in memory (React state)

### Refresh Token  
- HTTP‑only secure cookie (recommended)

---

## 🧩 Authentication Endpoints

### POST /auth/login  
Validates credentials and returns tokens.

### POST /auth/refresh-token  
Validates refresh token and returns new access token.

### POST /auth/logout (optional)  
Invalidates refresh token if stored server‑side.

---

## 🔐 Password Handling

- Passwords are hashed (BCrypt recommended)
- Never stored in plain text
- Admins/Managers can reset passwords

---

## 👥 Who Can Create Users?

| Role     | Can Create Users | Notes                          |
|----------|------------------|--------------------------------|
| Admin    | Yes              | Full access                    |
| Manager  | Yes              | Can create Users and Agents    |
| Agent    | No               | Cannot create accounts         |
| User     | No               | Cannot create accounts         |

---

## 🧭 Why This System?

- Stateless  
- Secure  
- Works with React + Spring Boot  
- No session storage required  
- Refresh tokens keep users logged in silently

