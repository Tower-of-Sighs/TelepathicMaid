package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.init.Stalker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerMaidGUIPacket {
    public ServerMaidGUIPacket() {

    }

    public static void encode(ServerMaidGUIPacket msg, FriendlyByteBuf buffer) {

    }

    public static ServerMaidGUIPacket decode(FriendlyByteBuf buffer) {
        return new ServerMaidGUIPacket();
    }

    public static void handle(ServerMaidGUIPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Stalker instance = Stalker.getInstanceOf(player);
            if (instance != null) {
                if (instance.getStalker() instanceof EntityMaid maid) {
                    maid.openMaidGui(player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
