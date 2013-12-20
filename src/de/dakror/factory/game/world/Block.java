package de.dakror.factory.game.world;

/**
 * @author Dakror
 */
public enum Block
{
	stone(0, 0),
	redstone_ore(1, 0),
	coal_ore(2, 0),
	iron_ore(3, 0),
	gold_ore(4, 0),
	diamond_ore(5, 0),
	emerald_ore(6, 0),
	lapis_ore(7, 0),
	silver_ore(1, 1),
	ruby_ore(2, 1),
	sapphire_ore(3, 1),
	copper_ore(5, 1),
	tin_ore(6, 1),
	uran_ore(7, 1),
	
	;
	
	public static final int SIZE = 48;
	
	int tx, ty;
	
	private Block(int tx, int ty)
	{
		this.tx = tx;
		this.ty = ty;
	}
}
