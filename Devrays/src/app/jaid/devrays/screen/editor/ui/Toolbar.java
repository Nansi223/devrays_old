package app.jaid.devrays.screen.editor.ui;

import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.screen.editor.tools.DrawHandler;
import app.jaid.devrays.screen.editor.tools.EnvironmentHandler;
import app.jaid.devrays.ui.NumericInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Toolbar extends Table {

	public static ButtonGroup	environmentTabs;
	public static TextField		nameInput;
	public static Button		toggleCodeCardButton, toggleEnvironmentOverviewCardButton, toggleTimersCardButton, toggleSwarmsCardButton, toggleEnemyProfilesCardButton;
	public static final int		TOOL_DRAW					= 1;
	public static final int		TOOL_DRAW_FILL				= 1;
	public static final int		TOOL_DRAW_PENCIL			= 0;
	public static final int		TOOL_DRAW_RUBBER			= 2;
	public static final int		TOOL_ENTITIES				= 2;
	public static final int		TOOL_ENTITIES_ENEMIES		= 0;
	public static final int		TOOL_ENVIRONMENT			= 3;
	public static final int		TOOL_ENVIRONMENT_POINT		= 0;
	public static final int		TOOL_ENVIRONMENT_POLYGON	= 2;
	public static final int		TOOL_ENVIRONMENT_RECT		= 1;
	public static final int		TOOL_LOGIC					= 4;
	public static final int		TOOL_OVERVIEW				= 0;
	public static final int		TOOL_PAINT					= 5;
	Table						panel, subpanel;
	Table[]						panels;
	Table[][]					subpanels;

	public Toolbar()
	{
		super(Devrays.skin);
		setBackground("background");
		debug();
		left();

		ButtonGroup tools = new ButtonGroup();
		String[] toolnames = new String[] {"Project", "Draw", "Entities", "Env", "Logic", "Paint"};
		for (int i = 0; i != toolnames.length; i++)
		{
			TextButton tool = new TextButton(toolnames[i], Devrays.skin, "toggle");
			tool.setName(String.valueOf(i));
			tool.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor)
				{
					EditorScreen.setTool(Integer.valueOf(actor.getName()));
				}
			});
			add(tool);
			tools.add(tool);
		}
		add("--");

		panels = new Table[6];
		subpanels = new Table[6][];

		for (int i = 0; i != 5; i++)
			panels[i] = new Table(Devrays.skin);

		TextButton compileButton = new TextButton("Compile", Devrays.skin);
		compileButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				EditorScreen.map.compile();
			}
		});

		// TOOL_OVERVIEW

		panels[TOOL_OVERVIEW].add(compileButton);
		nameInput = new TextField(EditorScreen.map.title, Devrays.skin);
		panels[TOOL_OVERVIEW].add(nameInput).width(250);

		// TOOL_DRAW

		addSubtools(TOOL_DRAW, "Pencil", "Fill", "Eraser");
		panels[TOOL_DRAW].add("| ");
		final Image tilePreview = new Image(Atlas.get("t/1"));
		panels[TOOL_DRAW].add(tilePreview).size(32, 32);
		NumericInput tileInput = new NumericInput(1, 1, 200, 0);
		tileInput.setListener(new NumericInput.Listener() {

			@Override
			public void change(NumericInput input, int value, float floatValue)
			{
				DrawHandler.drawTile = (byte) value;
				tilePreview.setDrawable(new TextureRegionDrawable(Atlas.get("t/" + value)));
			}
		});
		panels[TOOL_DRAW].add(tileInput);

		// TOOL_ENTITIES

		addSubtools(TOOL_ENTITIES, "Enemies");

		subpanels[TOOL_ENTITIES] = new Table[1];
		subpanels[TOOL_ENTITIES][TOOL_ENTITIES_ENEMIES] = new Table(Devrays.skin);
		subpanels[TOOL_ENTITIES][TOOL_ENTITIES_ENEMIES].add(" | ");

		toggleSwarmsCardButton = new TextButton("Swarms", Devrays.skin, "toggle");
		toggleSwarmsCardButton.addListener(new EnemiesUi.ToggleCardsButtonListener());
		subpanels[TOOL_ENTITIES][TOOL_ENTITIES_ENEMIES].add(toggleSwarmsCardButton);

		toggleEnemyProfilesCardButton = new TextButton("Enemy Profiles", Devrays.skin, "toggle");
		toggleEnemyProfilesCardButton.setChecked(true);
		toggleEnemyProfilesCardButton.addListener(new EnemiesUi.ToggleCardsButtonListener());
		subpanels[TOOL_ENTITIES][TOOL_ENTITIES_ENEMIES].add(toggleEnemyProfilesCardButton);

		// TOOL_ENVIRONMENT

		toggleEnvironmentOverviewCardButton = new TextButton("Overview", Devrays.skin, "toggle");
		toggleEnvironmentOverviewCardButton.setChecked(true);
		toggleEnvironmentOverviewCardButton.addListener(new EnvironmentUI.ToggleCardsButtonListener());
		panels[TOOL_ENVIRONMENT].add(toggleEnvironmentOverviewCardButton);

		environmentTabs = addSubtools(TOOL_ENVIRONMENT, "Point", "Rectangle", "Polygon");

		subpanels[TOOL_ENVIRONMENT] = new Table[3];
		subpanels[TOOL_ENVIRONMENT][TOOL_ENVIRONMENT_POINT] = new Table(Devrays.skin);

		// TOOL_LOGIC

		toggleCodeCardButton = new TextButton("Code", Devrays.skin, "toggle");
		toggleCodeCardButton.addListener(new LogicUi.ToggleCardsButtonListener());
		panels[TOOL_LOGIC].add(toggleCodeCardButton);

		toggleTimersCardButton = new TextButton("Timers", Devrays.skin, "toggle");
		toggleTimersCardButton.addListener(new LogicUi.ToggleCardsButtonListener());
		panels[TOOL_LOGIC].add(toggleTimersCardButton);

		// TOOL_PAINT

		// Init Tool Handlers

		EnvironmentHandler.selectedPoints = new Array<Point>();

		// Init Tool Cards

		LogicUi.init();
		EnemiesUi.init();
		EnvironmentUI.init();

		// Init Label Texts

		updateLabels();

		// add Panels

		panel = new Table(Devrays.skin);
		add(panel);
		subpanel = new Table(Devrays.skin);
		add(subpanel);
		changePanel();
	}

	public void changePanel()
	{
		panel.clearChildren();
		panel.add(panels[EditorScreen.tool]);
		changeSubPanel();
	}

	public void changeSubPanel()
	{
		subpanel.clearChildren();
		try
		{
			subpanel.add(subpanels[EditorScreen.tool][EditorScreen.subtool]);
		} catch (NullPointerException e)
		{}
	}

	public void resize()
	{
		setSize(Devrays.screenWidth, Gdx.graphics.getPpcY());
	}

	public void updateLabels()
	{}

	private ButtonGroup addSubtools(final int tool, String... names)
	{
		ButtonGroup subTools = new ButtonGroup();
		for (int i = 0; i != names.length; i++)
		{
			TextButton subTool = new TextButton(names[i], Devrays.skin, "toggle");
			subTool.setName(String.valueOf(i));
			subTool.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor)
				{
					EditorScreen.setSubtool(Integer.valueOf(actor.getName()));
				}
			});
			panels[tool].add(subTool);
			subTools.add(subTool);
		}
		return subTools;
	}
}
