package de.dakror.factory;

import javax.swing.UIManager;

import de.dakror.dakrorbin.Launch;
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
		Launch.init(args);
		
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
