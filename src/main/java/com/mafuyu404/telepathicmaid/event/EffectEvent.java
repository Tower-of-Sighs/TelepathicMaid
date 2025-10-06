package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import com.mafuyu404.telepathicmaid.util.EntitySightTracker;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import com.mafuyu404.telepathicmaid.util.OutlineRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

public class EffectEvent {
    private static final float GOLD_R = 1.0F;
    private static final float GOLD_G = 0.84F;
    private static final float GOLD_B = 0.0F;
    private static final float ALPHA = 0.8F;

    public static void initEffect() {
        WorldRenderEvents.AFTER_ENTITIES.register(EffectEvent::onAfterEntities);
    }

    private static void onAfterEntities(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        renderOutlineBox(context);

        renderMaidFrame(context, player);
    }

    private static void renderOutlineBox(WorldRenderContext context) {
        EntityMaid maid = ClientMaidUtil.getControllingMaid();
        if (maid == null) return;

        Entity targetEntity = EntitySightTracker.trackTargetEntity(maid, 32);
        if (targetEntity == null) return;

        PoseStack poseStack = context.matrixStack();
        Camera camera = context.camera();
        Frustum frustum = context.frustum();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        if (!frustum.isVisible(targetEntity.getBoundingBox())) return;

        Vec3 cameraPos = camera.getPosition();
        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.lineWidth(8.0f);

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);

        double expand = targetEntity.getBoundingBox().getSize() * 0.10;
        AABB aabb = targetEntity.getBoundingBox().inflate(expand);

        OutlineRenderer.renderOutlineBox(poseStack, vertexConsumer, matrix, aabb, GOLD_R, GOLD_G, GOLD_B, ALPHA);

        poseStack.popPose();

        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
    }

    private static void renderMaidFrame(WorldRenderContext context, Player player) {
        if (Stalker.hasInstanceOf(player)) return;
        if (!(player.getMainHandItem().getItem() instanceof ItemGarageKit)) return;

        int distance = 32;
        List<Entity> maidList = player.level().getEntities(player,
                player.getBoundingBox().inflate(distance),
                entity -> MaidUtil.matchMaid(entity, player));

        if (maidList.isEmpty()) return;

        EntityMaid target = (EntityMaid) maidList.getFirst();
        PoseStack poseStack = context.matrixStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Camera camera = context.camera();
        Vec3 cameraPos = camera.getPosition();

        poseStack.pushPose();
        poseStack.translate(
                target.getX() - cameraPos.x(),
                target.getY() - cameraPos.y(),
                target.getZ() - cameraPos.z()
        );

        OutlineRenderer.renderEntityOutline(poseStack, bufferSource, target, context.tickDelta());

        poseStack.popPose();
        bufferSource.endBatch();
    }
}