package de.dakror.factory.util;

import java.util.ArrayList;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.machine.Tube;
import de.dakror.factory.game.world.Block;
import de.dakror.gamesetup.util.Vector;
import de.dakror.gamesetup.util.path.AStar;
import de.dakror.gamesetup.util.path.Node;

/**
 * @author Dakror
 */
public class TubePathFinder extends AStar
{
	@Override
	protected void handleNeighbors(Node node, Vector target, ArrayList<Node> openList, ArrayList<Node> closedList)
	{
		for (int i = -1; i < 1; i++)
		{
			for (int j = -1; j < 1; j++)
			{
				if (i == j || (i == -1 && j == 1) || (i == 1 && j == -1)) continue;
				
				Vector v = new Vector(node.t.x + i, node.t.y + j);
				Node n = new Node(node.G + 1, v.sub(target).getLength(), v, node);
				
				if (closedList.contains(n)) continue;
				
				if (openList.contains(n))
				{
					if (openList.get(openList.indexOf(n)).G > n.G)
					{
						openList.get(openList.indexOf(n)).G = n.G;
						openList.get(openList.indexOf(n)).p = node;
					}
				}
				else for (Entity e : Game.world.getEntities())
					if (e.getX() == v.x * Block.SIZE && e.getY() == v.y * Block.SIZE && e instanceof Tube) openList.add(n);
			}
		}
	}
}
