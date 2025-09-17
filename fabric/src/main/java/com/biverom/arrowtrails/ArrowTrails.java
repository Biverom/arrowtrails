package com.biverom.arrowtrails;

import net.fabricmc.api.ModInitializer;

public class ArrowTrails implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ArrowTrailsCommon.init();
    }
}
