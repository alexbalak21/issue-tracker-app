# 🛠️ Issue Tracker Application

A full-stack, enterprise-grade Issue Tracker system built with Spring Boot and designed for team collaboration, ticketing, and support workflow management.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/Auth-JWT-yellow.svg)](https://jwt.io/)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Security & Authentication](#-security--authentication)
- [Permission System](#-permission-system)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Documentation](#-documentation)

---

## 🎯 Overview

The Issue Tracker Application is a comprehensive ticketing and support management system that enables organizations to:

- **Track Issues**: Create, assign, update, and resolve support tickets
- **Manage Teams**: Organize users with role-based permissions
- **Facilitate Communication**: Public conversation threads per ticket
- **Internal Collaboration**: Private notes visible only to staff
- **Monitor Progress**: Priority levels, status tracking, and timestamps

**Perfect for:**
- IT support teams
- Customer service departments
- Project management offices
- Help desk operations
- Bug tracking workflows

---

## ✨ Key Features

### 🔐 Authentication & Authorization
- **JWT-based Authentication** with access and refresh tokens
- **Role-Based Access Control (RBAC)** with granular permissions
- **Custom User Details** with Spring Security integration
- **Token refresh mechanism** for seamless user experience
- **Ownership-based authorization** (users can access their own tickets, admins see all)

### 🎫 Ticket Management
- **Complete CRUD operations** for tickets
- **Priority system** (configurable levels: Low, Medium, High, Critical)
- **Status tracking** (Open, In Progress, Resolved, Closed)
- **Assignment workflow** - assign tickets to agents/support staff
- **Search & filter** by status, priority, title keywords
- **Timestamps**: Created, updated, and resolved tracking
- **User ownership** - users create tickets, staff manages them

### 💬 Communication System
- **Conversation threads** - one per ticket for public communication
- **Messages** - chronological message history between users and staff
- **Real-time collaboration** between users and support teams
- **Sender tracking** - every message attributed to a user

### 📝 Internal Notes
- **Private notes** visible only to staff (agents/managers/admins)
- **CRUD operations** for notes linked to tickets
- **User attribution** - track who created and updated each note
- **Restricted access** - end users cannot see internal notes

### 👥 User Management
- **Admin-controlled user creation** (no public registration for staff)
- **Multi-role assignment** - users can have multiple roles
- **User profile management**
- **Role and permission assignment**
- **User listing and details** for admins

### 🛡️ Admin Panel
- **Role management** - create, update, assign permissions to roles
- **Permission management** - define granular system permissions
- **User-role assignment** - assign/remove roles from users
- **Priority management** - configure priority levels
- **Status management** - configure ticket statuses

---

## 🏗️ Architecture

### Application Layers

```
┌─────────────────────────────────────────┐
│         REST API Controllers            │
│   (Authentication, Tickets, Admin)      │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│      Security Layer (JWT + RBAC)        │
│  - JWT Filter                           │
│  - Permission Aspect                    │
│  - Ownership Validation                 │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         Service Layer                   │
│  (Business Logic & Validation)          │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│      Repository Layer (JPA)             │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│        PostgreSQL Database              │
└─────────────────────────────────────────┘
```

### Design Patterns
- **MVC Architecture** - Clean separation of concerns
- **Repository Pattern** - Spring Data JPA for data access
- **DTO Pattern** - Data Transfer Objects for API communication
- **AOP (Aspect-Oriented Programming)** - Permission enforcement via annotations
- **Dependency Injection** - Spring Framework IoC container

---

## 🔧 Technology Stack

### Backend Framework
- **Spring Boot 3.5.10** - Core application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Web** - RESTful API implementation
- **Hibernate** - ORM for PostgreSQL

### Security & Authentication
- **JWT (JSON Web Tokens)** - Token-based authentication
  - `jjwt-api 0.11.5` - JWT API
  - `jjwt-impl 0.11.5` - JWT implementation
  - `jjwt-jackson 0.11.5` - JSON processing for JWT
- **BCrypt** - Password hashing

### Database
- **PostgreSQL** - Relational database
- **HikariCP** - High-performance connection pooling

### Java & Build Tools
- **Java 21** - Latest LTS version
- **Maven** - Dependency management and build automation

### Development Tools
- **Spring Boot DevTools** - Hot reload for development
- **Logback** - Logging framework
- **Hibernate Statistics** - Performance monitoring

---

## 🗄️ Database Schema

### Entity Relationship Overview

```
users ←──────── tickets ──────→ conversation ──────→ messages
  │                │                                      │
  │                │                                      └─ sender_id
  │                └─────────→ notes
  │
  └─────→ user_roles ←─────→ roles ←─────→ role_permissions ←─────→ permissions
```

### Core Tables

#### **users**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | BIGINT        | Primary key                      |
| name         | VARCHAR(100)  | Full name                        |
| email        | VARCHAR(255)  | Unique email (login username)    |
| password     | VARCHAR(255)  | BCrypt hashed password           |
| created_at   | TIMESTAMP     | Account creation timestamp       |

#### **tickets**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | BIGINT        | Primary key                      |
| title        | VARCHAR(255)  | Ticket title                     |
| body         | TEXT          | Ticket description/details       |
| priority_id  | INT           | Foreign key → priority           |
| status_id    | INT           | Foreign key → status             |
| created_by   | BIGINT        | Foreign key → users              |
| assigned_to  | BIGINT        | Foreign key → users (nullable)   |
| created_at   | TIMESTAMP     | Creation timestamp               |
| updated_at   | TIMESTAMP     | Last modification timestamp      |
| resolved_at  | TIMESTAMP     | Resolution timestamp (nullable)  |

#### **notes** (Internal Only)
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | BIGINT        | Primary key                      |
| ticket_id    | BIGINT        | Foreign key → tickets            |
| created_by   | BIGINT        | Foreign key → users              |
| updated_by   | BIGINT        | Foreign key → users (nullable)   |
| body         | TEXT          | Note content                     |
| created_at   | TIMESTAMP     | Creation timestamp               |
| updated_at   | TIMESTAMP     | Last modification timestamp      |

#### **conversation**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | INT           | Primary key                      |
| ticket_id    | BIGINT        | Foreign key → tickets (unique)   |
| created_at   | TIMESTAMP     | Conversation start timestamp     |
| updated_at   | TIMESTAMP     | Last message timestamp           |

#### **messages**
| Column          | Type          | Description                      |
|-----------------|---------------|----------------------------------|
| id              | INT           | Primary key                      |
| conversation_id | INT           | Foreign key → conversation       |
| sender_id       | INT           | Foreign key → users              |
| body            | TEXT          | Message content                  |
| created_at      | TIMESTAMP     | Message timestamp                |
| updated_at      | TIMESTAMP     | Edit timestamp (nullable)        |

#### **priority**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | INT           | Primary key                      |
| name         | VARCHAR(50)   | Priority label (e.g., "High")    |
| level        | INT           | Numeric weight (1-5)             |

#### **status**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | INT           | Primary key                      |
| name         | VARCHAR(50)   | Status label (e.g., "Open")      |
| type         | VARCHAR(50)   | Category: open/active/closed     |

### RBAC Tables

#### **permissions**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | BIGINT        | Primary key                      |
| name         | VARCHAR(100)  | Unique permission identifier     |
| description  | VARCHAR(255)  | Human-readable description       |

#### **roles**
| Column       | Type          | Description                      |
|--------------|---------------|----------------------------------|
| id           | BIGINT        | Primary key                      |
| name         | VARCHAR(50)   | Unique role name                 |
| description  | VARCHAR(255)  | Role purpose description         |

#### **role_permissions** (Join Table)
| Column         | Type    | Description                      |
|----------------|---------|----------------------------------|
| role_id        | BIGINT  | Foreign key → roles              |
| permission_id  | BIGINT  | Foreign key → permissions        |

#### **user_roles** (Join Table)
| Column    | Type    | Description                      |
|-----------|---------|----------------------------------|
| user_id   | BIGINT  | Foreign key → users              |
| role_id   | BIGINT  | Foreign key → roles              |

---

## 🌐 API Endpoints

### Authentication (`/api/auth`)

| Method | Endpoint           | Description                    | Auth Required |
|--------|--------------------|--------------------------------|---------------|
| POST   | `/login`           | Authenticate user              | No            |
| POST   | `/register`        | Register new user              | No            |
| POST   | `/refresh`         | Refresh access token           | No            |
| POST   | `/logout`          | Logout user                    | Yes           |

### Tickets (`/api/tickets`)

| Method | Endpoint                  | Permission          | Description                    |
|--------|---------------------------|---------------------|--------------------------------|
| GET    | `/`                       | `ticket.read`       | Get all tickets (filtered)     |
| GET    | `/{id}`                   | `ticket.read`       | Get ticket details             |
| GET    | `/status/{statusId}`      | `ticket.read`       | Filter by status               |
| GET    | `/priority/{priorityId}`  | `ticket.read`       | Filter by priority             |
| GET    | `/search?keyword=...`     | `ticket.read`       | Search by title                |
| POST   | `/`                       | `ticket.create`     | Create new ticket              |
| PUT    | `/{id}`                   | `ticket.write`      | Update ticket                  |
| POST   | `/{id}/assign`            | `ticket.assign`     | Assign ticket to agent         |

### Messages (`/api/tickets`)

| Method | Endpoint                       | Permission            | Description                    |
|--------|--------------------------------|-----------------------|--------------------------------|
| GET    | `/{ticketId}/messages`         | `conversation.read`   | Get all messages for ticket    |
| POST   | `/{ticketId}/messages`         | `conversation.reply`  | Send message to ticket         |

### Notes (`/api/notes`)

| Method | Endpoint          | Permission     | Description                    |
|--------|-------------------|----------------|--------------------------------|
| GET    | `/{ticketId}`     | `note.read`    | Get all notes for ticket       |
| POST   | `/`               | `note.create`  | Create internal note           |
| PUT    | `/{noteId}`       | `note.create`  | Update note                    |
| DELETE | `/{noteId}`       | `note.create`  | Delete note                    |

### Admin - Users (`/api/admin/users`)

| Method | Endpoint              | Permission       | Description                    |
|--------|-----------------------|------------------|--------------------------------|
| GET    | `/`                   | `admin.manage`   | List all users                 |
| GET    | `/{userId}`           | `admin.manage`   | Get user details               |
| POST   | `/`                   | `admin.manage`   | Create new user                |
| POST   | `/{userId}/roles`     | `admin.manage`   | Assign roles to user           |
| DELETE | `/{userId}/roles`     | `admin.manage`   | Remove roles from user         |

### Admin - Roles (`/api/admin/roles`)

| Method | Endpoint                    | Permission       | Description                    |
|--------|-----------------------------|------------------|--------------------------------|
| GET    | `/`                         | `admin.manage`   | List all roles                 |
| POST   | `/`                         | `admin.manage`   | Create new role                |
| PUT    | `/{roleId}`                 | `admin.manage`   | Update role                    |
| PATCH  | `/{roleId}/permissions`     | `admin.manage`   | Add permissions to role        |

### Admin - Permissions (`/api/admin/permissions`)

| Method | Endpoint  | Permission       | Description                    |
|--------|-----------|------------------|--------------------------------|
| GET    | `/`       | `admin.manage`   | List all permissions           |
| POST   | `/`       | `admin.manage`   | Create new permission          |

### Priority & Status (`/api/priorities`, `/api/status`)

| Method | Endpoint  | Permission    | Description                    |
|--------|-----------|---------------|--------------------------------|
| GET    | `/`       | Public        | List all priorities/statuses   |
| POST   | `/`       | `admin.manage`| Create priority/status         |
| PUT    | `/{id}`   | `admin.manage`| Update priority/status         |
| DELETE | `/{id}`   | `admin.manage`| Delete priority/status         |

### User Profile (`/api/user`)

| Method | Endpoint  | Permission | Description                    |
|--------|-----------|------------|--------------------------------|
| GET    | `/`       | Any user   | Get current user profile       |

---

## 🔐 Security & Authentication

### JWT Authentication Flow

```
1. User → POST /api/auth/login (email + password)
   ↓
2. Server validates credentials
   ↓
3. Server generates:
   - Access Token (expires in 15 min)
   - Refresh Token (expires in 7 days)
   ↓
4. Client stores tokens
   ↓
5. Client → Request with Authorization: Bearer {access_token}
   ↓
6. JwtAuthenticationFilter validates token
   ↓
7. Security context populated with user details + permissions
   ↓
8. Request processed (if authorized)
   ↓
9. Access token expires → POST /api/auth/refresh (with refresh_token)
   ↓
10. New access token issued
```

### Security Configuration

- **CORS**: Configurable allowed origins via environment variable
- **CSRF**: Disabled (stateless JWT authentication)
- **Session Management**: STATELESS
- **Password Encoding**: BCrypt (strength 10)
- **Public Endpoints**: `/api/auth/**`, error pages
- **Protected Endpoints**: All others require valid JWT

### Token Structure

**Access Token Payload:**
```json
{
  "sub": "user@example.com",
  "userId": 123,
  "permissions": ["ticket.read", "ticket.write", "note.create"],
  "iat": 1706745600,
  "exp": 1706746500
}
```

**Refresh Token Payload:**
```json
{
  "sub": "user@example.com",
  "userId": 123,
  "type": "refresh",
  "iat": 1706745600,
  "exp": 1707350400
}
```

---

## 🛡️ Permission System

### Permission Categories

#### Ticket Permissions
- `ticket.read` - View tickets (users see own, staff sees all)
- `ticket.create` - Create new tickets
- `ticket.write` - Update tickets
- `ticket.assign` - Assign tickets to agents
- `ticket.delete` - Delete tickets

#### Conversation & Message Permissions
- `conversation.read` - View conversations and messages
- `conversation.reply` - Send messages in conversations

#### Note Permissions (Staff Only)
- `note.read` - View internal notes
- `note.create` - Create/update/delete internal notes
- `note.manage` - Full note management (admin)

#### User Permissions
- `user.read` - View user information
- `user.write` - Create or update users
- `user.delete` - Delete users
- `user.manage` - Full user management

#### Role Permissions
- `role.read` - View roles
- `role.write` - Create or update roles
- `role.delete` - Delete roles

#### Permission Management
- `permission.read` - View permissions
- `permission.write` - Create or update permissions
- `permission.delete` - Delete permissions

#### Admin Permissions
- `admin.manage` - Full administrative access (all features)

### Default Roles

#### User Role
- `ticket.read` (own tickets only)
- `ticket.create`
- `conversation.read`
- `conversation.reply`

#### Agent/Support Role
- `ticket.read` (all tickets)
- `ticket.write`
- `note.read`
- `note.create`
- `conversation.read`
- `conversation.reply`

#### Manager Role
- All Agent permissions +
- `ticket.assign`
- `ticket.delete`
- `note.manage`
- `user.read`

#### Admin Role
- `admin.manage` (full system access)

### Authorization Enforcement

#### Annotation-Based
```java
@GetMapping
@RequiresPermission("ticket.read")
@Ownership(OwnershipType.ALL_OR_SELF)
public List<Ticket> getAllTickets() { ... }
```

#### Ownership Types
- `ALL` - User must have permission to access any resource
- `SELF` - User can only access resources they own
- `ALL_OR_SELF` - Admins see all, users see own

#### AOP Implementation
- **PermissionAspect** - Intercepts `@RequiresPermission` methods
- Validates user has required permissions from JWT
- Checks ownership constraints
- Throws `AccessDeniedException` if unauthorized

---

## 📁 Project Structure

```
issue-tracker-app/
│
├── src/main/java/app/
│   ├── Application.java                    # Spring Boot entry point
│   │
│   ├── config/                             # Configuration classes
│   │   ├── CorsConfig.java                 # CORS policy
│   │   ├── DataInitializer.java            # Seed data on startup
│   │   ├── JpaConfig.java                  # JPA/Hibernate config
│   │   └── SecurityConfig.java             # Spring Security config
│   │
│   ├── controller/                         # REST API controllers
│   │   ├── admin/                          # Admin-only endpoints
│   │   │   ├── AdminUserController.java
│   │   │   ├── AdminRoleController.java
│   │   │   ├── AdminPermissionController.java
│   │   │   └── AdminPriorityController.java
│   │   ├── AuthController.java             # Authentication
│   │   ├── TicketController.java           # Ticket CRUD
│   │   ├── MessageController.java          # Conversation messages
│   │   ├── NoteController.java             # Internal notes
│   │   ├── UserController.java             # User profile
│   │   ├── PriorityController.java         # Priority management
│   │   ├── StatusController.java           # Status management
│   │   └── HomeController.java             # Health check
│   │
│   ├── dto/                                # Data Transfer Objects
│   │   ├── ApiResponse.java
│   │   ├── CreateTicketRequest.java
│   │   ├── UpdateTicketRequest.java
│   │   ├── AssignTicketRequest.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── RefreshTokenRequest.java
│   │   ├── CreateUserRequest.java
│   │   ├── CreateRoleRequest.java
│   │   ├── CreatePermissionRequest.java
│   │   ├── AssignRolesRequest.java
│   │   ├── NoteCreateRequest.java
│   │   ├── NoteUpdateRequest.java
│   │   ├── NoteResponse.java
│   │   ├── UserDto.java
│   │   ├── UserInfo.java
│   │   ├── UserSummary.java
│   │   ├── RoleDto.java
│   │   ├── PermissionDto.java
│   │   ├── PriorityDto.java
│   │   └── StatusDto.java
│   │
│   ├── model/                              # JPA Entities
│   │   ├── User.java
│   │   ├── Ticket.java
│   │   ├── Note.java
│   │   ├── Conversation.java
│   │   ├── Message.java
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   ├── UserRole.java
│   │   ├── Priority.java
│   │   └── Status.java
│   │
│   ├── repository/                         # Spring Data JPA Repositories
│   │   ├── UserRepository.java
│   │   ├── TicketRepository.java
│   │   ├── NoteRepository.java
│   │   ├── ConversationRepository.java
│   │   ├── MessageRepository.java
│   │   ├── RoleRepository.java
│   │   ├── PermissionRepository.java
│   │   ├── PriorityRepository.java
│   │   └── StatusRepository.java
│   │
│   ├── security/                           # Security components
│   │   ├── JwtService.java                 # JWT generation/validation
│   │   ├── JwtAuthenticationFilter.java    # JWT filter
│   │   ├── JwtAuthenticationToken.java     # Custom authentication
│   │   ├── CustomUserDetails.java          # UserDetails implementation
│   │   ├── RequiresPermission.java         # Permission annotation
│   │   ├── Ownership.java                  # Ownership annotation
│   │   ├── OwnershipType.java              # Ownership enum
│   │   └── PermissionAspect.java           # AOP permission enforcement
│   │
│   └── service/                            # Business logic layer
│       ├── AuthService.java                # Authentication logic
│       ├── UserService.java                # User management
│       ├── TicketService.java              # Ticket operations
│       ├── ConversationService.java        # Conversation management
│       ├── MessageService.java             # Message operations
│       ├── NoteService.java                # Note operations
│       ├── RoleService.java                # Role management
│       ├── PermissionService.java          # Permission management
│       ├── PriorityService.java            # Priority operations
│       ├── StatusService.java              # Status operations
│       └── CustomUserDetailsService.java   # Spring Security integration
│
├── src/main/resources/
│   ├── application.properties              # Spring Boot configuration
│   └── static/                             # Static web assets
│       ├── index.html
│       └── assets/                         # React frontend build
│
├── docs/                                   # Project documentation
│   ├── RBAC.md
│   ├── Permissions.md
│   ├── auth.md
│   ├── data_structure.md
│   ├── ticket_structure.md
│   └── user_management.md
│
├── scripts/                                # PowerShell utility scripts
│   ├── build_run.ps1
│   ├── create_permissions.ps1
│   ├── create_roles.ps1
│   ├── admin_create_user.ps1
│   └── test_api.ps1
│
├── init/                                   # Initialization scripts
│   ├── main.ps1
│   ├── 01-login-admin.ps1
│   ├── 02-create-permissions.ps1
│   ├── 03-create-role-user.ps1
│   ├── 04-create-user.ps1
│   ├── 05-create-role-support.ps1
│   └── 06-create-support-user.ps1
│
├── rest requests/                          # HTTP request files for testing
│   ├── admin.rest
│   ├── permissions.rest
│   ├── Tickets.rest
│   ├── user_roles.rest
│   └── requests.http
│
├── sql/                                    # Database scripts
│   └── create_tables.sql
│
├── logs/                                   # Application logs
│   └── app.log
│
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
├── roadmap.md                              # Development roadmap
├── dockerfile                              # Docker container definition
└── mvnw, mvnw.cmd                          # Maven wrapper scripts
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **PostgreSQL 12+**
- **Maven 3.8+** (or use included Maven wrapper)
- **Git**

### Installation

#### 1. Clone the repository
```bash
git clone <repository-url>
cd issue-tracker-app
```

#### 2. Set up PostgreSQL database
```sql
CREATE DATABASE issue_tracker;
CREATE USER tracker_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE issue_tracker TO tracker_user;
```

#### 3. Configure environment variables

Create a `.env` file or set environment variables:

```properties
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/issue_tracker
DATABASE_USERNAME=tracker_user
DATABASE_PASSWORD=your_password

# JWT Secrets (use strong random strings)
JWT_SECRET=your-256-bit-secret-key-here
JWT_REFRESH_SECRET=your-refresh-secret-key-here

# JWT Expiration (milliseconds)
JWT_EXPIRATION=900000          # 15 minutes
REFRESH_EXPIRATION=604800000   # 7 days

# Server
PORT=8100
SPRING_PROFILES_ACTIVE=prod

# CORS
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8100

# Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

#### 4. Build the application
```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or with installed Maven
mvn clean install
```

#### 5. Run the application
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using PowerShell script
.\run.ps1

# Or run the JAR directly
java -jar target/issue-tracker-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8100`

### Initial Setup

#### 1. Run initialization scripts

The application includes PowerShell scripts to set up initial data:

```powershell
cd init
.\main.ps1
```

This will:
- Create default permissions
- Create default roles (Admin, User, Support)
- Create admin user
- Create test users

#### 2. Default Admin Credentials

After running initialization scripts:
```
Email: admin@example.com
Password: admin123
```

**⚠️ Change the admin password immediately in production!**

### Verify Installation

```bash
# Health check
curl http://localhost:8100/

# Login
curl -X POST http://localhost:8100/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'
```

---

## ⚙️ Configuration

### Application Properties

The application uses `application.properties` with environment variable substitution for sensitive data.

**Key Configuration Options:**

| Property | Environment Variable | Default | Description |
|----------|---------------------|---------|-------------|
| `server.port` | `PORT` | 8100 | HTTP server port |
| `spring.datasource.url` | `DATABASE_URL` | - | PostgreSQL connection URL |
| `spring.datasource.username` | `DATABASE_USERNAME` | - | Database user |
| `spring.datasource.password` | `DATABASE_PASSWORD` | - | Database password |
| `app.jwt.secret` | `JWT_SECRET` | - | JWT signing secret (required) |
| `app.jwt.refresh.secret` | `JWT_REFRESH_SECRET` | - | Refresh token secret |
| `app.jwt.expiration` | `JWT_EXPIRATION` | 900000 | Access token expiration (ms) |
| `app.refresh.expiration` | `REFRESH_EXPIRATION` | 604800000 | Refresh token expiration (ms) |
| `app.security.allowed-origin` | `ALLOWED_ORIGINS` | http://localhost:8100 | CORS allowed origins |
| `spring.jpa.hibernate.ddl-auto` | `SPRING_JPA_HIBERNATE_DDL_AUTO` | update | Hibernate DDL mode |

### Hibernate DDL Mode Options

- `update` - Update schema without data loss (development)
- `create` - Drop and recreate schema (testing)
- `create-drop` - Drop schema on shutdown (testing)
- `validate` - Validate schema only (production)
- `none` - No schema management (production with migrations)

**Recommendation:** Use `validate` or `none` in production with proper database migrations.

### Logging Configuration

Logging is configured via `application.properties`:

```properties
logging.level.root=INFO
logging.level.app=DEBUG
logging.level.org.springframework.security=DEBUG
logging.file.name=logs/app.log
```

Log file rotation is handled by Logback with history cleanup on startup.

---

## 📚 Documentation

### Project Documentation Files

- **[README.md](README.md)** - This file (comprehensive guide)
- **[roadmap.md](roadmap.md)** - Development roadmap and milestones
- **[core_requirements.md](core_requirements.md)** - Core functional requirements
- **[api_doc.md](api_doc.md)** - API documentation
- **[docs/RBAC.md](docs/RBAC.md)** - Role-Based Access Control details
- **[docs/Permissions.md](docs/Permissions.md)** - Permission reference
- **[docs/auth.md](docs/auth.md)** - Authentication system documentation
- **[docs/data_structure.md](docs/data_structure.md)** - Database structure
- **[docs/ticket_structure.md](docs/ticket_structure.md)** - Ticket workflow
- **[docs/user_management.md](docs/user_management.md)** - User management guide

### API Testing

Use the provided `.rest` files in the `rest requests/` directory with VS Code REST Client extension or IntelliJ HTTP Client.

Example files:
- `admin.rest` - Admin operations
- `Tickets.rest` - Ticket operations
- `permissions.rest` - Permission management
- `user_roles.rest` - Role assignments

---

## 🔄 Workflow Examples

### Creating a Ticket

```
1. User authenticates → receives JWT
2. User → POST /api/tickets
   {
     "title": "Unable to login",
     "body": "Getting 404 error when trying to login",
     "priorityId": 3
   }
3. Server creates ticket with:
   - created_by = user's ID
   - status = "Open" (default)
   - timestamps set
4. Server auto-creates Conversation for ticket
5. Response returns created Ticket
```

### Assigning a Ticket

```
1. Manager authenticates
2. Manager → POST /api/tickets/123/assign
   {
     "assignedTo": 5
   }
3. Server validates:
   - User has "ticket.assign" permission
   - Agent (ID=5) exists
4. Ticket updated with assigned_to = 5
5. Response returns updated Ticket
```

### Adding a Message

```
1. User or Agent authenticates
2. User → POST /api/tickets/123/messages
   "I've tried resetting my password but still can't login"
3. Server:
   - Finds conversation for ticket 123
   - Creates message with sender = current user
   - Sets timestamp
4. Response returns Message object
```

### Creating Internal Note

```
1. Agent authenticates
2. Agent → POST /api/notes
   {
     "ticketId": 123,
     "body": "User's account was locked due to failed login attempts"
   }
3. Server validates:
   - User has "note.create" permission
   - Ticket exists
4. Note created with created_by = agent's ID
5. Response returns Note (visible only to staff)
```

---

## 🧪 Testing

### Manual Testing

Run the application and use the provided REST files:

```powershell
# Start the application
.\run.ps1

# In another terminal, run test scripts
cd scripts
.\test_api.ps1
```

### Unit Testing

```bash
mvn test
```

### Integration Testing

The project includes Spring Boot test support. Run with:

```bash
mvn verify
```

---

## 🐳 Docker Deployment

A `dockerfile` is included for containerization:

```bash
# Build image
docker build -t issue-tracker:latest .

# Run container
docker run -d \
  -p 8100:8100 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/issue_tracker \
  -e DATABASE_USERNAME=tracker_user \
  -e DATABASE_PASSWORD=your_password \
  -e JWT_SECRET=your-secret \
  -e JWT_REFRESH_SECRET=your-refresh-secret \
  --name issue-tracker \
  issue-tracker:latest
```

---

## 🛠️ Development

### Code Style
- Follow Spring Boot best practices
- Use Java 21 features (records, pattern matching, etc.)
- Keep controllers thin - business logic in services
- Use DTOs for API contracts
- Follow RESTful conventions

### Adding New Permissions

1. Define permission in database or via API:
   ```sql
   INSERT INTO permissions (name, description) 
   VALUES ('feature.action', 'Description');
   ```

2. Add to appropriate roles

3. Use in controllers:
   ```java
   @GetMapping("/feature")
   @RequiresPermission("feature.action")
   public ResponseEntity<?> getFeature() { ... }
   ```

### Adding New Endpoints

1. Create DTO classes for request/response
2. Implement service logic
3. Create controller method
4. Add `@RequiresPermission` annotation
5. Add ownership constraints if needed
6. Test with REST files

---

## 📊 Future Enhancements

### Planned Features
- ✅ JWT authentication with refresh tokens
- ✅ Role-based access control
- ✅ Ticket management system
- ✅ Conversation threads
- ✅ Internal notes
- 🔲 File attachments
- 🔲 Email notifications
- 🔲 Real-time updates (WebSocket)
- 🔲 Activity/audit logs
- 🔲 SLA timers and breach detection
- 🔲 Dashboard with metrics
- 🔲 Advanced reporting
- 🔲 Dark/light theme
- 🔲 Multi-language support
- 🔲 Mobile application

### Scalability Considerations
- Redis caching for frequently accessed data
- Message queue for notifications (RabbitMQ/Kafka)
- Database replication for read scaling
- CDN for static assets
- Load balancing for multiple instances

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add YourFeature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👥 Authors

- **Development Team** - Initial work and ongoing development

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community
- JWT.io for authentication standards
- All contributors and testers

---

## 📞 Support

For issues, questions, or contributions:
- Open an issue on GitHub
- Check the documentation in `/docs`
- Review REST API examples in `/rest requests`

---

**Built with ❤️ using Spring Boot**
