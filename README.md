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
