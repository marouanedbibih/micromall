# Keycloak Configuration Guide for MicroMall

This document outlines the necessary configuration steps for setting up Keycloak to work with the MicroMall user API.

## 1. Basic Setup

### Access Keycloak Admin Console

- URL: <http://localhost:7181/admin/>
- Username: admin
- Password: admin

## 2. Realm Configuration

### Create a New Realm

1. Hover over the realm dropdown in the top-left corner (defaults to "master")
2. Click "Create Realm"
3. Enter the name: `micromall`
4. Click "Create"

### Configure Realm Settings

1. Navigate to "Realm Settings"
2. Go to "Login" tab
   - Enable "User registration"
   - Enable "Email as username"
   - Click "Save"
3. Go to "Tokens" tab
   - Set Access Token Lifespan to 30 minutes
   - Click "Save"

## 3. Client Configuration

### Configure admin-cli Client

1. Navigate to "Clients"
2. Find and click on "admin-cli"
3. Change the following settings:
   - Access Type: `confidential`
   - Service Accounts Enabled: `ON`
   - Authorization Enabled: `ON`
   - Click "Save"
4. Go to "Credentials" tab
   - Copy the client secret: `WPj07HtjBYcMlIUiqcIyFsnobCZ0gGF7` (or the generated value)
   - This value must match the `keycloak.client-secret` in `application.yml`

### Service Account Roles

1. Go to "Service Account Roles" tab
2. In "Client Roles" dropdown, select "realm-management"
3. Add the following roles to "Assigned Roles":
   - `manage-users`
   - `view-users`
   - `query-users`
   - `manage-clients`

## 4. Role Configuration

### Create Roles

1. Navigate to "Realm Roles"
2. Click "Create role"
   - Role Name: `ADMIN`
   - Description: `Administrator role with full access`
   - Click "Save"
3. Click "Create role" again
   - Role Name: `CLIENT`
   - Description: `Regular client user`
   - Click "Save"

### Set Role Attributes (Optional)

1. Click on the `ADMIN` role
2. Go to "Attributes" tab
3. Add key-value pair:
   - Key: `isAdmin`
   - Value: `true`
4. Click "Save"

## 5. User Registration

### Configure Registration Form

1. Navigate to "Realm Settings" → "Login"
2. Make sure "User registration" is ON
3. Navigate to "Authentication" → "Required Actions"
4. Enable and set as Default:
   - "Verify Email"
   - "Update Profile"

## 6. Email Configuration (Optional for Development)

1. Navigate to "Realm Settings" → "Email"
2. Configure SMTP settings:
   - Host: `smtp.gmail.com` (or your SMTP server)
   - Port: `587`
   - From: `your-email@example.com`
   - Enable Authentication: ON
   - Username and Password: Your email credentials

## 7. Testing the Configuration

### Create Test Users

1. Navigate to "Users"
2. Click "Add user"
3. Fill in the form:
   - Username: `adminuser`
   - Email: `admin@example.com`
   - First Name: `Admin`
   - Last Name: `User`
   - User Enabled: ON
4. Click "Save"
5. Go to "Credentials" tab
   - Set Password: `adminpass`
   - Temporary: OFF
   - Click "Set Password"
6. Go to "Role Mappings" tab
   - Select `ADMIN` from Available Roles
   - Click "Add selected"

### Repeat for CLIENT user

1. Create another user with username `clientuser`
2. Assign the `CLIENT` role

## 8. Validating Configuration

To validate that your configuration is working correctly:

1. Make sure the client secret in your `application.yml` matches the one from Keycloak
2. Test creating a user through your API:

   ```bash
   curl -X POST \
     http://localhost:8080/api/users \
     -H 'Content-Type: application/json' \
     -d '{
       "username": "testuser",
       "email": "test@example.com",
       "firstName": "Test",
       "lastName": "User",
       "enabled": true,
       "password": "Password123",
       "role": "CLIENT"
     }'
   ```

3. Verify the user was created by checking the Keycloak admin console

## Troubleshooting

### 403 Forbidden Error

If you receive a 403 error when creating users:

1. Verify the client secret is correct
2. Check that the service account has the required roles
3. Make sure the admin-cli client has service accounts enabled

### Authentication Issues

If you have issues authenticating:

1. Check the token URL is correct (should be `http://localhost:7181/realms/micromall/protocol/openid-connect/token`)
2. Verify the client is configured as confidential
3. Make sure SSL settings match your environment

### Missing Roles

If roles aren't appearing:

1. Make sure you've correctly assigned realm roles (not client roles)
2. Check that the KeycloakService is correctly mapping roles from UserRepresentation
