package com.mafuyu404.telepathicmaid;

import com.mafuyu404.telepathicmaid.event.GarageKitEvent;
import com.mafuyu404.telepathicmaid.event.ServerConnectEvent;
import com.mafuyu404.telepathicmaid.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;

public class TelepathicMaid implements ModInitializer {
    public static final String MODID = "telepathicmaid";

    @Override
    public void onInitialize() {
        NetworkHandler.register();
        GarageKitEvent.initServerUseEntityEvent();
        ServerConnectEvent.initServerEvents();
    }
}
