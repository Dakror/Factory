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

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.util.Filter;
import de.dakror.factory.util.TubePoint;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class Smeltery extends Machine {
	public Smeltery(float x, float y) {
		super(x, y, 5, 3);
		
		name = "Schmelze";
		running = false;
		
		points.add(new TubePoint(2, 0, true, true, true));
		points.add(new TubePoint(2, 2, false, true, false));
		
		speed = 20;
		
		inputFilters.add(new Filter(Category.dust, null));
		inputFilters.add(new Filter(null, ItemType.coal));
		
		outputFilters.add(new Filter(Category.ingot, null));
	}
	
	@Override
	protected void drawIcon(Graphics2D g) {
		int size = 120;
		g.drawImage(Game.getImage("machine/smeltery.png"), x + (width - size) / 2, y + (height - size) / 2, size, size, Game.w);
		if (working) Helper.drawCooldownCircle(x + Block.SIZE - 16, y - 16, Block.SIZE * 3 + 32, 0.6f, Color.black, 1 - (((tick - startTick) % speed) / (float) speed), g);
	}
	
	@Override
	public Entity clone() {
		return new Smeltery(x / Block.SIZE, y / Block.SIZE);
	}
}
