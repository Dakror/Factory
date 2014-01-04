package de.dakror.factory.game.entity.machine.storage;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.ui.ItemList;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Storage extends Machine
{
	int capacity;
	
	public Storage(float x, float y)
	{
		super(x, y, 6, 3);
		points.add(new TubePoint(0, 2, true, true, false));
		points.add(new TubePoint(5, 2, false, true, false));
		
		name = "Lager";
		capacity = 50;
	}
	
	public int getCapacity()
	{
		return capacity;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		g.drawImage(Game.getImage("machine/storage.png"), x + (width - 128) / 2, y + (height - 128) / 2, Game.w);
		
		int h = Math.round(128 * (items.getLength() / capacity));
		
		Helper.drawImage(Game.getImage("machine/storage_fill.png"), x + (width - 128) / 2, y + (height - 128) / 2 + 128 - h, 128, h, 0, 128 - h, 128, h, g);
	}
	
	@Override
	protected void tick(int tick)
	{
		boolean en = new Boolean(running);
		running = items.getLength() < capacity;
		if (en && !running) Game.world.dispatchEntityUpdate(Cause.STORAGE_FULL, this);
	}
	
	@Override
	public Entity clone()
	{
		return new Storage(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source)
	{
		if (cause == Cause.ITEM_CONSUMED && Game.currentGame.getActiveLayer() instanceof ItemList) Game.currentGame.getActiveLayer().init();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (state == 1)
		{
			if (!(Game.currentGame.getActiveLayer() instanceof ItemList))
			{
				ItemList itemList = new ItemList(items);
				itemList.killOnUnfocus = true;
				Game.currentGame.addLayer(itemList);
			}
			else
			{
				((ItemList) Game.currentGame.getActiveLayer()).items = items;
				((ItemList) Game.currentGame.getActiveLayer()).killOnUnfocus = true;
				((ItemList) Game.currentGame.getActiveLayer()).addCategories = false;
				((ItemList) Game.currentGame.getActiveLayer()).init();
			}
		}
	}
}
