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
public class Washer extends Machine
{
	int speed, requested, tick, startTick, in, out;
	
	boolean working;
	
	public Washer(float x, float y)
	{
		super(x, y, 3, 3);
		
		name = "Erzwaescher";
		running = false;
		working = false;
		
		points.add(new TubePoint(1, 0, true, true, true));
		points.add(new TubePoint(1, 2, false, true, false));
		
		in = 1;
		out = 2;
		
		speed = 300;
		requested = 0;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/washer.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE / 2, y + Block.SIZE / 2, Block.SIZE * 2, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	protected void tick(int tick)
	{
		this.tick = tick;
		
		if (!working)
		{
			if (tick % REQUEST_SPEED == 0 && requested < in && requestItemFromMachine(Storage.class, ItemType.coal_ore)) requested++;
			
			if (tick % REQUEST_SPEED == 0 && items.get(ItemType.coal) > 0 && Game.world.isTube(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE + Block.SIZE))
			{
				Item item = new Item(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE, ItemType.coal);
				item.setTargetMachineType(Storage.class);
				Game.world.addEntity(item);
				items.add(ItemType.coal, -1);
			}
			
			if (items.get(ItemType.coal_ore) == in)
			{
				requested = 0;
				working = true;
				startTick = tick;
			}
		}
		
		if (working && (tick - startTick) % speed == 0 && startTick != tick)
		{
			items.set(ItemType.coal_ore, 0);
			items.set(ItemType.coal, out);
			working = false;
		}
	}
	
	@Override
	public Entity clone()
	{
		return new Washer(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate()
	{}
}
