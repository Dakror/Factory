package de.dakror.factory.game.entity.machine.tube;

import java.awt.Color;
import java.awt.Graphics2D;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.Entity;
import de.dakror.factory.game.entity.item.ItemType.Category;
import de.dakror.factory.game.world.Block;
import de.dakror.factory.ui.CloseButton;
import de.dakror.factory.ui.ItemSlot;
import de.dakror.factory.util.Filter;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class CopperTube extends IronTube
{
	public CopperTube(float x, float y)
	{
		super(x, y);
		
		name = "Kupfer-Rohr";
		
		color = Color.decode("#a22d16");
		bgColor = Color.decode("#a2908d");
		
		showItemList = false;
		
		initFilters(4);
	}
	
	@Override
	protected void initGUI()
	{
		boolean hadSome = container.components.size() > 0;
		container.components.clear();
		for (int i = 0; i < 4; i++)
		{
			final ItemSlot is = new ItemSlot((Game.getWidth() - 318) / 2 + 16 + i * ItemSlot.SIZE, (Game.getHeight() - 104) / 3 + 20, null, 0);
			is.category = filters[i].c;
			if (hadSome) is.selected = is.category == null;
			is.bg = arrows[i];
			is.keepClicked = true;
			final int j = i;
			is.addClickEvent(new ClickEvent()
			{
				@Override
				public void trigger()
				{
					for (int i = 0; i < filters.length; i++)
						filters[i] = new Filter(Category.nul, null);
					
					filters[j] = new Filter(null, null);
					
					Game.currentGame.worldActiveMachine = null;
				}
			});
			container.components.add(is);
		}
		
		CloseButton cb = new CloseButton((Game.getWidth() + 318) / 2 - CloseButton.SIZE, (Game.getHeight() - 100) / 3);
		cb.addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.worldActiveMachine = null;
			}
		});
		container.components.add(cb);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		Helper.drawContainer((Game.getWidth() - 318) / 2, (Game.getHeight() - 104) / 3, 318, 104, true, false, g);
		drawSuper(g);
	}
	
	@Override
	public Entity clone()
	{
		return new CopperTube(x / Block.SIZE, y / Block.SIZE);
	}
}
