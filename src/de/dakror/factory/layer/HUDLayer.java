package de.dakror.factory.layer;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;

public class HUDLayer extends Layer
{
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawContainer(0, Game.getHeight() - 100, Game.getWidth(), 100, false, false, g);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void init()
	{}
}
