package com.biverom.arrowtrails.mixin;

import com.biverom.arrowtrails.TrailSettings;
import com.biverom.arrowtrails.ArrowTrailsConfig;
import com.biverom.arrowtrails.ModRenderTypes;
import com.biverom.arrowtrails.TrailRenderer;
import com.biverom.arrowtrails.mixin_extras.ArrowTrailPointsAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArrowRenderer.class)
public class ArrowRendererMixin<T extends AbstractArrow> {

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!((ArrowTrailPointsAccessor)entity).shouldTrailRender()) return;
        TrailSettings settings = ArrowTrailsConfig.getSettingsForArrow(entity);
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        List<Vec3> points = ((ArrowTrailPointsAccessor)entity).getTrailPoints();
        Vec3 anchor = entity.getPosition(partialTicks);
        points.addFirst(anchor);
        TrailRenderer.drawTrail(anchor, points, pose, packedLight, partialTicks, settings, buffer);
        poseStack.popPose();
    }
}
