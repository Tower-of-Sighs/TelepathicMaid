package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.util.ClientUtil;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientConnectEvent {
    public static BlockPos maidPos;

    @SubscribeEvent
    public static void usingGarageKit(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || event.phase == TickEvent.Phase.START) return;
        if (ClientUtil.isUsingGarageKit()) {
            EntityMaid maid = ClientUtil.findLoadedMaid();
            if (maid != null) Stalker.connect(player, maid);
        }
    }

    @SubscribeEvent
    public static void checkMaidLoading(EntityJoinLevelEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (event.getEntity() instanceof EntityMaid maid) {
            boolean matched = MaidUtil.matchMaidModelId(maid, MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem()));
            if (matched) Stalker.connect(player, maid);
        }
    }
}
