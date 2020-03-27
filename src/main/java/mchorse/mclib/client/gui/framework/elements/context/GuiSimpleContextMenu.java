package mchorse.mclib.client.gui.framework.elements.context;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;
import java.util.function.Consumer;

public class GuiSimpleContextMenu extends GuiContextMenu
{
	public GuiListElement<Action> actions;

	public GuiSimpleContextMenu(Minecraft mc)
	{
		super(mc);

		this.actions = new GuiActionListElement(mc, (action) ->
		{
			action.get(0).runnable.run();
			this.removeFromParent();
		});

		this.actions.flex().parent(this.area).w(1, 0).h(1, 0);
		this.add(this.actions);
	}

	public GuiSimpleContextMenu action(String label, Runnable runnable)
	{
		return this.action(Icons.NONE, label, runnable);
	}

	public GuiSimpleContextMenu action(Icon icon, String label, Runnable runnable)
	{
		if (icon == null || label == null || runnable == null)
		{
			return this;
		}

		this.actions.add(new Action(icon, label, runnable));

		return this;
	}

	@Override
	public void setMouse(GuiContext context)
	{
		this.flex().set(context.mouseX, context.mouseY, 100, this.actions.scroll.scrollSize);
	}

	public static class GuiActionListElement extends GuiListElement<Action>
	{
		public GuiActionListElement(Minecraft mc, Consumer<List<Action>> callback)
		{
			super(mc, callback);

			this.scroll.scrollItemSize = 20;
		}

		@Override
		public void drawElement(Action element, int i, int x, int y, boolean hover, boolean selected)
		{
			int h = this.scroll.scrollItemSize;

			if (hover)
			{
				Gui.drawRect(x, y, x + this.scroll.w, y + this.scroll.scrollItemSize, 0x88000000 + McLib.primaryColor.get());
			}

			GlStateManager.color(1, 1, 1, 1);
			element.icon.render(x + 2, y + h / 2, 0, 0.5F);
			this.font.drawString(element.label, x + 22, y + (h - this.font.FONT_HEIGHT) / 2 + 1, 0xffffff);
		}
	}

	public static class Action
	{
		public Icon icon;
		public String label;
		public Runnable runnable;

		public Action(Icon icon, String label, Runnable runnable)
		{
			this.icon = icon;
			this.label = label;
			this.runnable = runnable;
		}
	}
}