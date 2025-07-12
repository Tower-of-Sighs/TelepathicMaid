package com.mafuyu404.telepathicmaid.network;

import com.mafuyu404.diligentstalker.init.Stalker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientMaidConnectPacket {
    private final BlockPos blockPos;

    public ClientMaidConnectPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void encode(ClientMaidConnectPacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.blockPos);
    }

    public static ClientMaidConnectPacket decode(FriendlyByteBuf buffer) {
        return new ClientMaidConnectPacket(buffer.readBlockPos());
    }

    public static void handle(ClientMaidConnectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            LocalPlayer player = Minecraft.getInstance().player;
            Entity entity = level.getEntity(msg.entityId);
            Stalker.connect(player, entity);
        });
        ctx.get().setPacketHandled(true);
    }
}
