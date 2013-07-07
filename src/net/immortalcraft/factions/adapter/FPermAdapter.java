package net.immortalcraft.factions.adapter;

import java.lang.reflect.Type;

import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParseException;

import net.immortalcraft.factions.FPerm;

public class FPermAdapter implements JsonDeserializer<FPerm>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FPermAdapter i = new FPermAdapter();
	public static FPermAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public FPerm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return FPerm.parse(json.getAsString());
	}
}
