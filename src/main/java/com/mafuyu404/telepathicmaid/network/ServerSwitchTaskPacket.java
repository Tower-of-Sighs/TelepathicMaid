package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.diligentstalker.network.Packet;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ServerSwitchTaskPacket(int index) implements Packet {

    public static final Type<ServerSwitchTaskPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TelepathicMaid.MODID, "server_switch_task"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerSwitchTaskPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerSwitchTaskPacket::index,
            ServerSwitchTaskPacket::new
    );

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Packet> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerSwitchTaskPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();

            Stalker instance = Stalker.getInstanceOf(ctx.player());

            if (instance != null && instance.getStalker() instanceof EntityMaid maid) {
                maid.setTask(tasks.get(msg.index));
            }
        });
    }

}
