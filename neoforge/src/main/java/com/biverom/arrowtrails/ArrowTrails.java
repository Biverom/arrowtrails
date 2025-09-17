package com.biverom.arrowtrails;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class ArrowTrails {

    public ArrowTrails(IEventBus eventBus) {
        ArrowTrailsCommon.init();
    }
}