package de.dakror.factory.game.entity.item;

import java.awt.Graphics2D;
import java.util.ArrayList;

import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.Storage;
import de.dakror.factory.game.entity.machine.Tube;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.gamesetup.util.Helper;
import de.dakror.gamesetup.util.Vector;

/**
 * @author Dakror
 */
public class Item extends Entity
{
	ItemType type;
	Vector lastPos;
	
	public Item(float x, float y, ItemType type)
	{
		super(x, y, Block.SIZE, Block.SIZE);
		this.type = type;
		drawBelow = false;
		speed = 2f;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		type.draw(x + (width - 32) / 2, y + (height - 32) / 2, g);
	}
	
	@Override
	protected void tick(int tick)
	{
		if (!Game.world.isTube(Helper.round(x, Block.SIZE), Helper.round(y, Block.SIZE))) // kill if stuck
		{
			for (Entity e : Game.world.getEntities())
			{
				if (e instanceof Storage)
				{
					((Storage) e).getItems().add(type, 1);
					break;
				}
			}
			dead = true;
		}
	}
	
	@Override
	public void move()
	{
		speed = Game.world.getTubeSpeed(Helper.round(x, Block.SIZE), Helper.round(y, Block.SIZE));
		super.move();
	}
	
	@Override
	public Entity clone()
	{
		return new Item(x, y, type);
	}
	
	public ItemType getItemType()
	{
		return type;
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source)
	{
		if (target == null && cause == Cause.ENTITY_ADDED)
		{
			lastPos = pos.clone();
			onReachTarget();
		}
	}
	
	@Override
	public void onRemoval()
	{}
	
	@Override
	public JSONObject getData() throws Exception
	{
		JSONObject o = new JSONObject();
		
		o.put("c", getClass().getName().replace("de.dakror.factory.game.entity.", ""));
		o.put("x", getPos().x);
		o.put("y", getPos().y);
		
		return o;
	}
	
	@Override
	public void setData(JSONObject data)
	{}
	
	@Override
	protected void onReachTarget()
	{
		ArrayList<Tube> neighbors = new ArrayList<>();
		
		Tube t = Game.world.getTube(pos.x, pos.y);
		
		if (t.isConnectedToInput())
		{
			for (Entity e : Game.world.getEntities())
			{
				if (e instanceof Machine && e.getArea().contains(pos.x, pos.y) && ((Machine) e).getTubePoints().size() > 0)
				{
					((Machine) e).getItems().add(type, 1);
					deathCause = Cause.ITEM_CONSUMED;
					dead = true;
					return;
				}
			}
		}
		
		int[][] neigh = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
		
		for (int i = 0; i < neigh.length; i++)
		{
			Tube l = Game.world.getTube(pos.x + Block.SIZE * neigh[i][0], pos.y + Block.SIZE * neigh[i][1]);
			if (l != null && l.isConnectedTo(t) && !l.isConnectedToExit()) neighbors.add(l);
		}
		
		if (neighbors.size() == 0) return;
		
		if (neighbors.size() > 1)
		{
			for (Tube tube : neighbors)
			{
				if (tube.x == lastPos.x && tube.y == lastPos.y)
				{
					neighbors.remove(tube);
					break;
				}
			}
		}
		
		lastPos = pos.clone();
		setTarget(neighbors.get((int) Math.floor((Math.random() * neighbors.size()))).getPos());
	}
	
	@Override
	public void onReachPathNode()
	{}
}
