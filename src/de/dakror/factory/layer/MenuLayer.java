package de.dakror.factory.layer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class MenuLayer extends Layer
{
	int theta;
	int thetaTo;
	int dir = 0;
	
	int speed = 5;
	
	Dimension newGame = new Dimension(1378, 225), loadGame = new Dimension(1281, 222), endGame = new Dimension(1643, 220);
	
	@Override
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < Math.ceil(Game.getWidth() / 512f); i++)
			for (int j = 0; j < Math.ceil(Game.getHeight() / 512f); j++)
				g.drawImage(Game.getImage("menu/bg.png"), i * 512, j * 512, Game.w);
		Helper.drawImageCenteredRelativeScaled(Game.getImage("menu/factory.png"), 40, 1, 1500, Game.getWidth(), Game.getHeight(), g);
		g.drawImage(Game.getImage("menu/newGame.png"), 200, Game.getHeight() / 4 + 50, newGame.width, newGame.height, Game.w);
		g.drawImage(Game.getImage("menu/loadGame.png"), Game.getWidth() - 200 - loadGame.width, Game.getHeight() / 4 + 50, loadGame.width, loadGame.height, Game.w);
		g.drawImage(Game.getImage("menu/endGame.png"), (Game.getWidth() - endGame.width) / 2, Game.getHeight() / 4 * 3, endGame.width, endGame.height, Game.w);
		
		AffineTransform old = g.getTransform();
		AffineTransform at = g.getTransform();
		at.rotate(Math.toRadians(theta), Game.getWidth() / 2, Game.getHeight() / 2 + 50);
		g.setTransform(at);
		int size = Game.getWidth() - 100 - newGame.width - loadGame.width;
		g.drawImage(Game.getImage("menu/wheel.png"), (Game.getWidth() - size) / 2, (Game.getHeight() - size) / 2 + 50, size, size, Game.w);
		g.setTransform(old);
	}
	
	@Override
	public void update(int tick)
	{
		theta = theta % 360;
		if (theta <= 0) theta = 360 - theta;
		
		if (thetaTo != theta && dir != 0)
		{
			theta = Helper.round(theta, speed);
			theta -= speed * (dir > 0 ? 1 : -1);
			
			if (theta == 220) Game.currentGame.newGame();
			if (theta == 320) Game.currentGame.addLayer(new LoadGameLayer());
			if (theta == 90) System.exit(0);
		}
		else dir = 0;
	}
	
	@Override
	public void init()
	{
		theta = thetaTo = 220;
		
		newGame = Helper.getRelativeScaled(newGame, new Dimension(1920, 1080), newGame);
		loadGame = Helper.getRelativeScaled(loadGame, new Dimension(1920, 1080), loadGame);
		endGame = Helper.getRelativeScaled(endGame, new Dimension(1920, 1080), endGame);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		if (dir != 0) return;
		
		if (new Rectangle(200, Game.getHeight() / 4 + 50, newGame.width, newGame.height).contains(e.getPoint())) // newGame
		{
			if (theta == 220) Game.currentGame.newGame();
			thetaTo = 220;
			if (theta == 90) dir = -1;
			else dir = 1;
		}
		if (new Rectangle(Game.getWidth() - 200 - loadGame.width, Game.getHeight() / 4 + 50, loadGame.width, loadGame.height).contains(e.getPoint())) // loadGame
		{
			if (theta == 320) Game.currentGame.addLayer(new LoadGameLayer());
			thetaTo = 320;
			if (theta == 220) dir = -1;
			else dir = 1;
		}
		if (new Rectangle((Game.getWidth() - endGame.width) / 2, Game.getHeight() / 4 * 3, endGame.width, endGame.height).contains(e.getPoint())) // endGame
		{
			if (theta == 90) System.exit(0);
			thetaTo = 90;
			if (theta == 220) dir = 1;
			else dir = -1;
		}
	}
}
