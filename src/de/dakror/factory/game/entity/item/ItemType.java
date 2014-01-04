package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public enum ItemType
{
	stone(0, 0, " Stein", Category.block),
	coal_ore(2, 0, "Kohle-Erz", Category.block),
	iron_ore(3, 0, "Eisenerz", Category.ore, Category.block),
	copper_ore(5, 1, "Kupfererz", Category.ore, Category.block),
	tin_ore(6, 1, "Zinnerz", Category.ore, Category.block),
	silver_ore(1, 1, "Silbererz", Category.ore, Category.block),
	gold_ore(4, 0, "Golderz", Category.ore, Category.block),
	
	coal(8, 21, "Kohle", Category.none),
	
	coal_dust(0, 7, "Kohlestaub", Category.dust),
	iron_dust(1, 7, "Eisenstaub", Category.dust),
	copper_dust(3, 7, "Kupferstaub", Category.dust),
	tin_dust(4, 7, "Zinnstaub", Category.dust),
	silver_dust(3, 4, "Silberstaub", Category.dust),
	gold_dust(2, 7, "Goldstaub", Category.dust),
	
	iron_ingot(15, 27, "Eisenbarren", Category.ingot),
	copper_ingot(8, 7, "Kupferbarren", Category.ingot),
	tin_ingot(9, 7, "Zinnbarren", Category.ingot),
	silver_ingot(1, 27, "Silberbarren", Category.ingot),
	gold_ingot(8, 28, "Goldbarren", Category.ingot),
	
	iron_plate(0, 30, "Eisenplatte", Category.plate),
	copper_plate(4, 30, "Kupferplatte", Category.plate),
	tin_plate(3, 30, "Zinnplatte", Category.plate),
	silver_plate(5, 30, "Silberplatte", Category.plate),
	gold_plate(1, 30, "Goldplatte", Category.plate),
	
	;
	
	public static enum Category
	{
		block("Block"),
		ore("Erz"),
		dust("Staub"),
		ingot("Barren"),
		plate("Platte"),
		none("Keine");
		
		public String name;
		
		private Category(String name)
		{
			this.name = name;
		}
	}
	
	public static EnumMap<ItemType, BufferedImage> images;
	static
	{
		images = new EnumMap<>(ItemType.class);
	}
	
	
	public int tx, ty;
	public List<Category> category;
	public String name;
	
	private ItemType(int tx, int ty, String name, Category... category)
	{
		this.tx = tx;
		this.ty = ty;
		this.category = Arrays.asList(category);
		this.name = name;
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		if (images.containsKey(this)) g.drawImage(images.get(this), x, y, Game.w);
		else
		{
			BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g1 = (Graphics2D) bi.getGraphics();
			Helper.drawImage(Game.getImage((category.contains(Category.block) ? "blocks" : "items") + ".png"), 0, 0, 32, 32, tx * 16, ty * 16, 16, 16, g1);
			g1.setColor(Color.black);
			if (category.contains(Category.block)) g1.drawRect(0, 0, 31, 31);
			
			images.put(this, bi);
		}
	}
	
	public static ItemType[] getItemsByCategory(Category category)
	{
		ArrayList<ItemType> t = new ArrayList<>();
		
		for (ItemType it : values())
			if (it.category.contains(category)) t.add(it);
		
		return t.toArray(new ItemType[] {});
	}
}
