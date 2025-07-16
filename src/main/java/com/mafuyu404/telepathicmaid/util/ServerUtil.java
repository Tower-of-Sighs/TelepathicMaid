package com.mafuyu404.telepathicmaid.util;

import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class ServerUtil {
    public static BlockPos findUnloadedMaid(Player player) {
        Level level = player.level();
        ItemStack itemStack = player.getMainHandItem();
        if (!(itemStack.getItem() instanceof ItemGarageKit)) return null;
        String levelName = level.dimension().location().toString();
        var data = MaidUtil.getFoxScrollData(player);
        if (data == null) return null;
        List<FoxScrollMessage.FoxScrollData> dataList = data.get(levelName);
        if (dataList == null) return null;
        BlockPos result = null;
        for (FoxScrollMessage.FoxScrollData foxScrollData : dataList) {
            if (Objects.equals(MaidUtil.getMaidIdOfGarageKit(itemStack), MaidUtil.getMaidIdOfScrollData(foxScrollData))) {
                result = foxScrollData.getPos();
            }
        }
        return result;
    }
}
