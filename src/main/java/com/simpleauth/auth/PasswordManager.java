package com.simpleauth.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.simpleauth.SimpleAuthMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PasswordManager {
    private static final File DATA_FILE = new File("config/simpleauth_passwords.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int BCRYPT_COST = 12;
    
    private Map<UUID, String> passwords = new HashMap<>();
    
    public void load() {
        if (!DATA_FILE.exists()) {
            DATA_FILE.getParentFile().mkdirs();
            save();
            SimpleAuthMod.LOGGER.info("Created new password file");
            return;
        }
        
        try (FileReader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> rawData = GSON.fromJson(reader, type);
            
            if (rawData != null) {
                passwords.clear();
                rawData.forEach((key, value) -> {
                    try {
                        passwords.put(UUID.fromString(key), value);
                    } catch (IllegalArgumentException e) {
                        SimpleAuthMod.LOGGER.warn("Invalid UUID in password file: " + key);
                    }
                });
            }
            
            SimpleAuthMod.LOGGER.info("Loaded " + passwords.size() + " registered users");
        } catch (IOException e) {
            SimpleAuthMod.LOGGER.error("Failed to load password file", e);
        }
    }
    
    public void save() {
        try {
            DATA_FILE.getParentFile().mkdirs();
            
            Map<String, String> rawData = new HashMap<>();
            passwords.forEach((uuid, hash) -> rawData.put(uuid.toString(), hash));
            
            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                GSON.toJson(rawData, writer);
            }
            
            SimpleAuthMod.LOGGER.info("Saved password data");
        } catch (IOException e) {
            SimpleAuthMod.LOGGER.error("Failed to save password file", e);
        }
    }
    
    public boolean isRegistered(UUID playerUuid) {
        return passwords.containsKey(playerUuid);
    }
    
    public boolean registerPassword(UUID playerUuid, String password) {
        if (isRegistered(playerUuid)) {
            return false;
        }
        
        String hashedPassword = BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray());
        passwords.put(playerUuid, hashedPassword);
        save();
        return true;
    }
    
    public boolean verifyPassword(UUID playerUuid, String password) {
        if (!isRegistered(playerUuid)) {
            return false;
        }
        
        String hashedPassword = passwords.get(playerUuid);
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
