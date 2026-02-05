🚌 BusBooking – Full Stack Bus Reservation System

Live Application:
👉 https://busbooking09-6.onrender.com

BusBooking is a complete web platform for searching buses, booking seats, managing users, and administering routes.
Built with Spring Boot (Backend) and Thymeleaf + Bootstrap (Frontend) with JWT authentication.

🚀 Features
👤 User Features

Register / Login with JWT authentication

Search buses by route

Book seats with preference

View booking history

Update profile & change password

Responsive UI

🧑‍💻 Admin Features

Add / Update / Delete buses

View all bookings

Manage profile

Dashboard analytics

🔐 Security

JWT based authentication

Role based access (USER / ADMIN)

Protected API routes

CSRF disabled for REST

Secure password hashing

🧱 Tech Stack
Layer	Technology
Backend	Spring Boot 3, Spring Security, JPA
Frontend	Thymeleaf, Bootstrap 5, JavaScript
Database	MySQL (Aiven/AWS RDS)
Auth	JWT
Deployment	Docker + Render

📁 Project Structure
src
└── main
    ├── java/com/Yash/Busbookingapp
    │   ├── config
    │   │   ├── CommandLineRunnerConfig.java
    │   │   ├── DataInitializer.java
    │   │   ├── SecurityConfig.java
    │   │   └── CrosConfig.java
    │   │
    │   ├── controller
    │   │   ├── admincontroller
    │   │   │   ├── AdminBookingController.java
    │   │   │   ├── AdminBusController.java
    │   │   │   └── AdminUserController.java
    │   │   │
    │   │   ├── AppErrorController.java
    │   │   ├── AuthController.java
    │   │   ├── BookingController.java
    │   │   ├── DashboardController.java
    │   │   ├── FaviconController.java
    │   │   ├── PageBookingController.java
    │   │   ├── PageController.java
    │   │   └── UserController.java
    │   │
    │   ├── dto
    │   │   ├── AuthRequest.java
    │   │   ├── BookingConfirmationDto.java
    │   │   ├── BookingRequest.java
    │   │   ├── UserDTO.java
    │   │   └── UserRegistrationRequest.java
    │   │
    │   ├── mapper
    │   │   └── UserMapper.java
    │   │
    │   ├── model
    │   │   ├── Booking.java
    │   │   ├── Bus.java
    │   │   └── Users.java
    │   │
    │   ├── repository
    │   │   ├── BookingRepository.java
    │   │   ├── BusRepository.java
    │   │   └── UserRepository.java
    │   │
    │   ├── security
    │   │   ├── CustomUserDetails.java
    │   │   ├── JwtRequestFilter.java
    │   │   └── JwtUtil.java
    │   │
    │   ├── service
    │   │   ├── impl
    │   │   │   ├── BookingServiceImpl.java
    │   │   │   ├── BusServiceImpl.java
    │   │   │   ├── TicketServiceImpl.java
    │   │   │   └── UserDetailsServiceImpl.java
    │   │   │
    │   │   ├── BookingService.java
    │   │   ├── BusService.java
    │   │   ├── TicketService.java
    │   │   └── UserService.java
    │   │
    │   └── BusbookingappApplication.java
    │
    └── resources
        ├── static
        │   ├── css
        │   │   ├── booking.css
        │   │   ├── bus-finder.css
        │   │   ├── bus.css
        │   │   ├── dashboard.css
        │   │   ├── history.css
        │   │   ├── login.css
        │   │   ├── profile.css
        │   │   ├── register.css
        │   │   └── style.css
        │   │
        │   ├── img
        │   │   └── demo.jpeg
        │   │
        │   └── js
        │       ├── api.js
        │       ├── booking.js
        │       ├── bus.js
        │       ├── dashboard.js
        │       ├── history.js
        │       ├── login.js
        │       ├── profile.js
        │       ├── register.js
        │       └── user-bus.js
        │
        ├── templates
        │   ├── admin
        │   │   ├── Adminbus.html
        │   │   ├── Admindashboard.html
        │   │   └── Adminprofile.html
        │   │
        │   ├── users
        │   │   ├── booking.html
        │   │   ├── dashboard.html
        │   │   ├── history.html
        │   │   ├── profile.html
        │   │   └── user-bus-finder.html
        │   │
        │   ├── error.html
        │   ├── login.html
        │   └── register.html
        │
        └── application.properties

