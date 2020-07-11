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


package de.dakror.factory;

import javax.swing.UIManager;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.UpdateThread;

/**
 * @author Dakror
 */
public class Factory {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// CFG.INTERNET = Helper.isInternetReachable();
		// UniVersion.offline = !CFG.INTERNET;
		//
		// UniVersion.init(SpamWars.class, CFG.VERSION, CFG.PHASE);
		// if (!UniVersion.offline) Reporter.init(new File(CFG.DIR, "log"));
		
		new Game();
		Game.currentFrame.init("Factory");
		try {
			Game.currentFrame.setFullscreen();
		} catch (IllegalStateException e) {
			System.exit(0);
		}
		
		Game.currentFrame.updater = new UpdateThread();
		
		while (true)
			Game.currentFrame.main();
	}
}
