package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.diligentstalker.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ServerMaidGUIPacket implements Packet {
    public ServerMaidGUIPacket() {

    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

    }

    public static ServerMaidGUIPacket decode(FriendlyByteBuf buffer) {
        return new ServerMaidGUIPacket();
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerMaidGUIPacket msg) {
        server.execute(() -> {
            Stalker instance = Stalker.getInstanceOf(player);
            if (instance != null) {
                if (instance.getStalker() instanceof EntityMaid maid) {
                    maid.openMaidGui(player);
                }
            }
        });
    }
}
