package com.mafuyu404.telepathicmaid;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec.ConfigValue<Integer> RENDER_RADIUS_NORMAL;
    public static ModConfigSpec.ConfigValue<Integer> RENDER_RADIUS_SPECIAL;

    public static ModConfigSpec init() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("Render Radius");

        RENDER_RADIUS_NORMAL = BUILDER
                .comment("For DroneStalker and ArrowStalker.")
                .define("Normal", 5);
        RENDER_RADIUS_SPECIAL = BUILDER
                .comment("For VoidStalker.")
                .define("Special", 7);
        builder.pop();

        return builder.build();
    }
}
