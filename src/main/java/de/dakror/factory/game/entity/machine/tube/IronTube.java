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


package de.dakror.factory.game.entity.machine.tube;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.ui.CloseButton;
import de.dakror.factory.ui.ItemList;
import de.dakror.factory.ui.ItemSlot;
import de.dakror.factory.util.Filter;

import org.json.JSONArray;
import org.json.JSONObject;

import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class IronTube extends Tube {
	static ItemList itemList;
	static Image[] arrows = new Image[4];
	
	protected boolean showItemList = true;
	
	public static void init() {
		for (int i = 0; i < 4; i++)
			arrows[i] = Game.getImage("arrow.png").getSubimage(i * 64, 0, 64, 64);
	}
	
	public IronTube(float x, float y) {
		super(x, y);
		color = Color.darkGray;
		bgColor = Color.decode("#999999");
		
		forceGuiToStay = true;
		
		name = "Eisen-Rohr";
		
		initFilters(36);
		
		if (Game.world != null) initGUI();
	}
	
	protected void initFilters(int length) {
		outputFilters.clear();
		
		for (int i = 0; i < length; i++)
			outputFilters.add(new Filter(null, null));
	}
	
	protected void initGUI() {
		container.components.clear();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < (576 / ItemSlot.SIZE); j++) {
				final ItemSlot is = new ItemSlot((Game.getWidth() - 616) / 2 + 16 + j * ItemSlot.SIZE, (Game.getHeight() - 300) / 3 + 20 + i * ItemSlot.SIZE,
																					outputFilters.get(i * 9 + j).t, outputFilters.get(i * 9 + j).t == null ? 0 : 1);
				is.category = outputFilters.get(i * 9 + j).c;
				is.bg = arrows[i];
				is.rightClickClear = true;
				is.pressEvent = new ClickEvent() {
					@Override
					public void trigger() {
						ItemSlot a = itemList.getSelectedItemSlot();
						if (a != null) {
							is.category = a.category;
							is.amount = a.amount;
							is.type = a.type;
						}
					}
				};
				container.components.add(is);
			}
		}
		
		CloseButton cb = new CloseButton((Game.getWidth() + 660) / 2 - CloseButton.SIZE, (Game.getHeight() - 300) / 3);
		cb.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				for (int i = 0; i < container.components.size() - 1; i++) {
					ItemSlot is = (ItemSlot) container.components.get(i);
					outputFilters.get(i).c = is.category;
					outputFilters.get(i).t = is.type;
				}
				
				if (Game.currentGame.getActiveLayer() instanceof ItemList) Game.currentGame.removeLayer(Game.currentGame.getActiveLayer());
				Game.currentGame.worldActiveMachine = null;
			}
		});
		container.components.add(cb);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		
		if (state == 2 && Game.currentGame.worldActiveMachine == this) initGUI();
		if (state == 2 && Game.currentGame.worldActiveMachine == this && showItemList) {
			if (!(Game.currentGame.getActiveLayer() instanceof ItemList)) {
				itemList = new ItemList();
				itemList.addCategories = true;
				Game.currentGame.addLayer(itemList);
			} else {
				((ItemList) Game.currentGame.getActiveLayer()).items = null;
				((ItemList) Game.currentGame.getActiveLayer()).killOnUnfocus = false;
				((ItemList) Game.currentGame.getActiveLayer()).addCategories = true;
				((ItemList) Game.currentGame.getActiveLayer()).keepSelected = false;
				((ItemList) Game.currentGame.getActiveLayer()).init();
			}
		}
	}
	
	@Override
	public void drawGUI(Graphics2D g) {
		Helper.drawContainer((Game.getWidth() - 660) / 2, (Game.getHeight() - 300) / 3, 660, 300, true, false, g);
		drawSuper(g);
	}
	
	public void drawSuper(Graphics2D g) {
		super.drawGUI(g);
	}
	
	/**
	 * 0 = true, 1 = empty, 2 = false
	 */
	public int matchesFilters(ItemType type, int direction) {
		int emtpy = 0;
		
		boolean match = false;
		for (int i = 0; i < outputFilters.size() / 4; i++) {
			Filter f = outputFilters.get(direction * outputFilters.size() / 4 + i);
			if (f.t == null) {
				emtpy++;
				continue;
			}
			
			if (type.matchesFilter(f)) match = true;
		}
		
		return emtpy == outputFilters.size() / 4 ? 1 : match ? 0 : 2;
	}
	
	@Override
	public JSONObject getData() throws Exception {
		JSONObject o = super.getData();
		
		JSONArray fs = new JSONArray();
		for (Filter f : outputFilters)
			fs.put(f.getData());
		o.put("f", fs);
		
		return o;
	}
	
	@Override
	public void setData(JSONObject data) throws Exception {
		outputFilters.clear();
		JSONArray os = data.getJSONArray("f");
		for (int i = 0; i < os.length(); i++)
			outputFilters.add(new Filter(os.getJSONArray(i)));
	}
	
	@Override
	public Entity clone() {
		return new IronTube(x / Block.SIZE, y / Block.SIZE);
	}
}
