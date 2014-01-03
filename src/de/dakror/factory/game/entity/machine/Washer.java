package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Washer extends Machine
{
	public Washer(float x, float y)
	{
		super(x, y, 3, 3);
		
		name = "Erzwaescher";
		running = false;
		
		points.add(new TubePoint(1, 0, true, true, true));
		points.add(new TubePoint(1, 2, false, true, false));
		
		speed = 300;
		requested = 0;
		
		inputs.add(ItemType.coal_ore);
		
		outputs.add(ItemType.coal);
		outputs.add(ItemType.coal);
		outputs.add(ItemType.coal);
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/washer.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE / 2, y + Block.SIZE / 2, Block.SIZE * 2, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone()
	{
		return new Washer(x / Block.SIZE, y / Block.SIZE);
	}
}
