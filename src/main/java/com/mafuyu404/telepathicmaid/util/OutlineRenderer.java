package com.mafuyu404.telepathicmaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class OutlineRenderer {
    private static final float OUTLINE_SCALE = 1.05F;
    private static final float[] GOLD_COLOR = {1.0F, 0.84F, 0.0F, 0.8F}; // RGBA

    public static void renderOutline(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity, float partialTicks) {
//        System.out.print(entity+"\n");
        // 获取实体渲染器
        EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?> entityRenderer = renderer.getRenderer(entity);
        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) return;

        // 保存当前渲染状态
        RenderSystem.disableDepthTest();
//        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(GOLD_COLOR[0], GOLD_COLOR[1], GOLD_COLOR[2], GOLD_COLOR[3]);
        RenderSystem.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//        RenderSystem.lineWidth(64.0F);

        // 创建临时Buffer
        VertexConsumer outlineBuffer = buffer.getBuffer(RenderType.lines());

        // 计算模型缩放中心（实体中心点）
        float yOffset = entity.getBbHeight() / 2.0F;
        poseStack.pushPose();

        poseStack.translate(0, yOffset, 0);
        poseStack.scale(OUTLINE_SCALE, OUTLINE_SCALE, OUTLINE_SCALE);
        poseStack.translate(0, -yOffset + 1.5, 0);
//        System.out.print(entity.getBbHeight()+"\n");

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.yBodyRot));

        // 渲染线框轮廓
        livingRenderer.getModel().renderToBuffer(
                poseStack,
                outlineBuffer,
                15728880, // 亮度
                OverlayTexture.RED_OVERLAY_V,
                GOLD_COLOR[0], GOLD_COLOR[1], GOLD_COLOR[2], GOLD_COLOR[3]
        );

        // 恢复渲染状态
        poseStack.popPose();
        RenderSystem.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
//        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderOutlineBox(PoseStack poseStack, VertexConsumer consumer, Matrix4f matrix, AABB aabb, float r, float g, float b, float a) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;

        // 绘制底部矩形
        line(consumer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        line(consumer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        line(consumer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        line(consumer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        // 绘制顶部矩形
        line(consumer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        line(consumer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        // 绘制垂直边
        line(consumer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        line(consumer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    // 绘制单条线
    private static void line(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float z1,
                             float x2, float y2, float z2, float r, float g, float b, float a) {
        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(0, 1, 0).endVertex();
    }
}
