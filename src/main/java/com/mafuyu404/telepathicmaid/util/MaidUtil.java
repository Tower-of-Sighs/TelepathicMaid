package com.mafuyu404.telepathicmaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mafuyu404.telepathicmaid.api.MoveControlAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MaidUtil {
    public static Map<String, List<FoxScrollMessage.FoxScrollData>> getFoxScrollData(Player player) {
        Map<String, List<FoxScrollMessage.FoxScrollData>> data = Maps.newHashMap();
        MaidWorldData maidWorldData = MaidWorldData.get(player.level());
        if (maidWorldData == null) return null;
        List<MaidInfo> maidInfos = maidWorldData.getPlayerMaidInfos(player);
        if (maidInfos == null) {
            maidInfos = Collections.emptyList();
        }
        maidInfos.forEach(info -> {
            List<FoxScrollMessage.FoxScrollData> scrollData = data.computeIfAbsent(info.getDimension(), dim -> Lists.newArrayList());
            scrollData.add(new FoxScrollMessage.FoxScrollData(info.getChunkPos(), info.getName(), info.getTimestamp()));
        });
        return data;
    }

    public static void setOperation(EntityMaid maid, String op) {
        ((MoveControlAccessor) maid.getMoveControl()).setMove(op);
    }

    public static String getMaidIdOfScrollData(FoxScrollMessage.FoxScrollData foxScrollData) {
        ComponentContents contents = foxScrollData.getName().getContents();
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
        CompoundTag nbt = itemStack.getOrCreateTag();
        CompoundTag info = (CompoundTag) nbt.get("EntityInfo");
        if (info != null) return info.getString("ModelId");
        return null;
    }

    public static boolean matchMaidModelId(EntityMaid maid, String id) {
        return maid.getModelId().equals(id);
    }
}
