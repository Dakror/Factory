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
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.gamesetup.ui.Component;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class BuildBar extends Component {
	public ArrayList<BuildButton> buttons;
	Point down, drag;
	int sX;
	
	int widthCache;
	
	public BuildBar() {
		super(0, Game.getHeight() - 100, Game.getWidth(), 100);
		buttons = new ArrayList<>();
		
		for (int i = 0; i < Game.buildableMachines.length; i++) {
			BuildButton bb = new BuildButton(15 + i * (BuildButton.SIZE + 32), Game.getHeight() - 84, Game.buildableMachines[i]);
			buttons.add(bb);
		}
		widthCache = 0;
		down = drag = null;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Helper.drawContainer(0, Game.getHeight() - 100, Game.getWidth(), 100, false, false, g);
		
		Shape o = g.getClip();
		g.setClip(10, Game.getHeight() - 100, Game.getWidth() - 20, 100);
		
		BuildButton hovered = null;
		for (BuildButton c : buttons) {
			c.draw(g);
			if (c.state == 2) hovered = c;
		}
		
		g.setClip(o);
		
		if (hovered != null) hovered.drawTooltip(Game.currentGame.mouse.x, Game.currentGame.mouse.y, g);
	}
	
	@Override
	public void update(int tick) {
		width = Game.getWidth();
		y = Game.getHeight() - 100;
		int w = buttons.size() * (BuildButton.SIZE + 32);
		
		// int drag = dragX > 0 && startX > 0 ? dragX - startX : saveX;
		
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).y = Game.getHeight() - 84;
			buttons.get(i).update(tick);
		}
		
		if (widthCache != Game.getWidth()) {
			for (int i = 0; i < buttons.size(); i++)
				buttons.get(i).x = 15 + i * (BuildButton.SIZE + 32) + (w > width - 20 ? 0 : (width - w) / 2);
			
			widthCache = Game.getWidth();
		}
		
		if (down != null & drag != null && w > width - 20) {
			int d = down.x - drag.x - sX;
			if (d < -15) d = -15;
			if (d > w - width - 15) d = w - width - 15;
			
			for (int i = 0; i < buttons.size(); i++)
				buttons.get(i).x = i * (BuildButton.SIZE + 32) - d;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		for (BuildButton b : buttons)
			b.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		for (BuildButton b : buttons)
			b.mousePressed(e);
		
		if (contains(e.getX(), e.getY()) && e.getButton() == MouseEvent.BUTTON1) {
			down = e.getPoint();
			sX = buttons.get(0).x;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (drag == null) {
			for (BuildButton b : buttons)
				b.mouseReleased(e);
		}
		down = null;
		drag = null;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK) drag = e.getPoint();
	}
}
