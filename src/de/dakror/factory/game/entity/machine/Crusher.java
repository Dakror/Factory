package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Crusher extends Machine
{
	public Crusher(float x, float y)
	{
		super(x, y, 2, 2);
		
		name = "Verschrotter";
		running = false;
		
		speed = 50;
		
		points.add(new TubePoint(0, 0, true, true, true));
		points.add(new TubePoint(1, 1, false, true, false));
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/crusher.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (running) Helper.drawCooldownCircle(x, y, width, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone()
	{
		return new Crusher(x / Block.SIZE, y / Block.SIZE);
	}
	
}
