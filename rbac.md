
# Role-Based Access Control (RBAC) in Issue Tracker App

## Overview
This document describes the RBAC (Role-Based Access Control) system implemented in the Issue Tracker App backend. The system is designed for flexibility, security, and ease of management, allowing administrators to define roles and permissions dynamically.

---

## Core Concepts

- **Permissions**: Atomic actions that can be performed in the system (e.g., `ticket.read`, `ticket.write`, `admin.manage`).
- **Roles**: Collections of permissions (e.g., `USER`, `AGENT`, `MANAGER`, `ADMIN`).
- **User Roles**: Users can have one or more roles, determining their access rights.
- **Role Permissions**: Admins can assign or remove permissions from any role.

---

## How RBAC Works in the App

### 1. **Entities & Structure**
- **User**: Represents an application user. Linked to one or more roles via `UserRole`.
- **Role**: Represents a named set of permissions (e.g., `AGENT`, `MANAGER`).
- **Permission**: Represents a single allowed action (e.g., `ticket.assign`).
- **UserRole**: Links users to roles (many-to-many).
- **RolePermission**: Links roles to permissions (many-to-many).

### 2. **Database Tables**
- `users`: Stores user accounts
- `roles`: Stores roles
- `permissions`: Stores permissions
- `user_roles`: Links users to roles
- `role_permissions`: Links roles to permissions

### 3. **Backend Implementation**
- **Model Classes**: `User`, `Role`, `Permission`, `UserRole`
- **Repositories**: `UserRepository`, `RoleRepository`, `PermissionRepository`
- **Services**: `UserService`, `RoleService`, `PermissionService`
- **Security**: JWT tokens include user permissions at login. The `@RequiresPermission` annotation and `PermissionAspect` enforce permission checks on endpoints.

### 4. **Permission Enforcement**
- Permissions are checked at the controller level using the `@RequiresPermission` annotation.
- The `PermissionAspect` intercepts calls and verifies the user’s permissions from the JWT.
- If the user lacks the required permission, access is denied.

### 5. **Admin Management**
- Admins can create new roles and permissions via the `/api/admin` endpoints.
- Admins can assign permissions to roles and roles to users dynamically.

---

## Example Permissions
- `ticket.read` — View tickets
- `ticket.write` — Create or update tickets
- `ticket.assign` — Assign tickets to agents
- `notes.read` — View internal notes
- `notes.write` — Add internal notes
- `admin.manage` — Manage roles and permissions

---

## Example Roles
- **USER**: `ticket.read`, `ticket.write`
- **AGENT**: `ticket.read`, `ticket.write`, `notes.read`, `notes.write`
- **MANAGER**: All agent permissions + `ticket.assign`
- **ADMIN**: All permissions, including `admin.manage`

---

## API Endpoints for RBAC
- `POST /api/admin/roles` — Create a new role
- `POST /api/admin/permissions` — Create a new permission
- `GET /api/admin/roles` — List all roles
- `GET /api/admin/permissions` — List all permissions
- `POST /api/admin/roles/{roleId}/permissions` — Assign permissions to a role
- `POST /api/admin/users/{userId}/roles` — Assign roles to a user

---

## Security Flow
1. User logs in and receives a JWT containing their permissions.
2. On each API request, the JWT is validated and permissions are extracted.
3. Endpoints annotated with `@RequiresPermission` are checked by `PermissionAspect`.
4. If the user has the required permission, the request proceeds; otherwise, access is denied.

---

## Extending RBAC
- New permissions and roles can be added without code changes, via admin endpoints.
- The system is designed to be dynamic and scalable for future needs.

---

## References
- See also: `docs/RBAC.md`, `docs/auth.md`, and `docs/user_management.md` for more details.