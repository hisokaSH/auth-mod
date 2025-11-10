package com.simpleauth.mixin;

import com.simpleauth.SimpleAuthMod;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
        // Check if player is authenticated
        if (!SimpleAuthMod.getAuthManager().isAuthenticated(player.getUuid())) {
            // Freeze player in place
            player.setVelocity(0, 0, 0);
            player.velocityModified = true;
            
            // Remind player every 5 seconds (100 ticks)
            if (player.age % 100 == 0) {
                if (SimpleAuthMod.getPasswordManager().isRegistered(player.getUuid())) {
                    player.sendMessage(Text.literal("Please login using: ")
                            .formatted(Formatting.YELLOW)
                            .append(Text.literal("/login <password>").formatted(Formatting.GREEN)), true);
                } else {
                    player.sendMessage(Text.literal("Please register using: ")
                            .formatted(Formatting.YELLOW)
                            .append(Text.literal("/register <password> <password>").formatted(Formatting.GREEN)), true);
                }
            }
        }
    }
}
