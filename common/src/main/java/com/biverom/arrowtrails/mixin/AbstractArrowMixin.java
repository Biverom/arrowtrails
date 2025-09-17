package com.biverom.arrowtrails.mixin;

import com.biverom.arrowtrails.ArrowTrailsConfig;
import com.biverom.arrowtrails.mixin_extras.ArrowTrailPointsAccessor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;
import java.util.List;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin implements ArrowTrailPointsAccessor {

    @Shadow
    protected boolean inGround;

    @Shadow
    protected int inGroundTime;

    @Unique
    public final Deque<Vec3> trailPoints = new java.util.ArrayDeque<>();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickHead(CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow)(Object)this;
        trailPoints.addFirst(arrow.position());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickReturn(CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow)(Object)this;
        while (trailPoints.size() > ArrowTrailsConfig.getSettingsForArrow(arrow).length()) {
            trailPoints.removeLast();
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    public void addParticle(Level instance, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    }

    @Override
    public List<Vec3> getTrailPoints() {
        return new java.util.ArrayList<>(this.trailPoints);
    }

    @Override
    public boolean shouldTrailRender() {
        AbstractArrow arrow = (AbstractArrow)(Object)this;
        return ((!inGround) || (inGroundTime < ArrowTrailsConfig.getSettingsForArrow(arrow).length()));
    }
}
