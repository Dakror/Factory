package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.Filter;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Platery extends Machine {
	public Platery(float x, float y) {
		super(x, y, 3, 9);
		
		name = "Walzwerk";
		running = false;
		
		points.add(new TubePoint(1, 0, true, true, true));
		points.add(new TubePoint(1, 8, false, true, false));
		
		speed = 40;
		
		inputFilters.add(new Filter(Category.ingot, null));
		
		outputFilters.add(new Filter(Category.plate, null));
	}
	
	@Override
	protected void drawIcon(Graphics2D g) {
		int size = 120;
		g.drawImage(Game.getImage("machine/plater.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x - 16, y + Block.SIZE * 3 - 16, Block.SIZE * 3 + 32, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
		
	}
	
	@Override
	public Entity clone() {
		return new Platery(x / Block.SIZE, y / Block.SIZE);
	}
}
