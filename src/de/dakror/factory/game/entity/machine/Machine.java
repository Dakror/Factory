package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.settings.TubePoint;

/**
 * @author Dakror
 */
public abstract class Machine extends Entity
{
	protected String name;
	protected ArrayList<TubePoint> points = new ArrayList<>();
	
	public Machine(int x, int y, int width, int height)
	{
		super(x * Block.SIZE, y * Block.SIZE);
		this.width = width * Block.SIZE;
		this.height = height * Block.SIZE;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(Color.black);
		g.drawRect((int) x, (int) y, width, height);
		
		drawIcon(g);
		
		for (TubePoint p : points)
		{
			g.setColor(p.in ? Color.blue : Color.green);
			if (p.horizontal) g.fillRect((int) x + p.x * Block.SIZE + 8, (int) y + p.y * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), Block.SIZE - 16, 4);
			else g.fillRect((int) x + p.x * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), (int) y + p.y * Block.SIZE + 8, 4, Block.SIZE - 16);
		}
		
		g.setColor(c);
	}
	
	protected abstract void drawIcon(Graphics2D g);
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onReachTarget()
	{}
}
