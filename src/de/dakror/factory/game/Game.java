package de.dakror.factory.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.Miner;
import de.dakror.factory.game.entity.machine.Pulverizer;
import de.dakror.factory.game.entity.machine.Smeltery;
import de.dakror.factory.game.entity.machine.Storage;
import de.dakror.factory.game.entity.machine.Tube;
import de.dakror.factory.game.entity.machine.Washer;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World;
import de.dakror.factory.layer.HUDLayer;
import de.dakror.gamesetup.GameFrame;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Game extends GameFrame
{
	public static final Machine[] buildableMachines = { new Tube(0, 0), new Miner(0, 0), new Pulverizer(0, 0), new Washer(0, 0), new Smeltery(0, 0), new Storage(0, 0) };
	public static Game currentGame;
	public static World world;
	
	Point mouseDown, mouseDownWorld, mouseDrag;
	
	public Machine activeMachine, worldActiveMachine;
	public boolean canPlace;
	
	public Game()
	{
		currentGame = this;
	}
	
	@Override
	public void initGame()
	{
		try
		{
			w.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/alagard.ttf")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (world == null)
		{
			world = new World(50, 50);
			world.generate();
			world.render();
			
			addLayer(world);
			addLayer(new HUDLayer());
		}
		
		drawLayers(g);
		
		Helper.drawString(getFPS() + " FPS", 0, 26, g, 18);
		Helper.drawString(getUPS() + " UPS", 0, 52, g, 18);
		
		try
		{
			if (activeMachine != null)
			{
				Point p = mouseDrag == null ? mouse : mouseDrag;
				activeMachine.setX(Helper.round(p.x - activeMachine.getWidth() / 2 - world.x % Block.SIZE, Block.SIZE) + world.x % Block.SIZE);
				activeMachine.setY(Helper.round(p.y - activeMachine.getHeight() / 2 - world.y % Block.SIZE, Block.SIZE) + world.y % Block.SIZE);
				
				Composite composite = g.getComposite();
				
				activeMachine.drawBelow(g);
				activeMachine.draw(g);
				activeMachine.drawAbove(g);
				
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				
				Color c = g.getColor();
				
				boolean cp = true;
				
				for (int i = 0; i < activeMachine.getWidth() / Block.SIZE; i++)
				{
					for (int j = 0; j < activeMachine.getHeight() / Block.SIZE; j++)
					{
						boolean free = true;
						for (Entity e : world.getEntities())
						{
							Rectangle r = e.getArea();
							r.translate(world.x, world.y);
							if (r.intersects(new Rectangle(activeMachine.getX() + i * Block.SIZE, activeMachine.getY() + j * Block.SIZE, Block.SIZE, Block.SIZE)))
							{
								free = false;
								break;
							}
						}
						g.setColor(free ? Color.white : Color.red);
						g.fillRect(activeMachine.getX() + i * Block.SIZE, activeMachine.getY() + j * Block.SIZE, Block.SIZE, Block.SIZE);
						
						if (!free) cp = false;
					}
				}
				
				canPlace = cp;
				
				g.setColor(c);
				g.setComposite(composite);
			}
		}
		catch (NullPointerException e)
		{}
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		
		if ((world.width > getWidth() || world.height > getHeight()) && mouseDown != null && e.getModifiers() == MouseEvent.BUTTON2_MASK)
		{
			int x = mouseDown.x - e.getX() - mouseDownWorld.x;
			int y = mouseDown.y - e.getY() - mouseDownWorld.y;
			
			if (x < 0) x = 0;
			if (x > world.width - getWidth()) x = world.width - getWidth();
			if (y < 0) y = 0;
			if (y > world.height - getHeight()) y = world.height - getHeight();
			
			world.x = -x;
			world.y = -y;
		}
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
		{
			mouseDrag = e.getPoint();
			if (canPlace && activeMachine != null && e.getY() < Game.getHeight() - 100) build();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		
		mouseDown = null;
		mouseDownWorld = null;
		if (mouseDrag != null) mouse = (Point) mouseDrag.clone();
		mouseDrag = null;
		
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			activeMachine = null;
			canPlace = false;
		}
		else if (e.getButton() == MouseEvent.BUTTON1 && canPlace && activeMachine != null && e.getY() < Game.getHeight() - 100) build();
	}
	
	public void build()
	{
		Machine machine = (Machine) activeMachine.clone();
		machine.setX(activeMachine.getX() - world.x);
		machine.setY(activeMachine.getY() - world.y);
		
		world.addEntity(machine.clone());
		
		world.dispatchEntityUpdate();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		worldActiveMachine = null;
		super.mousePressed(e);
		
		mouseDown = e.getPoint();
		mouseDownWorld = new Point(world.x, world.y);
	}
}
