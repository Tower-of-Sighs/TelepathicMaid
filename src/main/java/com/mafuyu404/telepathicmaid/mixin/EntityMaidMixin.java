package com.mafuyu404.telepathicmaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.api.IControllable;
import com.mafuyu404.diligentstalker.utils.StalkerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mafuyu404.diligentstalker.utils.StalkerUtil.limitSpeed;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class EntityMaidMixin implements IControllable {
    @Shadow public abstract Mob asEntity();

    @Shadow public abstract EntityMaid asStrictMaid();

    @Override
    public void pushAdditionalControl(CompoundTag compoundTag) {

    }

    @Override
    public Vec3 tickServerControl(CompoundTag input, Vec3 motion) {
        EntityMaid maid = this.asStrictMaid();
        float xRot = input.getFloat("xRot");
        float yRot = input.getFloat("yRot");
        maid.setXRot(xRot);
        maid.setYRot(yRot);
        float speed = input.getBoolean("Shift") ? 0.2f : 0.45f;
        Vec3 forward = Vec3.ZERO;
        Vec3 right = Vec3.ZERO;
        Vec3 result = Vec3.ZERO;
        if (input.getBoolean("Up") || input.getBoolean("Down")) {
            float x = 0;
            float z = 0;
            Vec3 lookAngle = StalkerUtil.calculateViewVector(xRot, yRot);
            double xz = Math.sqrt(lookAngle.x * lookAngle.x + lookAngle.z * lookAngle.z);
            float forwardX = (float) (lookAngle.x / xz);
            float forwardZ = (float) (lookAngle.z / xz);
            if (input.getBoolean("Up")) {
                x += forwardX * speed;
                z += forwardZ * speed;
            }
            if (input.getBoolean("Down")) {
                x -= forwardX * speed;
                z -= forwardZ * speed;
            }
            forward = limitSpeed(new Vec3(x, 0, z), speed);
        }
        if (input.getBoolean("Left") || input.getBoolean("Right")) {
            float x = 0;
            float z = 0;
            Vec3 subAngle = StalkerUtil.calculateViewVector(xRot, yRot - 90);
            double xz = Math.sqrt(subAngle.x * subAngle.x + subAngle.z * subAngle.z);
            float forwardX = (float) (subAngle.x / xz);
            float forwardZ = (float) (subAngle.z / xz);
            if (input.getBoolean("Left")) {
                x += forwardX * speed;
                z += forwardZ * speed;
            }
            if (input.getBoolean("Right")) {
                x -= forwardX * speed;
                z -= forwardZ * speed;
            }
            right = limitSpeed(new Vec3(x, 0, z), speed);
        }

        maid.setInSittingPose(input.getBoolean("Shift"));

        result = result.add(forward).add(right);

        result = new Vec3(
                Mth.lerp(0.3f, motion.x, motion.x + result.x),
                motion.y,
                Mth.lerp(0.3f, motion.z, motion.z + result.z)
        );

        result = limitSpeed(new Vec3(result.x, 0, result.z), speed).add(0, result.y, 0);

//        result = new Vec3(result.x * 0.8, result.y, result.z * 0.8);

        CompoundTag data = maid.getPersistentData();
        if (!data.getBoolean("JustStoreLastJumpInput")) {
            boolean canJump = maid.onGround() || maid.isInWater();
            if (input.getBoolean("Jump") && canJump) {
                result = result.add(0, 0.5, 0);

            }
        }
        data.putBoolean("JustStoreLastJumpInput", input.getBoolean("Jump"));

        return result;
    }
}
