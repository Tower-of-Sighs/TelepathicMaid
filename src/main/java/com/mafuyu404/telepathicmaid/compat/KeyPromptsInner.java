package com.mafuyu404.telepathicmaid.compat;

import com.mafuyu404.smartkeyprompts.SmartKeyPrompts;
import com.mafuyu404.telepathicmaid.TelepathicMaid;

public class KeyPromptsInner {
    public static void custom(String key, String desc) {
        SmartKeyPrompts.custom(TelepathicMaid.MODID, key, desc);
    }
}
