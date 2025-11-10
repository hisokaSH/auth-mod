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

public class LoginCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("login")
                .then(CommandManager.argument("password", StringArgumentType.word())
                        .executes(LoginCommand::execute)));
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
        
        // Check if already authenticated
        if (SimpleAuthMod.getAuthManager().isAuthenticated(player.getUuid())) {
            player.sendMessage(Text.literal("You are already logged in!")
                    .formatted(Formatting.YELLOW), false);
            return 0;
        }
        
        // Check if registered
        if (!SimpleAuthMod.getPasswordManager().isRegistered(player.getUuid())) {
            player.sendMessage(Text.literal("You are not registered! Use ")
                    .formatted(Formatting.RED)
                    .append(Text.literal("/register <password> <password>").formatted(Formatting.YELLOW))
                    .append(Text.literal(" to register.").formatted(Formatting.RED)), false);
            return 0;
        }
        
        // Verify password
        if (SimpleAuthMod.getPasswordManager().verifyPassword(player.getUuid(), password)) {
            SimpleAuthMod.getAuthManager().authenticate(player.getUuid());
            player.sendMessage(Text.literal("Successfully logged in! Welcome back!")
                    .formatted(Formatting.GREEN), false);
            SimpleAuthMod.LOGGER.info("Player " + player.getName().getString() + " logged in successfully");
            return 1;
        } else {
            player.sendMessage(Text.literal("Incorrect password! Please try again.")
                    .formatted(Formatting.RED), false);
            SimpleAuthMod.LOGGER.warn("Player " + player.getName().getString() + " failed login attempt");
            return 0;
        }
    }
}
