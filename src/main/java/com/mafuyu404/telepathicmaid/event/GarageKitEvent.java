package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;


@EventBusSubscriber(modid = TelepathicMaid.MODID)
public class GarageKitEvent {
    public static final String KEY = "StoredMaidUUID";

    @SubscribeEvent
    public static void rClick(PlayerInteractEvent.EntityInteract event) {
        if (event.getSide().isClient()) return;
        if (event.getTarget() instanceof EntityMaid maid) {
            Player player = event.getEntity();
            String maidId = MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem());
            if (maidId != null) {
                if (MaidUtil.matchMaidModelId(maid, maidId)) {
                    ItemStack itemStack = player.getMainHandItem();
                    if (MaidUtil.getGarageKitUUID(itemStack) == null) {
                        var maidData = itemStack.get(InitDataComponent.MAID_INFO.get());
                        if (maidData != null) {
                            CompoundTag tag = maidData.copyTag();
                            tag.putUUID(KEY, maid.getUUID());
                            itemStack.set(InitDataComponent.MAID_INFO.get(), CustomData.of(tag));
                            player.displayClientMessage(Component.translatable("message.diligentstalker.record_success"), true);
                        }
                    }
                }
//                player.displayClientMessage(Component.translatable("绑定失败"), true);
            }
        }
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        UUID uuid = MaidUtil.getGarageKitUUID(event.getItemStack());
        if (uuid != null) {
            event.getToolTip().add(Component.translatable("UUID:" + uuid).withStyle(ChatFormatting.GOLD));
        }
    }
}
