package com.mafuyu404.telepathicmaid.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.mafuyu404.diligentstalker.DiligentStalker.MODID;

public class NetworkHandler {
    private static final String PROTOCOL = "1.0";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID)
                .versioned(PROTOCOL);
        registrar.playToClient(
                ClientMaidConnectPacket.TYPE,
                ClientMaidConnectPacket.STREAM_CODEC,
                ClientMaidConnectPacket::handle
        );
        registrar.playToServer(
                ServerMaidGUIPacket.TYPE,
                ServerMaidGUIPacket.STREAM_CODEC,
                ServerMaidGUIPacket::handle
        );
        registrar.playToServer(
                ServerSwitchTaskPacket.TYPE,
                ServerSwitchTaskPacket.STREAM_CODEC,
                ServerSwitchTaskPacket::handle
        );
    }
}