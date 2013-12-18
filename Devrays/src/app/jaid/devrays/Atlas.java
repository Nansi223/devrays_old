package app.jaid.devrays;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Atlas {

	private static HashMap<String, TextureRegion>	index	= new HashMap<String, TextureRegion>();

	public static void add(TextureAtlas atlas)
	{
		for (AtlasRegion region : atlas.getRegions())
			index.put(region.name, region);
	}

	public static TextureRegion get(String name)
	{
		TextureRegion texture = index.get(name);
		return texture != null ? texture : index.get("none");
	}

}
