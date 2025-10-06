package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.event.StalkerEvents;
import com.mafuyu404.diligentstalker.utils.ControllableUtils;
import com.mafuyu404.telepathicmaid.network.ClientMaidConnectPacket;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import com.mafuyu404.telepathicmaid.util.ServerMaidUtil;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ServerConnectEvent {
    public static void initServerEvents() {
        UseItemCallback.EVENT.register(ServerConnectEvent::onUseGarageKit);
        StalkerEvents.CONNECT.register(ServerConnectEvent::onConnect);
        StalkerEvents.DISCONNECT.register(ServerConnectEvent::onDisconnect);
    }

    private static InteractionResultHolder<ItemStack> onUseGarageKit(Player player, Level level, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResultHolder.pass(player.getItemInHand(hand));

        var stack = player.getItemInHand(hand);

        if (MaidUtil.getGarageKitUUID(stack) == null) return InteractionResultHolder.pass(stack);

        BlockPos center = null;

        EntityMaid maid = ServerMaidUtil.findLoadedMaid(player);
        if (maid != null) center = maid.blockPosition();

        BlockPos blockPos = ServerMaidUtil.findUnloadedMaid(player);
        if (blockPos != null) center = blockPos;

        if (center == null) return InteractionResultHolder.pass(stack);

        NetworkHandler.sendToClient((ServerPlayer) player, NetworkHandler.CLIENT_MAID_CONNECT, new ClientMaidConnectPacket(center));

        return InteractionResultHolder.success(stack);
    }

    public static void onConnect(Player player, Entity entity) {
        if (entity instanceof EntityMaid maid) {
            ControllableUtils.setFuel(maid, 100);
            maid.setDeltaMovement(Vec3.ZERO);
        }
    }

    public static void onDisconnect(Player player, Entity entity) {
        if (entity instanceof EntityMaid maid) {
            if (!maid.isHomeModeEnable()) {
                maid.setPosRaw(player.getX(), player.getY(), player.getZ());
//                maid.teleportToOwner(player);
            }
        }
    }

//    @SubscribeEvent
//    public static void maidTick(MaidTickEvent event) {
//        CompoundTag tag = event.getMaid().getPersistentData();
//        if (tag.contains("MaidFollowDelay") && tag.getInt("MaidFollowDelay") > 0) {
//            System.out.print(tag.getInt("MaidFollowDelay")+"\n");
//            tag.putInt("MaidFollowDelay", tag.getInt("MaidFollowDelay") - 1);
//        }
//    }
}
