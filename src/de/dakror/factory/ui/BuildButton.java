package de.dakror.factory.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import de.dakror.factory.game.Game;
import de.dakror.factory.game.entity.machine.Machine;
import de.dakror.gamesetup.ui.ClickEvent;
import de.dakror.gamesetup.ui.ClickableComponent;
import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class BuildButton extends ClickableComponent
{
	public static final int SIZE = 68;
	
	Machine machine;
	BufferedImage image, tooltip;
	
	public BuildButton(int x, int y, final Machine s)
	{
		super(x, y, SIZE, SIZE);
		addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.activeMachine = (Machine) s.clone();
			}
		});
		machine = s;
		Dimension scale = Helper.scaleTo(new Dimension(machine.getWidth(), machine.getHeight()), new Dimension(width, height));
		image = Helper.toBufferedImage(machine.getImage().getScaledInstance(scale.width, scale.height, Image.SCALE_AREA_AVERAGING));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (state == 2) Helper.drawContainer(x - 10, y - 16, width + 20, height + 32, false, true, g);
		else Helper.drawOutline(x - 10, y - 10, width + 20, height + 20, false, g);
		
		g.drawImage(image, x + (SIZE - image.getWidth()) / 2, y + (SIZE - image.getHeight()) / 2, image.getWidth(), image.getHeight(), Game.w);
		
		if (!enabled)
		{
			if (state == 2) Helper.drawShadow(x - 20, y - 26, width + 40, height + 52, g);
			else Helper.drawShadow(x - 10, y - 10, width + 20, height + 20, g);
		}
	}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g)
	{
		if (tooltip == null)
		{
			float size = 35;
			FontMetrics fm = g.getFontMetrics(g.getFont().deriveFont(size));
			int w = fm.stringWidth(machine.getName());
			int h = fm.getHeight();
			tooltip = new BufferedImage(w + 20, h + 10, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) tooltip.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			Helper.drawShadow(0, 0, w + 20, h + 10, g2);
			Helper.drawOutline(0, 0, w + 20, h + 10, false, g2);
			
			g2.setFont(g.getFont());
			g2.setColor(Color.white);
			Helper.drawHorizontallyCenteredString(machine.getName(), 10, w, (int) size - 1, g2, (int) size);
		}
		
		g.drawImage(tooltip, x, y - tooltip.getHeight(), Game.w);
	}
	
	@Override
	public void update(int tick)
	{}
	
	public Machine getMachine()
	{
		return machine;
	}
}
