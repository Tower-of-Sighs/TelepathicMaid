package com.mafuyu404.telepathicmaid.event;

import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.network.ClientMaidConnectPacket;
import com.mafuyu404.telepathicmaid.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID)
public class ServerConnectEvent {
    @SubscribeEvent
    public static void ticks(TickEvent.PlayerTickEvent event) {
        Stalker instance = Stalker.getInstanceOf(event.player);
        if (instance != null) {
//            Entity entity = instance.getStalker();
//            if (entity instanceof EntityMaid maid) {
//                System.out.print(11111+"\n");
//                MaidUtil.setOperation(maid, false);
//            }
        }
    }

    @SubscribeEvent
    public static void rclick(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (level.isClientSide) return;
        BlockPos blockPos = ServerUtil.findUnloadedMaid(player);
        if (blockPos == null) return;
        NetworkHandler.sendToClient((ServerPlayer) player, new ClientMaidConnectPacket(blockPos));
    }
}
