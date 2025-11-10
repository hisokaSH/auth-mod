# Windows Build Instructions - IMPORTANT

## Problem
The Gradle wrapper jar file could not be included due to network restrictions. You have two options:

## Option 1: Install Gradle Directly (EASIEST)

1. **Download Gradle**
   - Go to: https://gradle.org/releases/
   - Download Gradle 8.8 (Binary-only)
   - Extract to a folder (e.g., `C:\Gradle\gradle-8.8`)

2. **Add to PATH**
   - Press Windows + R, type `sysdm.cpl`, press Enter
   - Go to "Advanced" tab → "Environment Variables"
   - Under "System Variables", find "Path", click "Edit"
   - Click "New" and add: `C:\Gradle\gradle-8.8\bin`
   - Click OK on all windows

3. **Build the mod**
   - Open PowerShell in the mod folder
   - Run: `gradle build`
   - Find JAR in `build\libs\simpleauth-1.0.0.jar`

## Option 2: Generate Gradle Wrapper

If you already have Gradle installed somewhere:

1. Open PowerShell in the mod folder
2. Run: `gradle wrapper --gradle-version 8.8`
3. This will download the wrapper jar
4. Then run: `.\gradlew.bat build`

## Option 3: Use a Pre-built JAR (FASTEST)

Instead of building from source, I can provide you a pre-built JAR file.

**Important:** If you want me to build it for you, let me know and I'll create a pre-compiled JAR file that you can use directly without building!

## Verification

After building, you should have:
- `build\libs\simpleauth-1.0.0.jar` - This is your mod file
- Place it in your server's `mods\` folder
- Restart the server

## Need Help?

If you're having trouble, I can:
1. Provide a pre-built JAR file (ready to use)
2. Create a different build system
3. Help troubleshoot your Gradle installation

Just let me know what you'd prefer!
