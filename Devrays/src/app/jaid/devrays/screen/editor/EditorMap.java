package app.jaid.devrays.screen.editor;

import static app.jaid.devrays.screen.editor.EditorScreen.*;
import static app.jaid.devrays.screen.editor.ui.Toolbar.*;
import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.io.MapCompiler;
import app.jaid.devrays.screen.editor.data.EnemyProfile;
import app.jaid.devrays.screen.editor.data.Event;
import app.jaid.devrays.screen.editor.data.Swarm;
import app.jaid.devrays.screen.editor.tools.DrawHandler;
import app.jaid.devrays.screen.editor.tools.EnvironmentHandler;
import app.jaid.devrays.screen.editor.ui.LogicUi;
import app.jaid.devrays.screen.editor.ui.Toolbar;
import app.jaid.devrays.ui.Cards;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.Tile;
import app.jaid.devrays.world.Tilemap;
import app.jaid.devrays.world.logic.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class EditorMap extends Map {

	private float	speedX, speedY, tileDarkness;

	public EditorMap()
	{
		tilemap = new Tilemap();
		points = new Array<Point>(Point.class);
		rects = new Array<Point[]>(Point[].class);
		polygons = new Array<Array<Point>>();
		events = new Array<Event>(Event.class);
		enemyProfiles = new Array<EnemyProfile>(EnemyProfile.class);
		swarms = new Array<Swarm>(Swarm.class);
		timers = new Array<Timer>(Timer.class);
	}

	public void compile()
	{
		title = Toolbar.nameInput.getText();
		Object result = MapCompiler.compile(this, LogicUi.getPseudoCode());

		Table popup = new Table(Devrays.skin);

		if (result != null)
		{
			String[] resultLog = (String[]) result;
			Table stats = new Table(Devrays.skin);
			for (String stat : resultLog[0].split("<tr>"))
			{
				for (String value : stat.split("<td>"))
					stats.add(value);
				stats.row();
			}

			popup.add(stats);
			popup.add(resultLog[1]);
		}
		else
			popup.add("Could not compile:\n" + (String) result);

		Cards.add("Compilation Report", popup, true, 0);
	}

	public void fill(int x, int y, byte type)
	{
		byte base = tilemap.getTile(x, y).type;
		if (type != base)
			subFill(x, y, base, type);
	}

	public void longtap(int x, int y)
	{
		Point point = Point.fromScreenPoint(x, Devrays.screenHeight - y, camera);

		switch (tool)
		{
			case TOOL_ENVIRONMENT:
				EnvironmentHandler.longtap(point);
			break;
		}
	}

	public void render()
	{
		{ // Camera Movement Input + Camera Movement Physics
			if (Gdx.input.isKeyPressed(Input.Keys.W))
				speedY = 0.5f;
			if (Gdx.input.isKeyPressed(Input.Keys.A))
				speedX = -0.5f;
			if (Gdx.input.isKeyPressed(Input.Keys.S))
				speedY = -0.5f;
			if (Gdx.input.isKeyPressed(Input.Keys.D))
				speedX = 0.5f;

			speedX = Math.min(Jtil.normalize(speedX, 0.05f), 10);
			speedY = Math.min(Jtil.normalize(speedY, 0.05f), 10);

			float speed = EditorScreen.cameraHeight / Devrays.screenHeight * 50;
			setCamera(Math.min(Math.max(cameraWidth / 2, camera.position.x + speedX * speed), map.tilemap.getWidth() - cameraWidth / 2), Math.min(Math.max(cameraHeight / 2, camera.position.y + speedY * speed), map.tilemap.getHeight() - cameraHeight / 2));
		}

		tilemap.render(camera);

		{ // Darken Animation (Fade), Enlight Animation (Shrink Dark Area)
			if (tool == TOOL_ENVIRONMENT)
				tileDarkness = Jtil.moveTo(tileDarkness, Devrays.delta, 0.8f);
			else
				tileDarkness = Jtil.normalize(tileDarkness, Devrays.delta);
			Devrays.batch.setColor(0, 0, 0, tileDarkness);
			Devrays.batch.draw(Atlas.get("pixel"), 0, 0, Devrays.screenWidth, Devrays.screenHeight);
		}

		switch (tool)
		{
			case TOOL_OVERVIEW:
			case TOOL_ENVIRONMENT:
				EnvironmentHandler.render();
			break;
			case TOOL_DRAW:
				DrawHandler.render();
			break;
		}
	}

	public void renderPolygons()
	{
		switch (tool)
		{
			case TOOL_OVERVIEW:
			case TOOL_ENVIRONMENT:
				EnvironmentHandler.renderPolygons();
			break;
		}
	}

	public void renderShapes()
	{
		switch (tool)
		{
			case TOOL_OVERVIEW:
			case TOOL_ENVIRONMENT:
				EnvironmentHandler.renderShapes();
			break;
		}
	}

	public void setSize(int width, int height)
	{
		width = Math.min(Math.max(32, width), 287);
		height = Math.min(Math.max(16, height), 266);

		int widthChange = width - tilemap.getWidth();
		int heightChange = height - tilemap.getHeight();

		// Tile[][] tiles = new Tile[tilemap.getWidth()][tilemap.getHeight()];
		// tilemap.tiles = tiles
		if (heightChange != 0)
		{
			for (int i = 0; i != tilemap.getWidth(); i++)
			{
				Tile[] columns = new Tile[height];
				System.arraycopy(tilemap.tiles[i], 0, columns, 0, Math.min(tilemap.getHeight() - (i == 0 ? 0 : 1) - (heightChange < 0 ? heightChange : 0), height));
				tilemap.tiles[i] = columns;
			}
			if (heightChange > 0)
				for (int iy = tilemap.getHeight() - heightChange; iy != tilemap.getHeight(); iy++)
					for (int ix = 0; ix != tilemap.getWidth(); ix++)
						tilemap.tiles[ix][iy] = new Tile(0); // TEMP (Wenn ich das Tile-Objekt abschaffe, ist byte type automatisch 0)
		}

		Tile[][] tiles = new Tile[width][tilemap.getHeight()];
		System.arraycopy(tilemap.tiles, 0, tiles, 0, Math.min(tilemap.getWidth(), width));
		tilemap.tiles = tiles;
		if (widthChange > 0)
			for (int ix = tilemap.getWidth() - widthChange; ix != tilemap.getWidth(); ix++)
				for (int iy = 0; iy != tilemap.getHeight(); iy++)
					tilemap.tiles[ix][iy] = new Tile(0); // TEMP (Wenn ich das Tile-Objekt abschaffe, ist byte type automatisch 0)
	}

	public void tap(int x, int y)
	{
		Point point = Point.fromScreenPoint(x, Devrays.screenHeight - y, camera);

		switch (tool)
		{

			case TOOL_DRAW:
				DrawHandler.tap(point);
			break;

			case TOOL_ENVIRONMENT:
				EnvironmentHandler.tap(point);
			break;

		}
	}

	public void touchDown(int screenX, int screenY)
	{
		Point point = Point.fromScreenPoint(screenX, Devrays.screenHeight - screenY, camera);

		switch (tool)
		{
			case TOOL_DRAW:
				DrawHandler.touchDown(point);
			break;
		}
	}

	public void touchDragged(int oldScreenX, int oldScreenY, int screenX, int screenY)
	{
		Point oldPoint = Point.fromScreenPoint(oldScreenX, Devrays.screenHeight - oldScreenY, camera);
		Point newPoint = Point.fromScreenPoint(screenX, Devrays.screenHeight - screenY, camera);

		switch (tool)
		{
			case TOOL_DRAW:
				DrawHandler.touchDragged(oldPoint, newPoint);
			break;
		}

	}

	private void subFill(int x, int y, byte base, byte type)
	{
		if (tilemap.getTile(x, y).type == base)
		{
			tilemap.getTile(x, y).type = type;
			if (x != tilemap.getWidth() - 1)
				subFill(x + 1, y, base, type);
			if (x != 0)
				subFill(x - 1, y, base, type);
			if (y != tilemap.getHeight() - 1)
				subFill(x, y + 1, base, type);
			if (y != 0)
				subFill(x, y - 1, base, type);
		}
	}
}
