package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.Items;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.TubePathFinder;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.Container.DefaultContainer;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;
import de.dakror.gamesetup.util.path.Path;

/**
 * @author Dakror
 */
public abstract class Machine extends Entity
{
	public static final int REQUEST_SPEED = 40;
	
	protected String name;
	protected ArrayList<TubePoint> points = new ArrayList<>();
	
	protected Items items;
	
	protected boolean running = true;
	protected boolean drawFrame = true;
	
	public DefaultContainer container;
	
	public Machine(float x, float y, int width, int height)
	{
		super(x * Block.SIZE, y * Block.SIZE, width * Block.SIZE, height * Block.SIZE);
		
		items = new Items();
		container = new DefaultContainer();
		
		addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.worldActiveMachine = Machine.this;
			}
		});
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(Color.black);
		if (drawFrame) g.drawRect(x, y, width, height);
		
		g.setColor(c);
		
		if (this instanceof Tube) drawIcon(g);
	}
	
	public void drawAbove(Graphics2D g)
	{
		if (this instanceof Tube) return;
		
		Color c = g.getColor();
		
		for (TubePoint p : points)
		{
			g.setColor(Color.white);
			g.fillRect(x + p.x * Block.SIZE + 4, y + p.y * Block.SIZE + 4, Block.SIZE - 8, Block.SIZE - 8);
			g.setColor(p.in ? Color.blue : Color.red);
			if (p.horizontal) g.fillRect(x + p.x * Block.SIZE + 8, y + p.y * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), Block.SIZE - 16, 4);
			else g.fillRect(x + p.x * Block.SIZE + (p.up ? 4 : Block.SIZE - 8), y + p.y * Block.SIZE + 8, 4, Block.SIZE - 16);
		}
		
		g.setColor(c);
		
		drawIcon(g);
	}
	
	public void drawGUI(Graphics2D g)
	{
		container.draw(g);
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
		if (contains2(e.getPoint()) && e.getButton() == MouseEvent.BUTTON3)
		{
			for (Entity e1 : Game.world.getEntities())
				if (e1 instanceof Item && getArea().intersects(e1.getArea())) return;
			
			dead = true;
		}
		
		if (!dead) super.mouseReleased(e);
	}
	
	public ArrayList<TubePoint> getTubePoints()
	{
		return points;
	}
	
	public void placeTubePoints()
	{
		for (TubePoint tp : points)
			Game.world.getEntities().add(new Tube(x / Block.SIZE + tp.x, y / Block.SIZE + tp.y));
	}
	
	public String getName()
	{
		return name;
	}
	
	public Items getItems()
	{
		return items;
	}
	
	@Override
	public void onRemoval()
	{
		if (this instanceof Tube) return;
		
		for (Entity e : Game.world.getEntities())
		{
			if (e instanceof Tube && getArea().contains(e.getArea())) e.setDead(true);
		}
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	@Override
	public void onReachPathNode()
	{}
	
	public boolean requestItemFromMachine(Class<?> m, ItemType type)
	{
		Path thePath = null;
		TubePoint tubepoint = null;
		Machine machine = null;
		for (Entity e : Game.world.getEntities())
		{
			if (e.getClass().equals(m))
			{
				Machine s = (Machine) e;
				if (s.getItems().get(type) <= 0) continue;
				
				TubePoint tp = null;
				for (TubePoint p : s.getTubePoints())
				{
					if (!p.in)
					{
						tp = p;
						break;
					}
				}
				
				TubePoint tp2 = null;
				for (TubePoint p : getTubePoints())
				{
					if (p.in)
					{
						tp2 = p;
						break;
					}
				}
				
				Path p = AStar.getPath(new Vector(s.getX() / Block.SIZE + tp.x, s.getY() / Block.SIZE + tp.y), new Vector(x / Block.SIZE + tp2.x, y / Block.SIZE + tp2.y), new TubePathFinder());
				if (p != null)
				{
					if (thePath == null || p.getLength() < thePath.getLength())
					{
						thePath = p;
						machine = s;
						tubepoint = tp;
					}
				}
			}
		}
		
		if (thePath == null) return false;
		
		thePath.setNodeReached();
		machine.getItems().add(type, -1);
		Item item = new Item(machine.getX() + tubepoint.x * Block.SIZE, machine.getY() + tubepoint.y * Block.SIZE, type);
		item.setTargetMachine(this);
		item.setPath(thePath);
		Game.world.addEntity(item);
		
		return true;
	}
}
