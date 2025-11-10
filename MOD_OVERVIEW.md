# Simple Auth Mod - Complete Overview

## What This Mod Does

This is a complete replacement for the datapack-based login system. It provides:

1. **True Commands**: Uses Minecraft's proper command system
   - `/register <password> <password>` for new users
   - `/login <password>` for returning users

2. **Alphanumeric Passwords**: Supports any combination of letters, numbers, and special characters
   - Minimum length: 4 characters
   - Maximum length: 32 characters

3. **Security**: Uses BCrypt hashing algorithm
   - Passwords are never stored in plain text
   - Industry-standard security

4. **Player Restrictions**: Unauthenticated players are:
   - Frozen in place (cannot move)
   - Reminded every 5 seconds to login/register
   - Cannot interact with the world

5. **Data Persistence**: Passwords saved in `config/simpleauth_passwords.json`
   - Survives server restarts
   - Easy to backup

## Key Differences from Old Datapack

| Feature | Old Datapack | New Mod |
|---------|--------------|---------|
| Commands | `/trigger login set 12345` | `/login password123` |
| Registration | `/trigger login set 12345` | `/register password123 password123` |
| Password Type | Numbers only | Letters, numbers, symbols |
| Password Storage | Scoreboard (plain numbers) | JSON file (BCrypt hashed) |
| Security | Weak (visible numbers) | Strong (hashed passwords) |
| User Experience | Confusing triggers | Clear commands |

## File Structure

```
SimpleAuthMod/
├── src/main/java/com/simpleauth/
│   ├── SimpleAuthMod.java              # Main initialization
│   ├── auth/
│   │   ├── AuthManager.java            # Tracks who is logged in
│   │   └── PasswordManager.java        # Saves/verifies passwords
│   ├── command/
│   │   ├── RegisterCommand.java        # Handles /register
│   │   └── LoginCommand.java           # Handles /login
│   └── mixin/
│       └── ServerPlayerEntityMixin.java # Freezes unauthenticated players
└── src/main/resources/
    ├── fabric.mod.json                  # Mod information
    └── simpleauth.mixins.json           # Mixin configuration
```

## How It Works

### 1. Player Joins Server
- `AuthManager.onPlayerJoin()` is called
- Checks if player is registered
- Shows appropriate message (register or login)

### 2. Player Uses /register
- `RegisterCommand.execute()` is called
- Validates passwords match
- Checks password length
- Hashes password with BCrypt
- Saves to JSON file
- Marks player as authenticated

### 3. Player Uses /login
- `LoginCommand.execute()` is called
- Retrieves hashed password from file
- Uses BCrypt to verify entered password
- If correct, marks player as authenticated

### 4. Every Tick
- `ServerPlayerEntityMixin.onTick()` is called
- If player not authenticated:
  - Freezes player movement
  - Shows reminder message every 5 seconds

### 5. Player Disconnects
- `AuthManager.onPlayerDisconnect()` is called
- Removes player from authenticated list
- Player must re-login next time

## Dependencies

The mod requires:
1. **Fabric Loader** 0.16.0 or higher
2. **Fabric API** for Minecraft 1.21.1
3. **BCrypt library** (included automatically)

## Building the Mod

1. Install JDK 21
2. Run `./gradlew build`
3. Find JAR in `build/libs/simpleauth-1.0.0.jar`

## Installation

1. Place JAR in server's `mods/` folder
2. Ensure Fabric Loader and Fabric API are installed
3. Start server
4. Password file will be created at `config/simpleauth_passwords.json`

## Configuration

Currently, all configuration is in the code:
- Password min/max length: `RegisterCommand.java` lines 35-42
- BCrypt cost factor: `PasswordManager.java` line 23
- Reminder frequency: `ServerPlayerEntityMixin.java` line 25

## Future Enhancements (Optional)

Possible additions you could make:
- `/changepassword <old> <new>` command
- Configurable password requirements
- Login attempt limits
- Timeout after X failed attempts
- Admin command to reset passwords
- Session timeout (auto-logout after X minutes)
- Permission levels for different player groups

## Security Notes

- Passwords are hashed with BCrypt (cost factor 12)
- Cannot be reversed or recovered
- If player forgets password, admin must delete their entry from JSON
- JSON file should be backed up regularly
- Consider file permissions on the config folder

## License

MIT License - Free to use, modify, and distribute
