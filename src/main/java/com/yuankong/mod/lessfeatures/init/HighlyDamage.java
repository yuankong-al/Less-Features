package com.yuankong.mod.lessfeatures.init;

import com.yuankong.mod.lessfeatures.config.LessFeaturesConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HighlyDamage {
    @SubscribeEvent
    public static void toHighlyDamage(LivingEvent.LivingUpdateEvent event){

        Entity entity = event.getEntity();
        World world = entity.world;

        if(LessFeaturesConfig.IS_HIGHLY_DAMAGE.get() && entity instanceof LivingEntity){
            if(entity.world.getDimensionKey().equals(World.OVERWORLD)){
                if(entity.getPosY()>LessFeaturesConfig.OVERWORLD_HIGHLY.get()){
                    entity.attackEntityFrom(selectDamageSource(world,1),1);
                }
            }

            if(entity.world.getDimensionKey().equals(World.THE_NETHER)){
                if(entity.getPosY()>LessFeaturesConfig.NETHER_HIGHLY.get()){
                    entity.attackEntityFrom(selectDamageSource(world,2),1);
                }
            }

            if(entity.world.getDimensionKey().equals(World.THE_END)){
                if(entity.getPosY()>LessFeaturesConfig.END_HIGHLY.get()){
                    entity.attackEntityFrom(selectDamageSource(world,3),1);
                }
            }

        }

    }

    private static DamageSource selectDamageSource(World world ,int worldType){
        if(worldType==1){
            if(world.isDaytime()){
                return randomDamageSource(3);
            }else{
                return randomDamageSource(4);
            }

        } else if (worldType == 2) {
            return randomDamageSource(3);
        }else {
            return randomDamageSource(4);
        }
    }

    private static DamageSource randomDamageSource(int x){
        DamageSource damageSource1 = new DamageSource("high.world.1").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        DamageSource damageSource2 = new DamageSource("high.world.2").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        DamageSource damageSource3 = new DamageSource("high.world.3").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        DamageSource damageSource4 = new DamageSource("high.world.4").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        Random random = new Random();
        switch(random.nextInt(x)) {
            case 0:
            default:
                return damageSource1;
            case 1:
                return damageSource2;
            case 2:
                return damageSource3;
            case 3:
                return damageSource4;
        }
    }

}
