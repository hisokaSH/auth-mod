package com.simpleauth.auth;

import com.simpleauth.SimpleAuthMod;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthManager {
    private final Set<UUID> authenticatedPlayers = new HashSet<>();
    
    public boolean isAuthenticated(UUID playerUuid) {
        return authenticatedPlayers.contains(playerUuid);
    }
    
    public void authenticate(UUID playerUuid) {
        authenticatedPlayers.add(playerUuid);
        SimpleAuthMod.LOGGER.info("Player " + playerUuid + " authenticated");
    }
    
    public void unauthenticate(UUID playerUuid) {
        authenticatedPlayers.remove(playerUuid);
    }
    
    public void onPlayerJoin(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        PasswordManager passwordManager = SimpleAuthMod.getPasswordManager();
        
        if (passwordManager.isRegistered(uuid)) {
            // Returning user - needs to login
            player.sendMessage(Text.literal("Welcome back! Please login using: ")
                    .formatted(Formatting.YELLOW)
                    .append(Text.literal("/login <password>").formatted(Formatting.GREEN)), false);
        } else {
            // New user - needs to register
            player.sendMessage(Text.literal("Welcome! Please register using: ")
                    .formatted(Formatting.YELLOW)
                    .append(Text.literal("/register <password> <password>").formatted(Formatting.GREEN)), false);
        }
    }
    
    public void onPlayerDisconnect(ServerPlayerEntity player) {
        unauthenticate(player.getUuid());
    }
}
