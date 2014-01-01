package de.dakror.factory.game.entity.item;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * @author Dakror
 */
public class Items
{
	EnumMap<ItemType, Float> res = new EnumMap<>(ItemType.class);
	
	public Items()
	{
		for (ItemType t : ItemType.values())
			res.put(t, 0f);
	}
	
	// public Items(JSONObject data) throws JSONException
	// {
	// this();
	//
	// JSONArray names = data.names();
	// for (int i = 0; i < data.length(); i++)
	// {
	// res.put(ItemType.valueOf(names.getString(i)), (float) data.getDouble(names.getString(i)));
	// }
	// }
	
	public int get(ItemType t)
	{
		return (int) (float) res.get(t);
	}
	
	public float getF(ItemType t)
	{
		return res.get(t);
	}
	
	public Items set(ItemType t, int value)
	{
		return set(t, (float) value);
	}
	
	public Items set(ItemType t, float value)
	{
		res.put(t, value);
		
		return this;
	}
	
	public void add(ItemType t, int value)
	{
		add(t, (float) value);
	}
	
	public void add(ItemType t, float value)
	{
		res.put(t, getF(t) + value);
	}
	
	public void add(Items r)
	{
		for (ItemType s : r.getFilled())
			add(s, r.getF(s));
	}
	
	public int size()
	{
		int s = 0;
		
		for (ItemType r : res.keySet())
			if (res.get(r) != 0) s++;
		
		return s;
	}
	
	public float getLength()
	{
		float length = 0;
		
		for (ItemType r : res.keySet())
			length += getF(r);
		
		return length;
	}
	
	public ArrayList<ItemType> getFilled()
	{
		ArrayList<ItemType> res = new ArrayList<>();
		
		for (ItemType r : ItemType.values())
			if (getF(r) != 0) res.add(r);
		
		return res;
	}
	
	// public JSONObject getData()
	// {
	// JSONObject o = new JSONObject();
	// try
	// {
	// for (ItemType r : ItemType.values())
	// {
	// o.put(r.name(), getF(r));
	// }
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// return o;
	// }
	
	public static Items mul(Items res, int f)
	{
		Items result = new Items();
		for (ItemType r : res.getFilled())
			result.set(r, res.get(r) * f);
		
		return result;
	}
}
