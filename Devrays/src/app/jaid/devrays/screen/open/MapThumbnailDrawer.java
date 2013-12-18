package app.jaid.devrays.screen.open;

import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Base64Coder;

public class MapThumbnailDrawer {

	public static final int	ZOOM	= 16;

	public static String base64(Map map, int width, int height)
	{
		FileHandle tempFile = Devrays.file("base64thumbnail.png");
		Texture thumbnailTexture = get(map, width, height);
		PixmapIO.writePNG(tempFile, thumbnailTexture.getTextureData().consumePixmap());
		return String.valueOf(Base64Coder.encode(tempFile.readBytes()));
	}

	public static Texture get(Map map, int width, int height)
	{
		FrameBuffer buffer = new FrameBuffer(Format.RGBA8888, width, height, true);

		OrthographicCamera camera = new OrthographicCamera(width, height);
		camera.translate(width / 2, height / 2);
		camera.update();

		Devrays.uibatch.setProjectionMatrix(camera.combined);
		buffer.begin();
		Devrays.uibatch.begin();
		{
			Tile tile;
			float size = height / ZOOM;

			for (int x = 0; x != ZOOM * 2; x++)
				for (int y = 0; y != ZOOM; y++)
				{
					tile = map.tilemap.tiles[x][y];

					if (tile.type != 0)
					{
						Devrays.uibatch.setColor(1, 1, 1, Interpolation.linear.apply(0.4f, 1, 1 - new Point(x, y).distanceTo(new Point(ZOOM * 1.3f, ZOOM / 2)) / 16));
						Devrays.uibatch.draw(Atlas.get("t/" + tile.type), size * x, size * y, size, size);
					}
				}
			Devrays.skin.getFont("norm32").draw(Devrays.uibatch, map.title, 10, height);
			Devrays.skin.getFont("norm16").drawMultiLine(Devrays.uibatch, map.tilemap.getWidth() + "x" + map.tilemap.getHeight(), 10, height - 34);
		}
		Devrays.uibatch.end();
		buffer.end();

		Texture texture = buffer.getColorBufferTexture();
		// region.flip(false, true);
		// buffer.dispose();
		return texture;
	}

}
