package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.event.StalkerEvents;
import com.mafuyu404.diligentstalker.init.NetworkHandler;
import com.mafuyu404.diligentstalker.utils.ControllableUtils;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.network.ClientMaidConnectPacket;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import com.mafuyu404.telepathicmaid.util.ServerMaidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = TelepathicMaid.MODID)
public class ServerConnectEvent {
    @SubscribeEvent
    public static void useGarageKit(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (level.isClientSide) return;

        if (MaidUtil.getGarageKitUUID(player.getMainHandItem()) == null) return;

        BlockPos center = null;
        EntityMaid maid = ServerMaidUtil.findLoadedMaid(player);
        if (maid != null) center = maid.blockPosition();

        BlockPos blockPos = ServerMaidUtil.findUnloadedMaid(player);
        if (blockPos != null) center = blockPos;

        if (center == null) return;
        NetworkHandler.sendToClient((ServerPlayer) player, new ClientMaidConnectPacket(center));
    }

    @SubscribeEvent
    public static void onConnect(StalkerEvents.ConnectEvent event) {
        if (event.getStalker() instanceof EntityMaid maid) {
            ControllableUtils.setFuel(maid, 100);
            maid.setDeltaMovement(Vec3.ZERO);
        }
    }

    @SubscribeEvent
    public static void onDisconnect(StalkerEvents.DisconnectEvent event) {
        if (event.getStalker() instanceof EntityMaid maid) {
            Player player = event.getEntity();
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
