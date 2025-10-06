package com.mafuyu404.telepathicmaid.network;

import com.mafuyu404.diligentstalker.network.Packet;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHandler {
    public static final ResourceLocation CLIENT_MAID_CONNECT = new ResourceLocation(TelepathicMaid.MODID, "client_maid_connect");
    public static final ResourceLocation SERVER_MAID_GUI = new ResourceLocation(TelepathicMaid.MODID, "server_maid_gui");
    public static final ResourceLocation SERVER_SWITCH_TASK = new ResourceLocation(TelepathicMaid.MODID, "server_switch_task");

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(CLIENT_MAID_CONNECT, (client, handler, buf, responseSender) -> {
            var msg = ClientMaidConnectPacket.decode(buf);
            ClientMaidConnectPacket.handle(msg, client);
        });

        ServerPlayNetworking.registerGlobalReceiver(SERVER_MAID_GUI, (server, player, handler, buf, responseSender) -> {
            var msg = ServerMaidGUIPacket.decode(buf);
            ServerMaidGUIPacket.handle(server, player, msg);
        });
        ServerPlayNetworking.registerGlobalReceiver(SERVER_SWITCH_TASK, (server, player, handler, buf, responseSender) -> {
            var msg = ServerSwitchTaskPacket.decode(buf);
            ServerSwitchTaskPacket.handle(server, player, msg);
        });
    }

    public static void sendToClient(ServerPlayer player, ResourceLocation id, Packet msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(PacketByteBufs.create());
        msg.encode(buf);
        ServerPlayNetworking.send(player, id, buf);
    }

    public static void sendToServer(ResourceLocation id, Packet msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(PacketByteBufs.create());
        msg.encode(buf);
        ClientPlayNetworking.send(id, buf);
    }
}