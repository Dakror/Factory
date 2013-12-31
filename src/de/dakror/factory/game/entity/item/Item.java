package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
import de.dakror.gamesetup.util.Helper;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;

/**
 * @author Dakror
 */
public class Item extends Entity
{
	ItemType type;
	
	public Item(float x, float y, ItemType type)
	{
		super(x, y, Block.SIZE, Block.SIZE);
		this.type = type;
		drawBelow = false;
		speed = 0.5f;
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
		if (type.block) g.drawRect(x + (width - 32) / 2, y + (height - 32) / 2, 32, 32);
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
	{}
	
	@Override
	public void onEntityUpdate()
	{
		setPath(AStar.getPath(new Vector(Math.round(x / Block.SIZE), Math.round(y / Block.SIZE)), pathTarget, new TubePathFinder()));
	}
}
