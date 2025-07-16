package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.mafuyu404.diligentstalker.api.IChunkMap;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.network.ClientMaidConnectPacket;
import com.mafuyu404.telepathicmaid.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID)
public class ServerConnectEvent {
    @SubscribeEvent
    public static void ticks(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.START) return;
        if (event.player.getPersistentData().contains("visualCenter")) {
            System.out.print(event.player.getPersistentData().get("visualCenter")+"\n");
        }
        if (!(player.getMainHandItem().getItem() instanceof ItemGarageKit)) {
            com.mafuyu404.diligentstalker.init.ServerUtil.clearVisualCenter(player);
        }
//        if (!Stalker.hasInstanceOf(player)) return;
//        visualCenter = null;
    }

    @SubscribeEvent
    public static void useGarageKit(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        if (level.isClientSide) return;
        if (player.getPersistentData().contains("visualCenter")) return;
        BlockPos blockPos = ServerUtil.findUnloadedMaid(player);
        if (blockPos == null) return;
        System.out.print("using\n");
        com.mafuyu404.diligentstalker.init.ServerUtil.setVisualCenter(player, blockPos);
        NetworkHandler.sendToClient((ServerPlayer) player, new ClientMaidConnectPacket(blockPos));
//        ((IChunkMap) ((ServerLevel) level).getChunkSource().chunkMap).loadLevelChunk((ServerPlayer) player, new ChunkPos(blockPos));
//        player.getPersistentData().putLong("visualCenter", blockPos.asLong());
    }
}
