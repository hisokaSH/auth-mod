# Build Instructions for Simple Auth Mod

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Internet connection (for downloading dependencies)

## Building the Mod

### Option 1: Using Gradle Wrapper (Recommended)

1. Open a terminal/command prompt in the mod directory
2. Run the build command:

**On Linux/Mac:**
```bash
./gradlew build
```

**On Windows:**
```cmd
gradlew.bat build
```

3. The compiled JAR file will be in `build/libs/simpleauth-1.0.0.jar`

### Option 2: Using Installed Gradle

If you have Gradle installed globally:
```bash
gradle build
```

## Installation

1. Copy the JAR file from `build/libs/` to your Minecraft server's `mods` folder
2. Ensure you have Fabric Loader and Fabric API installed
3. Start your server

## Development Setup

If you want to set up a development environment:

1. Import the project into your IDE (IntelliJ IDEA or Eclipse)
2. Run `./gradlew genSources` to generate Minecraft source code
3. Use the IDE's Gradle integration to build and run

## Troubleshooting

### Build fails with "Java version" error
- Make sure you have JDK 21 or higher installed
- Set JAVA_HOME environment variable to point to your JDK installation

### Dependencies not downloading
- Check your internet connection
- Try running `./gradlew build --refresh-dependencies`

### "Permission denied" error (Linux/Mac)
- Make gradlew executable: `chmod +x gradlew`

## File Structure

```
SimpleAuthMod/
├── src/main/java/com/simpleauth/
│   ├── SimpleAuthMod.java          # Main mod class
│   ├── auth/
│   │   ├── AuthManager.java        # Handles player authentication state
│   │   └── PasswordManager.java    # Handles password storage/verification
│   ├── command/
│   │   ├── RegisterCommand.java    # /register command
│   │   └── LoginCommand.java       # /login command
│   └── mixin/
│       └── ServerPlayerEntityMixin.java  # Restricts unauthenticated players
├── src/main/resources/
│   ├── fabric.mod.json             # Mod metadata
│   └── simpleauth.mixins.json      # Mixin configuration
├── build.gradle                     # Build configuration
├── gradle.properties                # Version settings
└── README.md                        # User documentation
```

## Customization

You can customize the mod by editing:
- Password length limits in `RegisterCommand.java`
- Action restrictions in `ServerPlayerEntityMixin.java`
- Messages shown to players in command classes
- BCrypt cost factor in `PasswordManager.java` (higher = more secure but slower)
