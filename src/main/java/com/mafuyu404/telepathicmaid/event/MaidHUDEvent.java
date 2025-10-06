package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.player.Player;

public class MaidHUDEvent {

    public static void initHUD() {
        HudRenderCallback.EVENT.register(MaidHUDEvent::onRenderGameOverlay);
    }

    public static void onRenderGameOverlay(GuiGraphics guiGraphics, float tickDelta) {
        EntityMaid maid = ClientMaidUtil.getControllingMaid();
        if (maid != null) {
            Player player = Minecraft.getInstance().player;
            Window window = Minecraft.getInstance().getWindow();
            int screenWidth = window.getGuiScaledWidth();
            int screenHeight = window.getGuiScaledHeight();


            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            scaleHUD(poseStack, screenWidth - 4, screenHeight - 4, 4);
            guiGraphics.renderItem(maid.getMainHandItem(), screenWidth - 20, screenHeight - 20);

            maid.isHomeModeEnable();

            poseStack.popPose();

            drawMaidCharacter(guiGraphics, 40, screenHeight - 20);
        }
    }

    private static void scaleHUD(PoseStack poseStack, int x, int y, float scale) {
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0f);
        poseStack.translate(-x, -y, 0);
    }

    private static void drawMaidCharacter(GuiGraphics guiGraphics, int x, int y) {
        EntityMaid maid = ClientMaidUtil.getControllingMaid();
        if (maid != null) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            poseStack.translate(x, y, 1); // Z值确保在UI上层

            float scale = 50 / maid.getBbHeight();
            poseStack.scale(scale, scale, scale);

            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            poseStack.mulPose(Axis.YP.rotationDegrees(maid.getYRot()));

            EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();
            renderer.render(maid,
                    0, 0, 0,
                    maid.getYRot(),
                    1,
                    poseStack,
                    guiGraphics.bufferSource(),
                    0xF000F0);

            poseStack.popPose();
        }
    }

    private void drawTaskListBg(GuiGraphics graphics) {
//        graphics.blit(new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/ gui/ maid_gui_task. png"), leftPos - 93, topPos + 5, 0, 0, 92, 251);
    }
}
