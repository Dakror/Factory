package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.ui.ItemSlot;
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
	public void drawGUI(Graphics2D g)
	{
		Helper.drawContainer((Game.getWidth() - 600) / 2, (Game.getHeight() - 300) / 3, 600, 300, true, false, g);
		super.drawGUI(g);
	}
	
	@Override
	protected void tick(int tick)
	{
		boolean en = new Boolean(running);
		running = items.getLength() < capacity;
		if (en && !running) Game.world.dispatchEntityUpdate();
	}
	
	@Override
	public Entity clone()
	{
		return new Storage(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate()
	{
		container.components.clear();
		ArrayList<ItemType> filled = items.getFilled();
		for (int i = 0; i < filled.size(); i++)
		{
			container.components.add(new ItemSlot((Game.getWidth() - 560) / 2 + (i * ItemSlot.SIZE), (Game.getHeight() - 300) / 3 + 20, filled.get(i), items.get(filled.get(i))));
		}
	}
}
