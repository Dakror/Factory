package de.dakror.factory.game.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Vector;

/**
 * @author Dakror
 */
public abstract class Entity extends ClickableComponent
{
	Vector pos;
	
	public Entity(float x, float y, int width, int height)
	{
		super((int) x, (int) y, width, height);
		pos = new Vector(x, y);
		
	}
	
	protected float speed;
	
	protected boolean dead;
	
	protected Vector target;
	
	public Vector getPos()
	{
		return pos;
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
	
	@Override
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
	
	public void move()
	{
		if (target == null || target.equals(getPos())) return;
		Vector distance = target.clone().sub(getPos());
		if (distance.getLength() > speed) distance.setLength(speed);
		Vector newPos = getPos().add(distance);
		pos.x = newPos.x;
		pos.y = newPos.y;
		
		if (distance.getLength() < speed) onReachTarget();
	}
	
	@Override
	public void update(int tick)
	{
		move();
		
		tick(tick);
	}
	
	public boolean contains(Point p)
	{
		Rectangle rectangle = getArea();
		rectangle.translate(Game.world.x, Game.world.y);
		
		return rectangle.contains(p);
	}
	
	protected abstract void tick(int tick);
	
	public void drawBelow(Graphics2D g)
	{
		g.fillRect(x, y, width, height);
	}
	
	@Override
	public abstract Entity clone();
	
	public Rectangle getArea()
	{
		return new Rectangle(x, y, width, height);
	}
	
	protected abstract void onReachTarget();
	
	public abstract void onEntityUpdate();
}
