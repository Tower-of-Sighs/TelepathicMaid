package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class GarageKitEvent {
    public static final String KEY = "StoredMaidUUID";

    public static void initGarageKitEvents() {
        ItemTooltipCallback.EVENT.register(GarageKitEvent::tooltip);
    }

    public static void initServerUseEntityEvent() {
        UseEntityCallback.EVENT.register(GarageKitEvent::rClick);
    }

    public static InteractionResult rClick(Player player, Level world, InteractionHand hand, Entity target, @Nullable EntityHitResult hitResult) {
        if (target instanceof EntityMaid maid) {
            String maidId = MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem());
            if (maidId != null) {
                if (MaidUtil.matchMaidModelId(maid, maidId)) {
                    ItemStack itemStack = player.getMainHandItem();
                    CompoundTag nbt = itemStack.getOrCreateTag();
                    if (!nbt.contains(KEY)) {
                        nbt.putUUID(KEY, maid.getUUID());
                        player.displayClientMessage(Component.translatable("message.diligentstalker.record_success"), true);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void tooltip(ItemStack stack, TooltipFlag flag, List<Component> tooltip) {
        UUID uuid = MaidUtil.getGarageKitUUID(stack);
        if (uuid != null) {
            tooltip.add(Component.translatable("UUID:" + uuid).withStyle(ChatFormatting.GOLD));
        }
    }
}
