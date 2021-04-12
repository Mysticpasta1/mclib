package mchorse.mclib;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import mchorse.mclib.client.gui.utils.ValueColors;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.commands.CommandCheats;
import mchorse.mclib.commands.CommandMcLib;
import mchorse.mclib.commands.utils.L10n;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.config.values.ValueBoolean;
import mchorse.mclib.config.values.ValueInt;
import mchorse.mclib.config.values.ValueRL;
import mchorse.mclib.events.RegisterConfigEvent;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Operation;
import mchorse.mclib.math.Operator;
import mchorse.mclib.utils.PayloadASM;
import mchorse.mclib.utils.Reference;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.command.CommandManager;

import java.util.Map;
import java.util.Objects;

/*
 * McLib mod
 * 
 * All it does is provides common code for McHorse's mods.
 */
public class McLib implements ModInitializer {

    public static final String CLIENT_PROXY = "mchorse.mclib.ClientProxy";
    public static final String SERVER_PROXY = "mchorse.mclib.CommonProxy";

    public static CommonProxy proxy;

    public static L10n l10n = new L10n(Reference.MOD_ID);

    /* Configuration */
    public static ValueBoolean opDropItems;

    public static ValueBoolean debugPanel;
    public static ValueColors favoriteColors;
    public static ValueInt primaryColor;
    public static ValueBoolean enableBorders;
    public static ValueBoolean enableCheckboxRendering;
    public static ValueBoolean enableTrackpadIncrements;
    public static ValueBoolean enableGridRendering;
    public static ValueInt userIntefaceScale;

    public static ValueBoolean enableCursorRendering;
    public static ValueBoolean enableMouseButtonRendering;
    public static ValueBoolean enableKeystrokeRendering;
    public static ValueInt keystrokeOffset;
    public static ValueInt keystrokeMode;

    public static ValueRL backgroundImage;
    public static ValueInt backgroundColor;

    public static ValueBoolean scrollbarFlat;
    public static ValueInt scrollbarShadow;
    public static ValueInt scrollbarWidth;

    public static ValueBoolean multiskinMultiThreaded;
    public static ValueBoolean multiskinClear;

    public static ValueInt maxPacketSize;


    @Override
    public void onInitialize() {
        proxy.preInit();
        proxy.init();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (server.isSinglePlayer())
            {
                CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                    dispatcher.register(CommandManager.literal("cheats").then(CommandManager.argument("enable", BoolArgumentType.bool()).executes((ctx) -> {
                            Objects.requireNonNull(Objects.requireNonNull(ctx.getSource().getEntity()).getServer()).getPlayerManager().setCheatsAllowed(ctx.getArgument("cheats", Boolean.class));
                            ctx.getSource().getEntity().getServer().save(false, false, true);
                            return Command.SINGLE_SUCCESS;
                })));
            }
            else
            {
                event.registerServerCommand(new CommandMcLib());
            }
        });

    }

    public void onConfigRegister(RegisterConfigEvent event)
    {
        opDropItems = event.opAccess.category(Reference.MOD_ID).getBoolean("drop_items", true);

        /* McLib's options */
        ConfigBuilder builder = event.createBuilder(Reference.MOD_ID);

        debugPanel = builder.category("appearance").getBoolean("debug_panel", false);
        debugPanel.invisible();
        primaryColor = builder.getInt("primary_color", 0x0088ff).color();
        enableBorders = builder.getBoolean("enable_borders", false);
        enableCheckboxRendering = builder.getBoolean("enable_checkbox_rendering", false);
        enableTrackpadIncrements = builder.getBoolean("enable_trackpad_increments", true);
        enableGridRendering = builder.getBoolean("enable_grid_rendering", true);
        userIntefaceScale = builder.getInt("user_interface_scale", 2, 0, 4);

        favoriteColors = new ValueColors("favorite_colors");
        builder.register(favoriteColors);

        builder.getCategory().markClientSide();

        enableCursorRendering = builder.category("tutorials").getBoolean("enable_mouse_rendering", false);
        enableMouseButtonRendering = builder.getBoolean("enable_mouse_buttons_rendering", false);
        enableKeystrokeRendering = builder.getBoolean("enable_keystrokes_rendering", false);
        keystrokeOffset = builder.getInt("keystroke_offset", 10, 0, 20);
        keystrokeMode = builder.getInt("keystroke_position", 1).modes(
            IKey.lang("mclib.keystrokes_position.auto"),
            IKey.lang("mclib.keystrokes_position.bottom_left"),
            IKey.lang("mclib.keystrokes_position.bottom_right"),
            IKey.lang("mclib.keystrokes_position.top_right"),
            IKey.lang("mclib.keystrokes_position.top_left")
        );

        builder.getCategory().markClientSide();

        backgroundImage = builder.category("background").getRL("image",  null);
        backgroundColor = builder.getInt("color",  0xcc000000).colorAlpha();

        builder.getCategory().markClientSide();

        scrollbarFlat = builder.category("scrollbars").getBoolean("flat", false);
        scrollbarShadow = builder.getInt("shadow", 0x88000000).colorAlpha();
        scrollbarWidth = builder.getInt("width", 4, 2, 10);

        builder.getCategory().markClientSide();

        multiskinMultiThreaded = builder.category("multiskin").getBoolean("multithreaded", true);
        multiskinClear = builder.getBoolean("clear", true);

        builder.getCategory().markClientSide();

        maxPacketSize = builder.category("vanilla").getInt("max_packet_size", PayloadASM.MIN_SIZE, PayloadASM.MIN_SIZE, Integer.MAX_VALUE);
        maxPacketSize.syncable();
    }


    public boolean checkModDependencies(Map<String, String> map, Environment side)
    {
        return true;
    }

    public void serverInit(ServerStartingEvent event)
    {

    }

    public static void main(String[] args) throws Exception
    {
        Operator.DEBUG = true;
        MathBuilder builder = new MathBuilder();

        test(builder, "1 - 2 * 3 + 4 ", 1 - 2 * 3 + 4  );
        test(builder, "2 * 3 - 8 + 7 ", 2 * 3 - 8 + 7  );
        test(builder, "3 - 7 + 2 * 4 ", 3 - 7 + 2 * 4  );
        test(builder, "8 / 4 - 3 * 10", 8 / 4 - 3 * 10 );
        test(builder, "2 - 4 * 5 / 8 ", 2 - 4 * 5 / 8D );
        test(builder, "3 / 4 * 8 - 10", 3 / 4D * 8 - 10);
        test(builder, "2 * 3 / 4 * 5 ", 2D * 3 / 4 * 5 );
        test(builder, "2 + 3 - 4 + 5 ", 2 + 3 - 4 + 5  );
        test(builder, "7 - 2 ^ 4 - 4 * 5 + 15 ^ 2", 7 - Math.pow(2, 4) - 4 * 5 + Math.pow(15, 2));
        test(builder, "5 -(10 + 20)", 5 -(10 + 20));

        IValue test = builder.parse("str_contains(\"minecraft:diamond_axe\", \"axe\") ? \"Yeet\" : \"olo\"");

        System.out.println(test.isNumber() + " " + test.stringValue() + " " + test.booleanValue() + " " + test.doubleValue());
    }

    public static void test(MathBuilder builder, String expression, double result) throws Exception
    {
        IValue value = builder.parse(expression);

        System.out.println(expression + " = " + value.get() + " (" + result + ") is " + Operation.equals(value.get().doubleValue(), result));
        System.out.println(value.toString() + "\n");
    }
}