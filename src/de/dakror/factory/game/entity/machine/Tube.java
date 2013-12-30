package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Vector;

public class Tube extends Machine
{
	boolean connectedToExit;
	boolean connectedToInput;
	
	public Tube(float x, float y)
	{
		super(x, y, 1, 1);
		
		name = "Rohr";
		drawFrame = false;
		connectedToExit = connectedToInput = false;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(Color.black);
		
		if (!Game.world.isTube(x, y - Block.SIZE)) g.fillRect(x, y, width, 4);
		if (!Game.world.isTube(x - Block.SIZE, y)) g.fillRect(x, y, 4, height);
		if (!Game.world.isTube(x, y + Block.SIZE)) g.fillRect(x, y + height - 4, width, 4);
		if (!Game.world.isTube(x + Block.SIZE, y)) g.fillRect(x + width - 4, y, 4, height);
		
		if (Game.world.isTube(x, y - Block.SIZE))
		{
			if (Game.world.isTube(x - Block.SIZE, y)) g.fillRect(x, y, 4, 4);
			if (Game.world.isTube(x + Block.SIZE, y)) g.fillRect(x + width - 4, y, 4, 4);
		}
		if (Game.world.isTube(x, y + Block.SIZE))
		{
			if (Game.world.isTube(x - Block.SIZE, y)) g.fillRect(x, y + height - 4, 4, 4);
			if (Game.world.isTube(x + Block.SIZE, y)) g.fillRect(x + width - 4, y + height - 4, 4, 4);
		}
		
		if (connectedToInput)
		{
			g.setColor(Color.blue);
			g.drawRect(x, y, width, height);
		}
		if (connectedToExit)
		{
			g.setColor(Color.red);
			g.drawRect(x + 1, y + 1, width - 2, height - 2);
		}
		
		g.setColor(c);
	}
	
	@Override
	public Entity clone()
	{
		return new Tube(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate()
	{
		connectedToExit = connectedToInput = false;
		
		for (Entity e : Game.world.getEntities())
		{
			if (e instanceof Machine && ((Machine) e).points.size() > 0)
			{
				for (TubePoint tp : ((Machine) e).points)
				{
					if ((tp.in && connectedToInput) || (tp.in && connectedToExit)) continue;
					
					if (new Vector(tp.x * Block.SIZE + e.getX(), tp.y * Block.SIZE + e.getY()).getDistance(getPos()) == Block.SIZE)
					{
						if (tp.horizontal)
						{
							if (tp.y * Block.SIZE + e.getY() == y) continue;
							if (Game.world.isTube(x - Block.SIZE, y) && Game.world.isTube(x + Block.SIZE, y)) continue;
						}
						else
						{
							if (tp.x * Block.SIZE + e.getX() == x) continue;
							if (Game.world.isTube(x, y - Block.SIZE) && Game.world.isTube(x, y + Block.SIZE)) continue;
						}
						
						if (tp.in) connectedToInput = true;
						else connectedToExit = true;
					}
				}
			}
		}
	}
}
