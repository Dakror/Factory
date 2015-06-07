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


package de.dakror.factory.game.entity.item;

import java.util.ArrayList;
import java.util.EnumMap;

import de.dakror.factory.util.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Dakror
 */
public class Items {
	EnumMap<ItemType, Float> res = new EnumMap<>(ItemType.class);
	
	public Items() {
		for (ItemType t : ItemType.values())
			res.put(t, 0f);
	}
	
	public Items(JSONObject data) throws JSONException {
		this();
		
		JSONArray names = data.names();
		for (int i = 0; i < data.length(); i++) {
			res.put(ItemType.valueOf(names.getString(i)), (float) data.getDouble(names.getString(i)));
		}
	}
	
	public int get(ItemType t) {
		return (int) (float) res.get(t);
	}
	
	public float getF(ItemType t) {
		return res.get(t);
	}
	
	public Items set(ItemType t, int value) {
		return set(t, (float) value);
	}
	
	public Items set(ItemType t, float value) {
		res.put(t, value);
		
		return this;
	}
	
	public void add(ItemType t, int value) {
		add(t, (float) value);
	}
	
	public void add(ItemType t, float value) {
		res.put(t, getF(t) + value);
	}
	
	public void add(Items r) {
		for (ItemType s : r.getFilled())
			add(s, r.getF(s));
	}
	
	public int size() {
		int s = 0;
		
		for (ItemType r : res.keySet())
			if (res.get(r) != 0) s++;
		
		return s;
	}
	
	public float getLength() {
		float length = 0;
		
		for (ItemType r : res.keySet())
			length += getF(r);
		
		return length;
	}
	
	public float getLength(Filter... filters) {
		float length = 0;
		
		for (ItemType r : ItemType.values()) {
			for (Filter f : filters) {
				if (r.matchesFilter(f)) {
					length += getF(r);
					break;
				}
			}
		}
		
		return length;
	}
	
	public float getLength(ArrayList<Filter> filters) {
		float length = 0;
		
		for (ItemType r : ItemType.values()) {
			for (Filter f : filters) {
				if (r.matchesFilter(f)) {
					length += getF(r);
					break;
				}
			}
		}
		
		return length;
	}
	
	public ArrayList<ItemType> getFilled() {
		ArrayList<ItemType> res = new ArrayList<>();
		
		for (ItemType r : ItemType.values())
			if (getF(r) != 0) res.add(r);
		
		return res;
	}
	
	public ArrayList<ItemType> getFilled(ArrayList<Filter> filters) {
		ArrayList<ItemType> res = new ArrayList<>();
		
		for (ItemType r : ItemType.values()) {
			for (Filter f : filters) {
				if (getF(r) != 0 && r.matchesFilter(f)) {
					res.add(r);
					break;
				}
			}
		}
		
		return res;
	}
	
	public JSONObject getData() {
		JSONObject o = new JSONObject();
		try {
			for (ItemType r : ItemType.values())
				if (getF(r) != 0) o.put(r.name(), getF(r));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public static Items mul(Items res, int f) {
		Items result = new Items();
		for (ItemType r : res.getFilled())
			result.set(r, res.get(r) * f);
		
		return result;
	}
	
}
