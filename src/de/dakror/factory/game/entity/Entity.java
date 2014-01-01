package de.dakror.factory.game.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.world.Block;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.Path;

/**
 * @author Dakror
 */
public abstract class Entity extends ClickableComponent
{
	protected float speed;
	protected boolean drawBelow, dead;
	protected Vector pos, target;
	protected Vector pathTarget;
	protected Path path;
	
	public Entity(float x, float y, int width, int height)
	{
		super((int) x, (int) y, width, height);
		pos = new Vector(x, y);
		drawBelow = true;
	}
	
	public Vector getPos()
	{
		return pos;
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
	
	public void setPathTarget(Vector target)
	{
		pathTarget = target;
	}
	
	public synchronized void setPath(Path p)
	{
		path = p;
		if (path != null) target = path.getNode().clone().mul(Block.SIZE);
	}
	
	public void move()
	{
		if (target == null || target.equals(getPos())) return;
		Vector distance = target.clone().sub(getPos());
		if (distance.getLength() > speed) distance.setLength(speed);
		Vector newPos = getPos().add(distance);
		pos.x = newPos.x;
		pos.y = newPos.y;
		
		if (target.equals(getPos()))
		{
			if (path != null) path.setNodeReached();
			if (path != null && !path.isPathComplete()) target = path.getNode().clone().mul(Block.SIZE);
			
			if ((path != null && path.isPathComplete()) || path == null) onReachTarget();
		}
	}
	
	@Override
	public void update(int tick)
	{
		move();
		
		x = (int) pos.x;
		y = (int) pos.y;
		
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
		if (drawBelow) g.fillRect(x, y, width, height);
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
