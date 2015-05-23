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

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;

/**
 * @author Dakror
 */
public class SuperStorage extends Storage {
	public SuperStorage(float x, float y) {
		super(x, y);
		width = 12 * Block.SIZE;
		height = 6 * Block.SIZE;
		points.get(1).x = 11;
		points.get(0).y = 5;
		points.get(1).y = 5;
		
		name = "Riesenlager";
		
		capacity = 5000;
	}
	
	@Override
	public Entity clone() {
		return new SuperStorage(x / Block.SIZE, y / Block.SIZE);
	}
}
