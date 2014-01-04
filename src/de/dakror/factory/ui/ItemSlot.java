package de.dakror.factory.ui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class ItemSlot extends ClickableComponent
{
	public static final int SIZE = 64;
	
	public ItemType type;
	public int amount;
	public Category category;
	public boolean keepClicked = false;
	public boolean tooltipOnRight = true;
	public boolean rightClickClear = false;
	
	public ClickEvent pressEvent;
	
	public Image bg;
	
	public boolean selected;
	
	public ItemSlot(int x, int y, ItemType type, int amount)
	{
		super(x, y, SIZE, SIZE);
		this.type = type;
		this.amount = amount;
		
		addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				selected = true;
			}
		});
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (state == 0 || (keepClicked && !selected))
		{
			if (amount > 0 || category != null || type == null) Helper.drawShadow(x, y, width, height, g);
			Helper.drawOutline(x, y, width, getHeight(), false, g);
		}
		if ((state != 0 || (keepClicked && selected))) Helper.drawContainer(x, y, width, height, false, state == 1 || selected, g);
		
		if (bg != null)
		{
			Composite c = g.getComposite();
			if (type != null || category != null) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.drawImage(bg, x + 10, y + 10, width - 20, height - 20, Game.w);
			g.setComposite(c);
		}
		
		if (type != null) type.draw(x + (width - 32) / 2, y + (height - 32) / 2, g);
		
		if (amount == 0 && category == null && bg == null)
		{
			int off = selected || state != 0 ? 8 : 0;
			Helper.drawShadow(x - off, y - off, width + off * 2, height + off * 2, g);
		}
		
		if (category != null)
		{
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			Helper.drawHorizontallyCenteredString("?", x, width, y + 50, g, 64);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		if (amount > 1) Helper.drawRightAlignedString(amount + "", x + width - 4, y + height - 6, g, 25);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (state == 1 && pressEvent != null) pressEvent.trigger();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		if (!contains(e.getX(), e.getY())) selected = false;
		else if (e.getButton() == MouseEvent.BUTTON3 && rightClickClear)
		{
			category = null;
			amount = 0;
			type = null;
		}
	}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g)
	{
		if (type == null) return;
		
		int width = g.getFontMetrics(g.getFont().deriveFont(25f)).stringWidth(category == null ? type.name : "Kategorie: " + category.name) + 20;
		int height = 40;
		Helper.drawShadow(x + 10 - (tooltipOnRight ? 0 : width), y, width, height, g);
		Helper.drawOutline(x + 10 - (tooltipOnRight ? 0 : width), y, width, height, false, g);
		Helper.drawString(category == null ? type.name : "Kategorie: " + category.name, x + 20 - (tooltipOnRight ? 0 : width), y + 27, g, 25);
	}
}
