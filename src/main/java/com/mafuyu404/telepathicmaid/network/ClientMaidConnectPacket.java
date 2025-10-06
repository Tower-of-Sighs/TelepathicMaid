package com.mafuyu404.telepathicmaid.network;

import com.mafuyu404.diligentstalker.network.Packet;
import com.mafuyu404.diligentstalker.utils.ClientStalkerUtil;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ClientMaidConnectPacket implements Packet {
    private final BlockPos blockPos;

    public ClientMaidConnectPacket(BlockPos blockPos) {
        this.blockPos = blockPos == null ? BlockPos.ZERO : blockPos;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
    }

    public static ClientMaidConnectPacket decode(FriendlyByteBuf buffer) {
        return new ClientMaidConnectPacket(buffer.readBlockPos());
    }

    public static void handle(ClientMaidConnectPacket msg, Minecraft client) {
        client.execute(() -> {
            ClientStalkerUtil.tryRemoteConnect(msg.blockPos, ClientMaidUtil::matchMaid);
        });
    }
}
