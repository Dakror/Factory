package de.dakror.factory.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.factory.game.entity.item.Items;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.ui.Component;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class ItemList extends Layer
{
	public static final int WIDTH = 360;
	
	public boolean killOnUnfocus = false;
	public boolean addCategories = false;
	
	public Items items;
	
	public ItemList()
	{}
	
	public ItemList(Items items)
	{
		this.items = items;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawContainer(Game.getWidth() - WIDTH, 0, WIDTH, Game.getHeight() - 110, true, false, g);
		drawComponents(g);
	}
	
	@Override
	public void update(int tick)
	{
		updateComponents(tick);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (!new Rectangle(Game.getWidth() - WIDTH, 0, WIDTH, Game.getHeight() - 110).contains(e.getPoint()) && killOnUnfocus) Game.currentGame.removeLayer(this);
	}
	
	@Override
	public void init()
	{
		components.clear();
		
		int perRow = (WIDTH - 40) / ItemSlot.SIZE;
		
		if (addCategories)
		{
			for (Category c : Category.values())
			{
				ItemSlot is = new ItemSlot(Game.getWidth() - WIDTH + 20 + (c.ordinal() % perRow) * ItemSlot.SIZE, 20 + (c.ordinal() / perRow) * ItemSlot.SIZE, ItemType.getItemsByCategory(c)[0], 0);
				is.keepClicked = true;
				is.category = c;
				is.tooltipOnRight = false;
				components.add(is);
			}
		}
		for (ItemType it : ItemType.values())
		{
			ItemSlot is = new ItemSlot(Game.getWidth() - WIDTH + 20 + ((it.ordinal() + (addCategories ? Category.values().length : 0)) % perRow) * ItemSlot.SIZE, 20 + ((it.ordinal() + (addCategories ? Category.values().length : 0)) / perRow) * ItemSlot.SIZE, it, items == null ? 1 : items.get(it));
			is.keepClicked = true;
			is.tooltipOnRight = false;
			components.add(is);
		}
	}
	
	public ItemSlot getSelectedItemSlot()
	{
		for (Component c : components)
			if (c instanceof ItemSlot && ((ItemSlot) c).selected) return (ItemSlot) c;
		
		return null;
	}
}
