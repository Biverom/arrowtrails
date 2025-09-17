package com.biverom.arrowtrails;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import org.joml.Vector3f;

public class ArrowTrailsConfig extends MidnightConfig {

    public static final String ARROW_SETTINGS = "arrow_settings";
    public static final String ADVANCED = "advanced";

    @Comment(category = ARROW_SETTINGS) public static Comment arrowTrailSettings;
    @Entry(category = ARROW_SETTINGS, min = 1, max = 64, isSlider = true) public static int arrowTrailLength = 8;
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String arrowTrailStartColor = "#ffffff";
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String arrowTrailEndColor = "#ffffff";
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float arrowTrailStartAlpha = 0.25f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float arrowTrailEndAlpha = 0.0f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float arrowTrailStartWidth = 0.2f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float arrowTrailEndWidth = 0.1f;

    @Comment(category = ARROW_SETTINGS) public static Comment critArrowTrailSettings;
    @Entry(category = ARROW_SETTINGS, min = 1, max = 64, isSlider = true) public static int critArrowTrailLength = 16;
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String critArrowTrailStartColor = "#ffe8a8";
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String critArrowTrailEndColor = "#ffb055";
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float critArrowTrailStartAlpha = 0.5f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float critArrowTrailEndAlpha = 0.0f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float critArrowTrailStartWidth = 0.3f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float critArrowTrailEndWidth = 0.15f;

    @Comment(category = ARROW_SETTINGS) public static Comment spectralArrowTrailSettings;
    @Entry(category = ARROW_SETTINGS, min = 1, max = 64, isSlider = true) public static int spectralArrowTrailLength = 16;
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String spectralArrowTrailStartColor = "#fcfc76";
    @Entry(category = ARROW_SETTINGS, isColor = true) public static String spectralArrowTrailEndColor = "#fcfc76";
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float spectralArrowTrailStartAlpha = 0.5f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float spectralArrowTrailEndAlpha = 0.0f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float spectralArrowTrailStartWidth = 0.3f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float spectralArrowTrailEndWidth = 0.15f;

    @Comment(category = ARROW_SETTINGS) public static Comment tippedArrowTrailSettings;
    @Entry(category = ARROW_SETTINGS, min = 1, max = 64, isSlider = true) public static int tippedArrowTrailLength = 16;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float tippedArrowTrailStartAlpha = 0.5f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float tippedArrowTrailEndAlpha = 0.0f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float tippedArrowTrailStartWidth = 0.3f;
    @Entry(category = ARROW_SETTINGS, min = 0f, max = 1f, precision = 100, isSlider = true) public static float tippedArrowTrailEndWidth = 0.15f;

    @Comment(category = ADVANCED) public static Comment renderingSettings;
    @Entry(category = ADVANCED) public static boolean delayedRendering = true;
    @Entry(category = ADVANCED) public static boolean disableTrails = false;

    private static Vector3f colorToVec(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int rgb = Integer.parseInt(hex, 16);
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        return new Vector3f(r, g, b);
    }

    public static Vector3f intToVec(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        return new Vector3f(r, g, b);
    }

    public static TrailSettings getSettingsForArrow(AbstractArrow arrow) {
        if (arrow instanceof SpectralArrow) {
            return new TrailSettings(
                    spectralArrowTrailLength, colorToVec(spectralArrowTrailStartColor), spectralArrowTrailStartAlpha, spectralArrowTrailStartWidth,
                    colorToVec(spectralArrowTrailEndColor), spectralArrowTrailEndAlpha, spectralArrowTrailEndWidth
            );
        } else if (arrow instanceof Arrow arrowEntity && arrowEntity.getColor() != -1) {
            Vector3f potionColor = intToVec(arrowEntity.getColor());
            return new TrailSettings(
                    tippedArrowTrailLength, potionColor, tippedArrowTrailStartAlpha, tippedArrowTrailStartWidth,
                    potionColor, tippedArrowTrailEndAlpha, tippedArrowTrailEndWidth
            );
        } else if (arrow.isCritArrow()) {
            return new TrailSettings(
                    critArrowTrailLength, colorToVec(critArrowTrailStartColor), critArrowTrailStartAlpha, critArrowTrailStartWidth,
                    colorToVec(critArrowTrailEndColor), critArrowTrailEndAlpha, critArrowTrailEndWidth
            );
        } else if (arrow instanceof Arrow) {
            return new TrailSettings(
                    arrowTrailLength, colorToVec(arrowTrailStartColor), arrowTrailStartAlpha, arrowTrailStartWidth,
                    colorToVec(arrowTrailEndColor), arrowTrailEndAlpha, arrowTrailEndWidth
            );
        }
        return new TrailSettings(
                arrowTrailLength, colorToVec(arrowTrailStartColor), arrowTrailStartAlpha, arrowTrailStartWidth,
                colorToVec(arrowTrailEndColor), arrowTrailEndAlpha, arrowTrailEndWidth
        );
    }

    public static void init() {
        MidnightConfig.init(Constants.MOD_ID, ArrowTrailsConfig.class);
    }
}
