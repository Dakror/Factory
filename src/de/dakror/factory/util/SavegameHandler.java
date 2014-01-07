package de.dakror.factory.util;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
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
		
		File file = new File(maps, Game.gameName + ".factory");
		try
		{
			file.createNewFile();
			JSONObject o = Game.world.getData();
			
			Compressor.compressFile(file, o.toString());
			Helper.setFileContent(new File(file.getPath() + ".raw"), o.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadGame(final File file)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				setPriority(Thread.MAX_PRIORITY);
				try
				{
					JSONObject o = new JSONObject(Compressor.decompressFile(file));
					Game.world.generate(o.getLong("seed"));
					Game.world.render();
					Game.gameName = file.getName().replace(".factory", "");
					Game.world.clear();
					
					JSONArray entities = o.getJSONArray("entities");
					for (int i = 0; i < entities.length(); i++)
					{
						JSONObject e = entities.getJSONObject(i);
						Entity entity = (Entity) Class.forName("de.dakror.factory.game.entity." + e.getString("c")).getConstructor(float.class, float.class).newInstance(e.getInt("x"), e.getInt("y"));
						entity.setData(e);
						
						Game.world.addEntity(entity);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
}
