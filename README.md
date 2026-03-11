# Support Portal API

A full-stack Issue Tracker application for managing, assigning, and resolving issues within a team. Built with a Spring Boot REST API backend and a React/TypeScript frontend served as static assets.

## Frontend Repository

This backend powers a modern **React / TypeScript + Tailwind CSS** frontend.  
You can find the frontend source code here:

🔗 **https://github.com/alexbalak21/Suppurt-Portal**

---

**Version:** 1.1 | **Docker Image:** `alexbalak/issue-tracker:latest` | **Default Port:** `8100`

---

## Tech Stack

### Backend
| | |
|---|---|
| **Runtime** | Java 21 |
| **Framework** | Spring Boot 3.5.10 |
| **Security** | Spring Security + JWT (jjwt 0.11.5) |
| **Persistence** | Spring Data JPA + Hibernate |
| **Database** | PostgreSQL |
| **Validation** | Jakarta Bean Validation |
| **Build** | Maven |

### Frontend
| | |
|---|---|
| **Framework** | React |
| **Language** | TypeScript |
| **Styling** | Tailwind CSS |

### Infrastructure
| | |
|---|---|
| **Container** | Docker (eclipse-temurin:21-jre-alpine) |

---

## Description

Users submit tickets that appear in a manager/supervisor dashboard. Managers assign tickets to Support Agents, who handle and resolve them. All access is controlled by a dynamic **Role-Based Access Control (RBAC)** system — permissions are embedded directly in the JWT so every request is validated without a database round-trip.

---

## Running the App

### Docker (recommended)

```bash
docker pull alexbalak/issue-tracker:latest

docker run -p 8100:8100 \
  -e DATABASE_URL=jdbc:postgresql://host:5432/issuedb \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=secret \
  -e JWT_SECRET=your_access_secret \
  -e JWT_REFRESH_SECRET=your_refresh_secret \
  alexbalak/issue-tracker:latest
```

### Build from Source

```bash
./mvnw clean package -DskipTests
java -jar target/issue-tracker-*.jar
```

### Environment Variables

| Variable | Default | Description |
|---|---|---|
| `PORT` | `8100` | Server port |
| `DATABASE_URL` | *(required)* | JDBC connection URL |
| `DATABASE_USERNAME` | *(required)* | DB username |
| `DATABASE_PASSWORD` | *(required)* | DB password |
| `JWT_SECRET` | *(required)* | Access token signing secret |
| `JWT_REFRESH_SECRET` | *(required)* | Refresh token signing secret |
| `JWT_EXPIRATION` | `900000` | Access token TTL in ms (15 min) |
| `REFRESH_EXPIRATION` | `604800000` | Refresh token TTL in ms (7 days) |
| `ALLOWED_ORIGINS` | `http://localhost:8100` | CORS allowed origin |
| `SPRING_PROFILES_ACTIVE` | `prod` | Active Spring profile |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Hibernate DDL mode |

---

## API Reference

All endpoints are prefixed with `/api`. Protected endpoints require `Authorization: Bearer <access_token>`.

### Authentication — `/api/auth`

| Method | Path | Permission | Description |
|---|---|---|---|
| POST | `/auth/login` | Public | Returns `access_token`, `refresh_token`, and user info |
| POST | `/auth/register` | Public | Register a new user |
| POST | `/auth/refresh` | Public | Exchange refresh token for a new access token |
| GET | `/auth/me` | Authenticated | Current user info from JWT |

**Login response shape:**
```json
{
  "message": "Login successful",
  "data": {
    "user": { "id": 1, "name": "Alice", "email": "alice@example.com", "permissions": ["ticket.read"] },
    "access_token": "eyJ...",
    "refresh_token": "eyJ..."
  }
}
```

---

### Tickets — `/api/tickets`

