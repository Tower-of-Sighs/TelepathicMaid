package com.mafuyu404.telepathicmaid.event;

import com.mafuyu404.telepathicmaid.TelepathicMaid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID, value = Dist.CLIENT)
public class ClientConnectEvent {

}
