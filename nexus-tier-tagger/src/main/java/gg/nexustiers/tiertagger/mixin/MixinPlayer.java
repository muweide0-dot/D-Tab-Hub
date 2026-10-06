package gg.nexustiers.tiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.nexustiers.tiertagger.NexusTierTagger;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public final class MixinPlayer {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Component nexus$appendTier(Component original) {
        if (!NexusTierTagger.isEnabled() || !NexusTierTagger.getConfig().showInNametags) {
            return original;
        }

        Player player = (Player) (Object) this;
        return NexusTierTagger.appendTier(player.getName().getString(), original);
    }
}