| Method | Path | Permission | Ownership | Description |
|---|---|---|---|---|
| GET | `/tickets` | `ticket.read` | ALL_OR_SELF | List all tickets (staff sees all; users see own) |
| GET | `/tickets/{id}` | `ticket.read` | ALL_OR_SELF | Ticket details |
| GET | `/tickets/assigned/me` | `ticket.read` | SELF | Tickets assigned to current user |
| GET | `/tickets/assigned/{userId}` | `ticket.read` | ALL_OR_SELF | Tickets assigned to a user |
| GET | `/tickets/status/{statusId}` | `ticket.read` | ALL_OR_SELF | Filter by status |
| GET | `/tickets/priority/{priorityId}` | `ticket.read` | ALL_OR_SELF | Filter by priority |
| GET | `/tickets/search?keyword=` | `ticket.read` | ALL_OR_SELF | Search by title keyword |
| POST | `/tickets` | `ticket.create` | — | Create a ticket |
| POST | `/tickets/create-for-user` | `ticket.manage` | — | Create a ticket on behalf of a user |
| PUT | `/tickets/{id}` | `ticket.write` | SELF | Full ticket update |
| PATCH | `/tickets/{id}` | `ticket.write` | SELF | Update priority |
| PATCH | `/tickets/{id}/body` | `ticket.write` | SELF | Update body only |
| PATCH | `/tickets/{id}/status` | `ticket.write` | SELF | Update status |
| PATCH | `/tickets/{id}/assign` | `ticket.assign` | — | Assign ticket to a user |

---

### Notes (Internal) — `/api/notes`

Notes are **staff-only** and never exposed to end users.

| Method | Path | Permission | Description |
|---|---|---|---|
| GET | `/notes/{ticketId}` | `note.read` | List notes for a ticket |
| POST | `/notes` | `note.create` | Add a note |
| PUT | `/notes/{noteId}` | `note.create` or `note.manage` | Update a note |
| DELETE | `/notes/{noteId}` | `note.create` or `note.manage` | Delete a note |

---

### Conversation Messages — `/api/tickets/{ticketId}/messages`

| Method | Path | Permission | Ownership | Description |
|---|---|---|---|---|
| GET | `/{ticketId}/messages` | `conversation.read` | ALL_OR_SELF | Get all messages for a ticket |
| POST | `/{ticketId}/messages` | `conversation.reply` | ALL_OR_SELF | Post a message |

---

### Users — `/api/users`

| Method | Path | Permission | Description |
|---|---|---|---|
| GET | `/users` | `user.read` | List all users (id + name). Filter by `?role={roleId}` |
| GET | `/users/me` | `user.read` | Current user's full profile including profile image |
| PATCH | `/users/me` | `user.update.self` | Update own name/email |
| POST | `/users/profile-image` | Authenticated | Upload profile image (multipart/form-data) |
| GET | `/users/{userId}/profile-image` | Authenticated | Get a user's profile image |
| DELETE | `/users/profile-image` | Authenticated | Delete own profile image |

---

### Admin — `/api/admin` *(requires `admin.manage`)*

**Users**

| Method | Path | Description |
|---|---|---|
| GET | `/admin/users` | List all users. Filter by `?role={roleId}` |
| GET | `/admin/users/{userId}` | Get user detail |
| POST | `/admin/users` | Create a user with roles |
| POST | `/admin/users/{userId}/roles` | Assign roles to a user |
| DELETE | `/admin/users/{userId}/roles` | Remove roles from a user |

**Roles**

| Method | Path | Description |
|---|---|---|
| GET | `/admin/roles` | List all roles |
| GET | `/admin/roles/{roleId}` | Get role detail |
| POST | `/admin/roles` | Create a role with permissions |
| PUT | `/admin/roles/{roleId}` | Update a role |
| PATCH | `/admin/roles/{roleId}/permissions` | Add permissions to a role |
| DELETE | `/admin/roles/{roleId}/permissions` | Remove permissions from a role |

**Permissions**

| Method | Path | Description |
|---|---|---|
| GET | `/admin/permissions` | List all permissions |
| POST | `/admin/permissions` | Create a permission |

---

## RBAC & Security

The permissions system is fully dynamic — no code changes are needed to introduce new roles or permissions.

- **Permissions** are stored in the database and fetched at login.
- **Permissions are embedded in the JWT** at login time (`permissions` claim), so every request is validated without a DB call.
- Endpoints use the `@RequiresPermission("permission.name")` annotation enforced by an AOP aspect (`PermissionAspect`).
- Ownership is enforced via `@Ownership(OwnershipType.SELF | ALL_OR_SELF)`, restricting regular users to their own resources.
- Token types: **access token** (default 15 min) + **refresh token** (7 days).

