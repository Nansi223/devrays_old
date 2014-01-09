package app.jaid.devrays.world.effect;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PixmapColor {

	public static final int							COLORS_PER_ENTRY	= 32;
	private static HashMap<TextureRegion, Color[]>	entries				= new HashMap<TextureRegion, Color[]>();

	public static Color retrieve(TextureRegion region)
	{
		if (entries.containsKey(region))
			return entries.get(region)[MathUtils.random(0, COLORS_PER_ENTRY - 1)];

		Rectangle regionBounds = new Rectangle(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
		region.getTexture().getTextureData().prepare();
		Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();

		Color color = new Color(0, 0, 0, 0);
		Color[] colors = new Color[COLORS_PER_ENTRY];

		for (int i = 0; i != COLORS_PER_ENTRY; i++)
		{
			while (color.a < 0.5f)
				color = new Color(pixmap.getPixel((int) MathUtils.random(regionBounds.x, regionBounds.x + regionBounds.width), (int) MathUtils.random(regionBounds.y, regionBounds.y + regionBounds.height)));
			colors[i] = new Color(color);
		}

		pixmap.dispose();

		entries.put(region, colors);

		return colors[0];
	}

}
