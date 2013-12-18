package de.dakror.factory.layer;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class MenuLayer extends Layer
{
	@Override
	public void draw(Graphics2D g)
	{
		g.drawImage(Game.getImage("bg.png"), 0, 0, Game.getWidth(), Game.getHeight(), Game.w);
		Helper.drawImageCenteredRelativeScaled(Game.getImage("factory.png"), 80, 1920, 1080, Game.getWidth(), Game.getHeight(), g);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void init()
	{}
}
