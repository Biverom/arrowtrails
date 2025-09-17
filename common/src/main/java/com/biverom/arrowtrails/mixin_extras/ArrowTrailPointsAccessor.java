package com.biverom.arrowtrails.mixin_extras;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface ArrowTrailPointsAccessor {
    List<Vec3> getTrailPoints();
    boolean shouldTrailRender();
}
