package com.mafuyu404.telepathicmaid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> RENDER_RADIUS_NORMAL;
    public static final ForgeConfigSpec.ConfigValue<Integer> RENDER_RADIUS_SPECIAL;

    static {
        BUILDER.push("Render Radius");

        RENDER_RADIUS_NORMAL = BUILDER
                .comment("For DroneStalker and ArrowStalker.")
                .define("Normal", 5);
        RENDER_RADIUS_SPECIAL = BUILDER
                .comment("For VoidStalker.")
                .define("Special", 7);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
