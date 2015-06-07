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


package de.dakror.factory.game;

import de.dakror.gamesetup.Updater;

/**
 * @author Dakror
 */
public class UpdateThread extends Updater {
	public UpdateThread() {}
	
	@Override
	public void updateBefore() {
		if (Game.currentGame.paused && Game.currentGame.tickWhenPaused == 0) Game.currentGame.tickWhenPaused = tick;
		if (!Game.currentGame.paused && Game.currentGame.tickWhenPaused != 0) {
			tick -= (tick - Game.currentGame.tickWhenPaused);
			Game.currentGame.tickWhenPaused = 0;
		}
	}
	
	@Override
	public void update() {}
}
