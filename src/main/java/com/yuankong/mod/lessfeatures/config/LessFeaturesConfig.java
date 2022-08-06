package com.yuankong.mod.lessfeatures.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LessFeaturesConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue FALL_WATER;
    public static ForgeConfigSpec.BooleanValue RIDING_ENTITY_DAMAGE;
    public static ForgeConfigSpec.BooleanValue IS_HIGHLY_DAMAGE;
    public static ForgeConfigSpec.IntValue OVERWORLD_HIGHLY;
    public static ForgeConfigSpec.IntValue NETHER_HIGHLY;
    public static ForgeConfigSpec.IntValue END_HIGHLY;
    public static ForgeConfigSpec.BooleanValue IS_INFINITE_Water;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("LessFeatures settings").push("general");
        FALL_WATER = COMMON_BUILDER.comment("Setting whether the water damage.").define("FallWaterDamage", true);
        RIDING_ENTITY_DAMAGE = COMMON_BUILDER.comment("Setting whether the riding entity damage.").define("RidingEntityDamage", true);
        IS_HIGHLY_DAMAGE = COMMON_BUILDER.comment("Whether the higher area have damage in overworld and nether?").define("HigherAreaHasDamage", true);
        OVERWORLD_HIGHLY = COMMON_BUILDER.comment("If Higher area hava damage,you can set how the highly of overworld/the nether/the end will hurt).").defineInRange("OverWorldHighly", 256, 255, 320);
        NETHER_HIGHLY = COMMON_BUILDER.defineInRange("TheNetherHighly",128,127,320);
        END_HIGHLY = COMMON_BUILDER.defineInRange("TheEndHighly",256,255,320);
        IS_INFINITE_Water = COMMON_BUILDER.comment("Whether infinite water?").define("IsInfiniteWater", false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
