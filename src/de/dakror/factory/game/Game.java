package de.dakror.factory.game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.world.World;
import de.dakror.factory.layer.HUDLayer;
import de.dakror.gamesetup.GameFrame;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Game extends GameFrame
{
	public static Game currentGame;
	public static World world;
	
	Point mouseDown, mouseDownWorld, mouseDrag;
	
	public Game()
	{
		currentGame = this;
	}
	
	@Override
	public void initGame()
	{
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
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		mouseDown = e.getPoint();
		mouseDownWorld = new Point(world.x, world.y);
	}
}
