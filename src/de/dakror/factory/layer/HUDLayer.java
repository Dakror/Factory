package de.dakror.factory.layer;

import java.awt.Graphics2D;

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
	}
	
	@Override
	public void update(int tick)
	{
		updateComponents(tick);
	}
	
	@Override
	public void init()
	{
		components.add(new BuildBar());
	}
}
