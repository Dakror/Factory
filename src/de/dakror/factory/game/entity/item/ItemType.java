package de.dakror.factory.game.entity.item;

/**
 * @author Dakror
 */
public enum ItemType
{
	coal_ore(2, 0, true),
	iron_ore(3, 0, true),
	stone(0, 0, true),
	
	coal_dust(0, 7, false),
	iron_dust(1, 7, false),
	
	;
	
	public int tx, ty;
	public boolean block;
	
	private ItemType(int tx, int ty, boolean block)
	{
		this.tx = tx;
		this.ty = ty;
		this.block = block;
	}
}
