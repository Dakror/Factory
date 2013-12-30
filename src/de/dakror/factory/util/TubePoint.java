package de.dakror.factory.util;

public class TubePoint
{
	public int x, y;
	public boolean in, horizontal, up;
	
	public TubePoint(int x, int y, boolean in, boolean horizontal, boolean up)
	{
		this.x = x;
		this.y = y;
		this.in = in;
		this.horizontal = horizontal;
		this.up = up;
	}
}
