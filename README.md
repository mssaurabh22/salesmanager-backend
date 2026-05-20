# CRM Application - Authentication Module

A production-ready authentication module for a modular monolith CRM application using Java 17 and Spring Boot 3.

## Architecture

This application follows a modular monolith architecture with clean separation between modules:

- **user-module**: Owns user entity and database operations
- **auth-module**: Handles authentication, JWT tokens, and refresh tokens

The auth-module communicates with the user-module only through the UserService interface using DTOs, ensuring no entity leakage.

## Features

- JWT-based authentication with access and refresh tokens
- Role-based access control (ADMIN, EMPLOYEE)
- Secure password encoding with BCrypt
- Stateless session management
- Comprehensive error handling
- Production-ready security configuration

## Getting Started

### Prerequisites

- Java 17
- Maven 3.6+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Users

The application creates default users on startup:

- **Admin**: `admin@crm.com` / `admin123`
- **Employee**: `employee@crm.com` / `employee123`

## API Endpoints

### Authentication Endpoints

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@crm.com",
  "password": "admin123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer"
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

#### Logout
```http
POST /api/auth/logout
Content-Type: application/json

{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Test Endpoints

#### Public Endpoint (No Authentication Required)
```http
GET /api/test/public
```

#### Protected Endpoint (Authentication Required)
```http
GET /api/test/protected
Authorization: Bearer <access_token>
```

#### Admin Only Endpoint
```http
GET /api/test/admin
Authorization: Bearer <access_token>
```

#### Employee Endpoint (EMPLOYEE or ADMIN role)
```http
GET /api/test/employee
Authorization: Bearer <access_token>
```

## Token Configuration

- **Access Token**: JWT with 15 minutes expiry
- **Refresh Token**: UUID stored in database with 7 days expiry
- **JWT Claims**: userId, role, email (subject)

## Security Features

- Stateless session management
- CSRF protection disabled (suitable for API-only applications)
- JWT-based authentication filter
- Role-based method security
- Password encoding with BCrypt
- Refresh token rotation on login

## Database

The application uses H2 in-memory database for development. Access the H2 console at:
`http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:crmdb`
- **Username**: `sa`
- **Password**: `password`

## Project Structure

```
src/main/java/com/crm/
├── CrmApplication.java
├── auth/
│   ├── controller/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── AuthResponse.java
│   │   ├── LoginRequest.java
│   │   ├── RefreshTokenRequest.java
│   │   └── RefreshTokenResponse.java
│   ├── entity/
│   │   └── RefreshToken.java
│   ├── repository/
│   │   └── RefreshTokenRepository.java
│   ├── security/
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtService.java
│   └── service/
│       ├── AuthService.java
│       ├── RefreshTokenService.java
│       └── impl/
│           ├── AuthServiceImpl.java
│           └── RefreshTokenServiceImpl.java
├── config/
│   ├── DataInitializer.java
│   ├── PasswordEncoderConfig.java
│   └── SecurityConfig.java
├── controller/
│   └── TestController.java
├── exception/
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── user/
    ├── dto/
    │   └── UserAuthDTO.java
    ├── entity/
    │   ├── Role.java
    │   └── User.java
    ├── repository/
    │   └── UserRepository.java
    └── service/
        ├── UserService.java
        └── impl/
            └── UserServiceImpl.java
```

## Usage Example

1. **Login to get tokens**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@crm.com","password":"admin123"}'
```

2. **Use access token for protected endpoints**:
```bash
curl -X GET http://localhost:8080/api/test/protected \
  -H "Authorization: Bearer <your_access_token>"
```

3. **Refresh token when access token expires**:
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<your_refresh_token>"}'
```

4. **Logout to revoke refresh token**:
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<your_refresh_token>"}'
```

## Future Enhancements

- Microservices migration ready architecture
- Enhanced RBAC with permissions
- OAuth2 integration
- Rate limiting
- Audit logging
- Password reset functionality
