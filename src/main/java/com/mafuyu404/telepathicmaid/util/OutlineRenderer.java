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

public final class OutlineRenderer {
    private static final float DEFAULT_SCALE = 1.05F;
    private static final float[] DEFAULT_COLOR = {1.0F, 0.84F, 0.0F, 0.8F};

    private OutlineRenderer() {
    }

    public static void renderEntityOutline(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity, float partialTicks) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<?> renderer = dispatcher.getRenderer(entity);
        if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) return;

        pushWireframeState(DEFAULT_COLOR);

        poseStack.pushPose();
        float yCenter = entity.getBbHeight() / 2.0F;
        poseStack.translate(0, yCenter, 0);
        poseStack.scale(DEFAULT_SCALE, DEFAULT_SCALE, DEFAULT_SCALE);
        poseStack.translate(0, -yCenter + 1.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.yBodyRot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
        livingRenderer.getModel().renderToBuffer(
                poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY,
                DEFAULT_COLOR[0], DEFAULT_COLOR[1], DEFAULT_COLOR[2], DEFAULT_COLOR[3]
        );
        poseStack.popPose();

        popWireframeState();
    }


    public static void renderOutlineBox(PoseStack poseStack, VertexConsumer consumer, Matrix4f matrix,
                                        AABB box, float r, float g, float b, float a) {
        float[][] points = {
                {(float) box.minX, (float) box.minY, (float) box.minZ},
                {(float) box.maxX, (float) box.minY, (float) box.minZ},
                {(float) box.maxX, (float) box.minY, (float) box.maxZ},
                {(float) box.minX, (float) box.minY, (float) box.maxZ},
                {(float) box.minX, (float) box.maxY, (float) box.minZ},
                {(float) box.maxX, (float) box.maxY, (float) box.minZ},
                {(float) box.maxX, (float) box.maxY, (float) box.maxZ},
                {(float) box.minX, (float) box.maxY, (float) box.maxZ}
        };

        line(consumer, matrix, points[0], points[1], r, g, b, a);
        line(consumer, matrix, points[1], points[2], r, g, b, a);
        line(consumer, matrix, points[2], points[3], r, g, b, a);
        line(consumer, matrix, points[3], points[0], r, g, b, a);

        line(consumer, matrix, points[4], points[5], r, g, b, a);
        line(consumer, matrix, points[5], points[6], r, g, b, a);
        line(consumer, matrix, points[6], points[7], r, g, b, a);
        line(consumer, matrix, points[7], points[4], r, g, b, a);

        for (int i = 0; i < 4; i++) {
            line(consumer, matrix, points[i], points[i + 4], r, g, b, a);
        }
    }


    private static void pushWireframeState(float[] color) {
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);
        RenderSystem.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    private static void popWireframeState() {
        RenderSystem.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void line(VertexConsumer consumer, Matrix4f matrix, float[] a, float[] b,
                             float r, float g, float b2, float a2) {
        consumer.vertex(matrix, a[0], a[1], a[2]).color(r, g, b2, a2).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, b[0], b[1], b[2]).color(r, g, b2, a2).normal(0, 1, 0).endVertex();
    }
}
