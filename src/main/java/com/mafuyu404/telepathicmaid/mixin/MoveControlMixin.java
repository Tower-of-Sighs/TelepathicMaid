package com.mafuyu404.telepathicmaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.control.MaidMoveControl;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.api.MoveControlAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MaidMoveControl.class, remap = false)
@Implements(@Interface(iface = MoveControlAccessor.class, prefix = "lazy$"))
public class MoveControlMixin extends MoveControl implements MoveControlAccessor {
    public MoveControlMixin(Mob p_24983_) {
        super(p_24983_);
    }

    public void setMove(String op) {
        operation = Operation.valueOf(op);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void qqq(CallbackInfo ci) {
        if (Stalker.hasInstanceOf(this.mob)) {
            ci.cancel();
        }
    }
}
