package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public enum ItemType
{
	coal_ore(2, 0, true, "Kohle-Erz"),
	iron_ore(3, 0, true, "Eisenerz"),
	stone(0, 0, true, "Stein"),
	
	coal_dust(0, 7, false, "Kohlestaub"),
	iron_dust(1, 7, false, "Eisenstaub"),
	
	;
	
	public static EnumMap<ItemType, BufferedImage> images;
	static
	{
		images = new EnumMap<>(ItemType.class);
	}
	
	
	public int tx, ty;
	public boolean block;
	public String name;
	
	private ItemType(int tx, int ty, boolean block, String name)
	{
		this.tx = tx;
		this.ty = ty;
		this.block = block;
		this.name = name;
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		if (images.containsKey(this)) g.drawImage(images.get(this), x, y, Game.w);
		else
		{
			BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g1 = (Graphics2D) bi.getGraphics();
			Helper.drawImage(Game.getImage((block ? "blocks" : "items") + ".png"), 0, 0, 32, 32, tx * 16, ty * 16, 16, 16, g1);
			g1.setColor(Color.black);
			if (block) g1.drawRect(0, 0, 31, 31);
			
			images.put(this, bi);
		}
	}
}
