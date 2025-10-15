package com.mafuyu404.telepathicmaid.compat;

import net.neoforged.fml.ModList;

public class KeyPrompts {
    private static final String MOD_ID = "smartkeyprompts";
    private static boolean INSTALLED = false;

    public static void init() {
        INSTALLED = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean custom(String key, String desc) {
        if (INSTALLED) {
            KeyPromptsInner.custom(key, desc);
        }
        return false;
    }
}
