package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.diligentstalker.network.Packet;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerMaidGUIPacket() implements Packet {
    public static final Type<ServerMaidGUIPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TelepathicMaid.MODID, "server_maid_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerMaidGUIPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerMaidGUIPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Packet> getStreamCodec() {
        return STREAM_CODEC;
    }

    public static void handle(ServerMaidGUIPacket msg, IPayloadContext ctx) {
        if (ctx.flow().isServerbound()) {
            ctx.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) ctx.player();
                Stalker instance = Stalker.getInstanceOf(player);
                if (instance != null && instance.getStalker() instanceof EntityMaid maid) {
                    maid.openMaidGui(player);
                }
            });
        }
    }
}
