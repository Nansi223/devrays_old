package app.jaid.devrays.screen.loading;

import static app.jaid.devrays.Devrays.assets;
import static app.jaid.devrays.Devrays.uibatch;

import java.util.HashMap;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadingScreen implements Screen {

	public static float								animation	= 5;
	public static boolean							done;
	public static Texture							logo;
	public static Rectangle							logoBounds;
	private static final HashMap<String, Class<?>>	extensions;
	private static boolean							skipRequested;
	static Texture									logolight;

	static
	{
		extensions = new HashMap<String, Class<?>>();
		extensions.put("png", Texture.class);
		extensions.put("jpg", Texture.class);
		extensions.put("jpeg", Texture.class);
		extensions.put("atlas", TextureAtlas.class);
		extensions.put("mp3", Music.class);
		extensions.put("fnt", BitmapFont.class);
		extensions.put("json", Skin.class);
	}

	// private Texture logo;

	public LoadingScreen(String[] files)
	{
		for (String name : files)
			assets.load(name, extensions.get(name.substring(name.lastIndexOf(".") + 1)));
	}

	@Override
	public void dispose()
	{
		logolight.dispose();
	}

	@Override
	public void hide()
	{}

	@Override
	public void pause()
	{}

	@Override
	public void render(float delta)
	{
		assets.update();
		animation += Devrays.delta * 2;

		if (animation > 11)
			if (assets.getProgress() == 1)
				done = true;
			else
				animation %= 11;

		skipRequested |= Gdx.input.isKeyPressed(Keys.SPACE);
		if (assets.getProgress() == 1 && skipRequested)
			done = true;

		uibatch.begin();
		{
			if (!skipRequested)
			{
				uibatch.setColor(Color.WHITE);
				uibatch.draw(logo, logoBounds.x, logoBounds.y, logoBounds.width, logoBounds.height);
				uibatch.setColor(1, 1, 1, (float) Math.sin(animation) / 2 + 0.5f);
				uibatch.draw(logolight, logoBounds.x, logoBounds.y, logoBounds.width, logoBounds.height);
			}
		}
		uibatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		logoBounds = new Rectangle();
		logoBounds.setSize(Devrays.screenHeight, Devrays.screenHeight / 2);
		logoBounds.setPosition(Devrays.screenWidth / 2 - logoBounds.width / 2, Devrays.screenHeight / 2 - logoBounds.height / 2);
	}

	@Override
	public void resume()
	{}

	@Override
	public void show()
	{
		logo = new Texture("atlas/logo.png");
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logolight = new Texture("atlas/logo_light.png");
		logolight.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

}
