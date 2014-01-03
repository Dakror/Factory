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
public class Pulverizer extends Machine
{
	public Pulverizer(float x, float y)
	{
		super(x, y, 3, 2);
		
		name = "Pulverisierer";
		running = false;
		
		points.add(new TubePoint(1, 0, true, true, true));
		points.add(new TubePoint(1, 1, false, true, false));
		
		speed = 600;
		
		inputs.add(ItemType.iron_ore);
		
		outputs.add(ItemType.iron_dust);
		outputs.add(ItemType.iron_dust);
	}
	
	@Override
	protected void doRequest()
	{
		if (requestItemFromMachine(Storage.class, ItemType.getItemsByCategory(Category.ore)))
		{
			inputs.set(0, requestedItemType);
			
			String name = requestedItemType.name();
			outputs.set(0, ItemType.valueOf(name.substring(0, name.indexOf("_") + 1) + "dust"));
			outputs.set(1, ItemType.valueOf(name.substring(0, name.indexOf("_") + 1) + "dust"));
			
			requested++;
		}
		else waitWithRequestUntilEntityUpdate = true;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/pulverizer.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE / 2, y, height, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone()
	{
		return new Pulverizer(x / Block.SIZE, y / Block.SIZE);
	}
}
