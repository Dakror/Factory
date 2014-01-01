package de.dakror.factory.game.entity.machine;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;
import de.dakror.gamesetup.util.path.Path;

/**
 * @author Dakror
 */
public class Miner extends Machine
{
	public Miner(float x, float y)
	{
		super(x, y, 2, 2);
		
		name = "Mine";
		points.add(new TubePoint(0, 0, false, true, true));
		enabled = false;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		int size = 64;
		g.drawImage(Game.getImage("machine/miner.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
	}
	
	@Override
	public Entity clone()
	{
		return new Miner(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	public void onEntityUpdate()
	{
		enabled = Game.world.isTube(x, y - Block.SIZE);
		
		if (enabled)
		{
			Item item = new Item(x + points.get(0).x * Block.SIZE, y + points.get(0).y * Block.SIZE, ItemType.IRON_DUST);
			
			Path thePath = null;
			for (Entity e : Game.world.getEntities())
			{
				if (e instanceof Storage)
				{
					Storage s = (Storage) e;
					
					TubePoint tp = null;
					for (TubePoint p : s.points)
					{
						if (p.in)
						{
							tp = p;
							break;
						}
					}
					
					Path path = AStar.getPath(new Vector(x / Block.SIZE + points.get(0).x, y / Block.SIZE + points.get(0).y), new Vector(s.getX() / Block.SIZE + tp.x, s.getY() / Block.SIZE + tp.y), new TubePathFinder());
					if (path != null) if (thePath == null || path.getLength() < thePath.getLength()) thePath = path;
				}
			}
			
			if (thePath != null)
			{
				thePath.setNodeReached();
				item.setPathTarget(thePath.getNode(thePath.getNodeCount() - 1));
				item.setPath(thePath);
				Game.world.addEntity(item);
			}
		}
	}
}
