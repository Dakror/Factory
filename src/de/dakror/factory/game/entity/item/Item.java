package de.dakror.factory.game.entity.item;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;
import de.dakror.gamesetup.util.path.Path;

/**
 * @author Dakror
 */
public class Item extends Entity
{
	ItemType type;
	Class<?> targetMachineType;
	Machine targetMachine;
	
	boolean findPathOnReachNode;
	
	public Item(float x, float y, ItemType type)
	{
		super(x, y, Block.SIZE, Block.SIZE);
		this.type = type;
		drawBelow = false;
		speed = 1f;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		type.draw(x + (width - 32) / 2, y + (height - 32) / 2, g);
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	public Entity clone()
	{
		return null;
	}
	
	@Override
	protected void onReachTarget()
	{
		if (targetMachine != null)
		{
			targetMachine.getItems().add(type, 1);
			dead = true;
		}
	}
	
	public boolean setTargetMachineType(Class<?> m)
	{
		targetMachineType = m;
		
		return findPathToTargetMachine();
	}
	
	public boolean findPathToTargetMachine()
	{
		Path thePath = null;
		for (Entity e : Game.world.getEntities())
		{
			if (e.getClass().equals(targetMachineType))
			{
				Machine s = (Machine) e;
				if (!s.isRunning()) continue;
				
				TubePoint tp = null;
				for (TubePoint p : s.getTubePoints())
				{
					if (p.in)
					{
						tp = p;
						break;
					}
				}
				
				Path path = AStar.getPath(new Vector(Math.round((float) x / Block.SIZE), Math.round((float) y / Block.SIZE)), new Vector(s.getX() / Block.SIZE + tp.x, s.getY() / Block.SIZE + tp.y), new TubePathFinder());
				if (path != null) if (thePath == null || path.getLength() < thePath.getLength())
				{
					thePath = path;
					targetMachine = s;
				}
			}
		}
		
		if (thePath == null) return false;
		else
		{
			if (thePath.getNodeCount() > 1 && thePath.getNode().clone().mul(Block.SIZE).equals(getPos())) thePath.setNodeReached();
			
			if (path == null || !path.equals(thePath))
			{
				setPathTarget(thePath.getNode(thePath.getNodeCount() - 1));
				setPath(thePath);
			}
			
			return true;
		}
	}
	
	public Class<?> getTargetMachineType()
	{
		return targetMachineType;
	}
	
	@Override
	public void onEntityUpdate()
	{
		findPathOnReachNode = true;
		if (path == null) onReachPathNode();
	}
	
	@Override
	public void onRemoval()
	{}
	
	@Override
	public void onReachPathNode()
	{
		if (findPathOnReachNode)
		{
			if (!findPathToTargetMachine())
			{
				path = null;
				target = null;
				targetMachine = null;
			}
			findPathOnReachNode = false;
		}
	}
}
