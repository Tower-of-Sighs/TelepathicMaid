package com.mafuyu404.telepathicmaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.mafuyu404.diligentstalker.init.Stalker;
import com.mafuyu404.telepathicmaid.TelepathicMaid;
import com.mafuyu404.telepathicmaid.util.ClientMaidUtil;
import com.mafuyu404.telepathicmaid.util.EntitySightTracker;
import com.mafuyu404.telepathicmaid.util.MaidUtil;
import com.mafuyu404.telepathicmaid.util.OutlineRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.List;

@EventBusSubscriber(modid = TelepathicMaid.MODID, value = Dist.CLIENT)
public class EffectEvent {
    private static final float GOLD_R = 1.0F; // 金色RGB值
    private static final float GOLD_G = 0.84F;
    private static final float GOLD_B = 0.0F;
    private static final float ALPHA = 0.8F; // 透明度

    @SubscribeEvent
    public static void outlineBox(RenderLevelStageEvent event) {
        EntityMaid maid = ClientMaidUtil.getControllingMaid();
        if (maid == null) return;
        Entity targetEntity = EntitySightTracker.trackTargetEntity(maid, 32);
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES ||
                targetEntity == null) return;

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        // 保存当前渲染状态
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.lineWidth(8.0F);

        // 应用相机变换
        poseStack.pushPose();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // 获取投影矩阵
        Matrix4f matrix = poseStack.last().pose();

        // 创建顶点
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);

        // 计算实体边框（扩大10%使其更明显）
        double expand = targetEntity.getBoundingBox().getSize() * 0.10;
        AABB aabb = targetEntity.getBoundingBox().inflate(expand);

        // 绘制金色边框
        OutlineRenderer.renderOutlineBox(poseStack, vertexConsumer, matrix, aabb, GOLD_R, GOLD_G, GOLD_B, ALPHA);

        // 提交绘制
        bufferSource.endBatch();

        // 恢复渲染状态
        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
    }

    @SubscribeEvent
    public static void frame(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Player player = Minecraft.getInstance().player;
        if (Stalker.hasInstanceOf(player)) return;

        if (!(player.getMainHandItem().getItem() instanceof ItemGarageKit)) return;

        int distance = 32;
        List<Entity> maidList = player.level().getEntities(player, player.getBoundingBox()
                .inflate(distance, distance, distance), entity -> MaidUtil.matchMaid(entity, player));
        if (!maidList.isEmpty()) {
            EntityMaid target = (EntityMaid) maidList.get(0);

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

            // 计算相机位置偏移
            Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            double x = target.getX() - cameraPos.x();
            double y = target.getY() - cameraPos.y();
            double z = target.getZ() - cameraPos.z();

            poseStack.pushPose();
            poseStack.translate(x, y, z);

            OutlineRenderer.renderEntityOutline(poseStack, bufferSource, target, event.getPartialTick().getRealtimeDeltaTicks());

            poseStack.popPose();
            bufferSource.endBatch();
        }
    }
}
