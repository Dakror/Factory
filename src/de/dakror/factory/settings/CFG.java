package de.dakror.factory.settings;

import java.io.File;
import java.util.Arrays;

/**
 * @author Dakror
 */
public class CFG
{
	public static final File DIR = new File(System.getProperty("user.home") + "/.dakror/Factory");
	
	// -- UniVersion -- //
	public static final int VERSION = 0;
	public static final int PHASE = 0;
	
	public static boolean INTERNET;
	
	static long time = 0;
	
	// -- debug profiling -- //
	public static void u()
	{
		if (time == 0) time = System.currentTimeMillis();
		else
		{
			p(System.currentTimeMillis() - time);
			time = 0;
		}
	}
	
	public static void p(Object... p)
	{
		if (p.length == 1) System.out.println(p[0]);
		else System.out.println(Arrays.toString(p));
	}
}
