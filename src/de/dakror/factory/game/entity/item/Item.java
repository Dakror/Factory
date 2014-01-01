package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
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
	Machine targetMachine;
	
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
	
	public void setTargetMachine(Machine m)
	{
		targetMachine = m;
	}
	
	public Machine getTargetMachine()
	{
		return targetMachine;
	}
	
	@Override
	public void onEntityUpdate()
	{
		Path p = AStar.getPath(new Vector(Math.round(x / Block.SIZE), Math.round(y / Block.SIZE)), pathTarget, new TubePathFinder());
		if (p == null)
		{
			setPath(null);
			target = null;
		}
		else
		{
			setPath(p);
		}
	}
	
	@Override
	public void onRemoval()
	{}
}
