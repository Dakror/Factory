package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Smeltery extends Machine
{
	public Smeltery(float x, float y)
	{
		super(x, y, 5, 3);
		
		name = "Schmelze";
		running = false;
		
		points.add(new TubePoint(2, 0, true, true, true));
		points.add(new TubePoint(2, 2, false, true, false));
		
		speed = 20;
		
		inputs.add(ItemType.iron_dust);
		inputs.add(ItemType.coal);
		
		outputs.add(ItemType.iron_ingot);
	}
	
	@Override
	protected void doRequest()
	{
		if (requested == 0)
		{
			if (requestItemFromMachine(Storage.class, ItemType.getItemsByCategory(Category.dust)))
			{
				inputs.set(0, requestedItemType);
				
				String name = requestedItemType.name();
				outputs.set(0, ItemType.valueOf(name.substring(0, name.indexOf("_") + 1) + "ingot"));
				
				requested++;
			}
			else waitWithRequestUntilEntityUpdate = true;
		}
		else super.doRequest();
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 120;
		g.drawImage(Game.getImage("machine/smeltery.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE - 16, y - 16, Block.SIZE * 3 + 32, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone()
	{
		return new Smeltery(x / Block.SIZE, y / Block.SIZE);
	}
}
