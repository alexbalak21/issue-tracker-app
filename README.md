# 🛠️ issue-tracker-app
A full‑stack Issue Tracker application for managing, assigning, and resolving issues within a team.

---

## 🚀 Tech Stack

### **Backend**
- Spring Boot  
- PostgreSQL  
- JWT Authentication  

### **Frontend**
- React  
- TypeScript  
- Tailwind CSS  

---

## 📌 Description

This application allows users to submit issues that appear in a manager or supervisor dashboard.  
Managers can assign issues to Agents (Support Engineers), track progress, and update statuses.

The system is designed to be extendable, minimal, and maintainable — with clean separation between backend services and a modern, responsive frontend.

---

## 📚 Documentation

- [Roadmap](roadmap.md)  
- [Ticket Structure](docs/ticket_structure.md)  

---

## 🧩 Ticket Model (Database Schema)

### **ticket**

| Field        | Type        | Description                     |
|--------------|-------------|---------------------------------|
| id           | int         | Primary key                     |
| title        | string      | Ticket title                    |
| body         | text        | Ticket description              |
| priority_id  | int (FK)    | Priority reference              |
| status_id    | int (FK)    | Status reference                |
| created_by   | int (FK)    | User who created the ticket     |
| assigned_to  | int (FK)    | Agent assigned                  |
| created_at   | timestamp   | Creation time                   |
| updated_at   | timestamp   | Last update                     |
| resolved_at  | timestamp   | Resolution time                 |

---

### **notes** (Internal‑Only)

| Field       | Type        | Description                               |
|-------------|-------------|-------------------------------------------|
| id          | int         | Primary key                               |
| ticket_id   | int (FK)    | Linked ticket                             |
| created_by  | int (FK)    | Agent/manager/admin who created the note  |
| updated_by  | int (FK)    | Agent/manager/admin who updated the note  |
| body        | text        | Internal note content                     |
| created_at  | timestamp   | Creation time                             |
| updated_at  | timestamp   | Last update                               |

---

### **conversation**

| Field      | Type        | Description             |
|------------|-------------|-------------------------|
| id         | int         | Primary key             |
| ticket_id  | int (FK)    | Linked ticket           |
| created_at | timestamp   | Creation time           |
| updated_at | timestamp   | Last update             |

---

### **messages**

| Field            | Type        | Description                     |
|------------------|-------------|---------------------------------|
| id               | int         | Primary key                     |
| conversation_id  | int (FK)    | Linked conversation             |
| sender_id        | int (FK)    | User or agent who sent message  |
| body             | text        | Message content                 |
| created_at       | timestamp   | Creation time                   |
| updated_at       | timestamp   | Last update                     |

---

## 🏷️ Priority Table

| Field | Type        | Description                 |
|-------|-------------|-----------------------------|
| id    | int         | Primary key                 |
| name  | string      | Priority label (e.g. Low)   |
| level | int         | Numeric weight (1–5)        |

---

## 📌 Status Table

| Field | Type        | Description                       |
|-------|-------------|-----------------------------------|
| id    | int         | Primary key                       |
| name  | string      | Status label (e.g. Open)          |
| type  | string      | Category (open, active, closed)   |

---

## 🧭 User Roles

- **User** → Can create issues  
- **Manager / Supervisor** → Can view all issues and assign them  
- **Agent (Support Engineer)** → Handles assigned issues  

---

## 🔐 Role & Permission System (RBAC)

The Issue Tracker includes a flexible **Role‑Based Access Control (RBAC)** system that allows administrators to create custom roles and assign granular permissions.

### 🧱 Core Concepts

- **Permissions** → Atomic actions such as `ticket.read`, `ticket.write`, `notes.write`, etc.  
- **Roles** → Collections of permissions (e.g., *Manager*, *Agent*, *User*).  
- **User Roles** → Users can have one or multiple roles.  
- **Role Permissions** → Admins can assign or remove permissions from any role.

This structure enables fine‑grained control over what each user can see or modify.

### 🗄️ Database Tables

| Table              | Purpose                                      |
|--------------------|----------------------------------------------|
| `permissions`      | Stores all available system permissions       |
| `roles`            | Stores admin‑defined roles                    |
| `role_permissions` | Many‑to‑many link between roles & permissions |
| `user_roles`       | Assigns roles to users                        |

### 🛠️ Examples

**Permissions**
- `ticket.read`
- `ticket.write`
- `ticket.assign`
- `notes.read`
- `notes.write`

**Role Example: Manager**
- `ticket.read`
- `ticket.write`
- `ticket.assign`
- `notes.read`
- `notes.write`

### 🔧 Enforcement

Permissions are embedded into the user’s JWT at login and validated by the backend.  
Endpoints can require specific permissions, ensuring only authorized users can perform certain actions.

### 🖥️ Admin Panel

Admins can:
- Create new roles  
- Assign permissions via a checkbox grid  
- Assign roles to users  

This makes the system fully dynamic and extendable without code changes.



## 📂 Project Structure (High‑Level)

```
issue-tracker-app/
│
├── src/main/java/app/
│   ├── controller/   # REST controllers (Auth, User, Admin, Home)
│   ├── service/      # Business logic (UserService, RoleService, PermissionService)
│   ├── repository/   # Spring Data JPA repositories (User, Role, Permission)
│   ├── security/     # JWT auth, permission checks, user details
│   ├── config/       # Security, CORS, data initialization
│   ├── model/        # Entities: User, Role, Permission, Ticket, Note, etc.
│   └── dto/          # Data Transfer Objects for API
│
├── src/main/resources/
│   └── application.properties  # Spring Boot config
│
├── sql/              # SQL scripts (e.g., create_tables.sql)
├── docs/             # Documentation (RBAC, data structures, etc.)
├── logs/             # Log output
├── env/              # Environment/config files
├── ...
```

**Backend Key Components:**
- **Controllers:** Handle API endpoints for authentication, user info, admin actions, and home.
- **Services:** Contain business logic for users, roles, permissions, etc.
- **Repositories:** Provide database access via Spring Data JPA.
- **Security:** Implements JWT authentication, permission checks, and user details.
- **Config:** Security, CORS, and data initialization settings.
- **Models:** Represent database entities (User, Role, Permission, Ticket, Note, Conversation, Message, Priority, Status, UserRole).
- **DTOs:** Used for API requests and responses.

*Frontend code is not present in this repository; the backend is the main focus.*

---

## 📦 Features

- JWT‑based authentication  
- Role‑based access  
- Issue creation & assignment  
- Priority & status management  
- Internal notes (private to staff)  
- Public conversation messages  
- Responsive UI  
- PostgreSQL relational schema  

---

## 🗄️ Database Notes

- Tickets reference users, agents, priorities, and statuses  
- Notes are **internal‑only** and never visible to end users  
- Messages belong to a conversation thread per ticket  
- Schema is clean, normalized, and scalable  

---

## 🧪 Future Enhancements

- File attachments  
- Activity logs  
- Email or in‑app notifications  
- Dark/light theme toggle  
- SLA timers  
- Audit trails  
- Advanced attachments module (multiple files, previews, secure storage)  
- Enhanced notification system (real‑time updates, WebSockets, push notifications)  
- Expanded audit logging (user actions, ticket lifecycle history, security events)  
- SLA timer engine (breach detection, escalation rules, automated reminders)  
- Manager dashboards (KPIs, ticket metrics, agent performance, workload distribution)

---
