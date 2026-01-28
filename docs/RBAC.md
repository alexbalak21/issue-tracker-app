# 🔐 Permission & Role Implementation (RBAC)

This document explains how the Issue Tracker implements a flexible, scalable **Role‑Based Access Control (RBAC)** system.  
It enables administrators to create roles dynamically, assign granular permissions, and enforce them across the backend.

---

## 🎯 Goal

The RBAC system allows:

- Dynamic role creation  
- Assigning read/write/delete permissions to roles  
- Assigning roles to users  
- Enforcing permissions in Spring Boot via JWT  

This provides:

- Unlimited custom roles  
- Unlimited permissions  
- A clean many‑to‑many schema  
- Easy enforcement in controllers and services  

---

## 🧱 Database Design (Recommended Schema)

The RBAC system introduces **four tables**.

---

### 1️⃣ `permissions`

Atomic actions your system supports.

| Field       | Type | Description                                      |
|-------------|------|--------------------------------------------------|
| id          | int  | Primary key                                      |
| name        | text | e.g. `ticket.read`, `ticket.write`, `notes.write` |
| description | text | Optional                                         |

---

### 2️⃣ `roles`

Roles created by administrators.

| Field       | Type | Description                          |
|-------------|------|--------------------------------------|
| id          | int  | Primary key                          |
| name        | text | e.g. `Manager`, `Agent`, `Supervisor` |
| description | text | Optional                             |

---

### 3️⃣ `role_permissions` (many‑to‑many)

This is effectively your “checkbox table”.

| Field         | Type     | Description |
|---------------|----------|-------------|
| role_id       | int (FK) | Role        |
| permission_id | int (FK) | Permission  |

- A role can have many permissions  
- A permission can belong to many roles  

---

### 4️⃣ `user_roles` (many‑to‑many)

| Field   | Type     | Description |
|---------|----------|-------------|
| user_id | int (FK) | User        |
| role_id | int (FK) | Role        |

A user may have multiple roles.

---

## 🧠 Why This Design Works

- No hardcoded permissions  
- Admins can create new roles anytime  
- Permissions can be added without code changes  
- Granular control (read/write/delete/etc.)  
- Permissions or roles can be embedded in JWT  
- Clean, normalized, scalable schema  

---

## 🔧 Example Permissions

| Permission          | Meaning                     |
|---------------------|-----------------------------|
| `ticket.read`       | Can view tickets            |
| `ticket.write`      | Can create/update tickets   |
| `ticket.assign`     | Can assign tickets          |
| `notes.read`        | Can view internal notes     |
| `notes.write`       | Can add internal notes      |
| `conversation.read` | Can read messages           |
| `conversation.write`| Can send messages           |

---

## 🧩 Example Role Setup

### **Manager**
- ticket.read  
- ticket.write  
- ticket.assign  
- notes.read  
- notes.write  
- conversation.read  
- conversation.write  

### **Agent**
- ticket.read  
- ticket.write  
- notes.read  
- notes.write  
- conversation.read  
- conversation.write  

### **User**
- ticket.write  
- conversation.read  
- conversation.write  

---

## 🛡️ Enforcing Permissions in Spring Boot

### **Step 1 — Load permissions into JWT**

```java
List<String> permissions = user.getRoles()
    .stream()
    .flatMap(role -> role.getPermissions().stream())
    .map(Permission::getName)
    .collect(Collectors.toList());


Add them to JWT claims:
```java
claims.put("permissions", permissions);
```
Step 2 — Create a custom annotation
java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    String value();
}
Step 3 — Validate permissions in a filter or AOP interceptor
java
if (!jwtPermissions.contains(requiredPermission)) {
    throw new AccessDeniedException("Missing permission: " + requiredPermission);
}
Step 4 — Use it in controllers
java
@RequiresPermission("ticket.assign")
@PostMapping("/tickets/{id}/assign")
public ResponseEntity<?> assignTicket(...) {
    ...
}
🖥️ Admin UI (Frontend)
The admin interface includes:

Roles Page
Create role

Edit role

Assign permissions (checkbox grid)

Users Page
Assign roles to users

Permissions Page
(Optional) Create new permissions

🧩 Example Permission Matrix
Permission	Manager	Agent	User
ticket.read	✔	✔	✔
ticket.write	✔	✔	✔
ticket.assign	✔	✖	✖
notes.read	✔	✔	✖
notes.write	✔	✔	✖
This is the scalable version of your “checkbox table” idea.

🎯 Summary
To implement dynamic roles and permissions:

✔ Add tables: permissions, roles, role_permissions, user_roles

✔ Store permissions in JWT

✔ Enforce permissions in Spring Security

✔ Build an admin UI for managing roles & permissions

This results in a professional, enterprise‑grade RBAC system that grows with your application.