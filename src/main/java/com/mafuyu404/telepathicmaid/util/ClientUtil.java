package com.mafuyu404.telepathicmaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class ClientUtil {
    public static EntityMaid findLoadedMaid() {
        Player player = Minecraft.getInstance().player;
        String targetedId = MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem());
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return null;
        for (Entity entity : level.entitiesForRendering()) {
            if (entity instanceof EntityMaid maid) {
                if (MaidUtil.matchMaidModelId(maid, targetedId)) {
                    return maid;
                }
            }
        }
        return null;
    }

    public static boolean isKeyPressedOfDesc(String key) {
        boolean result = false;
        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            if (key.equals(keyMapping.getName()) && isKeyPressed(keyMapping.getKey().getValue())) {
                result = true;
            }
        }
        return result;
    }
    public static boolean isKeyPressed(int glfwKeyCode) {
        Minecraft minecraft = Minecraft.getInstance();
        long windowHandle = minecraft.getWindow().getWindow();
        return GLFW.glfwGetKey(windowHandle, glfwKeyCode) == GLFW.GLFW_PRESS;
    }

    public static boolean isHoldingGarageKit() {
        Player player = Minecraft.getInstance().player;
        return player.getMainHandItem().getItem() instanceof ItemGarageKit;
    }
    public static boolean isUsingGarageKit() {
        return isHoldingGarageKit() && ClientUtil.isKeyPressedOfDesc("key.use");
    }
}
