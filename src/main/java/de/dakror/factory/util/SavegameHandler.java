package de.dakror.factory.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.Item;
import de.dakror.factory.game.world.World.Cause;
import de.dakror.factory.settings.CFG;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import de.dakror.gamesetup.util.Compressor;

/**
 * @author Dakror
 */
public class SavegameHandler {
	public static void saveGame() {
		new Thread() {
			@Override
			public void run() {
				File maps = new File(CFG.DIR, "maps");
				maps.mkdir();
				
				File file = new File(maps, Game.gameName + ".factory");
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(Game.world.getThumbnail(), "PNG", baos);
					String string = new BASE64Encoder().encode(baos.toByteArray());
					
					file.createNewFile();
					JSONObject o = Game.world.getData();
					o.put("thumb", string);
					
					Compressor.compressFile(file, o.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static void loadGame(final File file) {
		new Thread() {
			@Override
			public void run() {
				setPriority(Thread.MAX_PRIORITY);
				try {
					JSONObject o = new JSONObject(Compressor.decompressFile(file));
					Game.world.setStone();
					Game.world.generate(o.getLong("seed"));
					Game.world.render();
					Game.gameName = file.getName().replace(".factory", "");
					Game.world.clear();
					
					JSONArray entities = o.getJSONArray("entities");
					for (int i = 0; i < entities.length(); i++) {
						JSONObject e = entities.getJSONObject(i);
						Entity entity = (Entity) Class.forName("de.dakror.factory.game.entity." + e.getString("c")).getConstructor(float.class, float.class).newInstance(e.getInt("x"), e.getInt("y"));
						entity.setData(e);
						
						Game.world.addEntitySilently(entity);
					}
					
					for (Cause cause : Cause.values()) {
						Game.world.dispatchEntityUpdate(cause, null);
					}
					
					JSONArray items = o.getJSONArray("items");
					for (int i = 0; i < items.length(); i++) {
						JSONObject e = items.getJSONObject(i);
						Item item = new Item(e.getInt("x"), e.getInt("y"));
						item.setData(e);
						
						Game.world.addEntitySilently(item);
					}
					
					for (Cause cause : Cause.values()) {
						Game.world.dispatchEntityUpdate(cause, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static BufferedImage getBase64Thumbnail(String b64) {
		try {
			return ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(b64)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
