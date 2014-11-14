package de.dakror.factory.game.entity.item;

import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.entity.machine.tube.IronTube;
import de.dakror.factory.game.entity.machine.tube.Tube;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;

import org.json.JSONObject;

import de.dakror.gamesetup.util.Helper;
import de.dakror.gamesetup.util.Vector;

/**
 * @author Dakror
 */
public class Item extends Entity {
	ItemType type;
	Vector lastPos;
	
	public Item(float x, float y, ItemType type) {
		super(x, y, Block.SIZE, Block.SIZE);
		this.type = type;
		drawBelow = false;
		speed = 2f;
		lastPos = pos.clone();
	}
	
	public Item(float x, float y) {
		super(x, y, Block.SIZE, Block.SIZE);
		drawBelow = false;
		speed = 2f;
		lastPos = pos.clone();
	}
	
	@Override
	public void draw(Graphics2D g) {
		type.draw(x + (width - 32) / 2, y + (height - 32) / 2, g);
	}
	
	@Override
	protected void tick(int tick) {
		if (!Game.world.isTube(Helper.round(x, Block.SIZE), Helper.round(y, Block.SIZE))) // kill if stuck
		{
			pos = lastPos;
			x = (int) pos.x;
			y = (int) pos.y;
		}
	}
	
	@Override
	public void move() {
		speed = Game.world.getTubeSpeed(Helper.round(x, Block.SIZE), Helper.round(y, Block.SIZE));
		super.move();
	}
	
	@Override
	public Entity clone() {
		return new Item(x, y, type);
	}
	
	public ItemType getItemType() {
		return type;
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source) {
		if ((target == null || target.equals(pos)) && (cause == Cause.ENTITY_ADDED || cause == Cause.MACHINE_DONE)) {
			lastPos = pos.clone();
			onReachTarget();
		}
	}
	
	@Override
	public void onRemoval() {}
	
	@Override
	public JSONObject getData() throws Exception {
		JSONObject o = new JSONObject();
		
		o.put("x", (int) getPos().x);
		o.put("y", (int) getPos().y);
		o.put("t", type.ordinal());
		if (target != null) {
			o.put("tx", target.x);
			o.put("ty", target.y);
		}
		
		return o;
	}
	
	@Override
	protected void onReachTarget() {
		Tube[] neighbors = new Tube[4];
		
		Tube t = Game.world.getTube(pos.x, pos.y);
		
		if (t == null) return;
		
		if (t.isConnectedToInput()) {
			for (Entity e : Game.world.getEntities()) {
				if (e instanceof Machine && e.getArea().contains(pos.x, pos.y) && ((Machine) e).getTubePoints().size() > 0) {
					((Machine) e).getItems().add(type, 1);
					deathCause = Cause.ITEM_CONSUMED;
					dead = true;
					return;
				}
			}
		}
		
		int[][] neigh = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
		
		int[] neighborFilterResults = { 2, 2, 2, 2 };
		
		for (int i = 0; i < neigh.length; i++) {
			Tube l = Game.world.getTube(pos.x + Block.SIZE * neigh[i][0], pos.y + Block.SIZE * neigh[i][1]);
			if (l != null && l.isConnectedTo(t) && !l.isConnectedToExit()) {
				if (l.isConnectedToInput()) {
					boolean ok = true;
					
					Machine machine = null;
					for (Entity e : Game.world.getEntities()) {
						if (e instanceof Machine && e.getArea().contains(l.x, l.y) && ((Machine) e).getTubePoints().size() > 0) {
							machine = (Machine) e;
							break;
						}
					}
					
					for (Entity e : Game.world.getEntities()) {
						if (e instanceof Machine && e.getArea().contains(l.x, l.y) && !((Machine) e).wantsItem(type)) {
							ok = false;
							break;
						}
						
						if (e instanceof Item && e.getTarget() != null && e.getTarget().equals(l.getPos()) && machine.matchSameFilters(type, ((Item) e).type)) {
							ok = false;
							break;
						}
					}
					
					if (!ok) continue;
				}
				
				if (t instanceof IronTube) {
					neighborFilterResults[i] = ((IronTube) t).matchesFilters(type, i);
					if (neighborFilterResults[i] == 2) continue;
				}
				
				neighbors[i] = l;
			}
		}
		
		boolean hasOneWithFilter = false;
		for (int i = 0; i < 4; i++) {
			if (neighborFilterResults[i] == 0) {
				hasOneWithFilter = true;
				break;
			}
		}
		
		if (hasOneWithFilter) for (int i = 0; i < 4; i++)
			if (neighborFilterResults[i] == 1) neighbors[i] = null;
		
		int size = 0;
		for (int i = 0; i < 4; i++)
			if (neighbors[i] != null) size++;
		
		if (size == 0) return;
		
		if (size > 1) {
			for (int i = 0; i < 4; i++) {
				Tube tube = neighbors[i];
				if (tube != null && tube.x == lastPos.x && tube.y == lastPos.y) {
					neighbors[i] = null;
					break;
				}
			}
		}
		
		ArrayList<Tube> tubes = new ArrayList<>();
		for (int i = 0; i < 4; i++)
			if (neighbors[i] != null) tubes.add(neighbors[i]);
		
		lastPos = pos.clone();
		setTarget(tubes.get((int) Math.floor((Math.random() * tubes.size()))).getPos());
	}
	
	@Override
	public void onReachPathNode() {}
	
	@Override
	public void setData(JSONObject data) throws Exception {
		type = ItemType.values()[data.getInt("t")];
		if (data.has("tx")) target = new Vector(data.getInt("tx"), data.getInt("ty"));
	}
}
