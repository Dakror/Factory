package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Smeltery extends Machine
{
	int speed, requested, tick, startTick, in, out;
	
	boolean working;
	
	public Smeltery(float x, float y)
	{
		super(x, y, 5, 3);
		
		name = "Schmelze";
		running = false;
		working = false;
		
		points.add(new TubePoint(2, 0, true, true, true));
		points.add(new TubePoint(2, 2, false, true, false));
		
		in = 2;
		out = 1;
		
		speed = 20;
		requested = 0;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/smeltery.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE * 3 / 2, y + Block.SIZE / 2, Block.SIZE * 2, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	protected void tick(int tick)
	{
		this.tick = tick;
		if (!working)
		{
			if (tick % REQUEST_SPEED == 0 && requested < in && requestItemFromMachine(Storage.class, requested == 0 ? ItemType.iron_dust : ItemType.coal)) requested++;
			
			if (tick % REQUEST_SPEED == 0 && items.get(ItemType.iron_ingot) > 0 && Game.world.isTube(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE + Block.SIZE))
			{
				Item item = new Item(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE, ItemType.iron_ingot);
				item.setTargetMachineType(Storage.class);
				Game.world.addEntity(item);
				items.add(ItemType.iron_ingot, -1);
			}
			
			if (items.get(ItemType.iron_dust) == 1 && items.get(ItemType.coal) == 1)
			{
				requested = 0;
				working = true;
				startTick = tick;
			}
		}
		
		if (working && (tick - startTick) % speed == 0 && startTick != tick)
		{
			items.set(ItemType.iron_dust, 0);
			items.set(ItemType.coal, 0);
			items.set(ItemType.iron_ingot, out);
			working = false;
		}
	}
	
	@Override
	public Entity clone()
	{
		return new Smeltery(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate()
	{}
	
}
