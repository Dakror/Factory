package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;

/**
 * @author Dakror
 */
public class Storage extends Machine
{
	public Storage(float x, float y)
	{
		super(x, y, 6, 3);
		points.add(new TubePoint(0, 2, true, true, false));
		points.add(new TubePoint(5, 2, false, true, false));
		
		name = "Lager";
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		g.drawImage(Game.getImage("machine/storage.png"), x + (width - 128) / 2, y + (height - 128) / 2, Game.w);
	}
	
	@Override
	public Entity clone()
	{
		return new Storage(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate()
	{}
}
