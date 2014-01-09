package de.dakror.factory.game.world;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.storage.Storage;
import de.dakror.factory.game.entity.machine.tube.Tube;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.GameFrame;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.ui.Component;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class World extends Layer
{
	public static enum Cause
	{
		ENTITY_ADDED,
		ENTITY_REMOVED,
		
		ITEM_CONSUMED,
		MACHINE_DONE,
	}
	
	int[][] blocks;
	public int width, height;
	BufferedImage render;
	
	long seed;
	
	CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
	
	public int x, y;
	
	public World(int width, int height)
	{
		this.width = width * Block.SIZE;
		this.height = height * Block.SIZE;
		blocks = new int[width][height];
		x = y = 0;
		
		setStone();
	}
	
	public void setStone()
	{
		for (int i = 0; i < blocks.length; i++)
			for (int j = 0; j < blocks[0].length; j++)
				blocks[i][j] = Block.stone.ordinal();
	}
	
	public void render()
	{
		render = new BufferedImage(blocks.length * Block.SIZE, blocks[0].length * Block.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) render.getGraphics();
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks[0].length; j++)
			{
				Helper.drawImage(Game.getImage("blocks.png"), i * Block.SIZE, j * Block.SIZE, Block.SIZE, Block.SIZE, Block.values()[blocks[i][j]].tx * 16, Block.values()[blocks[i][j]].ty * 16, 16, 16, g);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (render == null) return;
		
		g.drawImage(render, x, y, Game.w);
		
		AffineTransform old = g.getTransform();
		AffineTransform at = g.getTransform();
		at.translate(x, y);
		g.setTransform(at);
		
		for (Entity e : entities)
			if (e.isVisible()) e.drawBelow(g);
		
		drawComponents(g);
		
		for (Entity e : entities)
			if (e instanceof Machine && e.isVisible()) ((Machine) e).drawAbove(g);
		
		g.setTransform(old);
	}
	
	@Override
	protected void drawComponents(Graphics2D g)
	{
		Component hovered = null;
		for (Component c : components)
		{
			if (c instanceof Entity && !((Entity) c).isVisible()) continue;
			c.draw(g);
			if (c.state == 2) hovered = c;
		}
		
		if (hovered != null) hovered.drawTooltip(GameFrame.currentFrame.mouse.x, GameFrame.currentFrame.mouse.y, g);
	}
	
	@Override
	public void update(int tick)
	{
		translateX = -x;
		translateY = -y;
		
		for (Entity e : entities)
		{
			if (e.isDead())
			{
				e.onRemoval();
				entities.remove(e);
				components.remove(e);
				dispatchEntityUpdate(e.deathCause == null ? Cause.ENTITY_REMOVED : e.deathCause, e);
			}
			else if (!Game.currentGame.paused) e.update(tick);
		}
	}
	
	public void generate()
	{
		long seed = (long) (System.nanoTime() + Math.random() * System.currentTimeMillis());
		generate(seed);
		this.seed = seed;
	}
	
	public void generate(long seed)
	{
		Random random = new Random(seed);
		Block[] ores = { Block.coal_ore, Block.iron_ore, Block.copper_ore, Block.tin_ore, Block.silver_ore, Block.gold_ore };
		for (int i = 0; i < random.nextDouble() * ores.length * 2 + ores.length; i++)
		{
			int radius = (int) Math.round(random.nextDouble() * 2) + 2;
			int index = i % ores.length;
			Point point = new Point((int) (random.nextDouble() * (blocks.length - radius * 2) + radius), (int) (random.nextDouble() * (blocks[0].length - radius * 2) + radius));
			fillCircle(point, radius, ores[index], 0.4f, random);
		}
		
		addEntity(new Storage((blocks.length - 6) / 2, 2));
		// blocks[3][1] = Block.iron_ore.ordinal();
		// blocks[3][2] = Block.iron_ore.ordinal();
		// blocks[4][1] = Block.iron_ore.ordinal();
		// blocks[4][2] = Block.iron_ore.ordinal();
		//
		// blocks[8][1] = Block.gold_ore.ordinal();
		// blocks[8][2] = Block.gold_ore.ordinal();
		// blocks[9][1] = Block.gold_ore.ordinal();
		// blocks[9][2] = Block.gold_ore.ordinal();
		//
		// addEntity(new Miner(3, 1));
		// addEntity(new Miner(8, 1));
		// addEntity(new Pulverizer(5, 1));
		//
		// addEntity(new Tube(3, 0));
		// addEntity(new Tube(4, 0));
		// addEntity(new Tube(5, 0));
		//
		// addEntity(new Tube(6, 3));
		// addEntity(new Tube(6, 4));
		// addEntity(new Tube(6, 5));
		// addEntity(new Tube(6, 6));
		// addEntity(new Tube(7, 6));
		//
		// addEntity(new Storage(7, 3));
		//
		// IronTube t = new IronTube(6, 0);
		// t.filters[0] = new Filter(Category.nul, null);
		// t.filters[18] = new Filter(Category.nul, null);
		// addEntity(t);
		// addEntity(new Tube(7, 0));
		// addEntity(new Tube(8, 0));
	}
	
	public void fillCircle(Point center, int radius, Block tile, float chance, Random random)
	{
		for (int i = -radius; i < radius; i++)
		{
			for (int j = -radius; j < radius; j++)
			{
				try
				{
					if (new Point(i + center.x, j + center.y).distance(center) <= radius && random.nextDouble() <= chance) blocks[i + center.x][j + center.y] = tile.ordinal();
				}
				catch (ArrayIndexOutOfBoundsException e)
				{}
			}
		}
	}
	
	public TubePoint getTubePoint(int x, int y)
	{
		for (Entity e : entities)
		{
			if (e instanceof Machine)
			{
				for (TubePoint tp : ((Machine) e).getTubePoints())
				{
					if (e.getX() + tp.x * Block.SIZE == x && e.getY() + tp.y * Block.SIZE == y) return tp;
				}
			}
		}
		
		return null;
	}
	
	public int getBlock(int x, int y)
	{
		if (x < 0 || y < 0 || x >= blocks.length || y >= blocks[0].length) return -1;
		
		return blocks[x][y];
	}
	
	public boolean isTube(float x, float y)
	{
		for (Entity e : entities)
			if (e instanceof Tube && e.getX() == x && e.getY() == y) return true;
		
		return false;
	}
	
	public float getTubeSpeed(float x, float y)
	{
		for (Entity e : entities)
			if (e instanceof Tube && e.getX() == x && e.getY() == y) return e.getSpeed();
		
		return 0;
	}
	
	public Tube getTube(float x, float y)
	{
		for (Entity e : entities)
			if (e instanceof Tube && e.getX() == x && e.getY() == y) return (Tube) e;
		
		return null;
	}
	
	public CopyOnWriteArrayList<Entity> getEntities()
	{
		return entities;
	}
	
	public void dispatchEntityUpdate(Cause cause, Object source)
	{
		for (Entity e : entities)
			e.onEntityUpdate(cause, source);
	}
	
	public void addEntity(Entity e)
	{
		if (e instanceof Machine) ((Machine) e).placeTubePoints();
		
		components.add(e);
		entities.add(e);
		
		dispatchEntityUpdate(Cause.ENTITY_ADDED, e.clone());
	}
	
	public void addEntitySilently(Entity e)
	{
		if (e instanceof Machine) ((Machine) e).placeTubePoints();
		
		components.add(e);
		entities.add(e);
	}
	
	public long getSeed()
	{
		return seed;
	}
	
	@Override
	public void init()
	{}
	
	public JSONObject getData() throws Exception
	{
		JSONObject data = new JSONObject();
		
		data.put("seed", seed);
		
		JSONArray e = new JSONArray();
		JSONArray i = new JSONArray();
		
		ArrayList<Entity> entities = new ArrayList<>(this.entities);
		Collections.sort(entities, new Comparator<Entity>()
		{
			@Override
			public int compare(Entity o1, Entity o2)
			{
				return Float.compare(o1.getPos().getLength(), o2.getPos().getLength());
			}
		});
		for (Entity e1 : entities)
		{
			if (e1 instanceof Tube && (((Tube) e1).isConnectedToExit() || ((Tube) e1).isConnectedToInput())) continue;
			if (e1 instanceof Item) i.put(e1.getData());
			else e.put(e1.getData());
		}
		
		data.put("entities", e);
		data.put("items", i);
		
		return data;
	}
	
	public void clear()
	{
		components.clear();
		entities.clear();
	}
	
	public BufferedImage getThumbnail()
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		draw((Graphics2D) bi.getGraphics());
		Dimension s = Helper.scaleTo(new Dimension(width, height), new Dimension(200, 200));
		return Helper.toBufferedImage(bi.getScaledInstance(s.width, s.height, Image.SCALE_REPLICATE));
	}
}
