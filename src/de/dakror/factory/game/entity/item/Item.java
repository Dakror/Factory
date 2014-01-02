package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;
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
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		Helper.drawImage(Game.getImage((type.block ? "blocks" : "items") + ".png"), x + (width - 32) / 2, y + (height - 32) / 2, 32, 32, type.tx * 16, type.ty * 16, 16, 16, g);
		Color c = g.getColor();
		g.setColor(Color.black);
		if (type.block) g.drawRect(x + (width - 32) / 2, y + (height - 32) / 2, 31, 31);
		g.setColor(c);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
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
