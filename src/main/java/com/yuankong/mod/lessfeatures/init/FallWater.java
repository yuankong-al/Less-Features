package com.yuankong.mod.lessfeatures.init;

import com.yuankong.mod.lessfeatures.config.LessFeaturesConfig;
import com.yuankong.mod.lessfeatures.event.EntityEventHandle;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.text.DecimalFormat;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FallWater {

    public static HashMap<Entity, Double> thisEntityDate = new HashMap<>();
    public static HashMap<Entity, Double> thisEntityDate2 = new HashMap<>();

    public static  HashMap<Entity,Float> isNoRiddingToFall = new HashMap<>();

    public static  HashMap<Entity,Entity> entityList = new HashMap<>();

    /*public static  HashMap<Entity,Entity> entityList2 = new HashMap<>();*/
    @SubscribeEvent
    public void toEntityFallEvent(LivingEvent.LivingUpdateEvent event){

        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity){

            World world = entity.world;

            if (thisEntityDate.get(entity) == null) {
                if (!entity.hasNoGravity()) {
                    if(entity.getMotion().y<0.5){
                        thisEntityDate.put(entity, entity.getPosY());

                    }
                }
            }

            if(thisEntityDate.get(entity) != null){
                if(entity.getMotion().y>0){
                    thisEntityDate.remove(entity);
                }
            }

            if(thisEntityDate2.get(entity) != null){
                if(entity.getMotion().y>0){
                    thisEntityDate2.remove(entity);
                }
            }

            if(thisEntityDate.get(entity) != null){
                if(entity.isInWater()){
                    thisEntityDate.remove(entity);
                }
            }


            if(entity instanceof PlayerEntity){
                if(!((PlayerEntity) entity).abilities.isCreativeMode && !entity.isSpectator()){
                    if (thisEntityDate.get(entity) != null && thisEntityDate2.get(entity) == null && (thisEntityDate.get(entity) - entity.getPosY()) >= 5) {
                        thisEntityDate2.put(entity, entity.getPosY());
                    }
                }
            }else {
                if (!entity.removed && !(entity instanceof WaterMobEntity) && thisEntityDate.containsKey(entity) && !thisEntityDate2.containsKey(entity) && (thisEntityDate.get(entity) - entity.getPosY()) >= 5) {
                    thisEntityDate2.put(entity, entity.getPosY());
                }
            }

            /*//当实体上升高度大于五格时清除状态。
            if(entityList.containsKey(entity) && thisEntityDate.containsKey(entity) && thisEntityDate.get(entity)-entity.getPosY()<-4){
                entityList.remove(entity);
            }*/

            if (LessFeaturesConfig.RIDING_ENTITY_DAMAGE.get()) {

                if (entity.isPassenger()) {//处理乘骑时落水/地
                    //落地船补丁
                    if(entity.getRidingEntity() instanceof BoatEntity && thisEntityDate2.get(entity.getRidingEntity())!=null){
                        BoatEntity boatEntity = (BoatEntity) entity.getRidingEntity();
                        removeBoat(world,boatEntity);
                        if(thisEntityDate2.get(entity)!=null){
                            entity.attackEntityFrom(DamageSource.FALL,fallHart(entity)+2);
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                        }
                        thisEntityDate2.remove(entity.getRidingEntity());
                    }

                    if(isNoRiddingToFall.get(entity)!=null){//骑生物时坠落造成乘骑主受伤
                        entity.attackEntityFrom(DamageSource.FALL,isNoRiddingToFall.get(entity)-5);
                        entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                        isNoRiddingToFall.remove(entity);
                    }

                    if (thisEntityDate.get(entity)!=null && thisEntityDate.get(entity) - entity.getPosY() >= 10) {
                        if (entity.getRidingEntity()!=null && entity.getRidingEntity().isInWater()) {
                            if (entity.getRidingEntity() instanceof MinecartEntity) {
                                entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                            }
                        }
                        /*if (entity.getRidingEntity() instanceof BoatEntity) {//落地 船处理
                            if (entity.getRidingEntity()!=null && entity.getRidingEntity().isOnGround() && !entity.getRidingEntity().isInWater()) {
                                BoatEntity boatEntity = (BoatEntity) entity.getRidingEntity();
                                entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                                doBoatDrop(boatEntity);
                                removeBoat(world,boatEntity);
                                //entity.fallDistance = fallHart(entity);
                            }
                        }*/
                    } else {
                        if (entity.getRidingEntity()!=null && entity.getRidingEntity().isInWater()) {
                            if (!entity.getRidingEntity().removed) {
                                thisEntityDate.remove(entity);
                                thisEntityDate2.remove(entity);
                            }

                        }
                        if (entity.getRidingEntity()!=null && entity.getRidingEntity().isOnGround()) {
                            if (!entity.getRidingEntity().removed) {
                                thisEntityDate.remove(entity);
                                thisEntityDate2.remove(entity);
                            }
                        }
                    }


                    if(thisEntityDate2.get(entity)!=null && entityList.get(entity)!=null){//处理落地船不足十格的下落受伤
                        if(thisEntityDate2.get(entity) - entity.getPosY()<4 &&
                                getBlock(world,entityList.get(entity),0,-1,0) != Blocks.AIR &&
                                ((getBlock(world,entity,0,-1.3,0) != Blocks.WATER && getBlock(world,entity,0,-1,0) != Blocks.WATER) && getBlock(world,entity,0,-0.5,0) != Blocks.WATER)){
                            entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);

                            entityList.remove(entity);
                        }

                    }
                    if(thisEntityDate.get(entity)!=null && thisEntityDate.get(entity)-entity.getPosY()>=7){//处理落水船
                        if(EntityEventHandle.noRidBoatFall2.get(entity.getRidingEntity())!=null && thisEntityDate.get(entity)!=null && thisEntityDate.get(entity) - entity.getPosY()<=11 && (getBlock(world,entity,0,-1.3,0) == Blocks.WATER || getBlock(world,entity,0,-0.8,0) == Blocks.WATER)){
                            entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                        }
                    }

                    //落地-船处理
                    if(entity.getRidingEntity()!=null && entity.getRidingEntity() instanceof BoatEntity){
                        if(thisEntityDate2.get(entity)!=null && getBlock(world,entity,0,-1,0) != Blocks.AIR && EntityEventHandle.noRidBoatFall2.get(entity.getRidingEntity())==null/*(world.getBlockState(new BlockPos(entity.getPosX(), entity.getPosY() - 1, entity.getPosZ())).getBlock() != Blocks.WATER || world.getBlockState(new BlockPos(entity.getPosX(), entity.getPosY() - 0.5, entity.getPosZ())).getBlock() != Blocks.WATER)*/){
                            entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                        }
                    }


                }else {//处理即将落地却中途下船导致无伤
                    if(thisEntityDate2.get(entity)!=null && entityList.get(entity)!=null){
                        if(getBlock(world,entity,0,-0.5,0) != Blocks.AIR && EntityEventHandle.noRidBoatFall.get(entity)==null){
                            entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                            entityList.remove(entity);
                        }

                        //落地船补丁：下落途中脱离船
                        if(entityList.get(entity)!=null && !entityList.get(entity).removed){
                            if((getBlock(world,entity,0,-1,0) != Blocks.AIR || getBlock(world,entity,0,-1.5,0) != Blocks.AIR) && getBlock(world,entity,0,-1,0) != Blocks.WATER){
                                removeBoat(world, (BoatEntity) entityList.get(entity));
                                entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                                entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                                entityList.remove(entity);
                            }

                        }else{
                            if(getBlock(world,entity,0,-0.5,0) != Blocks.AIR && getBlock(world,entity,0,-1,0) != Blocks.WATER){
                                entity.fallDistance = fallHart(entity)+5;
                                /*entity.attackEntityFrom(DamageSource.FALL,fallHart(entity));
                                entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);*/
                            }

                        }
                    }


                }

            }


            if(thisEntityDate2.get(entity)!=null){
                if(entity.isInWater()){//跳水伤害
                    if (LessFeaturesConfig.FALL_WATER.get()) {//从配置文件获取值
                        if (getBlock(world,entity,0,-1,0) != Blocks.AIR && getBlock(world,entity,0,-1,0) != Blocks.WATER &&
                                getBlock(world,entity,0,-2,0) != Blocks.AIR && getBlock(world,entity,0,-2,0) != Blocks.WATER) {

                            entity.attackEntityFrom(DamageSource.FALL, fallHart(entity));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);

                        } else{
                            entity.attackEntityFrom(DamageSource.FALL, (fallHart(entity)/2));
                            entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                        }

                    }
                    thisEntityDate.remove(entity);
                    thisEntityDate2.remove(entity);

                }

            }

        }

    }

    public static float fallHart(Entity entity ){
        double newFallDistance = 0;
        if(thisEntityDate2.get(entity)!=null){
            newFallDistance = (thisEntityDate2.get(entity) - entity.getPosY());
        }
        DecimalFormat df = new DecimalFormat("#.0");//将数值保留一位小数
        float hurt = Float.parseFloat(df.format(newFallDistance));//将double转为float;
        if(hurt<1){
            return 1;
        }else{
            return hurt;
        }

    }

    public static void removeBoat(World world,BoatEntity boatEntity){
        if(!boatEntity.removed){
            for(int i = 0; i < 3; ++i){
                boatEntity.entityDropItem(boatEntity.getBoatType().asPlank());
            }
            boatEntity.entityDropItem(Items.STICK);
            boatEntity.entityDropItem(Items.STICK);
            if(world.getGameRules().get(GameRules.DO_ENTITY_DROPS).get()){
                world.getGameRules().get(GameRules.DO_ENTITY_DROPS).set(false,world.getServer());
                boatEntity.attackEntityFrom(DamageSource.FALL, 4.5F);
                world.getGameRules().get(GameRules.DO_ENTITY_DROPS).set(true,world.getServer());
                boatEntity.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 1.0f);
            }else {
                boatEntity.attackEntityFrom(DamageSource.FALL, 4.5F);
                boatEntity.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 1.0f);
            }
        }


    }

    public static Block getBlock(World world, Entity entity, double addX, double addY, double addZ){
        return world.getBlockState(new BlockPos(entity.getPosX()+addX, entity.getPosY()+addY, entity.getPosZ()+addZ)).getBlock();
    }

    /*public static Entity getBoat(Entity entity){
        Entity boat = null;
        for(Entity x:entityList2.keySet()){
            if(entityList2.get(x).equals(entity)){
                boat = x;
            }
        }
        return boat;
    }*/

}
