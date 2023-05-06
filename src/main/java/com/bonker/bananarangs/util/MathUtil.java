package com.bonker.bananarangs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public static Iterable<BlockPos> insideHitbox(AABB hitbox) {
        return BlockPos.betweenClosed(Mth.floor(hitbox.minX), Mth.floor(hitbox.minY), Mth.floor(hitbox.minZ), Mth.floor(hitbox.maxX), Mth.floor(hitbox.maxY), Mth.floor(hitbox.maxZ));
    }

    public static List<BlockPos> insideHitboxSorted(AABB hitbox, Vec3 point) {
        List<BlockPos> list = new ArrayList<>();
        for (BlockPos pos : insideHitbox(hitbox)) list.add(pos);
        return sortByDistance(list, point);
    }

    public static List<BlockPos> sortByDistance(List<BlockPos> toSort, Vec3 point) {
        List<BlockPos> list = new ArrayList<>(toSort);
        bubbleSort(list, pos -> point.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()));
        return list;
    }

    public static <T> void bubbleSort(List<T> list, Function<T, Double> valueGetter) {
        int size = list.size();
        T temp;
        for (int i = 0; i < size; i++){
            for (int j = 1; j < (size-i); j++){
                if (valueGetter.apply(list.get(j - 1)) > valueGetter.apply(list.get(j))) {
                    temp = list.get(j-1);
                    list.set(j - 1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
}
