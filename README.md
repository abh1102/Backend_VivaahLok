# VivaahLok Backend API

A comprehensive Spring Boot 3.x backend for the VivaahLok User Mobile Application - a wedding services marketplace.

## Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Database:** MongoDB
- **Authentication:** JWT with Spring Security
- **Caching:** Redis (for OTP, sessions)
- **API Documentation:** Swagger/OpenAPI 3.0
- **Build Tool:** Maven

## Prerequisites

- Java 17+
- MongoDB 6.0+
- Redis 7.0+
- Maven 3.8+

## Configuration

Update `src/main/resources/application.properties`:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/vivaahlok

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT (Change in production)
jwt.secret=YourSuperSecretKey
jwt.expiration=86400000
```

## Running the Application

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## API Endpoints Summary

### 1. Authentication APIs (`/api/v1/auth`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/send-otp` | Send OTP to phone |
| POST | `/verify-otp` | Verify OTP and login |
| POST | `/resend-otp` | Resend OTP |
| POST | `/register/user` | Register new user |
| POST | `/logout` | Logout user |
| POST | `/refresh-token` | Refresh JWT token |

### 2. User Profile APIs (`/api/v1/users`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/profile` | Get current user profile |
| PUT | `/profile` | Update user profile |
| POST | `/profile/image` | Upload profile image |
| DELETE | `/profile/image` | Delete profile image |

### 3. Home & Discovery APIs (`/api/v1/home`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/banners` | Get promotional banners |
| GET | `/categories` | Get all service categories |
| GET | `/occasions` | Get all occasion types |
| GET | `/nearby` | Get nearby services |
| GET | `/featured` | Get featured vendors |

### 4. Facilities APIs (`/api/v1/facilities`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get all facilities |
| GET | `/{id}` | Get facility details |

### 5. Vendors APIs (`/api/v1/vendors`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get all vendors with filters |
| GET | `/{id}` | Get vendor details |
| GET | `/category/{category}` | Get vendors by category |
| GET | `/search` | Search vendors |
| GET | `/{id}/reviews` | Get vendor reviews |
| POST | `/{id}/reviews` | Add review |

### 6. Bookings APIs (`/api/v1/bookings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get user's bookings |
| GET | `/{id}` | Get booking details |
| POST | `/` | Create new booking |
| PUT | `/{id}` | Update booking |
| DELETE | `/{id}` | Cancel booking |
| PUT | `/{id}/status` | Update booking status |

### 7. Wishlist APIs (`/api/v1/wishlist`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get user's wishlist |
| POST | `/` | Add to wishlist |
| DELETE | `/{vendorId}` | Remove from wishlist |

### 8. Reminders APIs (`/api/v1/reminders`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get all reminders |
| GET | `/{id}` | Get reminder details |
| POST | `/` | Create reminder |
| PUT | `/{id}` | Update reminder |
| DELETE | `/{id}` | Delete reminder |
| DELETE | `/` | Delete all reminders |

### 9. Notifications APIs (`/api/v1/notifications`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register-device` | Register FCM token |
| GET | `/` | Get notifications history |
| PUT | `/{id}/read` | Mark as read |
| PUT | `/read-all` | Mark all as read |

### 10. Invitation Cards APIs (`/api/v1/cards`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/designs` | Get card design templates |
| POST | `/` | Create invitation card |
| GET | `/` | Get user's created cards |
| GET | `/{id}` | Get card details |
| DELETE | `/{id}` | Delete card |
| GET | `/{id}/share` | Get shareable link |

### 11. Contact APIs (`/api/v1`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/contact/vendor` | Contact vendor |
| GET | `/contacts` | Get user's contacts |
| POST | `/contacts/sync` | Sync contacts |

### 12. Settings APIs (`/api/v1/settings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get user settings |
| PUT | `/` | Update settings |
| PUT | `/location` | Update location |

### 13. Master Data APIs (`/api/v1/master`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/cities` | Get list of cities |
| GET | `/service-types` | Get service types |
| GET | `/occasion-types` | Get occasion types |

## Project Structure

```
src/main/java/com/vivaahlok/vivahlok/
├── config/                 # Configuration classes
├── controller/             # REST Controllers
├── dto/                    # Data Transfer Objects
│   ├── request/           # Request DTOs
│   └── response/          # Response DTOs
├── entity/                 # MongoDB Document entities
├── exception/              # Custom exceptions
├── repository/             # MongoDB repositories
├── security/               # Security configuration
├── service/                # Business logic services
└── util/                   # Utility classes
```

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Total APIs: 55

| Module | Count |
|--------|-------|
| Authentication | 6 |
| User Profile | 4 |
| Home & Discovery | 5 |
| Facilities | 2 |
| Vendors | 6 |
| Bookings | 6 |
| Wishlist | 3 |
| Reminders | 6 |
| Notifications | 4 |
| Invitation Cards | 6 |
| Contact | 3 |
| Settings | 3 |
| Master Data | 3 |

## License

Proprietary - VivaahLok
