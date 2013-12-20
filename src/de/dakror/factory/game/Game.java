package de.dakror.factory.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.Miner;
import de.dakror.factory.game.entity.machine.Storage;
import de.dakror.factory.game.entity.machine.Tube;
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
	public static final Machine[] buildableMachines = { new Tube(0, 0, 0), new Miner(0, 0), new Storage(0, 0) };
	public static Game currentGame;
	public static World world;
	
	Point mouseDown, mouseDownWorld, mouseDrag;
	
	public Machine activeMachine;
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
		
		addLayer(new HUDLayer());
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (world == null)
		{
			world = new World(50, 50);
			world.render();
		}
		world.draw(g);
		
		drawLayers(g);
		
		Helper.drawString(getFPS() + " FPS", 0, 26, g, 18);
		
		if (activeMachine != null)
		{
			activeMachine.setX(Helper.round(mouse.x - activeMachine.getWidth() / 2, Block.SIZE) + world.x % Block.SIZE);
			activeMachine.setY(Helper.round(mouse.y - activeMachine.getHeight() / 2, Block.SIZE) + world.y % Block.SIZE);
			
			Composite composite = g.getComposite();
			
			activeMachine.drawBelow(g);
			activeMachine.draw(g);
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			
			Color c = g.getColor();
			
			canPlace = true;
			
			for (int i = 0; i < activeMachine.getWidth() / Block.SIZE; i++)
			{
				for (int j = 0; j < activeMachine.getHeight() / Block.SIZE; j++)
				{
					boolean free = true;
					for (Entity e : world.entities)
					{
						if (e.getArea().intersects(new Rectangle((int) activeMachine.getX() + i * Block.SIZE, (int) activeMachine.getY() + j * Block.SIZE, Block.SIZE, Block.SIZE)))
						{
							free = false;
							break;
						}
					}
					g.setColor(free ? Color.white : Color.red);
					g.fillRect((int) activeMachine.getX() + i * Block.SIZE, (int) activeMachine.getY() + j * Block.SIZE, Block.SIZE, Block.SIZE);
					
					if (!free) canPlace = false;
				}
			}
			
			g.setColor(c);
			g.setComposite(composite);
		}
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
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		
		mouseDown = null;
		mouseDownWorld = null;
		mouseDrag = null;
		
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			activeMachine = null;
			canPlace = false;
		}
		else if (e.getButton() == MouseEvent.BUTTON1 && canPlace && activeMachine != null)
		{
			Machine machine = (Machine) activeMachine.clone();
			machine.setX(machine.getX() - world.x);
			machine.setY(machine.getY() - world.y);
			world.entities.add(machine);
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		super.mouseWheelMoved(e);
		
		if (activeMachine != null && activeMachine instanceof Tube) ((Tube) activeMachine).setDirection((((Tube) activeMachine).getDirection() + 1) % 4);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		mouseDown = e.getPoint();
		mouseDownWorld = new Point(world.x, world.y);
	}
}
