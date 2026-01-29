# PERMISSIONS.md

## 🔐 Role‑Based Access Control — Permission Reference

This document lists all permissions used across the Issue Tracker application.  
Permissions are grouped by domain (Ticket, User, Role, etc.) and represent the atomic actions users may perform.

---

# 📌 Ticket Permissions

| Permission       | Description                         |
|------------------|-------------------------------------|
| `ticket.read`    | View tickets                        |
| `ticket.write`   | Create or update tickets            |
| `ticket.assign`  | Assign tickets to users             |
| `ticket.delete`  | Delete tickets                      |

---

# 💬 Conversation & Message Permissions

| Permission             | Description                         |
|------------------------|-------------------------------------|
| `conversation.read`    | View conversations & messages       |
| `conversation.write`   | Send messages / add conversation    |

---

# 📝 Note Permissions

| Permission     | Description                   |
|----------------|-------------------------------|
| `note.read`    | View internal notes           |
| `note.write`   | Add internal notes            |

---

# 👤 User Permissions

| Permission     | Description                         |
|----------------|-------------------------------------|
| `user.read`    | View users                          |
| `user.write`   | Create or update users              |
| `user.delete`  | Delete users                        |
| `user.manage`  | Full user management (admin-level)  |

---

# 🛡️ Role Permissions

| Permission     | Description                   |
|----------------|-------------------------------|
| `role.read`    | View roles                    |
| `role.write`   | Create or update roles        |
| `role.delete`  | Delete roles                  |

---

# 🧩 Permission Management Permissions

| Permission           | Description                         |
|----------------------|-------------------------------------|
| `permission.read`    | View permissions                    |
| `permission.write`   | Create or update permissions        |
| `permission.delete`  | Delete permissions                  |

---

# 🏛️ Admin Permissions

| Permission       | Description               |
|------------------|---------------------------|
| `admin.manage`   | Full administrative access |
