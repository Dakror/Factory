package de.dakror.factory.game.entity.machine.tube;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.ui.CloseButton;
import de.dakror.factory.ui.ItemList;
import de.dakror.factory.ui.ItemSlot;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class IronTube extends Tube
{
	static ItemList itemList;
	static Image[] arrows = new Image[4];
	
	public static void init()
	{
		for (int i = 0; i < 4; i++)
			arrows[i] = Game.getImage("arrow.png").getSubimage(i * 64, 0, 64, 64);
	}
	
	public IronTube(float x, float y)
	{
		super(x, y);
		color = Color.darkGray;
		bgColor = Color.decode("#cccccc");
		
		name = "Eisen-Rohr";
		
		if (Game.world != null) initGUI();
	}
	
	private void initGUI()
	{
		container.components.clear();
		
		CloseButton cb = new CloseButton((Game.getWidth() + 660) / 2 - CloseButton.SIZE, (Game.getHeight() - 300) / 3);
		cb.addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.removeLayer(itemList);
				Game.currentGame.worldActiveMachine = null;
			}
		});
		container.components.add(cb);
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < (576 / ItemSlot.SIZE); j++)
			{
				final ItemSlot is = new ItemSlot((Game.getWidth() - 616) / 2 + 20 + j * ItemSlot.SIZE, (Game.getHeight() - 300) / 3 + 20 + i * ItemSlot.SIZE, null, 0);
				is.bg = arrows[i];
				is.pressEvent = new ClickEvent()
				{
					@Override
					public void trigger()
					{
						ItemSlot a = itemList.getSelectedItemSlot();
						if (a != null)
						{
							is.category = a.category;
							is.amount = a.amount;
							is.type = a.type;
						}
					}
				};
				container.components.add(is);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (state == 1)
		{
			initGUI();
			if (!(Game.currentGame.getActiveLayer() instanceof ItemList))
			{
				itemList = new ItemList();
				itemList.addCategories = true;
				Game.currentGame.addLayer(itemList);
			}
			else
			{
				((ItemList) Game.currentGame.getActiveLayer()).items = null;
				((ItemList) Game.currentGame.getActiveLayer()).killOnUnfocus = false;
				((ItemList) Game.currentGame.getActiveLayer()).addCategories = true;
				((ItemList) Game.currentGame.getActiveLayer()).init();
			}
		}
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		Helper.drawContainer((Game.getWidth() - 660) / 2, (Game.getHeight() - 300) / 3, 660, 300, true, false, g);
		super.drawGUI(g);
	}
	
	@Override
	public Entity clone()
	{
		return new IronTube(x / Block.SIZE, y / Block.SIZE);
	}
}
