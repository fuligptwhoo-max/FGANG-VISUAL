package com.fgangvisuals.utils;

public class ColorUtils {
    public static int getRainbow(float sat, float bri, long offset) {
        float hue = (System.currentTimeMillis() + offset) % 2000L / 2000f;
        return java.awt.Color.HSBtoRGB(hue, sat, bri);
    }

    public static int pulseColor(int base, float speed, float minBri) {
        float t = System.currentTimeMillis() % (long) (2000 / speed) / (2000f / speed);
        float sine = (float) (Math.sin(t * Math.PI * 2) * 0.5 + 0.5);
        float bri = minBri + (1 - minBri) * sine;
        int r = (int) (((base >> 16) & 0xFF) * bri);
        int g = (int) (((base >> 8) & 0xFF) * bri);
        int b = (int) ((base & 0xFF) * bri);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static int interpolate(int c1, int c2, float f) {
        int r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        return 0xFF000000 | ((int) (r1 + (r2 - r1) * f) << 16)
            | ((int) (g1 + (g2 - g1) * f) << 8)
            | (int) (b1 + (b2 - b1) * f);
    }

    public static int adjustBrightness(int color, float factor) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static int hexToInt(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        return Integer.parseInt(hex, 16) | 0xFF000000;
    }
}
