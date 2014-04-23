package de.dakror.factory.ui;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class CloseButton extends ClickableComponent
{
	public static int SIZE = 48;
	
	public CloseButton(int x, int y)
	{
		super(x, y, SIZE, SIZE);
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (state == 0)
		{
			Helper.drawShadow(x, y, width, height, g);
			Helper.drawOutline(x, y, width, height, true, g);
		}
		else Helper.drawContainer(x, y, width, height, true, state == 1, false, g);
		
		int size = 25;
		g.drawImage(Game.getImage("menu/close.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
	}
	
	@Override
	public void update(int tick)
	{}
}
