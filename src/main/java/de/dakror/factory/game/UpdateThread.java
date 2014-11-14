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
