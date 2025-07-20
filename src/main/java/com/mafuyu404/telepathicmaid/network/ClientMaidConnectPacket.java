package com.mafuyu404.telepathicmaid.network;

import com.mafuyu404.diligentstalker.utils.ClientStalkerUtil;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientMaidConnectPacket {
    private final BlockPos blockPos;

    public ClientMaidConnectPacket(BlockPos blockPos) {
        this.blockPos = blockPos == null ? BlockPos.ZERO : blockPos;
    }

    public static void encode(ClientMaidConnectPacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.blockPos);
    }

    public static ClientMaidConnectPacket decode(FriendlyByteBuf buffer) {
        return new ClientMaidConnectPacket(buffer.readBlockPos());
    }

    public static void handle(ClientMaidConnectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientStalkerUtil.tryRemoteConnect(msg.blockPos, ClientMaidUtil::matchMaid);
        });
        ctx.get().setPacketHandled(true);
    }
}
