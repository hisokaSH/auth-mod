package com.simpleauth;

import com.simpleauth.auth.AuthManager;
import com.simpleauth.auth.PasswordManager;
import com.simpleauth.command.LoginCommand;
import com.simpleauth.command.RegisterCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAuthMod implements ModInitializer {
    public static final String MOD_ID = "simpleauth";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static PasswordManager passwordManager;
    private static AuthManager authManager;
    
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Simple Auth Mod");
        
        // Initialize managers
        passwordManager = new PasswordManager();
        authManager = new AuthManager();
        
        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RegisterCommand.register(dispatcher);
            LoginCommand.register(dispatcher);
        });
        
        // Handle server start
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            passwordManager.load();
            LOGGER.info("Simple Auth Mod loaded successfully");
        });
        
        // Handle server stop
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            passwordManager.save();
            LOGGER.info("Simple Auth Mod saved data");
        });
        
        // Handle player join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            authManager.onPlayerJoin(handler.player);
        });
        
        // Handle player disconnect
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            authManager.onPlayerDisconnect(handler.player);
        });
    }
    
    public static PasswordManager getPasswordManager() {
        return passwordManager;
    }
    
    public static AuthManager getAuthManager() {
        return authManager;
    }
}
