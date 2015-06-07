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


package de.dakror.factory.game.entity.machine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Miner extends Machine {
	ItemType[] types;
	int speed;
	
	boolean spittout;
	
	public Miner(float x, float y) {
		super(x, y, 2, 2);
		
		name = "Mine";
		running = false;
		
		points.add(new TubePoint(0, 0, false, true, true));
		
		speed = 800;
	}
	
	@Override
	protected void drawIcon(Graphics2D g) {
		int size = 64;
		g.drawImage(Game.getImage("machine/miner.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (running) Helper.drawCooldownCircle(x, y, width, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone() {
		return new Miner(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	protected void tick(int tick) {
		this.tick = tick;
		if (startTick == 0) startTick = tick;
		
		if (running && (tick - startTick) % speed == 0 && tick != startTick) {
			items.add(types[(int) (Math.random() * types.length)], 1);
			return;
		}
		
		if ((tick - startTick) % REQUEST_SPEED == 0 && spittout && items.getLength() > 0) {
			ArrayList<ItemType> filled = items.getFilled();
			ItemType type = filled.get((int) (Math.random() * filled.size()));
			Item item = new Item(x + points.get(0).x * Block.SIZE, y + points.get(0).y * Block.SIZE, type);
			items.add(type, -1);
			Game.world.addEntity(item);
		}
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source) {
		running = true;
		
		if (cause == Cause.ENTITY_ADDED || cause == Cause.ENTITY_REMOVED) spittout = Game.world.isTube(x, y - Block.SIZE);
		
		if (cause == Cause.ENTITY_ADDED) {
			types = new ItemType[4];
			
			if (Game.world != null) for (int i = 0; i < 4; i++)
				types[i] = ItemType.valueOf(Block.values()[Game.world.getBlock(x / Block.SIZE + i % 2, y / Block.SIZE + i / 2)].name());
		}
	}
}
