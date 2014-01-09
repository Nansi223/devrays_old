package app.jaid.devrays;

import static app.jaid.devrays.Devrays.batch;
import app.jaid.Point;
import app.jaid.devrays.debug.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Graphics {

	public static final PolygonSpriteBatch	polygons	= new PolygonSpriteBatch(256);
	public static final ShapeRenderer		shapes		= new ShapeRenderer(256);

	public static void draw(TextureRegion sprite, float x, float y, float width, float height)
	{
		draw(sprite, x, y, width, height, Color.toFloatBits(1f, 1f, 1f, 1f));
	}

	public static void draw(TextureRegion sprite, float x, float y, float width, float height, Color color)
	{
		draw(sprite, x, y, width, height, Color.toFloatBits(color.r, color.g, color.b, color.a));
	}

	public static void draw(TextureRegion sprite, float x, float y, float width, float height, float color)
	{
		batch.setColor(color);

		if (Log.show)
		{
			batch.setColor(batch.getColor().mul(1, 1, 1, 0.25f));
			batch.draw(sprite, x - width / 2, y - height / 2, width, height);

			ShapeRenderer shape = new ShapeRenderer();

			Gdx.gl.glLineWidth(2);
			shape.setProjectionMatrix(batch.getProjectionMatrix());
			shape.begin(ShapeType.Line);
			shape.rect(x - width / 2, y - height / 2, width, height, Color.BLUE, Color.BLUE, Color.BLUE, Color.CYAN);
			shape.end();

			batch.setColor(Color.MAGENTA);
			batch.draw(Atlas.get("star"), x - 0.1f, y - 0.1f, 0.2f, 0.2f);
		}
		else
			batch.draw(sprite, x - width / 2, y - height / 2, width, height);
	}

	public static void drawPolygon(Point[] points, Color color)
	{
		Mesh mesh = new Mesh(true, points.length, 0, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Color, 4, ShaderProgram.COLOR_ATTRIBUTE));
		float[] vertices = new float[points.length * 6];

		for (int i = 0; i != points.length; i++)
		{
			vertices[i * 6] = points[i].x;
			vertices[i * 6 + 1] = points[i].y;
			vertices[i * 6 + 2] = 1;
			vertices[i * 6 + 3] = 1;
			vertices[i * 6 + 4] = 1;
			vertices[i * 6 + 4] = 1;
		}

		// polygons.setColor(color);
		// mesh.render(, GL20.GL_TRIANGLES);
		// polygons.draw(new PolygonRegion(Atlas.get("rainbow"), vertices, new short[0]), 0, 0);
	}

	public static void drawRect(float x, float y, float width, float height, Color color)
	{
		shapes.setColor(color);
		shapes.rect(x, y, width, height);
	}

	public static void drawRect(Rectangle rect, Color color)
	{
		drawRect(rect.x, rect.y, rect.width, rect.height, color);
	}

	public static void updateProjection()
	{
		shapes.setProjectionMatrix(batch.getProjectionMatrix());
		polygons.setProjectionMatrix(batch.getProjectionMatrix());
	}
}
