# Fintech Backend API

Spring Boot backend project that simulates a fintech-style onboarding and transaction flow.

## Features
- User registration with OTP generation
- OTP verification and user activation
- JWT-based login
- Auto account creation with default balance = 1000
- Money transfer between users
- Transaction history by user

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- MySQL
- JWT
- Maven
- Postman

## API Endpoints

### Register User
POST `/api/register`

Sample request:
```json
{
  "name": "Sooraj",
  "email": "sooraj@gmail.com",
  "mobile": "9876543210"
}

Verify OTP

POST /api/verify-otp

Sample request:

{
  "mobileOrEmail": "9876543210",
  "otp": "727417"
}
Login

POST /api/login

Sample request:

{
  "mobileOrEmail": "9876543210"
}
Transfer Money

POST /api/transfer

Sample request:

{
  "senderUserId": 1,
  "receiverUserId": 2,
  "amount": 200
}

Transaction History

GET /api/transactions/{userId}
