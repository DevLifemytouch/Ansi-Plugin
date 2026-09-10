package de.lifemytouch.ansi.core.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.Color;

public class GradientUtil {

    private static final int MIN_VIRTUAL_LENGTH = 8;

    public static BaseComponent[] createGradient(String text, Color start, Color end) {
        return createShineGradient(text, start, end, Float.NEGATIVE_INFINITY, 0);
    }

    public static BaseComponent[] createShineGradient(String text, Color start, Color end, float shinePosition, float shineWidth) {
        TextComponent[] components = new TextComponent[text.length()];
        int length = text.length();

        int virtualLength = Math.max(length, MIN_VIRTUAL_LENGTH);
        float offset = (virtualLength - length) / 2f;

        for (int i = 0; i < length; i++) {
            float virtualIndex = i + offset;
            float linearRatio = virtualLength <= 1 ? 0 : virtualIndex / (virtualLength - 1);
            float ratio = smoothstep(linearRatio);

            int r = lerp(start.getRed(),   end.getRed(),   ratio);
            int g = lerp(start.getGreen(), end.getGreen(), ratio);
            int b = lerp(start.getBlue(),  end.getBlue(),  ratio);

            if (shineWidth > 0) {
                float distance = Math.abs(i - shinePosition);
                if (distance < shineWidth) {
                    float shineStrength = smoothstep(1f - (distance / shineWidth));
                    r = (int) (r + (255 - r) * shineStrength);
                    g = (int) (g + (255 - g) * shineStrength);
                    b = (int) (b + (255 - b) * shineStrength);
                }
            }

            r = clamp(r);
            g = clamp(g);
            b = clamp(b);

            String hex = String.format("#%02x%02x%02x", r, g, b);

            TextComponent charComponent = new TextComponent(String.valueOf(text.charAt(i)));
            charComponent.setColor(ChatColor.of(hex));
            charComponent.setBold(true);
            components[i] = charComponent;
        }

        return components;
    }

    public static String toLegacyText(BaseComponent[] components) {
        return TextComponent.toLegacyText(components);
    }

    private static float smoothstep(float x) {
        x = Math.max(0f, Math.min(1f, x));
        return x * x * (3f - 2f * x);
    }

    private static int lerp(int a, int b, float ratio) {
        return (int) (a + ratio * (b - a));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}