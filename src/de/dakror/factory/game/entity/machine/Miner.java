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
public class Miner extends Machine
{
	ItemType[] types;
	int startTick;
	int speed;
	int tick;
	
	public Miner(float x, float y)
	{
		super(x, y, 2, 2);
		name = "Mine";
		points.add(new TubePoint(0, 0, false, true, true));
		running = false;
		speed = 800;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/miner.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (running) Helper.drawCooldownCircle(x, y, width, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone()
	{
		return new Miner(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	protected void tick(int tick)
	{
		this.tick = tick;
		if (startTick == 0) startTick = tick;
		
		if (running && (tick - startTick) % speed == 0) dig();
	}
	
	public void dig()
	{
		Item item = new Item(x + points.get(0).x * Block.SIZE, y + points.get(0).y * Block.SIZE, types[(int) (Math.random() * types.length)]);
		if (item.setTargetMachineType(Storage.class)) Game.world.addEntity(item);
		else running = false;
	}
	
	@Override
	public void onEntityUpdate()
	{
		running = Game.world.isTube(x, y - Block.SIZE);
		
		types = new ItemType[4];
		
		if (Game.world != null)
		{
			for (int i = 0; i < 4; i++)
			{
				types[i] = ItemType.valueOf(Block.values()[Game.world.getBlock(x / Block.SIZE + i % 2, y / Block.SIZE + i / 2)].name());
			}
		}
	}
}
