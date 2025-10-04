package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID)
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
                    CompoundTag nbt = itemStack.getOrCreateTag();
                    if (!nbt.contains(KEY)) {
                        nbt.putUUID(KEY, maid.getUUID());
                        player.displayClientMessage(Component.translatable("message.diligentstalker.record_success"), true);
                        return;
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
            event.getToolTip().add(Component.translatable("UUID:" + uuid.toString()).withStyle(ChatFormatting.GOLD));
        }
    }
}
