package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;

/**
 * @author Dakror
 */
public class Miner extends Machine
{
	
	public Miner(int x, int y)
	{
		super(x, y, 2, 2);
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/miner.png"), (int) x + (width - size) / 2, (int) y + (height - size) / 2, size, size, Game.w);
	}
}
