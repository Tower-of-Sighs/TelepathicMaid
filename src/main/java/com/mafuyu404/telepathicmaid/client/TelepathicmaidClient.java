package com.mafuyu404.telepathicmaid.client;

import com.mafuyu404.telepathicmaid.event.EffectEvent;
import com.mafuyu404.telepathicmaid.event.GarageKitEvent;
import com.mafuyu404.telepathicmaid.event.MaidActionEvent;
import com.mafuyu404.telepathicmaid.event.MaidHUDEvent;
import net.fabricmc.api.ClientModInitializer;

public class TelepathicmaidClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GarageKitEvent.initGarageKitEvents();
        EffectEvent.initEffect();
        MaidActionEvent.initClientEvents();
        MaidHUDEvent.initHUD();
    }
}
