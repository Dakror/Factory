package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.Items;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.Container.DefaultContainer;

/**
 * @author Dakror
 */
public abstract class Machine extends Entity
{
	public static final int REQUEST_SPEED = 40;
	
	protected String name;
	protected ArrayList<TubePoint> points = new ArrayList<>();
	protected ArrayList<ItemType> inputs = new ArrayList<>();
	protected ArrayList<ItemType> outputs = new ArrayList<>();
	
	protected Items items;
	
	protected ItemType requestedItemType;
	
	protected boolean running = true;
	protected boolean drawFrame = true;
	protected boolean waitWithRequestUntilEntityUpdate = false;
	protected boolean working = false;
	
	protected int speed, requested, tick, startTick;
	
	public DefaultContainer container;
	
	public Machine(float x, float y, int width, int height)
	{
		super(x * Block.SIZE, y * Block.SIZE, width * Block.SIZE, height * Block.SIZE);
		
		items = new Items();
		requested = 0;
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
	{
		this.tick = tick;
		if (inputs.size() > 0)
		{
			if (!working)
			{
				if (tick % REQUEST_SPEED == 0 && items.getLength(outputs) > 0 && Game.world.isTube(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE + Block.SIZE))
				{
					ItemType it = items.getFilled().get(0);
					Item item = new Item(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE, it);
					Game.world.addEntity(item);
					items.add(it, -1);
				}
				
				if (items.getLength(inputs) == inputs.size())
				{
					requested = 0;
					working = true;
					startTick = tick;
				}
			}
			
			if (working && (tick - startTick) % speed == 0 && startTick != tick)
			{
				for (ItemType it : inputs)
					items.set(it, 0);
				
				for (ItemType it : outputs)
					items.add(it, 1);
				
				working = false;
			}
		}
	}
	
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
	
	@Override
	public void onEntityUpdate(Cause cause, Object source)
	{
		if ((cause == Cause.ITEM_CONSUMED) || cause == Cause.ENTITY_ADDED) waitWithRequestUntilEntityUpdate = false;
	}
	
	@Override
	public JSONObject getData() throws Exception
	{
		JSONObject o = new JSONObject();
		
		o.put("c", getClass().getName().replace("de.dakror.factory.game.entity.", ""));
		o.put("x", x);
		o.put("y", y);
		o.put("i", items.getData());
		o.put("w", working);
		o.put("r", running);
		o.put("is", inputs);
		o.put("os", outputs);
		o.put("rIT", requestedItemType);
		o.put("sT", startTick);
		o.put("wWRUEU", waitWithRequestUntilEntityUpdate);
		
		return o;
	}
	
	@Override
	public void setData(JSONObject data)
	{}
}
