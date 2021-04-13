package mchorse.mclib.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Keys
{
    public static final String[] KEYS = new String[GLFW.GLFW_KEY_LAST];

    public static String getKeyName(int key)
    {
        if (key < 0 || key >= 256)
        {
            return null;
        }

        if (KEYS[key] == null)
        {
            KEYS[key] = getKey(key);
        }

        return KEYS[key];
    }

    private static String getKey(int key)
    {
        switch (key)
        {
            case GLFW.GLFW_KEY_MINUS:
                return "-";
            case GLFW.GLFW_KEY_EQUAL:
                return "=";
            case GLFW.GLFW_KEY_LEFT_BRACKET:
                return "[";
            case GLFW.GLFW_KEY_RIGHT_BRACKET:
                return "]";
            case GLFW.GLFW_KEY_SEMICOLON:
                return ";";
            case GLFW.GLFW_KEY_APOSTROPHE:
                return "'";
            case GLFW.GLFW_KEY_BACKSLASH:
                return "\\";
            case GLFW.GLFW_KEY_COMMA:
                return ",";
            case GLFW.GLFW_KEY_PERIOD:
                return ".";
            case GLFW.GLFW_KEY_SLASH:
                return "/";
            case GLFW.GLFW_KEY_GRAVE_ACCENT:
                return "`";
            case GLFW.GLFW_KEY_TAB:
                return "Tab";
            case GLFW.GLFW_KEY_CAPS_LOCK:
                return "Caps Lock";
            case GLFW.GLFW_KEY_LEFT_SHIFT:
                return "L. Shift";
            case GLFW.GLFW_KEY_LEFT_CONTROL:
                return "L. Ctrl";
            case GLFW.GLFW_KEY_LEFT_ALT:
                return "L. Alt";
            case GLFW.GLFW_KEY_LEFT_SUPER:
                return Minecraft.IS_RUNNING_ON_MAC ? "L. Cmd" : "L. Win";
            case GLFW.GLFW_KEY_RIGHT_SHIFT:
                return "R. Shift";
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                return "R. Ctrl";
            case GLFW.GLFW_KEY_RIGHT_ALT:
                return "R. Alt";
            case GLFW.GLFW_KEY_RIGHT_SUPER:
                return Minecraft.IS_RUNNING_ON_MAC ? "R. Cmd" : "R. Win";
            case GLFW.GLFW_KEY_KP_DIVIDE:
                return "Numpad /";
            case GLFW.GLFW_KEY_KP_MULTIPLY:
                return "Numpad *";
            case GLFW.GLFW_KEY_KP_SUBTRACT:
                return "Numpad -";
            case GLFW.GLFW_KEY_KP_ADD:
                return "Numpad +";
            case GLFW.GLFW_KEY_KP_DECIMAL:
                return "Numpad .";
        }

        String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));

        if (name.length() > 1)
        {
            name = name.substring(0, 1) + name.substring(1).toLowerCase();
        }

        if (name.startsWith("Numpad"))
        {
            name = name.replace("Numpad", "Numpad ");
        }

        return name;
    }
}