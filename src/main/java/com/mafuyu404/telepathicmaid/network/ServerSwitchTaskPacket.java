package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mafuyu404.diligentstalker.init.Stalker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ServerSwitchTaskPacket {
    private final int index;

    public ServerSwitchTaskPacket(int index) {
        this.index = index;
    }

    public static void encode(ServerSwitchTaskPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.index);
    }

    public static ServerSwitchTaskPacket decode(FriendlyByteBuf buffer) {
        return new ServerSwitchTaskPacket(buffer.readInt());
    }

    public static void handle(ServerSwitchTaskPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();

            Stalker instance = Stalker.getInstanceOf(ctx.get().getSender());

            if (instance != null && instance.getStalker() instanceof EntityMaid maid) {
                maid.setTask(tasks.get(msg.index));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
