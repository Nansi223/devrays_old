package app.jaid.devrays;

import app.jaid.devrays.debug.Log;
import app.jaid.devrays.debug.Schedule;
import app.jaid.devrays.debug.SessionBuilder;
import app.jaid.devrays.net.SessionSender;
import app.jaid.devrays.screen.game.GameScreen;
import app.jaid.devrays.screen.loading.LoadingScreen;
import app.jaid.devrays.screen.menu.MenuScreen;
import app.jaid.devrays.ui.Cards;
import app.jaid.devrays.ui.Tooltip;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

public class Devrays extends Game {

	public static AssetManager		assets;
	public static SpriteBatch		batch, uibatch;
	public static boolean			debug;
	public static float				delta;
	public static InputMultiplexer	input;
	public static Devrays			instance;
	public static boolean			onSmartphone;
	public static float				runtime;
	public static int				screenWidth, screenHeight;
	public static Skin				skin;
	public static final float		speed	= 1f;					// DEBUG / TEMP
	public static long				start	= TimeUtils.millis();
	public static BitmapFont		toastFont;						// zu verschieben nach Toast.java
	public static Stage				ui;
	private static Stage			world;

	public static FileHandle file(String path)
	{
		// if (Gdx.app.getType() == ApplicationType.Desktop)
		// return Gdx.files.absolute(System.getenv("APPDATA") + "/devrays/" + path);
		return Gdx.files.external("devrays/" + path);
	}

	public static Table getNewUi()
	{
		ui.clear();
		Cards.clear();
		Table rootTable = new Table(skin);
		rootTable.setFillParent(true);
		rootTable.setTransform(false);
		rootTable.debug();
		ui.addActor(rootTable);
		return rootTable;
	}

	public static void startGame()
	{
		instance.setScreen(new GameScreen());
	}

	@Override
	public void create()
	{
		instance = this;
		onSmartphone = Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;

		world = new Stage();
		batch = (SpriteBatch) world.getSpriteBatch();
		ui = new Stage();
		input = new InputMultiplexer(ui);
		input.addProcessor(new GlobalInputListener());
		uibatch = (SpriteBatch) ui.getSpriteBatch();
		Gdx.input.setInputProcessor(input);

		assets = new AssetManager();
		setScreen(new LoadingScreen(new String[] {"atlas/pack.atlas", "font/devrays.fnt", "skin/uiskin.json", "skin/jaidskin.json", "atlas/logo_light.png"}));
		SessionSender.execute();
	}

	@Override
	public void dispose()
	{
		assets.dispose();
		ui.dispose();
		world.dispose();
		toastFont.dispose();
		skin.dispose();

		SessionBuilder.save();
	}

	@Override
	public void render()
	{
		try
		{
			if (getScreen() instanceof LoadingScreen && LoadingScreen.done)
				start();

			InputCore.update();

			delta = Gdx.graphics.getDeltaTime() * speed;
			runtime += delta;

			Schedule.update();

			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			super.render();

			ui.act(delta);
			ui.draw();

			if (debug)
				Table.drawDebug(ui);

			uibatch.begin();
			{
				uibatch.setColor(Color.WHITE);
				// Log.render();
				Tooltip.render();
			}
			uibatch.end();

			if (getScreen() instanceof MenuScreen)
				((MenuScreen) getScreen()).render2();

			InputCore.mouseMoved = false;

		} catch (Exception e)
		{
			Log.e(e);
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		screenWidth = width;
		screenHeight = height;

		Log.p("Screen Width", Devrays.screenWidth);
		Log.p("Screen Height", Devrays.screenHeight);

		ui.setViewport(width, height);
		Cards.resize(width, height);

		super.resize(width, height);
	}

	private void start()
	{
		Atlas.add(assets.get("atlas/pack.atlas", TextureAtlas.class));
		toastFont = assets.get("font/devrays.fnt");
		skin = assets.get("skin/jaidskin.json", Skin.class);

		setScreen(new MenuScreen());
	}

}
