package de.dakror.factory.game.entity;

import java.awt.Graphics2D;

import de.dakror.gamesetup.util.Drawable;
import de.dakror.gamesetup.util.EventListener;

/**
 * @author Dakror
 */
public class Entity extends EventListener implements Drawable
{
	public Entity()
	{}
	
	@Override
	public void draw(Graphics2D g)
	{}
	
	@Override
	public void update(int tick)
	{}
	
}
