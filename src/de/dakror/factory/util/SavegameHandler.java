package de.dakror.factory.util;

import java.io.File;

import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.settings.CFG;
import de.dakror.gamesetup.util.Compressor;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class SavegameHandler
{
	public static void saveGame()
	{
		File maps = new File(CFG.DIR, "maps");
		maps.mkdir();
		
		File file = new File(maps, Game.gameName + ".map");
		try
		{
			file.createNewFile();
			JSONObject o = Game.world.getData();
			
			Compressor.compressFile(new File(file.getPath() + ".bin"), o.toString());
			Helper.setFileContent(file, o.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
