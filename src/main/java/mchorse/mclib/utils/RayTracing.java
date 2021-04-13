package mchorse.mclib.utils;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RayTracing
{
    /**
     * Get the entity at which given player is looking at
     */
    public static Entity getTargetEntity(Entity input, double maxReach)
    {
        EntityRayTraceResult result = rayTraceWithEntity(input, maxReach);

        return result.getType() == RayTraceResult.Type.ENTITY ? result.getEntity() : null;
    }

    /**
     * Kind of like rayTrace method, but as well it takes into account entity
     * ray tracing
     */
    public static EntityRayTraceResult rayTraceWithEntity(Entity input, double maxReach)
    {
        double blockDistance = maxReach;

        RayTraceResult result = rayTrace(input, maxReach, 1.0F, false);
        Vector3d eyes = new Vector3d(input.getPosX(), input.getPosY() + input.getEyeHeight(), input.getPosZ());

        if (result != null)
        {
            blockDistance = result.getHitVec().distanceTo(eyes);
        }

        Vector3d look = input.getLook(1.0F);
        Vector3d max = eyes.add(look.x * maxReach, look.y * maxReach, look.z * maxReach);
        Vector3d hit = null;
        Entity target = null;

        float area = 1.0F;

        List<Entity> list = input.world.getEntitiesInAABBexcluding(input, input.getBoundingBox().expand(look.x * maxReach, look.y * maxReach, look.z * maxReach).grow(area, area, area), new Predicate<Entity>()
        {
            @Override
            public boolean apply(@Nullable Entity entity)
            {
                return entity != null && entity.canBeCollidedWith();
            }
        });

        double entityDistance = blockDistance;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);

            if (entity == input) {
                continue;
            }

            AxisAlignedBB aabb = entity.getEntity().getBoundingBox().grow(entity.getCollisionBorderSize());
            Optional<Vector3d> intercept = aabb.rayTrace(eyes, max);

            if (intercept.isPresent()) {
                if (aabb.contains(eyes)) {
                    if (entityDistance >= 0.0D) {
                        hit = intercept == null ? eyes : intercept.get();
                        target = entity;
                        entityDistance = 0.0D;
                    }
                } else if (intercept != null) {
                    double eyesDistance = eyes.distanceTo(intercept.get());

                    if (eyesDistance < entityDistance || entityDistance == 0.0D) {
                        if (entity.getLowestRidingEntity() == input.getLowestRidingEntity() && !input.canRiderInteract()) {
                            if (entityDistance == 0.0D) {
                                hit = intercept.get();
                                target = entity;
                            }
                        } else {
                            hit = intercept.get();
                            target = entity;
                            entityDistance = eyesDistance;
                        }
                    }
                }
            }

            if (target != null) {
                result = new EntityRayTraceResult(target, hit);
            }

            return (EntityRayTraceResult) result;
        }
        return (EntityRayTraceResult) result;
    }

    /**
     * This method is extracted from {@link Entity} class, because it was marked
     * as client side only code.
     */
    public static RayTraceResult rayTrace(Entity input, double blockReachDistance, float partialTicks, boolean rayTraceFluids)
    {
        Vector3d eyePos = new Vector3d(input.getPosX(), input.getPosY() + input.getEyeHeight(), input.getPosZ());
        Vector3d eyeDir = input.getLook(partialTicks);
        Vector3d eyeReach = eyePos.add(eyeDir.x * blockReachDistance, eyeDir.y * blockReachDistance, eyeDir.z * blockReachDistance);

        return input.world.rayTraceBlocks(new RayTraceContext(eyePos, eyeReach, RayTraceContext.BlockMode.OUTLINE, rayTraceFluids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, input));
    }
}
