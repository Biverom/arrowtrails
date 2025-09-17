package com.biverom.arrowtrails;

import org.joml.Vector3f;

public record TrailSettings(int length, Vector3f startColor, float startAlpha, float startWidth, Vector3f endColor,
                            float endAlpha, float endWidth) {
}