/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.factory.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;

import org.json.JSONObject;

import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.Path;

/**
 * @author Dakror
 */
public abstract class Entity extends ClickableComponent {
	protected float speed;
	protected boolean drawBelow, dead;
	protected Vector pos, target;
	protected Vector pathTarget;
	protected Path path;
	public Cause deathCause;
	protected Color bgColor = Color.white;
	
	public Entity(float x, float y, int width, int height) {
		super((int) x, (int) y, width, height);
		pos = new Vector(x, y);
		drawBelow = true;
	}
	
	public Vector getPos() {
		return pos;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public Vector getTarget() {
		return target;
	}
	
	public void setTarget(Vector target) {
		this.target = target;
	}
	
	public void setPathTarget(Vector target) {
		pathTarget = target;
	}
	
	public synchronized void setPath(Path p) {
		path = p;
		if (path != null) target = path.getNode().clone().mul(Block.SIZE);
	}
	
	public void move() {
		if (target == null || target.equals(getPos())) return;
		Vector distance = target.clone().sub(getPos());
		if (distance.getLength() > speed) distance.setLength(speed);
		Vector newPos = getPos().add(distance);
		pos.x = newPos.x;
		pos.y = newPos.y;
		
		if (target.equals(getPos())) {
			if (path != null) path.setNodeReached();
			if (path != null && !path.isPathComplete()) {
				target = path.getNode().clone().mul(Block.SIZE);
				onReachPathNode();
			}
			
			if ((path != null && path.isPathComplete()) || path == null) onReachTarget();
		}
	}
	
	@Override
	public void update(int tick) {
		move();
		
		x = (int) pos.x;
		y = (int) pos.y;
		
		tick(tick);
	}
	
	public boolean contains(Point p) {
		Rectangle rectangle = getArea();
		rectangle.translate(Game.world.x, Game.world.y);
		
		return rectangle.contains(p);
	}
	
	public boolean contains2(Point p) {
		Rectangle rectangle = getArea();
		
		return rectangle.contains(p);
	}
	
	protected abstract void tick(int tick);
	
	public void drawBelow(Graphics2D g) {
		if (drawBelow) {
			Color c = g.getColor();
			g.setColor(bgColor);
			g.fillRect(x, y, width, height);
			g.setColor(c);
		}
	}
	
	@Override
	public abstract Entity clone();
	
	public Rectangle getArea() {
		return new Rectangle(x, y, width, height);
	}
	
	public boolean isVisible() {
		return y + Game.world.y >= -height && x + Game.world.x >= -width && y + Game.world.y <= Game.getHeight() && x + Game.world.x <= Game.getWidth();
	}
	
	protected abstract void onReachTarget();
	
	public abstract void onRemoval();
	
	public abstract void onReachPathNode();
	
	public abstract void onEntityUpdate(Cause cause, Object source);
	
	public abstract JSONObject getData() throws Exception;
	
	public abstract void setData(JSONObject data) throws Exception;
}
