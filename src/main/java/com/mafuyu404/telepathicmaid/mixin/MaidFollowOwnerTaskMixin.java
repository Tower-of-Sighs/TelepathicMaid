package com.mafuyu404.telepathicmaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFollowOwnerTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.diligentstalker.utils.ServerStalkerUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MaidFollowOwnerTask.class, remap = false)
public class MaidFollowOwnerTaskMixin {
    @Inject(method = "maidStateConditions", at = @At("HEAD"), cancellable = true)
    private void modify(EntityMaid maid, CallbackInfoReturnable<Boolean> cir) {
        if (maid.getOwner() instanceof Player player) {
            if (ServerStalkerUtil.hasVisualCenter(player)) {
                cir.setReturnValue(false);
            }
        }
        if (!maid.isHomeModeEnable()) {
            if (Stalker.hasInstanceOf(maid)) {
                cir.setReturnValue(false);
            }
        }
    }
}
