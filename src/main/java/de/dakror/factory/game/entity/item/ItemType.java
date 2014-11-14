package de.dakror.factory.game.entity.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import de.dakror.factory.game.Game;
import de.dakror.factory.util.Filter;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public enum ItemType {
	stone(0, 0, "Stein", Category.block),
	coal_ore(2, 0, "Kohle-Erz", Category.block),
	iron_ore(3, 0, "Eisenerz", Category.ore, Category.block, Category.material_iron),
	copper_ore(5, 1, "Kupfererz", Category.ore, Category.block, Category.material_copper),
	tin_ore(6, 1, "Zinnerz", Category.ore, Category.block, Category.material_tin),
	silver_ore(1, 1, "Silbererz", Category.ore, Category.block, Category.material_silver),
	gold_ore(4, 0, "Golderz", Category.ore, Category.block, Category.material_gold),
	
	coal(8, 21, "Kohle", Category.none),
	scrap(2, 13, "Schrott", Category.none),
	
	coal_dust(0, 7, "Kohlestaub", Category.dust),
	iron_dust(1, 7, "Eisenstaub", Category.dust, Category.material_iron),
	copper_dust(3, 7, "Kupferstaub", Category.dust, Category.material_copper),
	tin_dust(4, 7, "Zinnstaub", Category.dust, Category.material_tin),
	silver_dust(3, 4, "Silberstaub", Category.dust, Category.material_silver),
	gold_dust(2, 7, "Goldstaub", Category.dust, Category.material_gold),
	
	iron_ingot(15, 27, "Eisenbarren", Category.ingot, Category.material_iron),
	copper_ingot(8, 7, "Kupferbarren", Category.ingot, Category.material_copper),
	tin_ingot(9, 7, "Zinnbarren", Category.ingot, Category.material_tin),
	silver_ingot(1, 27, "Silberbarren", Category.ingot, Category.material_silver),
	gold_ingot(8, 28, "Goldbarren", Category.ingot, Category.material_gold),
	
	iron_plate(0, 30, "Eisenplatte", Category.plate, Category.material_iron),
	copper_plate(4, 30, "Kupferplatte", Category.plate, Category.material_copper),
	tin_plate(3, 30, "Zinnplatte", Category.plate, Category.material_tin),
	silver_plate(5, 30, "Silberplatte", Category.plate, Category.material_silver),
	gold_plate(1, 30, "Goldplatte", Category.plate, Category.material_gold),
	
	nul(13, 14, "Nichts", Category.nul),
	
	;
	
	public static enum Category {
		block("Block"),
		ore("Erz"),
		dust("Staub"),
		ingot("Barren"),
		plate("Platte"),
		none("Misc."),
		nul("Nichts"),
		
		item("Alle"),
		
		// -- materials -- //
		material_iron("Eisen"),
		material_copper("Kuper"),
		material_tin("Zinn"),
		material_silver("Silber"),
		material_gold("Gold"),
		
		;
		
		public String name;
		
		private Category(String name) {
			this.name = name;
		}
	}
	
	public static EnumMap<ItemType, BufferedImage> images;
	static {
		images = new EnumMap<>(ItemType.class);
	}
	
	
	public int tx, ty;
	public List<Category> categories;
	public String name;
	
	private ItemType(int tx, int ty, String name, Category... category) {
		this.tx = tx;
		this.ty = ty;
		categories = new ArrayList<>();
		for (Category c : category)
			categories.add(c);
		if (!name.equals("Schrott")) categories.add(Category.item);
		
		this.name = name;
	}
	
	public void draw(int x, int y, Graphics2D g) {
		if (images.containsKey(this)) g.drawImage(images.get(this), x, y, Game.w);
		else {
			BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g1 = (Graphics2D) bi.getGraphics();
			Helper.drawImage(Game.getImage((categories.contains(Category.block) ? "blocks" : "items") + ".png"), 0, 0, 32, 32, tx * 16, ty * 16, 16, 16, g1);
			g1.setColor(Color.black);
			if (categories.contains(Category.block)) g1.drawRect(0, 0, 31, 31);
			
			images.put(this, bi);
		}
	}
	
	public boolean matchesFilter(Filter f) {
		if (f.c == null) return f.t == this;
		return categories.contains(f.c);
	}
	
	public boolean hasMaterial() {
		return getMaterial() != null;
	}
	
	public Category getMaterial() {
		for (Category c : categories)
			if (c.name().startsWith("material_")) return c;
		
		return null;
	}
	
	public static ItemType[] getItemsByCategories(Category... categories) {
		ArrayList<ItemType> t = new ArrayList<>();
		List<Category> list = Arrays.asList(categories);
		
		for (ItemType it : values())
			if (it.categories.containsAll(list)) t.add(it);
		
		return t.toArray(new ItemType[] {});
	}
}
