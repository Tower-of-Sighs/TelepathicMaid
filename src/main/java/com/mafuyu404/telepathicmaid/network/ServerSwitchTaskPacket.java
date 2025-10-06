package com.mafuyu404.telepathicmaid.network;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.diligentstalker.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ServerSwitchTaskPacket implements Packet {
    private final int index;

    public ServerSwitchTaskPacket(int index) {
        this.index = index;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(index);
    }

    public static ServerSwitchTaskPacket decode(FriendlyByteBuf buffer) {
        return new ServerSwitchTaskPacket(buffer.readInt());
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerSwitchTaskPacket msg) {
        server.execute(() -> {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();

            Stalker instance = Stalker.getInstanceOf(player);

            if (instance != null && instance.getStalker() instanceof EntityMaid maid) {
                maid.setTask(tasks.get(msg.index));
            }
        });
    }
}
