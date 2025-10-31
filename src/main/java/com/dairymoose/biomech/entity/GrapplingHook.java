package com.dairymoose.biomech.entity;

import org.joml.Vector3f;
import org.slf4j.Logger;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor.GrappleInfo;
import com.mojang.logging.LogUtils;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrapplingHook extends ThrowableItemProjectile {

	private static final Logger LOGGER = LogUtils.getLogger();
	
	public MechPart mechPart = null;
	public Entity entityOwner = null;
	public boolean didHit = false;
	public int lifeSpan = -1;
	public static final int DISCARD_LIFE_SPAN = -1;
	public long clientHitTick = -1;
	
	private Vec3 clientSavedImpactPoint;
	

	public static final EntityType<GrapplingHook> GRAPPLING_HOOK_ENTITY = EntityType.Builder
			.<GrapplingHook>of(GrapplingHook::new, MobCategory.MISC).sized(0.40F, 0.40F)
			.clientTrackingRange(30).updateInterval(20).build(new ResourceLocation(BioMech.MODID, "grappling_hook").toString());
	
	public GrapplingHook(EntityType<? extends GrapplingHook> entityType, Level level) {
		super(entityType, level);
		if (this.level().isClientSide) {
			mechPart = GrappleArmArmor.clientLastUsedArm;
		}
	}
	
	public GrapplingHook(Level level, LivingEntity living) {
		super(GRAPPLING_HOOK_ENTITY, living, level);
		//super(GRAPPLING_HOOK_ENTITY, level);
	}

	public GrapplingHook(Level p_37476_, double p_37477_, double p_37478_, double p_37479_) {
		super(GRAPPLING_HOOK_ENTITY, p_37477_, p_37478_, p_37479_, p_37476_);
	}
	
	@Override
	protected void updateRotation() {
		if (!this.didHit)
			super.updateRotation();
	}

	public static final EntityDataAccessor<Integer> DATA_ENTITY_OWNER_ID = SynchedEntityData.defineId(GrapplingHook.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Vector3f> DATA_IMPACT_POINT = SynchedEntityData.defineId(GrapplingHook.class, EntityDataSerializers.VECTOR3);
	public static final EntityDataAccessor<Integer> DATA_MECH_PART_ORDINAL = SynchedEntityData.defineId(GrapplingHook.class, EntityDataSerializers.INT);
	
	@Override
	protected Vec3 getLeashOffset() {
		return new Vec3(0.0, 0.0, 0.0);
	}
	
	private void onHitLogic() {
		this.didHit = true;
		Vec3 location = new Vec3(this.getEntityData().get(DATA_IMPACT_POINT));
		
		lifeSpan = DISCARD_LIFE_SPAN;
		this.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
		this.setNoGravity(true);
		this.setPos(location);
		
		LOGGER.debug("Grappling hook hit with owner = " + entityOwner);
		//if (!this.level().isClientSide) {
			if (entityOwner != null) {
				GrappleInfo grappleInfo = this.getOrCreateGrappleInfoForEntity(entityOwner);
				LOGGER.debug("grappleInfoMap size=" + GrappleArmArmor.grappleInfoMap.size());
				if (grappleInfo != null) {
					Vec3 loc = location;
					this.updateGrappleInfo(grappleInfo, loc);
				}
				if (!this.level().isClientSide) {
					entityOwner.resetFallDistance();
				}
			} else {
				if (this.level().isClientSide) {
					clientSavedImpactPoint = location;
				}
				//BioMech.grappleHookPos = null;
			}
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
    	if (accessor == DATA_ENTITY_OWNER_ID) {
    		Integer entityOwnerId = this.entityData.get(DATA_ENTITY_OWNER_ID);
    		Entity e = this.level().getEntity(entityOwnerId);
    		this.entityOwner = e;
    		LOGGER.debug("sync data update: " + e);
    		
    		if (this.clientSavedImpactPoint != null && this.entityOwner != null) {
    			GrappleInfo grappleInfo = this.getOrCreateGrappleInfoForEntity(entityOwner);
    			LOGGER.debug("grappleInfoMap size=" + GrappleArmArmor.grappleInfoMap.size());
    			if (grappleInfo != null) {
    				this.updateGrappleInfo(grappleInfo, this.clientSavedImpactPoint);
    			}
    		}
    	} else if (accessor == DATA_IMPACT_POINT) {
    		this.onHitLogic();
    	} else if (accessor == DATA_MECH_PART_ORDINAL) {
    		int ordinal = this.getEntityData().get(DATA_MECH_PART_ORDINAL).intValue();
    		if (ordinal != -1) {
    			this.mechPart = MechPart.values()[ordinal];
    		}
    	}
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ENTITY_OWNER_ID, -1);
        this.getEntityData().define(DATA_IMPACT_POINT, new Vector3f(0.0f, 0.0f, 0.0f));
        this.getEntityData().define(DATA_MECH_PART_ORDINAL, -1);
     }

	@Override
	public void tick() {
		super.tick();
	
		if (entityOwner != null) {
			if (!this.level().isClientSide) {
				this.getEntityData().set(DATA_ENTITY_OWNER_ID, this.entityOwner.getId());
			}
			
			if (!didHit) {
				GrappleInfo grappleInfo = this.getOrCreateGrappleInfoForEntity(entityOwner);
				if (grappleInfo.grappleTetherDistance == -1.0f) {
					this.didHit = true;
					return;
				}
				if (grappleInfo != null) {
					//grappleInfo.hookPos = this.position();
				}
			}
		} else {
			if (!this.level().isClientSide) {
				BioMech.LOGGER.debug("Cleanup invalid grappling hook: " + this);
				this.discard();
			}
		}
		
		if (!this.level().isClientSide) {
			if (this.getEntityData().get(DATA_MECH_PART_ORDINAL).intValue() == -1) {
				this.getEntityData().set(DATA_MECH_PART_ORDINAL, this.mechPart.ordinal());
			}
			
			if (lifeSpan > 0) {
				--lifeSpan;
			}
			if (lifeSpan == 0) {
				this.discard();
			}
		}
	}
	
	protected void onHitEntity(EntityHitResult p_37486_) {
		super.onHitEntity(p_37486_);
		if (!didHit) {
			p_37486_.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.10F);
		}
	}

	private void updateGrappleInfo(GrappleInfo grappleInfo, Vec3 loc) {
		grappleInfo.hookPos = loc;
		float tetherDistance = (float) loc.distanceTo(entityOwner.position());
		if (grappleInfo.grappleMaximumTetherDistance < tetherDistance) {
			grappleInfo.grappleMaximumTetherDistance = tetherDistance;
		}
		if (grappleInfo.grappleMaximumTetherDistance < GrappleArmArmor.defaultMaximumTetherDistance) {
			grappleInfo.grappleMaximumTetherDistance = GrappleArmArmor.defaultMaximumTetherDistance; 
		}
		grappleInfo.grappleTetherDistance = tetherDistance;
		if (entityOwner.isControlledByLocalInstance()) {
			//BioMech.fallingDeltaY = entityOwner.getDeltaMovement().y;
		}
		
		if (this.entityOwner != null) {
			this.entityOwner.resetFallDistance();
		}
		LOGGER.debug("Got hit at location " + loc + " with distance " + tetherDistance + " and horizontalDist=" + loc.subtract(entityOwner.position()).horizontalDistance());
	}
	
	private GrappleInfo getOrCreateGrappleInfoForEntity(Entity entity) {
		GrappleInfo grappleInfo = GrappleArmArmor.grappleInfoMap.get(entityOwner.getUUID());
		if (grappleInfo != null) {
			LOGGER.debug("Got existing map entry for entity=" + entity);
			return grappleInfo; 
		} else {
			if (entity instanceof Player player) {
				LOGGER.debug("Created new map entry for player=" + player);
				GrappleInfo toReturn = new GrappleInfo();
				GrappleArmArmor.grappleInfoMap.put(player.getUUID(), toReturn);
				return toReturn;
			}
		}
		LOGGER.debug("Invalid map entry for entity=" + entity);
		return null;
	}
	
	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		
		if (!didHit && !this.level().isClientSide) {
			didHit = true;
			this.getEntityData().set(DATA_IMPACT_POINT, hitResult.getLocation().toVector3f());
		}

	}

	protected Item getDefaultItem() {
		return BioMechRegistry.ITEM_GRAPPLING_HOOK.get();
	}
}
