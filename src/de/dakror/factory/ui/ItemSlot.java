package de.dakror.factory.ui;

import java.awt.Graphics2D;

import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class ItemSlot extends ClickableComponent
{
	public static final int SIZE = 64;
	
	ItemType type;
	int amount;
	
	public ItemSlot(int x, int y, ItemType type, int amount)
	{
		super(x, y, SIZE, SIZE);
		this.type = type;
		this.amount = amount;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawShadow(x, y, width, getHeight(), g);
		Helper.drawOutline(x, y, width, getHeight(), false, g);
		
		type.draw(x + (width - 32) / 2, y + (height - 32) / 2, g);
		
		if (amount > 1) Helper.drawRightAlignedString(amount + "", x + width - 4, y + height - 6, g, 25);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g)
	{
		int width = g.getFontMetrics(g.getFont().deriveFont(25f)).stringWidth(type.name) + 20;
		int height = 40;
		Helper.drawShadow(x + 10, y, width, height, g);
		Helper.drawOutline(x + 10, y, width, height, false, g);
		Helper.drawString(type.name, x + 20, y + 27, g, 25);
	}
}
