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

## 📂 Project Structure (High‑Level)

issue-tracker-app/
backend/        # Spring Boot API
frontend/       # React + TS + Tailwind UI


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

---
