package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.DiligentStalker;
import com.mafuyu404.diligentstalker.event.StalkerControl;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.util.ClientUtil;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID, value = Dist.CLIENT)
public class ClientConnectEvent {
    @SubscribeEvent
    public static void usingGarageKit(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || event.phase == TickEvent.Phase.START) return;
        if (ClientUtil.isUsingGarageKit() && !Stalker.hasInstanceOf(player)) {
            // 在加载范围内
            EntityMaid maid = ClientUtil.findLoadedMaid();
            if (maid != null) Stalker.connect(player, maid);
        } else {
            StalkerControl.setVisualCenter(null);
        }
    }

    @SubscribeEvent
    public static void checkMaidLoading(EntityJoinLevelEvent event) {
        // 远程连接
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!Stalker.hasInstanceOf(player) && event.getEntity() instanceof EntityMaid maid) {
            System.out.print(event.getEntity() + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            boolean matched = MaidUtil.matchMaidModelId(maid, MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem()));
            if (matched) Stalker.connect(player, maid);
        }
//        System.out.print(event.getEntity() + "\n");
    }
}
