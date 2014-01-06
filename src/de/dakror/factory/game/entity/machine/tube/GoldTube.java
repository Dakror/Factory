package de.dakror.factory.game.entity.machine.tube;

import java.awt.Color;

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;

/**
 * @author Dakror
 */
public class GoldTube extends Tube
{
	public GoldTube(float x, float y)
	{
		super(x, y);
		speed = 10f;
		color = Color.decode("#cd6f00");
		bgColor = Color.decode("#cdb598");
		
		name = "Gold-Rohr";
	}
	
	@Override
	public Entity clone()
	{
		return new GoldTube(x / Block.SIZE, y / Block.SIZE);
	}
}
