package de.dakror.factory.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.world.Tile;

/**
 * @author Dakror
 */
public class Machine extends Entity
{
	public Machine(int x, int y, int width, int height)
	{
		super(x * Tile.SIZE, y * Tile.SIZE);
		this.width = width * Tile.SIZE;
		this.height = height * Tile.SIZE;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(Color.white);
		g.fillRect((int) x, (int) y, width, height);
		g.setColor(Color.black);
		g.drawRect((int) x, (int) y, width, height);
		
		g.setColor(c);
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onReachTarget()
	{}
}
