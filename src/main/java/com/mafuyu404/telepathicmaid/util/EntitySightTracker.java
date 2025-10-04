package com.mafuyu404.telepathicmaid.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntitySightTracker {

    /**
     * 追踪实体视线方向的目标实体
     * @param origin 视线发出的实体
     * @param range 最大追踪范围
     * @return 视线命中的第一个实体，如果没有则返回null
     */
    public static Entity trackTargetEntity(Entity origin, double range) {
        // 获取客户端世界实例
        if (!origin.level().isClientSide) return null;
        
        // 获取实体眼睛位置（1.0F表示完整高度的眼睛位置）
        Vec3 eyePosition = origin.getEyePosition(1.0F);
        
        // 获取视线方向向量
        Vec3 lookVec = origin.getViewVector(1.0F);
        
        // 计算视线终点
        Vec3 endPoint = eyePosition.add(lookVec.scale(range));
        
        // 创建实体过滤条件
        Predicate<Entity> filter = entity -> 
            entity != null && 
            entity.isAlive() && 
            entity.isPickable() && 
            entity != origin && 
            !origin.isPassengerOfSameVehicle(entity);
        
        // 执行实体射线追踪
        EntityHitResult result = rayTraceEntities(
                origin.level(),
                origin,
                eyePosition,
                endPoint,
                new AABB(eyePosition, endPoint).inflate(1.0),
                filter,
                range
        );
        
        return result != null ? result.getEntity() : null;
    }

    /**
     * 自定义实体射线追踪方法
     */
    private static EntityHitResult rayTraceEntities(
            Level world,
            Entity origin,
            Vec3 startVec,
            Vec3 endVec,
            AABB boundingBox,
            Predicate<Entity> filter,
            double range
    ) {
        // 获取范围内所有候选实体
        List<Entity> entities = world.getEntities(origin, boundingBox, filter);
        
        Entity closestEntity = null;
        Vec3 closestHit = null;
        double minDistance = range;
        
        // 遍历所有候选实体
        for (Entity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(entity.getPickRadius());
            Vec3 hitVec = entityBox.clip(startVec, endVec).orElse(null);
            
            if (hitVec != null) {
                double distance = startVec.distanceTo(hitVec);
                
                // 检查是否为最近的实体
                if (distance < minDistance) {
                    closestEntity = entity;
                    closestHit = hitVec;
                    minDistance = distance;
                }
            }
        }
        
        return closestEntity != null ? new EntityHitResult(closestEntity, closestHit) : null;
    }
}