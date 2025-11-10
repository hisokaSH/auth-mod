# Simple Auth Mod

A simple authentication mod for Minecraft 1.21.1 (Fabric) that adds `/register` and `/login` commands with alphanumeric password support.

## Features

- ✅ `/register <password> <password>` - Register with password confirmation
- ✅ `/login <password>` - Login for returning users
- ✅ Alphanumeric passwords (letters, numbers, special characters supported)
- ✅ Secure password hashing using BCrypt
- ✅ Player lockdown until authenticated (frozen in place)
- ✅ Persistent password storage in `config/simpleauth_passwords.json`
- ✅ Session management (stay logged in until disconnect)

## Installation

1. Install Fabric Loader for Minecraft 1.21.1
2. Install Fabric API
3. Place the mod JAR file in your `mods` folder
4. Start the server

## Usage

### First Time Users
When a player joins for the first time, they will see:
```
Please register using: /register <password> <password>
```

Example:
```
/register MySecurePass123 MySecurePass123
```

### Returning Users
When a registered player joins, they will see:
```
Please login using: /login <password>
```

Example:
```
/login MySecurePass123
```

### Password Requirements
- Minimum length: 4 characters
- Maximum length: 32 characters
- Can contain letters, numbers, and special characters

## Data Storage

Passwords are stored in `config/simpleauth_passwords.json` as BCrypt hashes. The file format is:
```json
{
  "player-uuid": "bcrypt-hashed-password"
}
```

**Note:** Passwords are hashed and cannot be recovered. If a player forgets their password, you'll need to manually delete their entry from the JSON file.

## Building from Source

1. Clone this repository
2. Run `./gradlew build`
3. Find the JAR in `build/libs/`

## License

MIT License
