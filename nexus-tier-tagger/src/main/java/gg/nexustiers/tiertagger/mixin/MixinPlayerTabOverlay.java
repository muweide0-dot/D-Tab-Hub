package gg.nexustiers.tiertagger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.nexustiers.tiertagger.NexusTierTagger;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
public final class MixinPlayerTabOverlay {
    @ModifyReturnValue(method = "getNameForDisplay", at = @At("RETURN"))
    @Nullable
    public Component nexus$appendTier(@Nullable Component original, PlayerInfo entry) {
        if (!NexusTierTagger.isEnabled() || !NexusTierTagger.getConfig().showInPlayerList) {
            return original;
        }

        return NexusTierTagger.appendTier(entry.getProfile().name(), original);
    }
}
