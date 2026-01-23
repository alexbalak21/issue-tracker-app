# 📌 Core Requirements

This document defines the essential functional and non‑functional requirements for the Issue Tracker application.  
It serves as the foundation for architecture, development, and future enhancements.

---

## 1. Purpose of the Application

- Provide an internal issue‑tracking system for teams.
- Allow Users to submit tickets describing problems or requests.
- Allow Agents to handle assigned tickets.
- Allow Managers and Admins to oversee operations, assign tickets, and create users.
- Maintain a clean, scalable, and secure backend with a modern frontend.

---

## 2. User Roles & Permissions

### 2.1 Roles
- **Admin**
- **Manager**
- **Agent**
- **User**

### 2.2 Permissions Overview

| Action                     | User | Agent | Manager | Admin |
|---------------------------|------|-------|---------|-------|
| Create tickets            | ✔️   | ✔️    | ✔️      | ✔️    |
| View assigned tickets     | ✔️   | ✔️    | ✔️      | ✔️    |
| Assign tickets            | ❌   | ❌    | ✔️      | ✔️    |
| Create users              | ❌   | ❌    | ✔️      | ✔️    |
| Add internal notes        | ❌   | ✔️    | ✔️      | ✔️    |
| Participate in messages   | ✔️   | ✔️    | ✔️      | ✔️    |
| Manage priorities/status  | ❌   | ❌    | ✔️      | ✔️    |

---

## 3. Core Features

### 3.1 Authentication
- JWT‑based authentication
- Access token (short‑lived)
- Refresh token (long‑lived)
- Login route
- Refresh‑token route
- No public registration (Admins/Managers create users)

### 3.2 Ticket Management
- Create, update, assign, and resolve tickets
- Track priority and status
- Track timestamps (created, updated, resolved)

### 3.3 Notes (Internal Only)
- Private notes visible only to staff (Agent/Manager/Admin)
- Linked to tickets

### 3.4 Conversations & Messages
- Public conversation thread per ticket
- Messages between user and support staff

### 3.5 Priority & Status System
- Configurable priority levels
- Configurable status categories (open, active, closed)

---

## 4. Database Requirements

### 4.1 Core Tables
- users  
- tickets  
- notes  
- conversations  
- messages  
- priority  
- status  

### 4.2 Constraints
- Foreign keys for all relationships
- Timestamps for all entities
- Passwords stored hashed (BCrypt)

---

## 5. Frontend Requirements

- React + TypeScript
- Tailwind CSS for UI
- Login page
- Dashboard for tickets
- Ticket detail view
- Notes panel (staff only)
- Conversation/messages panel
- User management panel (Admin/Manager)
- Token refresh handling via Axios interceptor

---

## 6. Backend Requirements

- Spring Boot REST API
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Modular package structure (auth, users, tickets, notes, etc.)
- Global exception handling
- Validation on all DTOs

---

## 7. Non‑Functional Requirements

### 7.1 Security
- JWT authentication
- Role‑based access control
- Hashed passwords
- Input validation

### 7.2 Scalability
- Stateless backend
- Efficient relational schema
- Clear separation of concerns

### 7.3 Maintainability
- Clean architecture
- Modular code organization
- Documentation for all modules

### 7.4 Performance
- Fast API responses
- Optimized queries
- Pagination for ticket lists

---

## 8. Future Enhancements

- File attachments  
- Activity logs  
- Email or in‑app notifications  
- Dark/light theme toggle  
- SLA timers  
- Audit trails  
- Advanced attachments module  
- Enhanced notification system  
- Expanded audit logging  
- SLA engine with escalation rules  
- Manager dashboards (KPIs, metrics, workload)

