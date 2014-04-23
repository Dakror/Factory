package de.dakror.factory.util;

import de.dakror.factory.game.entity.item.ItemType;
import de.dakror.factory.game.entity.item.ItemType.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Filter
{
	public Category c;
	public ItemType t;
	
	public Filter(Category category, ItemType type)
	{
		c = category;
		t = type;
		
		if (c != null && t == null) t = ItemType.getItemsByCategories(c)[0];
	}
	
	public Filter(JSONArray a) throws JSONException
	{
		if (!a.isNull(0)) c = Category.values()[a.getInt(0)];
		if (!a.isNull(1)) t = ItemType.values()[a.getInt(1)];
	}
	
	public JSONArray getData()
	{
		JSONArray d = new JSONArray();
		
		d.put(c == null ? JSONObject.NULL : c.ordinal());
		d.put(t == null ? JSONObject.NULL : t.ordinal());
		
		return d;
	}
	
	@Override
	public String toString()
	{
		return getData().toString();
	}
}
