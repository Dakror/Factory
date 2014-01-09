package de.dakror.factory.layer;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FilenameFilter;

import de.dakror.factory.game.Game;
import de.dakror.factory.settings.CFG;
import de.dakror.factory.ui.CloseButton;
import de.dakror.factory.ui.SavegameLabel;
import de.dakror.gamesetup.layer.Layer;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class LoadGameLayer extends Layer
{
	public LoadGameLayer()
	{
		modal = true;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		drawModality(g);
		Helper.drawContainer(Game.getWidth() / 4, Game.getHeight() / 4, Game.getWidth() / 2, Game.getHeight() / 2, true, false, g);
		
		drawComponents(g);
	}
	
	@Override
	public void update(int tick)
	{
		updateComponents(tick);
	}
	
	@Override
	public void init()
	{
		components.clear();
		CloseButton cb = new CloseButton(Game.getWidth() / 4 * 3 - CloseButton.SIZE, Game.getHeight() / 4);
		cb.addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.removeLayer(LoadGameLayer.this);
			}
		});
		components.add(cb);
		
		File[] files = new File(CFG.DIR, "maps").listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".factory");
			}
		});
		
		for (int i = 0; i < files.length; i++)
		{
			SavegameLabel sgl = new SavegameLabel(i, files[i]);
			components.add(sgl);
		}
	}
}
