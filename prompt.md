# ExpenseFlow


You are a full-stack engineer. Build a simple demo of **ExpenseFlow**: an expenses & purchase requests application with **Angular + Tailwind** frontend, **Java 21 / Spring Boot 3** backend, and **PostgreSQL** database. Everything must run using local setup and/or **Docker**. The client will not configure anything; **you** must provision, configure, test all command the client need to run, and deliver the full system.

---

## Development approach
* Use Sprint method to deliver in iterative milestones (10-20 sprints minimum).
* Every sprint end review all code and functionality against the brief. If anything is missing or incomplete, fix it before proceeding, finally do a commit (with message) but don't push to remote repo.
* After all sprints are done, do a final review of the entire codebase and functionality against the brief. Ensure everything is complete, polished, and meets the quality standards before the final commit.


## Rules
* Use context7 if needed for best practices.
* Follow best practices for each technology.
* Take the necessary time, prioritize quality, you have no time constraints.

---

## 1) Scope & Demo Constraints

* **Roles & auth:** No real authentication. A simple screen allows choosing a role (Employee, Manager, Finance) for the session.

* **Main features:**
  * Employee creates and submits **Expense Reports** (with items and simple receipt upload) and **Purchase Requests**.
  * Manager can approve or return with comment.
  * Finance marks as Paid/Reimbursed, manages categories & cost centers, exports a simple CSV.
* **UI/UX:** Simple design, light theme only, basic responsive, no advanced accessibility, no micro-interactions or animations.

---


## 2) Technology Choices (latest stable)

* **Frontend:** Angular (LTS), TypeScript, TailwindCSS, Angular Router, Reactive Forms.
* **Backend:** Java 21, Spring Boot 3 (REST, Validation), Spring Data JPA.
* **Database:** PostgreSQL 17.
* **Packaging & Infra:** Docker and Docker Compose for frontend, backend, and Postgres.
* **Observability:** Simple logs.
---


## 3) Domain & Data Model

* **Entities:**
  * ExpenseReport (title, employeeName, currency, total, status, submittedAt, created/updated)
  * ExpenseItem (reportId, txDate, amount, currency, category, costCenter, attachmentPath)
  * PurchaseRequest (employeeName, vendor, total, status, justification, attachmentPath, submittedAt)
  * Settings (categories[], costCenters[])
* **Statuses:** Draft → Submitted → Returned|Approved → Paid.
* **Integrity:** Basic FK relations.
* **Migrations:** A single schema creation script is enough.

---


## 4) Application Architecture

* **Structure:** Two main folders: `backend/` and `frontend/`.
* **API:** Simple REST with pagination and filtering.
* **Security:** No real auth, just client-side role selection, CORS enabled, basic server-side validation.
* **Files:** Receipts uploaded to a local folder (in backend).

---


## 5) Frontend (Angular + Tailwind)

* **Login panel:** simple role selection (Employee, Manager, Finance).
* **Layouts:** simple sidebar and topbar, basic responsive, light theme only.
* **Screens:**
  * Employee: dashboard, create/edit report, manage items, simple receipt upload, submit.
  * Manager: list of reports to approve/return with comment.
  * Finance: mark as paid, simple CSV export, manage categories/cost centers.

---


## 6) Backend (Spring Boot)

* **Modules/layers:** controllers, services, repositories, domain models, DTOs.
* **Validation:** bean validation on DTOs, simple error handling.
* **Storage:** receipt files uploaded to a local folder.
* **CSV export:** simple CSV export.

---


## 7) Infrastructure Dockerized

* **Containers:** backend, frontend, postgres.
* **Security:** minimum open ports, CORS enabled, basic validation of uploaded files.
* **Deployment:** a single docker-compose to launch everything.

---


## 8) Quality Gates & Testing

* **Coding standards:** clean and readable code.
---


## 9) Deliverables

* **Running system** via Docker Compose.
* **Repository** with clear structure.
* **Simple README:** instructions to run, entity structure.
* 
---


## 10) Modern Design (simplified)

* **Simple and clear design:** basic layout, readable tables and forms.
* **Light theme only.**

---


## 11) Critical Practices

* Keep dependencies recent and stable by use context7 for best practices.
* Consistent typing in TypeScript and Java, no persistence models exposed to the web.
* Validate uploaded files (type/size), clean filenames.
* Simple error handling and clear user messages.