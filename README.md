# AuthService

`AuthService` is the service layer within the Hospital Management System that handles **user authentication and account management**.  
It plays a central role in security, user registration, login, and password update processes of the application.

## Features

### 🔑 User Registration
- Allows new users to register in the system.
- Passwords are securely stored using **bcrypt**.
- Depending on the role (**doctor / patient**), users are added to the corresponding table.
- A confirmation email is sent after registration.

### 🔐 Login
- Users log in with their **national ID number** and password.
- A **JWT token** is generated upon successful login.

### 🔄 Password Management
- Users can verify their current password and set a new one.

### 📧 Email Update
- Users can update their registered email address.

### 📝 Audit Log & Security
- All critical actions are recorded in the audit log.
- User data confidentiality is protected.

# AppointmentService

`AppointmentService` contains all the business logic related to **appointment management** in the Hospital Management System.  
It is responsible for creating doctors’ schedules, generating appointment slots, and managing patient–doctor appointments.

## Features

### 🗓️ Doctor Schedule Management
- A doctor’s weekly schedule can be created (**generateDoctorScheduleResponse**).
- Working days, start–end times, and slot durations are defined.
- The schedule can be modified later if needed (**changeDoctorSchedule**).

### 📆 Appointment Slot Management
- Slots are generated based on a doctor’s schedule for a given date range (**createAppointmentSlot**).
- Break times (e.g., lunch 12:00–13:00) are automatically skipped.
- Conflicts are prevented (no duplicate slots at the same time).

### 🔎 Appointment Slot Filtering
- Patients can search for available slots based on certain criteria (**getFilteredSlots**).
- The returned result includes doctor name, department info, and slot time.

### 🏥 Departments and Doctor Lists
- All registered departments can be listed (**getDepartments**).
- Doctors working in a specific department can be listed (**getDoctors**).

### 📋 Appointment Management
- A patient can book an available slot (**makeAppointment**).
- Notes can be added to an appointment (**addNote**).
- Appointment status can be updated (**changeAppointmentStatus**), e.g.:
  - `BOOKED`
  - `CANCELLED` (if cancelled, the slot becomes available again)
  - `COMPLETED`, etc.

### 👨‍⚕️👩‍🦰 Appointment Retrieval
- Doctors can view their own appointments (**getAppointmentsToDoctor**).
- Patients can view their own appointments (**getAppointmentsToPatient**).
- The response includes appointment time, doctor/patient info, status, and prescription details.

### 💊 Prescription Integration
- If a prescription is issued during an appointment, the response object includes prescription data (medication list, dosage, notes, etc.).
- Mapping (**entity → response DTO**) is handled centrally.

### 📧 Email Notification
- When an appointment is successfully created, an email notification is sent to the patient.


# AuditLogService

`AuditLogService` provides **audit trail capabilities** in the Hospital Management System by recording important actions.  
It logs **who performed what action, when, and from which IP address**, ensuring both security and internal system transparency.

## Features

### 🧑 User Information
- The **username** of the user performing the action is recorded.

### ⚡ Action Tracking
- Tracks which operation was performed (e.g., `"CREATE"`, `"UPDATE"`, `"DELETE"`, `"LOGIN"`, `"LOGOUT"`).

### 📦 Entity Information
- Which **table/entity** was affected (`entityName`).
- Which specific **record** was affected (`entityId`).

### ⏰ Timestamp
- The **date and time** of the action (`timestamp`) are automatically recorded.

### 🌐 IP Address Logging
- The **client IP address** performing the action is logged.

### 📝 Additional Details
- Optional extra information about the action (`additionalDetails`) can be stored.  
- Example: `"Password changed"`, `"Patient record deleted"`, etc.


# CompleteUserInformationService

`CompleteUserInformationService` is used to **complete missing doctor or patient information** for users registered in the system.  
A user is first created as a basic account (`User`), and then detailed information (`Doctor` or `Patient`) is filled through this service according to their role.

## Features

### 🧑‍⚕️ Complete Doctor Information
- Information provided by the user (`hospitalPhoneNumber`, `roomNumber`, `specialization`, `departmentName`) is applied to the existing doctor record.
- If the specified **department** (`Department`) does not exist in the system, an **error** is returned.
- On successful completion, a message like `"Successfully completed user information and user id X"` is returned.



# EmailService
EmailService is used to send automatic email notifications related to user actions and appointments in the system.
It uses Spring’s JavaMailSender infrastructure and has two main functions:
## Features
### 📧 Post-Registration Email
Automatically sends a notification email when a user registers in the system.
The message includes the user’s first name, last name, and a welcome message.
If the email cannot be sent, a MailSendException is thrown.


### 📧 Appointment Confirmation Email
When a patient creates an appointment, a detailed confirmation email is sent.
The message content includes the appointment time, doctor’s name, department name, and a greeting for the patient.
If the email cannot be sent, a MailSendException is thrown.



# MedicineService

`MedicineService` is the service layer used for **medicine management and prescription operations** in the system.  
It includes core functions that support both **patient** and **doctor** workflows.

## Features

### 💊 Add Medicine
- Adds a new medicine to the system.
- Throws an **error** if a medicine with the same **name** and **dosage** already exists.
- When adding a medicine, the following information can be provided:
  - **name**
  - **dosage**
  - **duration**
  - **instructions**
 
### 💊 List Medicine
- Listing all medicine
- Or listing by using filtering features.


