package com.bonker.bananarangs.util;

import net.minecraft.world.phys.Vec3;

public class MathUtil {
    public static double clamp(double value, double max) {
        return clamp(value, value, max);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(min, value), max);
    }

    public static Vec3 clamp(Vec3 values, double min, double max) {
        return new Vec3(clamp(values.x, min, max), clamp(values.y, min, max), clamp(values.z, min, max));
    }
}
