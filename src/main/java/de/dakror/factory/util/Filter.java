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

import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Filter {
	public Category c;
	public ItemType t;
	
	public Filter(Category category, ItemType type) {
		c = category;
		t = type;
		
		if (c != null && t == null) t = ItemType.getItemsByCategories(c)[0];
	}
	
	public Filter(JSONArray a) throws JSONException {
		if (!a.isNull(0)) c = Category.values()[a.getInt(0)];
		if (!a.isNull(1)) t = ItemType.values()[a.getInt(1)];
	}
	
	public JSONArray getData() {
		JSONArray d = new JSONArray();
		
		d.put(c == null ? JSONObject.NULL : c.ordinal());
		d.put(t == null ? JSONObject.NULL : t.ordinal());
		
		return d;
	}
	
	@Override
	public String toString() {
		return getData().toString();
	}
}
