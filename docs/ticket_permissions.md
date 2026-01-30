# 🛡️ Ticket Permission & Ownership System

## How Authorization Works in This Application
This document explains the permission-driven and ownership-aware authorization model used for ticket operations.

The system is designed to be:
- **Role-agnostic** (no role checks in code)
- **Permission-driven** (roles only grant permissions)
- **Ownership-aware** (users can access their own tickets even without global permissions)
- **Extensible** (adding new roles requires zero code changes)

---

## 1. Overview
Authorization is controlled by two custom annotations:

```java
@RequiresPermission("ticket.read")
@Ownership(OwnershipType.ALL_OR_SELF)
```

These annotations are processed by the `PermissionAspect`, which decides whether the current user:
- Has the required permission, or
- Lacks the permission but owns the resource, or
- Should be denied access

This creates a clean, predictable, scalable authorization layer.

---

## 2. Permission Model
Permissions define what actions a user can perform globally.

**Core ticket permissions:**
```
ticket.create
ticket.read
ticket.write
ticket.delete
ticket.assign
```

**Optional advanced permissions:**
```
ticket.comment
ticket.status.update
ticket.priority.update
```

> **Important:**
> Users do not need `ticket.read` or `ticket.write` to access their own tickets. Ownership rules handle that automatically.

---

## 3. Ownership Model
Ownership determines whether a user can access a specific ticket when they do not have the required global permission.

Ownership is based on:
- `createdBy == currentUserId`, or
- `assignedTo == currentUserId`

The `OwnershipType` enum defines how ownership interacts with permissions:

```java
public enum OwnershipType {
    ALL,            // must have the permission
    SELF,           // must own the resource
    ALL_OR_SELF     // permission OR ownership
}
```

---

## 4. How the Aspect Decides Access
The `PermissionAspect` evaluates access in this order:

**Step 1 — Check permission**
If the user has the required permission (e.g., `ticket.read`):
- → Access granted to ALL tickets
- This is how ADMIN, MANAGER, SUPPORT get global access.

**Step 2 — Ownership fallback**
If the user does not have the permission:
- If `OwnershipType.ALL_OR_SELF` → allow access only to own tickets
- If `OwnershipType.SELF` → allow update only to own tickets
- If `OwnershipType.ALL` → deny access
- This is how USER gets access to their own tickets without having global permissions.

---

## 5. Controller Behavior

**Read all tickets**
```java
@RequiresPermission("ticket.read")
@Ownership(OwnershipType.ALL_OR_SELF)
```
- ADMIN / MANAGER / SUPPORT → see all tickets
- USER → sees only their own tickets

**Read single ticket**
```java
@RequiresPermission("ticket.read")
@Ownership(OwnershipType.ALL_OR_SELF)
```
- ADMIN / MANAGER / SUPPORT → can read any ticket
- USER → can read only their own tickets

**Create ticket**
```java
@RequiresPermission("ticket.create")
```
- USER has `ticket.create` → can create tickets
- No ownership needed

**Update ticket**
```java
@RequiresPermission("ticket.write")
@Ownership(OwnershipType.SELF)
```
- ADMIN / MANAGER / SUPPORT → can update any ticket
- USER → can update only their own tickets

**Delete ticket**
```java
@RequiresPermission("ticket.delete")
```
- Only ADMIN / MANAGER (depending on permissions)
- USER cannot delete tickets

**Assign ticket**
```java
@RequiresPermission("ticket.assign")
```
- SUPPORT / MANAGER / ADMIN
- USER cannot assign tickets

---

## 6. Role → Permission Mapping (Recommended)

| Permission        | USER | SUPPORT | MANAGER | ADMIN |
|-------------------|:----:|:-------:|:-------:|:-----:|
| ticket.create     |  ✔️  |   ✔️    |   ✔️    |  ✔️   |
| ticket.read       |  ❌  |   ✔️    |   ✔️    |  ✔️   |
| ticket.write      |  ❌  |   ✔️    |   ✔️    |  ✔️   |
| ticket.delete     |  ❌  |   ❌    |   ✔️    |  ✔️   |
| ticket.assign     |  ❌  |   ✔️    |   ✔️    |  ✔️   |

This matrix requires no code changes — only database updates.

---

## 7. Summary
- Permissions control global access
- Ownership controls personal access
- ADMIN / MANAGER / SUPPORT get global access via permissions
- USER gets access to their own tickets via ownership
- Adding new roles requires zero code changes
- The system is clean, scalable, and production-ready
