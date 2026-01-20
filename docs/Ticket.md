# 🎟️ Recommended Ticket Table Fields

## 🧩 Core Fields
Essential fields for any ticketing system:

- **id** — Primary key  
- **title** — Short summary  
- **description** — High‑level explanation  
- **body** — Full detailed report   
- **priority_id** — FK → priority table  
- **status_id** — FK → status table  
- **category_id** — FK → category table (optional but very useful)  

---

## 👤 User & Assignment Fields
Track who created and who handles the ticket:

- **created_by** — FK → users table  
- **assigned_to** — FK → agents/support engineers  
- **assigned_by** — FK → manager/supervisor (optional but powerful)  

---

## 🕒 Timestamps
Standard for auditing and sorting:

- **created_at**  
- **updated_at**  
- **resolved_at** — When the ticket was actually solved  
- **closed_at** — When the ticket was officially closed  

---

## 📊 Support‑Specific Fields
Common in real support systems:

- **severity** — Optional if separated from priority  
- **impact** — Low / Medium / High (optional)  
- **source** — Web, mobile, email, internal, etc.  
- **environment** — Browser, OS, device (optional but helpful)  
- **attachments** — Could be a separate table (`ticket_attachments`)  

---

## 🧭 Workflow Fields
Useful for tracking and automation:

- **sla_due_at** — Deadline based on SLA  
- **first_response_at** — When an agent first responded  
- **last_activity_at** — For sorting active tickets  
- **is_escalated** — Boolean  
- **escalation_level** — Tier 1 → Tier 2 → Tier 3  

---

## 📝 Internal Notes
Usually stored in a separate table:

### `ticket_notes` table
- **id**  
- **ticket_id**  
- **author_id**  
- **note_body**  
- **created_at**  

---

## 🧱 Minimal Version (Lean Schema)
A minimalist, clean schema:

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


This is enough for a fully functional issue tracker.

---

## 🧩 Optional Enhancements
For future scalability:

- Tags system  
- Activity log  
- Audit trail  
- Ticket history (status changes)  
- Custom fields (dynamic schema)  
