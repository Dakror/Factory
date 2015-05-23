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
 

package de.dakror.factory.game.entity.machine.storage;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.ui.ItemList;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Storage extends Machine {
	int capacity;
	
	public Storage(float x, float y) {
		super(x, y, 6, 3);
		points.add(new TubePoint(0, 2, true, true, false));
		points.add(new TubePoint(5, 2, false, true, false));
		
		name = "Lager";
		capacity = 50;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	@Override
	protected void drawIcon(Graphics2D g) {
		g.drawImage(Game.getImage("machine/storage.png"), x + (width - 128) / 2, y + (height - 128) / 2, Game.w);
		int h = Math.round(128 * (items.getLength() / capacity));
		Helper.drawImage(Game.getImage("machine/storage_fill.png"), x + (width - 128) / 2, y + (height - 128) / 2 + 128 - h, 128, h, 0, 128 - h, 128, h, g);
	}
	
	@Override
	protected void tick(int tick) {
		this.tick = tick;
		if (outputFilters.size() > 0) {
			if (startTick == 0) {
				startTick = tick;
				return;
			}
			
			if ((tick - startTick) % REQUEST_SPEED == 0 && items.getLength(outputFilters) > 0
					&& Game.world.isTube(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE + Block.SIZE)) {
				ArrayList<ItemType> f = items.getFilled(outputFilters);
				ItemType it = f.get((int) (Math.random() * f.size()));
				
				Item item = new Item(x + points.get(1).x * Block.SIZE, y + points.get(1).y * Block.SIZE, it);
				Game.world.addEntity(item);
				items.add(it, -1);
			}
		}
	}
	
	@Override
	public Entity clone() {
		return new Storage(x / Block.SIZE, y / Block.SIZE);
	}
	
	@Override
	public void onEntityUpdate(Cause cause, Object source) {}
	
	@Override
	public boolean wantsItem(ItemType t) {
		return items.getLength() < capacity;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if (state == 2 && e.getButton() == MouseEvent.BUTTON1 && Game.currentGame.worldActiveMachine == this) {
			ClickEvent ge = new ClickEvent() {
				@Override
				public void trigger() {
					outputFilters = ((ItemList) Game.currentGame.getActiveLayer()).getSelectedItemSlots();
					startTick = 0;
				}
			};
			
			if (!(Game.currentGame.getActiveLayer() instanceof ItemList)) {
				ItemList itemList = new ItemList(items);
				itemList.keepSelected = true;
				itemList.addCategories = true;
				itemList.generalEvent = ge;
				itemList.killOnUnfocus = true;
				Game.currentGame.addLayer(itemList);
				itemList.setSelectedItemSlots(outputFilters);
			} else {
				((ItemList) Game.currentGame.getActiveLayer()).items = items;
				((ItemList) Game.currentGame.getActiveLayer()).addCategories = true;
				((ItemList) Game.currentGame.getActiveLayer()).killOnUnfocus = true;
				((ItemList) Game.currentGame.getActiveLayer()).keepSelected = true;
				((ItemList) Game.currentGame.getActiveLayer()).generalEvent = ge;
				((ItemList) Game.currentGame.getActiveLayer()).init();
				((ItemList) Game.currentGame.getActiveLayer()).setSelectedItemSlots(outputFilters);
			}
		}
	}
}
