package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.world.Block;

public class Tube extends Machine
{
	/**
	 * 0 = left<br>
	 * 1 = top<br>
	 * 2 = right<br>
	 * 3 = down<br>
	 */
	int direction;
	
	public Tube(int x, int y, int direction)
	{
		super(x, y, 0, 0);
		this.direction = direction;
		if (direction == 0 || direction == 3)
		{
			width = 3 * Block.SIZE;
			height = Block.SIZE;
		}
		else
		{
			height = 3 * Block.SIZE;
			width = Block.SIZE;
		}
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		if (width == 3 * Block.SIZE) g.drawImage(Game.getImage("tube.png"), (int) x, (int) y, width, height, Game.w);
		else
		{
			AffineTransform old = g.getTransform();
			AffineTransform at = g.getTransform();
			at.rotate(Math.toRadians(90), x + width / 2, y + height / 2);
			g.setTransform(at);
			g.drawImage(Game.getImage("tube.png"), (int) x - Block.SIZE, (int) y + Block.SIZE, height, width, Game.w);
			g.setTransform(old);
		}
	}
}
