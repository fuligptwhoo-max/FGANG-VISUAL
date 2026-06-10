package com.fgangvisuals.utils;

public class MathUtils {
    public static float lerp(float start, float end, float factor) {
        return start + (end - start) * factor;
    }

    public static float smoothLerp(float start, float end, float factor) {
        float s = factor * factor * (3 - 2 * factor);
        return lerp(start, end, s);
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static double distance2D(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double round(double v, int places) {
        double f = Math.pow(10, places);
        return Math.round(v * f) / f;
    }

    public static float easeOutExpo(float x) {
        return x == 1 ? 1 : (float) (1 - Math.pow(2, -10 * x));
    }

    public static class MovingAverage {
        private final float[] samples;
        private int index = 0, count = 0;
        public MovingAverage(int size) { samples = new float[size]; }
        public void add(float v) { samples[index] = v; index = (index + 1) % samples.length; if (count < samples.length) count++; }
        public float getAverage() { if (count == 0) return 0; float s = 0; for (int i = 0; i < count; i++) s += samples[i]; return s / count; }
        public void reset() { index = 0; count = 0; }
    }
}
