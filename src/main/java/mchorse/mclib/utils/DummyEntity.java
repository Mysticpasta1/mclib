package mchorse.mclib.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

import java.util.Arrays;

/**
 * Dummy entity
 *
 * This class is used in model editor as a player substitution for the model
 * methods.
 */
public class DummyEntity extends LivingEntity
{
    private final ItemStack[] held;
    public ItemStack right;
    public ItemStack left;

    public DummyEntity(EntityType<LivingEntity> dummyEntity, World worldIn)
    {
        super(dummyEntity, worldIn);

        this.right = new ItemStack(Items.DIAMOND_SWORD);
        this.left = new ItemStack(Items.GOLDEN_SWORD);
        this.held = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    }

    public void setItems(ItemStack left, ItemStack right)
    {
        this.left = left;
        this.right = right;
    }

    public void toggleItems(boolean toggle)
    {
        int main = EquipmentSlotType.MAINHAND.getSlotIndex();
        int off = EquipmentSlotType.OFFHAND.getSlotIndex();

        if (toggle)
        {
            this.held[main] = this.right;
            this.held[off] = this.left;
        }
        else
        {
            this.held[main] = this.held[off] = ItemStack.EMPTY;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList()
    {
        return Arrays.asList(this.held);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn)
    {
        return this.held[slotIn.getSlotIndex()];
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack)
    {
        this.held[slotIn.getSlotIndex()] = stack;
    }

    @Override
    public HandSide getPrimaryHand()
    {
        return HandSide.RIGHT;
    }
}