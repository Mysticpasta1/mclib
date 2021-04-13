package mchorse.mclib.utils;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.List;

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

        RayTraceResult result = rayTrace(input, maxReach, 1.0F);
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

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity = list.get(i);

            if (entity == input)
            {
                continue;
            }

            AxisAlignedBB aabb = entity.getEntity().getBoundingBox().grow(entity.getCollisionBorderSize());
            RayTraceResult intercept = aabb.calculateIntercept(eyes, max);

            if (aabb.contains(eyes))
            {
                if (entityDistance >= 0.0D)
                {
                    hit = intercept == null ? eyes : intercept.getHitVec();
                    target = entity;
                    entityDistance = 0.0D;
                }
            }
            else if (intercept != null)
            {
                double eyesDistance = eyes.distanceTo(intercept.getHitVec());

                if (eyesDistance < entityDistance || entityDistance == 0.0D)
                {
                    if (entity.getLowestRidingEntity() == input.getLowestRidingEntity() && !input.canRiderInteract())
                    {
                        if (entityDistance == 0.0D)
                        {
                            hit = intercept.getHitVec();
                            target = entity;
                        }
                    }
                    else
                    {
                        hit = intercept.getHitVec();
                        target = entity;
                        entityDistance = eyesDistance;
                    }
                }
            }
        }

        if (target != null)
        {
            result = new EntityRayTraceResult(target, hit);
        }

        return result;
    }

    /**
     * This method is extracted from {@link Entity} class, because it was marked
     * as client side only code.
     */
    public static RayTraceResult rayTrace(Entity input, double blockReachDistance, float partialTicks)
    {
        Vec3d eyePos = new Vec3d(input.posX, input.posY + input.getEyeHeight(), input.posZ);
        Vec3d eyeDir = input.getLook(partialTicks);
        Vec3d eyeReach = eyePos.addVector(eyeDir.x * blockReachDistance, eyeDir.y * blockReachDistance, eyeDir.z * blockReachDistance);

        return input.world.rayTraceBlocks(eyePos, eyeReach, false, false, true);
    }
}
