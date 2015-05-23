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
 

package de.dakror.factory.layer;

import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.util.SavegameHandler;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.button.TextButton;
import de.dakror.gamesetup.util.Helper;

public class PauseLayer extends Layer {
	public PauseLayer() {
		modal = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		drawModality(g);
		
		Helper.drawContainer((Game.getWidth() - TextButton.WIDTH) / 2 - 20, Game.getHeight() / 3 - 20, TextButton.WIDTH + 40, TextButton.HEIGHT * 4 + 40, true, false, g);
		drawComponents(g);
	}
	
	@Override
	public void update(int tick) {
		updateComponents(tick);
	}
	
	@Override
	public void init() {
		TextButton back = new TextButton((Game.getWidth() - TextButton.WIDTH) / 2, Game.getHeight() / 3, "Weiter");
		back.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				Game.currentGame.removeLayer(PauseLayer.this);
			}
		});
		components.add(back);
		TextButton save = new TextButton((Game.getWidth() - TextButton.WIDTH) / 2, Game.getHeight() / 3 + TextButton.HEIGHT, "Speichern");
		save.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				SavegameHandler.saveGame();
			}
		});
		components.add(save);
		TextButton load = new TextButton((Game.getWidth() - TextButton.WIDTH) / 2, Game.getHeight() / 3 + TextButton.HEIGHT * 2, "Laden");
		load.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				Game.currentGame.addLayer(new LoadGameLayer());
			}
		});
		components.add(load);
		TextButton quit = new TextButton((Game.getWidth() - TextButton.WIDTH) / 2, Game.getHeight() / 3 + TextButton.HEIGHT * 3, "Beenden");
		quit.addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				Game.currentGame.paused = false;
				Game.world = null;
				Game.currentGame.setLayer(new MenuLayer());
			}
		});
		components.add(quit);
	}
}
