package mchorse.mclib.network.mclib.server;

import mchorse.mclib.McLib;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.network.mclib.common.PacketDropItem;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class ServerHandlerDropItem extends ServerMessageHandler<PacketDropItem>
{
    @Override
    public void run(ServerPlayerEntity player, PacketDropItem message)
    {
        if (player.isCreative() && McLib.opDropItems.get() || OpHelper.isPlayerOp(player))
        {
            ItemStack stack = message.stack;

            player.inventory.addItemStackToInventory(stack);
            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, 1F);
            player.container.detectAndSendChanges();
        }
    }
}