package com.mafuyu404.telepathicmaid;

import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(TelepathicMaid.MODID)
public class TelepathicMaid {
    public static final String MODID = "telepathicmaid";

    public TelepathicMaid(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(NetworkHandler::register);

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.init());
    }
}
