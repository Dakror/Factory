package de.dakror.factory.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.Miner;
import de.dakror.factory.game.entity.machine.Platery;
import de.dakror.factory.game.entity.machine.Pulverizer;
import de.dakror.factory.game.entity.machine.Smeltery;
import de.dakror.factory.game.entity.machine.Washer;
import de.dakror.factory.game.entity.machine.storage.Storage;
import de.dakror.factory.game.entity.machine.storage.SuperStorage;
import de.dakror.factory.game.entity.machine.tube.GoldTube;
import de.dakror.factory.game.entity.machine.tube.IronTube;
import de.dakror.factory.game.entity.machine.tube.SilverTube;
import de.dakror.factory.game.entity.machine.tube.Tube;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World;
import de.dakror.factory.layer.HUDLayer;
import de.dakror.factory.util.SavegameHandler;
import de.dakror.gamesetup.GameFrame;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Game extends GameFrame
{
	public static final Machine[] buildableMachines = { new Tube(0, 0), new IronTube(0, 0), new GoldTube(0, 0), new SilverTube(0, 0), new Miner(0, 0), new Pulverizer(0, 0), new Washer(0, 0), new Smeltery(0, 0), new Platery(0, 0), new Storage(0, 0), new SuperStorage(0, 0) };
	public static Game currentGame;
	public static World world;
	public static String gameName;
	
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
			IronTube.init();
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
			// gameName = JOptionPane.showInputDialog(null, "enter a worldname", System.currentTimeMillis() + "");
			if (gameName == null) gameName = System.currentTimeMillis() + "";
			world = new World(50, 50);
			world.generate();
			world.render();
			
			addLayer(world);
			addLayer(new HUDLayer());
		}
		
		drawLayers(g);
		
		Helper.drawString("FPS: " + getFPS(), 0, 26, g, 18);
		Helper.drawString("UPS: " + getUPS(), 0, 52, g, 18);
		Helper.drawString("E: " + Game.world.getEntities().size(), 0, 78, g, 18);
		
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
							if (e instanceof Item) continue;
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
				
				if (!new Rectangle(0, 0, Game.getWidth(), Game.getHeight()).contains(activeMachine.getArea())) cp = false;
				
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
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		
		if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) SavegameHandler.saveGame();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		mouseDown = e.getPoint();
		mouseDownWorld = new Point(world.x, world.y);
	}
}
