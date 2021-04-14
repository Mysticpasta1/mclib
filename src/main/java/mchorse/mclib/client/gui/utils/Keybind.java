package mchorse.mclib.client.gui.utils;

import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Keys;
import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Keybind class
 */
public class Keybind
{
    public IKey label;
    public IKey category = IKey.EMPTY;
    public int mainKey;
    public int[] heldKeys;
    public Runnable callback;
    public boolean inside;
    public boolean active = true;
    public Supplier<Boolean> activeSupplier;

    public Keybind(IKey label, int mainKey, Runnable callback)
    {
        this.label = label;
        this.mainKey = mainKey;
        this.callback = callback;
    }

    public Keybind held(int... keys)
    {
        this.heldKeys = Arrays.copyOf(keys, keys.length);

        return this;
    }

    public Keybind inside()
    {
        this.inside = true;

        return this;
    }

    public Keybind active(Supplier<Boolean> active)
    {
        this.activeSupplier = active;

        return this;
    }

    public Keybind active(boolean active)
    {
        this.active = active;

        return this;
    }

    public Keybind category(IKey category)
    {
        this.category = category;

        return this;
    }

    public String getKeyCombo()
    {
        String label = Keys.getKeyName(this.mainKey);

        if (this.heldKeys != null)
        {
            for (int held : this.heldKeys)
            {
                label = Keys.getKeyName(held) + " + " + label;
            }
        }

        return label;
    }

    public boolean check(int keyCode, boolean inside)
    {
        if (keyCode != this.mainKey)
        {
            return false;
        }

        if (this.heldKeys != null)
        {
            for (int key : this.heldKeys)
            {
                if (!this.isKeyDown(key))
                {
                    return false;
                }
            }
        }

        if (this.inside)
        {
            return inside;
        }

        return true;
    }

    protected boolean isKeyDown(int key)
    {
        if (key == GLFW.GLFW_KEY_LEFT_SHIFT || key == GLFW.GLFW_KEY_RIGHT_SHIFT)
        {
            return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
        }
        else if (key == GLFW.GLFW_KEY_LEFT_CONTROL || key == GLFW.GLFW_KEY_RIGHT_CONTROL)
        {
            return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
        }
        else if (key == GLFW.GLFW_KEY_LEFT_ALT || key == GLFW.GLFW_KEY_RIGHT_ALT)
        {
            return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_ALT);
        }

        return Keyboard.isKeyDown(key);
    }

    public boolean isActive()
    {
        if (this.activeSupplier != null)
        {
            return this.activeSupplier.get();
        }

        return this.active;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Keybind)
        {
            Keybind keybind = (Keybind) obj;

            return this.mainKey == keybind.mainKey && Arrays.equals(this.heldKeys, keybind.heldKeys) && this.inside == keybind.inside;
        }

        return super.equals(obj);
    }
}