### Built-in Permissions

| Permission | Description |
|---|---|
| `ticket.read` | Read tickets |
| `ticket.create` | Create tickets |
| `ticket.write` | Update tickets (status, priority, body) |
| `ticket.assign` | Assign tickets to users |
| `ticket.manage` | Create tickets on behalf of others |
| `note.read` | Read internal notes |
| `note.create` | Create/edit own notes |
| `note.manage` | Manage all notes |
| `conversation.read` | Read ticket messages |
| `conversation.reply` | Post ticket messages |
| `user.read` | Read user list |
| `user.update.self` | Update own profile |
| `admin.manage` | Full admin access (users, roles, permissions) |

---

## Data Models

### Ticket

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key |
| `title` | string | Ticket title |
| `body` | text | Ticket description |
| `priority_id` | int (FK) | Priority reference |
| `status_id` | int (FK) | Status reference |
| `created_by` | int (FK) | User who created the ticket |
| `assigned_to` | int (FK) | Assigned agent |
| `created_at` | timestamp | Creation time |
| `updated_at` | timestamp | Last update |
| `resolved_at` | timestamp | Resolution time |

### Notes (Internal-Only)

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key |
| `ticket_id` | int (FK) | Linked ticket |
| `created_by` | int (FK) | Author |
| `updated_by` | int (FK) | Last editor |
| `body` | text | Note content |
| `created_at` | timestamp | Creation time |
| `updated_at` | timestamp | Last update |

### Conversation & Messages

| Field | Type | Description |
|---|---|---|
| `conversation.id` | int | Primary key |
| `conversation.ticket_id` | int (FK) | Linked ticket |
| `message.id` | int | Primary key |
| `message.conversation_id` | int (FK) | Linked conversation |
| `message.sender_id` | int (FK) | Sender |
| `message.body` | text | Message content |

### Priority

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key |
| `name` | string | Label (e.g. Low, High) |
| `level` | int | Numeric weight (1-5) |

### Status

| Field | Type | Description |
|---|---|---|
| `id` | int | Primary key |
| `name` | string | Label (e.g. Open, In Progress) |
| `type` | string | Category: `open`, `active`, `closed` |

---

## Project Structure

```
issue-tracker-app/
├── src/main/java/app/
│   ├── controller/
│   │   ├── admin/               # AdminUserController, AdminRoleController, AdminPermissionController, AdminPriorityController
│   │   ├── AuthController       # /api/auth/*
│   │   ├── TicketController     # /api/tickets/*
│   │   ├── NoteController       # /api/notes/*
│   │   ├── MessageController    # /api/tickets/{id}/messages
│   │   ├── UserController       # /api/users/*
│   │   ├── UserProfileImageController
│   │   ├── PriorityController
│   │   └── StatusController
│   ├── service/                 # Business logic
│   ├── repository/              # Spring Data JPA repositories
│   ├── security/                # JWT, @RequiresPermission, @Ownership, AOP aspect
│   ├── config/                  # SecurityConfig, CorsConfig, DataInitializer
│   ├── model/                   # JPA entities
│   ├── dto/                     # Request/Response DTOs
│   ├── mapper/                  # Entity <-> DTO mappers
│   └── projection/              # JPA projections
├── src/main/resources/
│   ├── application.properties
│   └── static/                  # Built React frontend (index.html + assets)
├── sql/                         # Raw SQL scripts
├── docs/                        # Architecture & API documentation
├── dockerfile
└── pom.xml
```

---

## Documentation

- [Roadmap](roadmap.md)
- [RBAC Design](docs/RBAC.md)
- [Ticket Structure](docs/ticket_structure.md)
- [API Improvements](api_improvments.md)

---

## Future Enhancements

- Ticket delete endpoint
- File attachments
- Activity / audit log
- Email or in-app notifications
- SLA timers with breach detection and escalation
- Real-time updates via WebSockets
- Manager dashboards (KPIs, agent workload, ticket metrics)
- Dark / light theme toggle
