package com.mafuyu404.telepathicmaid.network;

import com.mafuyu404.diligentstalker.network.Packet;
import com.mafuyu404.diligentstalker.utils.ClientStalkerUtil;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientMaidConnectPacket(BlockPos blockPos) implements Packet {
    public static final Type<ClientMaidConnectPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TelepathicMaid.MODID, "client_maid_connect"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientMaidConnectPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientMaidConnectPacket::blockPos,
            ClientMaidConnectPacket::new
    );

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Packet> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientMaidConnectPacket msg, IPayloadContext ctx) {
        if (ctx.flow().isClientbound()) {
            ctx.enqueueWork(() -> {
                ClientStalkerUtil.tryRemoteConnect(msg.blockPos, ClientMaidUtil::matchMaid);
            });
        }
    }
}
