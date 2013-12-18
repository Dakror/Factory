package de.dakror.factory.game.world;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.util.Drawable;

/**
 * @author Dakror
 */
public class World implements Drawable
{
	int[][] blocks;
	public int width, height;
	BufferedImage render;
	
	public int x, y;
	
	public World(int width, int height)
	{
		this.width = width * Tile.SIZE;
		this.height = height * Tile.SIZE;
		blocks = new int[width][height];
		x = y = 0;
		
		init();
	}
	
	public void init()
	{
		for (int i = 0; i < blocks.length; i++)
			for (int j = 0; j < blocks[0].length; j++)
				blocks[i][j] = Tile.stone.ordinal();
		
		generate();
	}
	
	public void render()
	{
		render = new BufferedImage(blocks.length * Tile.SIZE, blocks[0].length * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) render.getGraphics();
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks[0].length; j++)
			{
				g.drawImage(Game.getImage("tile/" + Tile.values()[blocks[i][j]].name() + ".png"), i * Tile.SIZE, j * Tile.SIZE, 64, 64, null);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (render == null) return;
		
		g.drawImage(render, x, y, Game.w);
	}
	
	@Override
	public void update(int tick)
	{}
	
	public void generate()
	{
		for (int i = 0; i < 5; i++)
		{
			int radius = (int) Math.round(Math.random() * 2) + 2;
			fillCircle(new Point((int) (Math.random() * (blocks.length - radius * 2) + radius), (int) (Math.random() * (blocks[0].length - radius * 2) + radius)), radius, Tile.values()[(int) (Math.random() * Tile.values().length)], (float) Math.random());
		}
	}
	
	public void fillCircle(Point center, int radius, Tile tile, float chance)
	{
		for (int i = -radius; i < radius; i++)
		{
			for (int j = -radius; j < radius; j++)
			{
				try
				{
					if (new Point(i + center.x, j + center.y).distance(center) <= radius && Math.random() <= chance) blocks[i + center.x][j + center.y] = tile.ordinal();
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
