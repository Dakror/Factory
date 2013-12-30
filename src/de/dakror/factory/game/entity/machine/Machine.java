package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePoint;

/**
 * @author Dakror
 */
public abstract class Machine extends Entity
{
	protected String name;
	protected ArrayList<TubePoint> points = new ArrayList<>();
	
	protected boolean enabled = true;
	protected boolean drawFrame = true;
	
	public Machine(float x, float y, int width, int height)
	{
		super(x * Block.SIZE, y * Block.SIZE, width * Block.SIZE, height * Block.SIZE);
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(Color.black);
		if (drawFrame) g.drawRect(x, y, width, height);
		
		drawIcon(g);
		
		for (TubePoint p : points)
		{
			g.setColor(p.in ? Color.blue : Color.red);
			if (p.horizontal) g.fillRect(x + p.x * Block.SIZE + 8, y + p.y * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), Block.SIZE - 16, 4);
			else g.fillRect(x + p.x * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), y + p.y * Block.SIZE + 8, 4, Block.SIZE - 16);
		}
		
		g.setColor(c);
	}
	
	public BufferedImage getImage()
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Machine m = (Machine) clone();
		m.x = 0;
		m.y = 0;
		
		drawIcon(g);
		
		return bi;
	}
	
	protected abstract void drawIcon(Graphics2D g);
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onReachTarget()
	{}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON3) dead = true;
	}
	
	public String getName()
	{
		return name;
	}
}
