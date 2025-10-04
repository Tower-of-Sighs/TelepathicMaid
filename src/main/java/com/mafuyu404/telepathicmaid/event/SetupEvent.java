package com.mafuyu404.telepathicmaid.event;

import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.compat.KeyPrompts;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TelepathicMaid.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SetupEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(KeyPrompts::init);
    }
}
