package de.dakror.factory.game.entity.item;

/**
 * @author Dakror
 */
public enum ItemType
{
	COAL_ORE(2, 0, true),
	IRON_ORE(3, 0, true),
	
	COAL_DUST(0, 7, false),
	IRON_DUST(1, 7, false),
	
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
