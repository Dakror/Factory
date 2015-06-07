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


package de.dakror.factory.settings;

import java.io.File;
import java.util.Arrays;

/**
 * @author Dakror
 */
public class CFG {
	public static final File DIR = new File(System.getProperty("user.home") + "/.dakror/Factory");
	
	// -- UniVersion -- //
	public static final int VERSION = 0;
	public static final int PHASE = 0;
	
	public static boolean INTERNET;
	
	static long time = 0;
	
	static {
		DIR.mkdirs();
	}
	
	// -- debug profiling -- //
	public static void u() {
		if (time == 0) time = System.currentTimeMillis();
		else {
			p(System.currentTimeMillis() - time);
			time = 0;
		}
	}
	
	public static void p(Object... p) {
		if (p.length == 1) System.out.println(p[0]);
		else System.out.println(Arrays.toString(p));
	}
}
