package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mafuyu404.diligentstalker.init.NetworkHandler;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.network.ServerSwitchTaskPacket;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import java.util.List;

@EventBusSubscriber(modid = TelepathicMaid.MODID, value = Dist.CLIENT)
public class MaidActionEvent {
//    @SubscribeEvent
//    public static void attack(InputEvent.MouseButton.Pre event) {
//        if (Minecraft.getInstance().screen != null) return;
//        if (event.getAction() != InputConstants.PRESS) return;
//        Entity stalker = ClientStalkerUtil.getLocalStalker();
//        if (stalker instanceof EntityMaid maid) {
//            HitResult hitResult = Minecraft.getInstance().hitResult;
//            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
//                Entity target = ((EntityHitResult) hitResult).getEntity();
//                maid.doHurtTarget(target);
//            }
//        }
//    }

    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event) {
        if (Minecraft.getInstance().screen != null) return;
        EntityMaid maid = ClientMaidUtil.getControllingMaid();
        if (maid != null) {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();

            int index = tasks.indexOf(maid.getTask());
            index += event.getScrollDeltaY() < 0 ? 1 : -1;
            index = Mth.clamp(index, 0, tasks.size() - 1);

            IMaidTask mid = tasks.get(index);
            maid.setTask(mid);

            IMaidTask left = index - 1 == -1 ? null : tasks.get(index - 1);
            IMaidTask right = index + 1 == tasks.size() ? null : tasks.get(index + 1);

            String current = mid.getUid().getPath();

            current = translateTask(current);
            String last = left == null ? "" : translateTask(left.getUid().getPath());
            String next = right == null ? "" : translateTask(right.getUid().getPath());

            int size = Math.max(last.length(), next.length());

            current = "§f" + "  - " + current + " -  ";
            last = "§7" + getBlank(last, size) + last;
            next = "§7" + next + getBlank(next, size);

            Component result = Component.translatable(last + current + next);

            Minecraft.getInstance().player.displayClientMessage(result, true);

            NetworkHandler.sendToServer(new ServerSwitchTaskPacket(index));
        }
    }

    private static String getBlank(String str, int size) {
        if (size == str.length()) return "";
        return "  ".repeat(size - str.length());
    }

    private static String translateTask(String id) {
        return Component.translatable(String.format("task.touhou_little_maid.%s", id)).getString();
    }
}
