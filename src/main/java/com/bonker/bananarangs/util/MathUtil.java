package com.bonker.bananarangs.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class MathUtil {

    // copied from LocalCoordinates.java for relative coordinates (^ ^ ^)
    public static Vec3 relativePosition(Vec2 rot, Vec3 pos, double forwards, double up, double left) {
        float f = Mth.cos((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f1 = Mth.sin((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(-rot.x * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-rot.x * ((float)Math.PI / 180F));
        float f4 = Mth.cos((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        float f5 = Mth.sin((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        Vec3 vec31 = new Vec3(f * f2, f3, f1 * f2);
        Vec3 vec32 = new Vec3(f * f4, f5, f1 * f4);
        Vec3 vec33 = vec31.cross(vec32).scale(-1.0D);
        double d0 = vec31.x * forwards + vec32.x * up + vec33.x * left;
        double d1 = vec31.y * forwards + vec32.y * up + vec33.y * left;
        double d2 = vec31.z * forwards + vec32.z * up + vec33.z * left;
        return new Vec3(pos.x + d0, pos.y + d1, pos.z + d2);
    }

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
