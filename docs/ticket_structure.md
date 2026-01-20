# 🎟️ Ticket Structure

A minimal, scalable ticket model designed for clean support workflows.

---

## 🧩 Ticket Table Schema

ticket
 ├── id
 ├── title
 ├── body
 ├── priority_id
 ├── status_id
 ├── created_by
 ├── assigned_to
 ├── created_at
 ├── updated_at
 └── resolved_at


The ticket table remains intentionally minimal.  
Notes and messages are stored separately to keep the core schema clean.

---

## 📝 Notes (Internal-Only)

Internal notes are visible **only** to agents, managers, and admins.  
Users never see these notes.

### Notes Table Schema

notes
 ├── id
 ├── ticket_id        ← FK → ticket.id
 ├── created_by       ← FK → users.id
 ├── updated_by       ← FK → users.id
 ├── body
 ├── created_at
 └── updated_at

### Notes Behavior

- A ticket can have **multiple notes**  
- Notes are **internal-only** (agent/manager/admin)  
- Perfect for:
  - internal comments  
  - troubleshooting steps  
  - escalation notes  
  - sensitive information  

---

## 💬 Conversations & Messages (User ↔ Agent)

For public communication between the user and the assigned agent:

### Conversation Table

conversation
├── id
├── ticket_id
├── created_at
└── updated_at


### Messages Table

messages
├── id
├── conversation_id
├── sender_id
├── body
├── created_at
└── updated_at


Messages are visible to both the user and the agent.  
Notes remain private.

---

## 🧱 Why This Structure Works

- Ticket table stays minimal and clean  
- Notes and messages are separated by purpose  
- Supports unlimited notes and messages  
- Matches real support systems (Zendesk, Jira Service Desk, Freshdesk)  
- Easy to extend with:
  - attachments  
  - internal-only flags  
  - audit logs  
  - role-based visibility  

---

If you want, I can also generate:

- SQL schema (PostgreSQL)  
- Spring Boot entities with relationships  
- TypeScript interfaces  
- A Mermaid ERD diagram  
