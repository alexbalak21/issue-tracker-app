# Entity List

This document lists all entities in the project, with their fields and a very short description for each. Each entity is presented as a table for clarity.

---

## Ticket

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique ticket identifier          |
| title         | String            | Ticket title                      |
| body          | String            | Ticket description/body           |
| priorityId    | int               | Priority reference                |
| statusId      | int               | Status reference                  |
| createdBy     | int               | User who created the ticket       |
| assignedTo    | Integer           | User assigned to the ticket       |
| createdAt     | LocalDateTime     | Ticket creation timestamp         |
| updatedAt     | LocalDateTime     | Last update timestamp             |
| resolvedAt    | LocalDateTime     | Resolution timestamp              |

---

## Conversation

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique conversation identifier    |
| ticketId      | int               | Related ticket                    |
| createdBy     | int               | User who started the conversation |
| createdAt     | LocalDateTime     | Conversation creation time        |

---

## Message

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique message identifier         |
| conversationId| int               | Related conversation              |
| senderId      | int               | User who sent the message         |
| body          | String            | Message content                   |
| createdAt     | LocalDateTime     | Message creation time             |

---

## Note

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique note identifier            |
| ticketId      | int               | Related ticket                    |
| authorId      | int               | User who wrote the note           |
| body          | String            | Note content                      |
| createdAt     | LocalDateTime     | Note creation time                |

---

## Permission

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique permission identifier      |
| name          | String            | Permission name                   |
| description   | String            | Permission description            |

---

## Priority

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique priority identifier        |
| name          | String            | Priority name                     |
| level         | int               | Priority level                    |

---

## Role

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique role identifier            |
| name          | String            | Role name                         |
| description   | String            | Role description                  |

---

## Status

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique status identifier          |
| name          | String            | Status name                       |
| description   | String            | Status description                |

---

## User

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique user identifier            |
| username      | String            | Username                          |
| email         | String            | User email                        |
| password      | String            | User password (hashed)            |
| createdAt     | LocalDateTime     | User creation time                |

---

## UserRole

| Field         | Type              | Description                       |
|-------------- |------------------|-----------------------------------|
| id            | int               | Unique user-role identifier        |
| userId        | int               | User reference                    |
| roleId        | int               | Role reference                    |
