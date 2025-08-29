# Spring Security Example

This module demonstrates comprehensive Spring Security implementation including authentication, authorization, role-based access control, and security best practices.

## Features

### 🔐 Authentication & Authorization

- **User Authentication**: Custom login form with Spring Security
- **Role-Based Access Control**: Three user roles (USER, MODERATOR, ADMIN)
- **Method-Level Security**: @PreAuthorize annotations for fine-grained control
- **Custom Login Flow**: Custom login page and authentication handling

### 🗄️ Data Management

- **JPA Integration**: User entity with Spring Data JPA
- **Password Encoding**: BCrypt password hashing
- **H2 Database**: In-memory database for demonstration
- **User Repository**: Custom user management service

### 🌐 Web Interface

- **Thymeleaf Templates**: Server-side templating with security integration
- **Responsive Design**: Clean, modern UI for all security features
- **Role-Based Navigation**: Different menus based on user roles
- **Access Control**: Visual feedback for unauthorized access

## Security Configuration

### User Roles & Permissions

| Role | Access Level | Endpoints | Features |
|------|-------------|-----------|----------|
| **USER** | Basic | `/user/**`, `/api/user/**` | Profile access, basic features |
| **MODERATOR** | Elevated | `/moderator/**`, `/api/moderator/**` | Content moderation, user management |
| **ADMIN** | Full | `/admin/**`, `/api/admin/**` | Complete system access, user management |

### Protected Endpoints

- **Public**: `/`, `/home`, `/login`, `/api/public`
- **User**: `/user/profile`, `/api/user/info`
- **Moderator**: `/moderator/panel`, `/api/moderator/stats`
- **Admin**: `/admin/panel`, `/api/admin/users`

## Demo Users

The application comes with pre-configured users for testing:

| Username | Password | Role | Access Level |
|----------|----------|------|--------------|
| `admin` | `admin123` | ADMIN | Full system access |
| `moderator` | `mod123` | MODERATOR | User + moderator features |
| `user` | `user123` | USER | Basic user features |

## Usage

### Running the Application

```bash
make run-security
```

### Testing Security Features

1. **Access the application**: <http://localhost:8080>
2. **Login with different users** to test role-based access
3. **Try accessing restricted areas** to see access control in action
4. **Test API endpoints** to see JSON responses with user context

### Testing Scenarios

#### Scenario 1: User Access Control

1. Login as `user` (password: `user123`)
2. Try to access `/admin/panel` → Should see "Access Denied"
3. Access `/user/profile` → Should work fine

#### Scenario 2: Admin Full Access

1. Login as `admin` (password: `admin123`)
2. Access all areas: `/admin/panel`, `/moderator/panel`, `/user/profile`
3. View all users in the admin panel

#### Scenario 3: API Security Testing

1. Test `/api/public` → Works without login
2. Test `/api/user/info` → Requires user login
3. Test `/api/admin/users` → Requires admin role

## Database

### H2 Console Access

- **URL**: <http://localhost:8080/h2-console>
- **JDBC URL**: `jdbc:h2:mem:securitydb`
- **Username**: `sa`
- **Password**: `password`

### Database Schema

The application automatically creates:

- `users` table with user information
- `roles` enum for user permissions
- Sample data for demonstration

## Code Structure

```none
05_security/
├── src/main/java/com/example/spring/security/
│   ├── SecurityApplication.java          # Main application class
│   ├── config/
│   │   └── SecurityConfig.java          # Spring Security configuration
│   ├── controller/
│   │   └── SecurityController.java      # Web endpoints and API
│   ├── entity/
│   │   ├── User.java                    # User entity with UserDetails
│   │   └── Role.java                    # User role enumeration
│   ├── repository/
│   │   └── UserRepository.java          # Data access layer
│   └── service/
│       └── UserService.java             # User management service
└── src/main/resources/
    ├── application.yml                   # Application configuration
    ├── static/
    │   └── css/
    │       └── security.css             # Common CSS styles for all templates
    └── templates/                        # Thymeleaf templates
        ├── home.html                     # Home page
        ├── login.html                    # Login form
        ├── dashboard.html                # User dashboard
        └── access-denied.html            # Access denied page
```

## Security Features Demonstrated

### 1. Authentication

- Custom login form
- Password encoding with BCrypt
- Session management
- Logout functionality

### 2. Authorization

- URL-based access control
- Role-based permissions
- Method-level security
- Access denied handling

### 3. User Management

- User entity with Spring Security integration
- Role-based user creation
- Password validation and encoding
- User repository with JPA

### 4. Web Security

- CSRF protection (disabled for H2 console only)
- Secure headers configuration
- Form-based authentication
- Custom error pages

## CSS Organization

### Centralized Styling

- **Common CSS File**: `src/main/resources/static/css/security.css`
- **Responsive Design**: Mobile-first approach with media queries
- **Consistent Theming**: Unified color scheme and typography
- **Component-Based**: Modular CSS classes for different UI elements

### CSS Features

- **Role-Based Colors**: Different colors for admin, moderator, and user elements
- **Interactive Elements**: Hover effects and transitions
- **Form Styling**: Consistent input fields and buttons
- **Card Layouts**: Clean, modern card-based design
- **Navigation**: Responsive navigation with role-based styling
- **Utility Classes**: Helper classes for common styling needs

### Template Integration

All Thymeleaf templates now use the external CSS file:

```html
<link rel="stylesheet" th:href="@{/css/security.css}">
```

This approach provides:

- **Maintainability**: Single source of truth for all styles
- **Performance**: CSS can be cached by browsers
- **Consistency**: Uniform appearance across all pages
- **Scalability**: Easy to add new styles and modify existing ones

## Customization

### Adding New Roles

1. Add new role to `Role` enum
2. Update `SecurityConfig` with new role mappings
3. Add role-specific endpoints in controller
4. Create role-specific templates

### Adding New Security Rules

1. Modify `SecurityConfig.filterChain()` method
2. Add new request matchers with role requirements
3. Update controller methods with appropriate annotations

### Custom Authentication

1. Implement custom `AuthenticationProvider`
2. Add custom authentication logic in `UserService`
3. Configure custom authentication in `SecurityConfig`

## Best Practices Demonstrated

1. **Password Security**: BCrypt encoding for secure password storage
2. **Role Hierarchy**: Logical role progression (USER → MODERATOR → ADMIN)
3. **Access Control**: Multiple levels of security (URL, method, view)
4. **Error Handling**: Custom access denied pages and error messages
5. **User Experience**: Clear navigation and role-based UI elements
6. **Security Headers**: Proper security configuration for production use

## Production Considerations

- Replace H2 with production database (PostgreSQL, MySQL)
- Enable CSRF protection for all endpoints
- Add rate limiting and brute force protection
- Implement password reset functionality
- Add audit logging for security events
- Use HTTPS in production
- Consider OAuth2/OpenID Connect for enterprise use
