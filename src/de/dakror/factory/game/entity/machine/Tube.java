package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;

import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.util.TubePoint;

/**
 * @author Dakror
 */
public class Tube extends Machine
{
	public static class GoldTube extends Tube
	{
		public GoldTube(float x, float y)
		{
			super(x, y);
			speed = 10f;
			color = Color.decode("#cd6f00");
			
			name = "Gold-Rohr";
		}
		
		@Override
		public Entity clone()
		{
			return new GoldTube(x / Block.SIZE, y / Block.SIZE);
		}
	}
	
	public static class IronTube extends Tube
	{
		public IronTube(float x, float y)
		{
			super(x, y);
			color = Color.darkGray;
			bgColor = Color.decode("#cccccc");
			
			name = "Eisen-Rohr";
		}
		
		@Override
		public Entity clone()
		{
			return new IronTube(x / Block.SIZE, y / Block.SIZE);
		}
	}
	
	public static class SilverTube extends IronTube
	{
		public SilverTube(float x, float y)
		{
			super(x, y);
			speed = 10f;
			color = Color.decode("#5b7c82");
			
			name = "Silber-Rohr";
		}
		
		@Override
		public Entity clone()
		{
			return new SilverTube(x / Block.SIZE, y / Block.SIZE);
		}
	}
	
	public static float highestSpeed = 10f; // keep in sync!!
	
	boolean connectedToExit;
	boolean connectedToInput;
	
	float speed;
	Color color;
	
	/**
	 * left, up, right, down
	 */
	boolean[] connections = { false, false, false, false };
	
	public Tube(float x, float y)
	{
		super(x, y, 1, 1);
		
		name = "Stein-Rohr";
		drawFrame = false;
		connectedToExit = connectedToInput = false;
		
		speed = 2f;
		color = Color.black;
	}
	
	@Override
	protected void drawIcon(Graphics2D g)
	{
		Color c = g.getColor();
		g.setColor(color);
		
		if (!connections[0]) g.fillRect(x, y, 4, height);
		if (!connections[1]) g.fillRect(x, y, width, 4);
		if (!connections[2]) g.fillRect(x + width - 4, y, 4, height);
		if (!connections[3]) g.fillRect(x, y + height - 4, width, 4);
		
		if (connections[1])
		{
			if (connections[0]) g.fillRect(x, y, 4, 4);
			if (connections[2]) g.fillRect(x + width - 4, y, 4, 4);
		}
		if (connections[3])
		{
			if (connections[0]) g.fillRect(x, y + height - 4, 4, 4);
			if (connections[2]) g.fillRect(x + width - 4, y + height - 4, 4, 4);
		}
		
		if (connectedToInput)
		{
			g.setColor(Color.blue);
			g.drawRect(x, y, width - 1, height - 1);
		}
		
		if (connectedToExit)
		{
			g.setColor(Color.blue);
			g.drawRect(x + 1, y + 1, width - 1, height - 1);
		}
		
		g.setColor(c);
	}
	
	@Override
	public float getSpeed()
	{
		return speed;
	}
	
	@Override
	public Entity clone()
	{
		return new Tube(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source)
	{
		if (cause == Cause.ENTITY_ADDED || cause == Cause.ENTITY_REMOVED)
		{
			connectedToExit = connectedToInput = false;
			TubePoint tp = Game.world.getTubePoint(x, y);
			if (tp != null)
			{
				if (tp.in) connectedToInput = true;
				else connectedToExit = true;
			}
			
			connections[0] = Game.world.isTube(x - Block.SIZE, y);
			tp = Game.world.getTubePoint(x - Block.SIZE, y);
			if (tp != null && (tp.horizontal || (!tp.horizontal && tp.up))) connections[0] = false;
			
			connections[1] = Game.world.isTube(x, y - Block.SIZE);
			tp = Game.world.getTubePoint(x, y - Block.SIZE);
			if (tp != null && (!tp.horizontal || (tp.horizontal && tp.up))) connections[1] = false;
			
			connections[2] = Game.world.isTube(x + Block.SIZE, y);
			tp = Game.world.getTubePoint(x + Block.SIZE, y);
			if (tp != null && (tp.horizontal || (!tp.horizontal && !tp.up))) connections[2] = false;
			
			connections[3] = Game.world.isTube(x, y + Block.SIZE);
			tp = Game.world.getTubePoint(x, y + Block.SIZE);
			if (tp != null && (!tp.horizontal || (tp.horizontal && !tp.up))) connections[3] = false;
		}
	}
	
	public boolean isConnectedTo(Tube o)
	{
		onEntityUpdate(null, null);
		o.onEntityUpdate(null, null);
		
		if (o.x == x && Math.abs(o.y - y) == Block.SIZE)
		{
			if (o.y > y) return connections[3] && o.connections[1];
			else return connections[1] && o.connections[3];
		}
		else if (o.y == y && Math.abs(o.x - x) == Block.SIZE)
		{
			if (o.x > x) return connections[2] && o.connections[0];
			else return connections[0] && o.connections[2];
		}
		
		return false;
	}
	
	@Override
	public JSONObject getData() throws Exception
	{
		JSONObject o = new JSONObject();
		
		o.put("c", getClass().getName().replace("de.dakror.factory.game.entity.", ""));
		o.put("x", x);
		o.put("y", y);
		
		return o;
	}
	
	public boolean isConnectedToExit()
	{
		return connectedToExit;
	}
	
	public boolean isConnectedToInput()
	{
		return connectedToInput;
	}
}
