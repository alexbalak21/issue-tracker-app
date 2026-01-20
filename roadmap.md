# 🧭 Project Roadmap

A complete development workflow for building the Issue Tracker application.  
This roadmap is divided into major chapters with clear, actionable steps.

---

## 1. Project Planning & Requirements

### 1.1 Define the purpose
- Internal issue tracker for teams  
- Admin/Manager‑controlled user creation  
- Agents handle assigned tickets  
- Users submit tickets  

### 1.2 Define core features
- JWT authentication (access + refresh tokens)  
- Ticket CRUD  
- Internal notes  
- Conversations & messages  
- Priority & status system  
- Role‑based access control  

### 1.3 Define non‑functional requirements
- Security  
- Scalability  
- Maintainability  
- Clean architecture  

---

## 2. Database & Schema Design

### 2.1 Define core tables
- users  
- tickets  
- notes  
- conversations  
- messages  
- priority  
- status  

### 2.2 Define relationships
- user → tickets  
- ticket → notes  
- ticket → conversation → messages  
- priority/status → tickets  

### 2.3 Create ERD diagram
- Visualize all tables  
- Validate relationships  
- Adjust for real workflows  

---

## 3. Authentication System (JWT)

### 3.1 JWT setup
- Access token (short‑lived)  
- Refresh token (long‑lived)  

### 3.2 Endpoints
- POST `/auth/login`  
- POST `/auth/refresh-token`  
- POST `/auth/logout` (optional)  

### 3.3 User creation rules
- Only Admin/Manager can create users  
- No public registration  

### 3.4 Password handling
- Hash passwords (BCrypt)  
- Admin/Manager can reset passwords  

---

## 4. Backend Architecture (Spring Boot)

### 4.1 Project structure
- controllers  
- services  
- repositories  
- entities  
- DTOs  
- security  

### 4.2 Implement modules
- Auth module  
- User management module  
- Ticket module  
- Notes module  
- Conversation module  
- Message module  

### 4.3 Validation & error handling
- Global exception handler  
- Validation annotations  
- Custom error responses  

---

## 5. Frontend Architecture (React + TypeScript)

### 5.1 Project setup
- React + Vite  
- Tailwind CSS  
- Routing structure  

### 5.2 Authentication
- Login page  
- Token storage (memory + cookie)  
- Axios interceptor for auto‑refresh  

### 5.3 UI modules
- Dashboard  
- Ticket list  
- Ticket detail  
- Notes panel  
- Conversation/messages panel  
- User management (admin/manager only)  

### 5.4 State management
- React Query or Redux  
- Global auth context  

---

## 6. API Integration

### 6.1 Connect frontend to backend
- Auth endpoints  
- Ticket CRUD  
- Notes CRUD  
- Messages CRUD  
- User management  

### 6.2 Handle loading & error states
- Toast notifications  
- Skeleton loaders  
- Retry logic  

---

## 7. Testing

### 7.1 Backend tests
- Unit tests (services)  
- Integration tests (controllers)  

### 7.2 Frontend tests
- Component tests  
- API mocks  
- Auth flow tests  

---

## 8. Deployment

### 8.1 Backend
- Dockerize Spring Boot  
- Deploy to server or cloud  

### 8.2 Frontend
- Build React app  
- Serve via Nginx or cloud hosting  

### 8.3 Environment variables
- JWT secrets  
- Database credentials  
- CORS configuration  

---

## 9. Documentation

### 9.1 Technical docs
- README  
- auth.md  
- ticket_data_structure.md  
- user_management.md  

### 9.2 Developer onboarding
- How to run backend  
- How to run frontend  
- How to create users  

---

## 10. Future Enhancements

### 10.1 Optional features
- File attachments  
- Activity logs  
- Notifications  
- SLA timers  
- Audit trails  
- Dark/light theme  

