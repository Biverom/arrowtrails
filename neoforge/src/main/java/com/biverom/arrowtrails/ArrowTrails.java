package com.biverom.arrowtrails;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ArrowTrails {

    public ArrowTrails(IEventBus eventBus) {
        ArrowTrailsCommon.init();
    }
}