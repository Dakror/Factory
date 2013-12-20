package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;

/**
 * @author Dakror
 */
public class Storage extends Machine
{
	
	public Storage(int x, int y)
	{
		super(x, y, 6, 3);
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		g.drawImage(Game.getImage("machine/storage.png"), (int) x + (width - 128) / 2, (int) y + (height - 128) / 2, Game.w);
	}
}
