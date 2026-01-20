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

## 🧩 Issue Model

Each issue contains the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `title` | string | Short summary of the issue |
| `description` | string | High‑level explanation |
| `body` | text | Full detailed report |
| `priority_id` | FK | Links to a priority table |
| `created_by` | FK (user_id) | User who created the issue |
| `assigned_to` | FK (agent_id) | Agent assigned to handle it |
| `created_at` | timestamp | Auto‑generated |
| `updated_at` | timestamp | Auto‑updated |

---

## 🔥 Priority / Relevance System

Priorities are stored in a dedicated table so they can be added, edited, or removed dynamically.

Default priorities:

- **Urgent** — Blocks the work process  
- **Normal** — Annoying but manageable  
- **Small** — Minor bug or inconvenience  

This design keeps the system flexible and future‑proof.

---

## 🧭 User Roles

- **User** → Can create issues  
- **Manager / Supervisor** → Can view all issues and assign them  
- **Agent (Support Engineer)** → Handles assigned issues  

---

## 📂 Project Structure (High‑Level)

issue-tracker-app/
├── backend/        # Spring Boot API
└── frontend/       # React + TS + Tailwind UI


---

## 📦 Features

- JWT‑based authentication  
- Role‑based access  
- Issue creation & assignment  
- Priority management  
- Responsive UI  
- PostgreSQL relational schema  

---

## 🗄️ Database Notes

The `priority` table allows dynamic management of relevance levels.  
Issues reference users and agents through foreign keys, ensuring clean relational integrity.

---

## 🧪 Future Enhancements

- Comment system on issues  
- File attachments  
- Activity logs  
- Email or in‑app notifications  
- Dark/light theme toggle  

---

## 📜 License

MIT License (or your preferred license)

---

## 🤝 Contributions

Pull requests are welcome.  
For major changes, open an issue to discuss what you’d like to modify.