test/java/com/Yash/Busbookingapp
├── controller
│   ├── AdminController
│   │   ├── AdminBookingControllerTest.java
│   │   ├── AdminBusControllerTest.java
│   │   └── AdminUserControllerTest.java
│   │
│   ├── AuthControllerTest.java
│   ├── BookingControllerTest.java
│   └── UserControllerTest.java
│
├── service
│   ├── BookingServiceImplTest.java
│   ├── BusServiceImplTest.java
│   ├── TicketServiceImplTest.java
│   └── UserDetailsServiceImplTest.java
│
└── BusbookingappApplicationTests.java

.gitattributes  
.gitignore  
dockerfile  
mvnw  
mvnw.cmd  
pom.xml

⚙️ Setup & Installation
1️⃣ Prerequisites

Java 21+

Maven

MySQL Server

Docker (optional)

2️⃣ Clone Repository
git clone https://github.com/<your-username>/Busbooking09.git
cd Busbooking09

3️⃣ Configure Database

Create MySQL database:

CREATE DATABASE busbooking;


Edit application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/busbooking
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

4️⃣ Run Application
Using Maven
mvn spring-boot:run

Using Docker
docker build -t busbookingapp .
docker run -p 8080:8080 busbookingapp


Open → http://localhost:8080

🌐 API Endpoints
🔓 Auth Endpoints
1. Register User

POST /api/auth/register

Request

{
  "name": "Yash",
  "email": "yash@mail.com",
  "username": "yash",
  "password": "1234"
}


Response

{
  "message": "User registered successfully"
}

2. Login

POST /api/auth/login

Request

{
  "username": "yash",
  "password": "1234"
}


Response

{
  "token": "jwt-token-here"
}

🚌 Bus Endpoints
3. Get All Buses

GET /api/buses

Response

[
  {
    "id": 1,
    "fromLocation": "Delhi",
    "toLocation": "Mumbai",
    "price": 800,
    "totalSeats": 40
  }
]

4. Add Bus (ADMIN)

POST /admin/api/buses

Request

{
  "fromLocation": "Delhi",
  "toLocation": "Jaipur",
  "departureTime": "2025-06-10T10:00",
  "arrivalTime": "2025-06-10T16:00",
  "price": 600,
  "totalSeats": 35
}

🎫 Booking Endpoints
5. Create Booking

POST /api/bookings

Request

{
  "passengerName": "Rahul",
  "contactInfo": "rahul@mail.com",
  "seatCount": 2,
  "seatPreference": "Window",
  "busId": 1
}


Response

{
  "message": "Booking successful"
}

6. Get User History

GET /api/bookings/history

Response

[
  {
    "id": 10,
    "passengerName": "Rahul",
    "seatCount": 2,
    "busId": 1
  }
]

✅ Data Validation Rules
Field	Rule
email	valid email format
password	min 6 characters
seatCount	≥ 1
locations	not empty
price	positive number
🧪 Testing APIs

Use Postman:

Login → Get JWT

Add Header:

Authorization: Bearer <token>


Call protected APIs.

🧩 Frontend Pages
Page	Route
Login	/login
Register	/register
Dashboard	/users/dashboard
Bus Finder	/users/bus-finder
Booking	/users/booking
History	/users/history
Admin	/admin/Admindashboard
🐳 Deployment on Render
Dockerfile
FROM openjdk:17
COPY target/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

Render Settings

Port: 8080

Env Vars: DB credentials

Start Command: Docker

🛠 Future Improvements

Payment gateway

Seat selection UI

Email confirmation

Admin analytics

👨‍💻 Author

Yash Sengar
Full Stack Developer
