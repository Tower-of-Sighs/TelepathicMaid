package com.mafuyu404.telepathicmaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import com.mafuyu404.telepathicmaid.network.ServerMaidGUIPacket;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void gui(Screen screen, CallbackInfo ci) {
        if (screen instanceof InventoryScreen) {
            EntityMaid maid = ClientMaidUtil.getControllingMaid();
            if (maid != null) {
                NetworkHandler.sendToServer(NetworkHandler.SERVER_MAID_GUI, new ServerMaidGUIPacket());
                ci.cancel();
            }
        }
    }
}
