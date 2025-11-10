package com.simpleauth.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.simpleauth.SimpleAuthMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RegisterCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("register")
                .then(CommandManager.argument("password", StringArgumentType.word())
                        .then(CommandManager.argument("confirmPassword", StringArgumentType.word())
                                .executes(RegisterCommand::execute))));
    }
    
    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        if (!source.isExecutedByPlayer()) {
            source.sendError(Text.literal("This command can only be used by players"));
            return 0;
        }
        
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return 0;
        
        String password = StringArgumentType.getString(context, "password");
        String confirmPassword = StringArgumentType.getString(context, "confirmPassword");
        
        // Check if already registered
        if (SimpleAuthMod.getPasswordManager().isRegistered(player.getUuid())) {
            player.sendMessage(Text.literal("You are already registered! Use ")
                    .formatted(Formatting.RED)
                    .append(Text.literal("/login <password>").formatted(Formatting.YELLOW))
                    .append(Text.literal(" to login.").formatted(Formatting.RED)), false);
            return 0;
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            player.sendMessage(Text.literal("Passwords do not match! Please try again.")
                    .formatted(Formatting.RED), false);
            return 0;
        }
        
        // Check password length
        if (password.length() < 4) {
            player.sendMessage(Text.literal("Password must be at least 4 characters long!")
                    .formatted(Formatting.RED), false);
            return 0;
        }
        
        if (password.length() > 32) {
            player.sendMessage(Text.literal("Password must be at most 32 characters long!")
                    .formatted(Formatting.RED), false);
            return 0;
        }
        
        // Register the password
        if (SimpleAuthMod.getPasswordManager().registerPassword(player.getUuid(), password)) {
            SimpleAuthMod.getAuthManager().authenticate(player.getUuid());
            player.sendMessage(Text.literal("Successfully registered! You are now logged in.")
                    .formatted(Formatting.GREEN), false);
            SimpleAuthMod.LOGGER.info("Player " + player.getName().getString() + " registered successfully");
            return 1;
        } else {
            player.sendMessage(Text.literal("Registration failed! Please try again.")
                    .formatted(Formatting.RED), false);
            return 0;
        }
    }
}
