package com.yuankong.mod.lessfeatures;

import com.yuankong.mod.lessfeatures.config.LessFeaturesConfig;
import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lessfeatures")
public class LessFeatures {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public LessFeatures() {

        MinecraftForge.EVENT_BUS.register(new FallWater());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LessFeaturesConfig.COMMON_CONFIG);

        //MinecraftForge.EVENT_BUS.register(new EntityEventHandle());
        // Register ourselves for server and other game events we are interested in

    }


}
