package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.network.ClientMaidConnectPacket;
import com.mafuyu404.telepathicmaid.util.ServerMaidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID)
public class ServerConnectEvent {
    @SubscribeEvent
    public static void useGarageKit(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (level.isClientSide) return;
        BlockPos center = null;
        EntityMaid maid = ServerMaidUtil.findLoadedMaid(player);
        if (maid != null) center = maid.blockPosition();
        BlockPos blockPos = ServerMaidUtil.findUnloadedMaid(player);
        if (blockPos != null) center = blockPos;

        if (center == null) return;
        NetworkHandler.sendToClient((ServerPlayer) player, new ClientMaidConnectPacket(center));
    }
}
