# 🎟️ Ticket Data Structure

---

## 🧩 Ticket Table

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

## 📝 Notes (Internal Only)

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

## 💬 Conversation Table

| Field      | Type        | Description             |
|------------|-------------|-------------------------|
| id         | int         | Primary key             |
| ticket_id  | int (FK)    | Linked ticket           |
| created_at | timestamp   | Creation time           |
| updated_at | timestamp   | Last update             |

---

## 💬 Messages Table

| Field            | Type        | Description                     |
|------------------|-------------|---------------------------------|
| id               | int         | Primary key                     |
| conversation_id  | int (FK)    | Linked conversation             |
| sender_id        | int (FK)    | User or agent who sent message  |
| body             | text        | Message content                 |
| created_at       | timestamp   | Creation time                   |
| updated_at       | timestamp   | Last update                     |
