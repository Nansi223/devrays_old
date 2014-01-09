package app.jaid.devrays.screen.editor;

import static app.jaid.devrays.Devrays.batch;
import static app.jaid.devrays.screen.editor.ui.Toolbar.*;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.Graphics;
import app.jaid.devrays.Meta;
import app.jaid.devrays.screen.editor.tools.DrawHandler;
import app.jaid.devrays.screen.editor.tools.EnvironmentHandler;
import app.jaid.devrays.screen.editor.ui.EnemiesUi;
import app.jaid.devrays.screen.editor.ui.EnvironmentUI;
import app.jaid.devrays.screen.editor.ui.LogicUi;
import app.jaid.devrays.screen.editor.ui.Toolbar;
import app.jaid.devrays.world.Map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;

public class EditorScreen implements Screen {

	// public static float blocksY; // 12 Blöcke + die Blöcke, die von der Ui-Leiste verdeckt werden
	public static OrthographicCamera	camera;
	public static float					cameraWidth, cameraHeight;
	public static EditorInput			input;
	public static EditorMap				map;
	public static float					rotation;
	public static final int				TASKTYPE_SETSPAWNPOINT	= 1;
	public static int					tool, subtool;
	public static Toolbar				toolbar;
	private static Label				taskLabel;
	private static Table				ui;

	public static void endTask(Object returnObject)
	{
		taskLabel.setVisible(false);
		toolbar.setVisible(true);
		setTool(TaskHandler.goBackTool);
		setSubtool(TaskHandler.goBackSubtool);
		if (returnObject != null)
			TaskHandler.endTask(returnObject);
		TaskHandler.taskType = 0;
	}

	public static void setCamera(float x, float y)
	{
		// -1 übergeben, um auszusagen, dass jener Wert unverändert bleibt

		if (x != -1)
			camera.position.x = x;
		if (y != -1)
			camera.position.y = y;

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Graphics.updateProjection();

		if (camera.position.y >= map.tilemap.getHeight() - cameraHeight / 2 && cameraHeight != map.tilemap.getHeight())
			toolbar.setVisible(false);
		else if (TaskHandler.taskType == 0)
			toolbar.setVisible(true);
	}

	public static void setSubtool(int subtool)
	{
		EditorScreen.subtool = subtool;

		if (tool == Toolbar.TOOL_ENVIRONMENT)
		{
			EnvironmentHandler.selectedPoints.clear();
			toolbar.changeSubPanel();
		}

		{ // Check tool and subtool and eventually open subtool specific Cards
			if (tool == TOOL_ENTITIES && subtool == TOOL_ENTITIES_ENEMIES)
				EnemiesUi.open();
		}
	}

	public static void setTool(int newTool)
	{
		if (tool != newTool)
		{
			{ // tool == old tool, check that and close current Cards
				if (tool == TOOL_LOGIC)
					LogicUi.close();
				if (tool == TOOL_ENTITIES)
					EnemiesUi.close();
				if (tool == TOOL_ENVIRONMENT)
				{
					EnvironmentUI.close();
					EnvironmentHandler.clearSelections();
				}
			}

			tool = newTool;
			setSubtool(0);
			toolbar.changePanel();

			{ // tool == new tool, check and open new Cards
				if (tool == TOOL_LOGIC)
					LogicUi.open();
				if (tool == TOOL_ENVIRONMENT)
					EnvironmentUI.open();
			}
		}
	}

	public static void setZoom(float zoom)
	{
		cameraHeight = Math.min(Math.max(3, zoom), map.tilemap.getHeight());

		cameraWidth = cameraHeight * Devrays.screenWidth / Devrays.screenHeight;
		camera.viewportWidth = cameraWidth;
		camera.viewportHeight = cameraHeight;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Graphics.updateProjection();
	}

	public static void startTask(int objectType, String message)
	{
		taskLabel.setText(message);
		taskLabel.setVisible(true);
		toolbar.setVisible(false);
		EnvironmentHandler.clearSelections();
		setTool(Toolbar.TOOL_ENVIRONMENT);
		setSubtool(objectType);
	}

	private Cell<?>	toolbarCell;

	public EditorScreen(Map openedMap)
	{
		if (openedMap == null)
			map = NewMapLoader.get();
		else
			map = EditorMapLoader.load(openedMap);
	}

	@Override
	public void dispose()
	{}

	@Override
	public void hide()
	{}

	@Override
	public void pause()
	{}

	@Override
	public void render(float delta)
	{
		/*
		 * if (Gdx.input.isKeyPressed(Input.Keys.PERIOD) || Gdx.input.isKeyPressed(Input.Keys.VOLUME_UP))
		 * setZoom(cameraHeight + Devrays.delta * 10);
		 * if (Gdx.input.isKeyPressed(Input.Keys.COMMA) || Gdx.input.isKeyPressed(Input.Keys.VOLUME_DOWN))
		 * setZoom(cameraHeight - Devrays.delta * 10);
		 */

		batch.begin();
		{
			map.render(); // Render entities etc
		}
		batch.end();

		Graphics.shapes.begin(ShapeType.Filled);
		{
			map.renderShapes();
		}
		Graphics.shapes.end();

		Graphics.polygons.begin();
		{
			map.renderPolygons();
		}
		Graphics.polygons.end();
	}

	@Override
	public void resize(int width, int height)
	{
		toolbar.resize();
		setZoom(cameraHeight);
		toolbarCell.width(Devrays.screenWidth);
	}

	@Override
	public void resume()
	{}

	@Override
	public void show()
	{
		Meta.initSDKData();

		camera = new OrthographicCamera();
		cameraHeight = 12;

		input = new EditorInput();
		Devrays.input.addProcessor(input);
		Devrays.input.addProcessor(new GestureDetector(new EditorGestureInput()));

		ui = Devrays.getNewUi().top().left();
		toolbar = new Toolbar();
		toolbarCell = ui.add(new ScrollPane(toolbar));
		ui.row();
		taskLabel = new Label("", Devrays.skin, "big");
		taskLabel.setVisible(false);
		ui.add(taskLabel);

		DrawHandler.drawTile = 1;
	}
}
