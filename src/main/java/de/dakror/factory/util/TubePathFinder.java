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


package de.dakror.factory.util;

import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.tube.Tube;
import de.dakror.factory.game.world.Block;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;
import de.dakror.gamesetup.util.path.Node;

/**
 * @author Dakror
 */
public class TubePathFinder extends AStar {
	@Override
	protected void handleNeighbors(Node node, Vector target, ArrayList<Node> openList, ArrayList<Node> closedList) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == j || (i == -1 && j == 1) || (i == 1 && j == -1)) continue;
				
				Vector v = new Vector(node.t.x + i, node.t.y + j);
				if (v.x < 0 || v.y < 0) continue;
				
				Tube tube = null;
				for (Entity e : Game.world.getEntities()) {
					if (e.getX() == node.t.x * Block.SIZE && e.getY() == node.t.y * Block.SIZE && e instanceof Tube) {
						tube = (Tube) e;
						break;
					}
				}
				
				if (tube == null) continue;
				
				Node n = new Node(node.G + Tube.highestSpeed / tube.getSpeed(), v.clone().sub(target).getLength(), v, node);
				if (closedList.contains(n)) continue;
				
				if (openList.contains(n)) {
					if (openList.get(openList.indexOf(n)).G > n.G) {
						openList.get(openList.indexOf(n)).G = n.G;
						openList.get(openList.indexOf(n)).p = node;
					}
				} else {
					for (Entity e : Game.world.getEntities()) {
						if (!(e instanceof Tube)) continue;
						
						if (e.getX() == v.x * Block.SIZE && e.getY() == v.y * Block.SIZE && tube.isConnectedTo((Tube) e)) {
							openList.add(n);
							break;
						}
					}
				}
			}
		}
	}
}
