package mchorse.mclib.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mchorse.mclib.McLib;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import java.util.List;

final public class CommandCheats extends McCommandBase
{
    @Override
    public L10n getL10n()
    {
        return McLib.l10n;
    }


    public String getName()
    {
        return "cheats";
    }


    public String getUsage(ICommandSender sender)
    {
        return "mclib.commands.cheats";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}cheats {7}<enabled:true|false>{r}";
    }


    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        sender.getEntityWorld().getWorldInfo().setAllowCommands(CommandBase.parseBoolean(args[0]));
        server.saveAllWorlds(false);
    }


    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, McCommandBase.BOOLEANS);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public int run(CommandContext<Object> context) throws CommandSyntaxException {
        return 0;
    }
}
