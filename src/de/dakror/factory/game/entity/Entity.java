package de.dakror.factory.game.entity;

import java.awt.Graphics2D;

import de.dakror.gamesetup.util.Drawable;
import de.dakror.gamesetup.util.EventListener;
import de.dakror.gamesetup.util.Vector;

/**
 * @author Dakror
 */
public abstract class Entity extends EventListener implements Drawable
{
	protected float x, y;
	protected int width, height;
	protected float speed;
	
	protected boolean dead;
	
	protected Vector target;
	
	public Entity(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	
	public Vector getTarget()
	{
		return target;
	}
	
	public void setTarget(Vector target)
	{
		this.target = target;
	}
	
	public Vector getPos()
	{
		return new Vector(x, y);
	}
	
	public void move()
	{
		if (target == null || target.equals(getPos())) return;
		Vector distance = target.clone().sub(getPos());
		if (distance.getLength() > speed) distance.setLength(speed);
		Vector newPos = getPos().add(distance);
		x = newPos.x;
		y = newPos.y;
		
		if (distance.getLength() < speed) onReachTarget();
	}
	
	@Override
	public void update(int tick)
	{
		move();
		
		tick(tick);
	}
	
	protected abstract void tick(int tick);
	
	public void drawBelow(Graphics2D g)
	{
		g.fillRect((int) x, (int) y, width, height);
	}
	
	protected abstract void onReachTarget();
}
