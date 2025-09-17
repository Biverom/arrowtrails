package com.biverom.arrowtrails;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TrailRenderer {

    public record DelayedArrowTrail(Vec3 anchor, List<Vec3> points, PoseStack.Pose pose, int packedLight,
                                    float partialTicks, TrailSettings settings) {
    }

    public static final List<DelayedArrowTrail> delayedTrails = new ArrayList<>();

    public static void drawTrail(
            Vec3 anchor,
            List<Vec3> points,
            PoseStack.Pose pose,
            int packedLight,
            float partialTicks,
            TrailSettings settings,
            MultiBufferSource bufferSource
    ) {
        if (ArrowTrailsConfig.disableTrails) return;
        if (ArrowTrailsConfig.delayedRendering) {
            delayedTrails.add(new DelayedArrowTrail(anchor, points, pose, packedLight, partialTicks, settings));
        } else {
            drawTrailRaw(anchor, points, pose, packedLight, partialTicks, settings, bufferSource);
        }
    }

    public static void drawTrailRaw(
            Vec3 anchor,
            List<Vec3> points,
            PoseStack.Pose pose,
            int packedLight,
            float partialTicks,
            TrailSettings settings,
            MultiBufferSource bufferSource
    ) {
        if (points.size() < 2) return;

        float total = settings.length() + 1f;

        Vec3[] left = new Vec3[points.size()];
        Vec3[] right = new Vec3[points.size()];

        for (int i = 0; i < points.size(); i++) {
            Vec3 p = points.get(i).subtract(anchor);

            float t = ((float) i + partialTicks) / total;
            float w = Mth.lerp(t, settings.startWidth(), settings.endWidth());

            Vec3 dir;
            if (i == points.size() - 1) {
                dir = p.subtract(points.get(i - 1).subtract(anchor)).normalize();
            } else {
                dir = points.get(i + 1).subtract(anchor).subtract(p).normalize();
            }

            if (Minecraft.getInstance().getCameraEntity() == null) return;
            Vec3 cameraPos = Minecraft.getInstance().getCameraEntity().getPosition(partialTicks);
            Vec3 viewDir = cameraPos.subtract(anchor).subtract(p).normalize();
            Vec3 side = dir.cross(viewDir).normalize().scale(w * 0.5f);

            left[i] = p.add(side);
            right[i] = p.subtract(side);
        }

        VertexConsumer consumer = bufferSource.getBuffer(ModRenderTypes.ARROWTRAILS_TRAILS);
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3 v1a = left[i];
            Vec3 v1b = right[i];
            Vec3 v2a = left[i + 1];
            Vec3 v2b = right[i + 1];

            float t1 = ((float) i + partialTicks) / total;
            float t2 = ((float) (i + 1) + partialTicks) / total;

            Vector3f c1 = lerp(t1, settings.startColor(), settings.endColor());
            Vector3f c2 = lerp(t2, settings.startColor(), settings.endColor());
            float a1 = Mth.lerp(t1, settings.startAlpha(), settings.endAlpha());
            float a2 = Mth.lerp(t2, settings.startAlpha(), settings.endAlpha());

            vertex(pose, consumer, packedLight, (float) v1a.x, (float) v1a.y, (float) v1a.z, c1, a1);
            vertex(pose, consumer, packedLight, (float) v2a.x, (float) v2a.y, (float) v2a.z, c2, a2);
            vertex(pose, consumer, packedLight, (float) v2b.x, (float) v2b.y, (float) v2b.z, c2, a2);
            vertex(pose, consumer, packedLight, (float) v1b.x, (float) v1b.y, (float) v1b.z, c1, a1);
        }
    }

    private static Vector3f lerp(float t, Vector3f a, Vector3f b) {
        return new Vector3f(
                Mth.lerp(t, a.x, b.x),
                Mth.lerp(t, a.y, b.y),
                Mth.lerp(t, a.z, b.z)
        );
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, int packedLight, float x, float y, float z, Vector3f c, float a) {
        consumer.addVertex(pose, x, y, z).setColor(c.x, c.y, c.z, a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0, 0, 1);
    }

    public static void drawDelayedTrails(MultiBufferSource bufferSource) {
        for (DelayedArrowTrail trail : delayedTrails) {
            drawTrailRaw(trail.anchor(), trail.points(), trail.pose(), trail.packedLight(), trail.partialTicks(), trail.settings(), bufferSource);
        }
        delayedTrails.clear();
    }
}
