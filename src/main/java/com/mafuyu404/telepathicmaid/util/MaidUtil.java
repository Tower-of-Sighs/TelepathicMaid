package com.mafuyu404.telepathicmaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollPackage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mafuyu404.telepathicmaid.api.MoveControlAccessor;
import com.mafuyu404.telepathicmaid.event.GarageKitEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.mojang.serialization.Codec;

public class MaidUtil {
    public static Map<String, List<FoxScrollPackage.FoxScrollData>> getFoxScrollData(Player player) {
        Map<String, List<FoxScrollPackage.FoxScrollData>> data = Maps.newHashMap();
        MaidWorldData maidWorldData = MaidWorldData.get(player.level());
        if (maidWorldData == null) return null;
        List<MaidInfo> maidInfos = maidWorldData.getPlayerMaidInfos(player);
        if (maidInfos == null) {
            maidInfos = Collections.emptyList();
        }
        maidInfos.forEach(info -> {
            List<FoxScrollPackage.FoxScrollData> scrollData = data.computeIfAbsent(info.getDimension(), dim -> Lists.newArrayList());
            scrollData.add(new FoxScrollPackage.FoxScrollData(info.getChunkPos(), info.getName(), info.getTimestamp()));
        });
        return data;
    }

    public static void setOperation(EntityMaid maid, String op) {
        ((MoveControlAccessor) maid.getMoveControl()).setMove(op);
    }

    public static String getMaidIdOfScrollData(FoxScrollPackage.FoxScrollData foxScrollData) {
        ComponentContents contents = foxScrollData.name().getContents();
        if (contents instanceof TranslatableContents translatable) {
            String key = translatable.getKey();
            key = key.replace("model.", "");
            key = key.replace(".name", "");
            key = key.replace(".", ":");
            return key;
        }
        return null;
    }

    public static String getMaidIdOfGarageKit(ItemStack itemStack) {
        var maidData = itemStack.get(InitDataComponent.MAID_INFO.get());
        if (maidData == null) return null;
        return maidData.read(Codec.STRING.fieldOf(InitDataComponent.MODEL_ID_TAG_NAME)).result().orElse(null);
    }

    public static boolean matchMaidModelId(EntityMaid maid, String id) {
        return maid.getModelId().equals(id);
    }

    public static boolean matchMaid(Entity entity, Player player) {
        if (player == null) return false;
        if (entity instanceof EntityMaid maid) {
            String maidId = MaidUtil.getMaidIdOfGarageKit(player.getMainHandItem());
            UUID GarageKitUUID = getGarageKitUUID(player.getMainHandItem());
            boolean matchModelId = MaidUtil.matchMaidModelId(maid, maidId);
            boolean matchOwner = maid.getOwnerUUID() != null && maid.getOwnerUUID().equals(player.getUUID());
            boolean matchMaidUUID = GarageKitUUID != null && GarageKitUUID.equals(maid.getUUID());
            return matchModelId && matchOwner && matchMaidUUID;
        }
        return false;
    }

    public static UUID getGarageKitUUID(ItemStack itemStack) {
        var maidData = itemStack.get(InitDataComponent.MAID_INFO.get());
        if (maidData != null) {
            CompoundTag tag = maidData.copyTag();
            if (tag.contains(GarageKitEvent.KEY)) {
                return tag.getUUID(GarageKitEvent.KEY);
            }
        }
        return null;
    }
}
