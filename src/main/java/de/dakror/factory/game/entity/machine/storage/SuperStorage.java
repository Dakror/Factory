package de.dakror.factory.game.entity.machine.storage;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;

/**
 * @author Dakror
 */
public class SuperStorage extends Storage {
	public SuperStorage(float x, float y) {
		super(x, y);
		width = 12 * Block.SIZE;
		height = 6 * Block.SIZE;
		points.get(1).x = 11;
		points.get(0).y = 5;
		points.get(1).y = 5;
		
		name = "Riesenlager";
		
		capacity = 5000;
	}
	
	@Override
	public Entity clone() {
		return new SuperStorage(x / Block.SIZE, y / Block.SIZE);
	}
}
