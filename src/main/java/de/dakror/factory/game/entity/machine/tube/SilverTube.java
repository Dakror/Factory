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

import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;

/**
 * @author Dakror
 */
public class SilverTube extends IronTube {
	public SilverTube(float x, float y) {
		super(x, y);
		speed = 10f;
		color = Color.decode("#3c6d76");
		bgColor = Color.decode("#b1dae1");
		name = "Silber-Rohr";
	}
	
	@Override
	public Entity clone() {
		return new SilverTube(x / Block.SIZE, y / Block.SIZE);
	}
}
