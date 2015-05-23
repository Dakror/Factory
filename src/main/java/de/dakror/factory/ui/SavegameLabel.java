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
 

package de.dakror.factory.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.dakror.factory.game.Game;
import de.dakror.factory.util.SavegameHandler;

import org.json.JSONException;
import org.json.JSONObject;

import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Compressor;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class SavegameLabel extends ClickableComponent {
	public static final int HEIGHT = 100;
	
	File file;
	
	BufferedImage thm;
	JSONObject data;
	String lmd;
	
	public SavegameLabel(int y, File file) {
		super(Game.getWidth() / 4 + 20, Game.getHeight() / 4 + 20 + y * HEIGHT, Game.getWidth() / 2 - 70, HEIGHT);
		this.file = file;
		
		try {
			data = new JSONObject(Compressor.decompressFile(file));
			thm = SavegameHandler.getBase64Thumbnail(data.getString("thumb"));
			lmd = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(new Date(file.lastModified()));
			
			addClickEvent(new ClickEvent() {
				@Override
				public void trigger() {
					Game.currentGame.loadGame(SavegameLabel.this.file);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		Shape c = g.getClip();
		g.setClip(Game.getWidth() / 4 + 20, Game.getHeight() / 4 + 20, Game.getWidth() / 2 - 40, Game.getHeight() / 2 - 40);
		
		if (state == 0) {
			Helper.drawShadow(x, y, width, height, g);
			Helper.drawOutline(x, y, width, height, false, g);
		} else Helper.drawContainer(x, y, width, height, false, state == 1, g);
		
		g.drawImage(thm, x + 10, y + 10, HEIGHT - 20, HEIGHT - 20, Game.w);
		Helper.drawString("Factory vom " + file.getName().replace(".factory", "").replace("-", ":"), x + HEIGHT, y + 30, g, 30);
		Helper.drawRightAlignedString("zuletzt gespielt am " + lmd, x + width - 10, y + height - 10, g, 20);
		
		g.setClip(c);
	}
	
	@Override
	public void update(int tick) {}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g) {
		if (x < this.x + HEIGHT - 10 && x > this.x + 10) {
			g.drawImage(thm, x, y, Game.w);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (new Rectangle(Game.getWidth() / 4 + 20, Game.getHeight() / 4 + 20, Game.getWidth() / 2 - 40, Game.getHeight() / 2 - 40).contains(e.getPoint())) super.mouseMoved(e);
		else state = 0;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (new Rectangle(Game.getWidth() / 4 + 20, Game.getHeight() / 4 + 20, Game.getWidth() / 2 - 40, Game.getHeight() / 2 - 40).contains(e.getPoint())) super.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (new Rectangle(Game.getWidth() / 4 + 20, Game.getHeight() / 4 + 20, Game.getWidth() / 2 - 40, Game.getHeight() / 2 - 40).contains(e.getPoint())) super.mouseReleased(e);
	}
}
