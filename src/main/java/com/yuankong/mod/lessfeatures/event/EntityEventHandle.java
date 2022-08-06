package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.config.LessFeaturesConfig;
import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandle {

    public static HashMap<Entity,Boolean> noRidBoatFall = new HashMap<>();
    public static HashMap<Entity,Boolean> noRidBoatFall2 = new HashMap<>();
    @SubscribeEvent
    public static void toEntityEvent(EntityEvent event){
        Entity entity = event.getEntity();
        if(LessFeaturesConfig.RIDING_ENTITY_DAMAGE.get()){
            if (entity instanceof PlayerEntity) {//处理落地船时坐起坐起导致无伤
                World world = entity.world;

                Entity entity1 = world.getEntitiesWithinAABB(BoatEntity.class,
                                new AxisAlignedBB(entity.getPosX() - 0.5, entity.getPosY() - 0.1, entity.getPosZ() - 0.5,
                                        entity.getPosX() + 0.5, entity.getPosY() - 0.6, entity.getPosZ() + 0.5), null).stream().sorted(new Object() {
                            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                return Comparator.comparing(_entcnd -> _entcnd.getDistanceSq(_x, _y, _z));
                            }
                        }.compareDistOf(entity.getPosX(), entity.getPosY(), entity.getPosZ())).findFirst().orElse(null);

                if(noRidBoatFall.get(entity)==null){
                    if (entity1 != null) {
                        noRidBoatFall.put(entity,true);
                    }

                }else{
                    if(entity1 == null && FallWater.getBlock(world,entity,0,-0.5,0) != Blocks.AIR){
                        noRidBoatFall.remove(entity);
                    }
                    if(entity1 != null && entity1.isInWater()){
                        noRidBoatFall.remove(entity);
                    }
                }

            }
        }


        if(LessFeaturesConfig.RIDING_ENTITY_DAMAGE.get()){
            if(entity instanceof BoatEntity){
                World world = entity.world;

                if(FallWater.getBlock(world,entity,0,-1,0) == Blocks.AIR && !entity.isInWater() && FallWater.thisEntityDate.get(entity)==null){
                    FallWater.thisEntityDate.put(entity, entity.getPosY());
                }

                //船的下落事实，用于落地-船的判断
                if(noRidBoatFall2.get(entity)==null && FallWater.thisEntityDate.get(entity) != null && FallWater.thisEntityDate.get(entity) - entity.getPosY() >= 4){
                    noRidBoatFall2.put(entity, true);
                }

                if(FallWater.thisEntityDate.get(entity) != null && FallWater.thisEntityDate.get(entity) - entity.getPosY() >= 5){
                    if (entity.isBeingRidden()) {
                        for (int i = 0; i < entity.getPassengers().size(); i++) {
                            FallWater.entityList.put(entity.getPassengers().get(i),entity);
                        }
                    }
                }

                if(FallWater.thisEntityDate.get(entity) != null && FallWater.thisEntityDate.get(entity) - entity.getPosY() > 9){//落地船处理
                    if((FallWater.getBlock(world,entity,0,-1,0) != Blocks.AIR || FallWater.getBlock(world,entity,-1,-1,0) != Blocks.AIR || FallWater.getBlock(world,entity,1,-1,0) != Blocks.AIR || FallWater.getBlock(world,entity,0,-1,-1) != Blocks.AIR || FallWater.getBlock(world,entity,0,-1,+1) != Blocks.AIR) && FallWater.getBlock(world,entity,0,-1,0) != Blocks.WATER && FallWater.getBlock(world,entity,0,-0.5,0) != Blocks.WATER){
                        entity.fallDistance = 0;//船的判定比较诡异，加入这个可以减少一点点诡异，比如不加时大于6格才扣血
                        if (!entity.removed) {//落地船受伤
                            BoatEntity boatEntity = (BoatEntity) entity;
                            if (boatEntity.isBeingRidden()) {
                                for (int i = 0; i < boatEntity.getPassengers().size(); i++) {
                                    boatEntity.getPassengers().get(i).attackEntityFrom(DamageSource.FALL, FallWater.fallHart(boatEntity.getPassengers().get(i))+2);
                                    entity.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1.0f, 1.0f);
                                }
                            }

                            FallWater.removeBoat(world,boatEntity);
                            FallWater.thisEntityDate2.put(entity,1d);
                        }
                    }
                }

                if(FallWater.getBlock(world,entity,0,-1,0) == Blocks.WATER || FallWater.getBlock(world,entity,0,-0.5,0) == Blocks.WATER){//洛水船处理
                    if(FallWater.thisEntityDate.get(entity) != null && FallWater.thisEntityDate.get(entity) - entity.getPosY() >= 11){
                        if (!entity.removed) {
                            BoatEntity boatEntity = (BoatEntity) entity;
                            FallWater.removeBoat(world,boatEntity);
                        }
                    } else if (FallWater.thisEntityDate.get(entity) != null && FallWater.thisEntityDate.get(entity) - entity.getPosY() >= 9) {
                        BoatEntity boatEntity = (BoatEntity) entity;
                        boatEntity.attackEntityFrom(DamageSource.FALL, 4.5F);
                    }
                }

                //清除状态
                if(entity.removed){
                    FallWater.thisEntityDate.remove(entity);
                    FallWater.thisEntityDate2.remove(entity);
                    noRidBoatFall2.remove(entity);
                }

                if(FallWater.thisEntityDate.get(entity) != null && entity.isOnGround() && FallWater.getBlock(world,entity,0,-0.5,0) != Blocks.AIR){
                    FallWater.thisEntityDate.remove(entity);
                    /*if(noRidBoatFall2.get(entity)!=null){
                        noRidBoatFall2.remove(entity);
                    }*/
                }

                if(FallWater.thisEntityDate.get(entity) != null && entity.isInWater() && FallWater.getBlock(world,entity,0,-0.5,0) != Blocks.AIR){
                    FallWater.thisEntityDate.remove(entity);
                    /*if(noRidBoatFall2.get(entity)!=null){
                        noRidBoatFall2.remove(entity);
                    }*/
                }

                if(noRidBoatFall2.get(entity)!=null && entity.isInWater() && FallWater.getBlock(world,entity,0,-0.2,0) != Blocks.AIR){
                    noRidBoatFall2.remove(entity);
                }


                /*if(!entity.removed){
                    Entity playerEntity = world.getEntitiesWithinAABB(PlayerEntity.class,
                                    new AxisAlignedBB(entity.getPosX() - 25, 0, entity.getPosZ() - 25,
                                            entity.getPosX() + 25, 256, entity.getPosZ() + 25), null).stream().sorted(new Object() {
                                Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                    return Comparator.comparing(_entcnd -> _entcnd.getDistanceSq(_x, _y, _z));
                                }
                            }.compareDistOf(entity.getPosX(), entity.getPosY(), entity.getPosZ())).findFirst().orElse(null);

                    if(playerEntity != null){
                        FallWater.entityList2.put(entity,playerEntity);
                    }

                }*/

            }
        }

    }

}
