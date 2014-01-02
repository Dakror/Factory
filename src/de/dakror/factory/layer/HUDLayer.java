package de.dakror.factory.layer;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.ui.BuildBar;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class HUDLayer extends Layer
{
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawContainer(0, Game.getHeight() - 100, Game.getWidth(), 100, false, false, g);
		
		drawComponents(g);
		
		if (Game.currentGame.worldActiveMachine != null) Game.currentGame.worldActiveMachine.drawGUI(g);
	}
	
	@Override
	public void update(int tick)
	{
		updateComponents(tick);
		
		if (Game.currentGame.worldActiveMachine != null) Game.currentGame.worldActiveMachine.container.update(tick);
	}
	
	@Override
	public void init()
	{
		components.add(new BuildBar());
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		super.mouseMoved(e);
		if (Game.currentGame.worldActiveMachine != null) Game.currentGame.worldActiveMachine.container.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (Game.currentGame.worldActiveMachine != null) Game.currentGame.worldActiveMachine.container.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		if (Game.currentGame.worldActiveMachine != null) Game.currentGame.worldActiveMachine.container.mouseReleased(e);
	}
}
