package com.mafuyu404.telepathicmaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.mafuyu404.diligentstalker.init.Stalker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractMaidContainer.class, remap = false)
public class AbstractMaidContainerMixin {
    @Shadow @Final protected EntityMaid maid;

    @Inject(method = "addPlayerInv", at = @At("HEAD"), cancellable = true)
    private void cancel(Inventory playerInventory, CallbackInfo ci) {
        if (Stalker.hasInstanceOf(playerInventory.player)) {
            ci.cancel();
        }
    }

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true, remap = true)
    private void valid(Player playerIn, CallbackInfoReturnable<Boolean> cir) {
        Stalker instance = Stalker.getInstanceOf(playerIn);
        if (instance != null) {
            if (instance.getStalker().getUUID().equals(maid.getUUID())) {
                cir.setReturnValue(true);
            }
        }
    }
}
