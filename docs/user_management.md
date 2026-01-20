## 👤 User Creation & Management

In this system, **end‑users do not register themselves**.  
All accounts are created and managed internally by **Admins** or **Managers** to maintain full control over roles, permissions, and system access.

This approach is ideal for internal company environments where only authorized staff should exist in the system.

---

### 🧩 User Table

| Field       | Type        | Description                          |
|-------------|-------------|--------------------------------------|
| id          | int         | Primary key                          |
| name        | string      | Full name of the user                |
| email       | string      | Unique login identifier              |
| password    | string      | Hashed password                      |
| role        | string      | user, agent, manager, admin          |
| created_at  | timestamp   | Account creation time                |
| updated_at  | timestamp   | Last update                          |

---

### 🛠️ Who Can Create Users?

| Role     | Can Create Users | Notes                                      |
|----------|------------------|--------------------------------------------|
| Admin    | ✅ Yes           | Full access to all user management         |
| Manager  | ✅ Yes           | Can create Users and Agents                |
| Agent    | ❌ No            | Cannot create or modify accounts           |
| User     | ❌ No            | Cannot create or modify accounts           |

---

### 📝 User Creation Workflow

1. **Admin/Manager opens the User Management panel**  
2. **Creates a new user** by entering:
   - Name  
   - Email  
   - Role (user, agent, manager, admin)  
   - Temporary password  
3. The system stores the password **hashed**  
4. The new user logs in and updates their password  
5. Admin/Manager can later:
   - Update roles  
   - Disable accounts  
   - Reset passwords  

---

### 🎯 Why No Public Registration?

- Prevents unauthorized access  
- Eliminates spam or fake accounts  
- Ensures only verified staff enter the system  
- Simplifies backend logic (no signup, no email verification)  
- Matches real internal ticketing systems (Jira, ServiceNow, Freshdesk internal mode)

---

### 🔐 Recommended Roles

| Role     | Purpose                                  |
|----------|-------------------------------------------|
| user     | Creates tickets                           |
| agent    | Handles assigned tickets                  |
| manager  | Oversees agents, assigns tickets, creates users |
| admin    | Full system control                       |

---