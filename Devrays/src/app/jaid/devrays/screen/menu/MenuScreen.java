package app.jaid.devrays.screen.menu;

import static app.jaid.devrays.Devrays.uibatch;

import java.util.ArrayList;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.game.GameScreen;
import app.jaid.devrays.screen.loading.LoadingScreen;
import app.jaid.devrays.screen.open.OpenScreen;
import app.jaid.devrays.ui.NumericInput;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

	public static Table		ui;
	public static String	VERT, FRAG;
	public Button			b;
	public Rectangle		logoBounds;
	public float			logoShrinkAnimation;
	public ArrayList<Star>	stars	= new ArrayList<Star>();
	public float			starSpawnTime;

	@Override
	public void dispose()
	{

	}

	@Override
	public void hide()
	{}

	@Override
	public void pause()
	{

	}

	@Override
	public void render(float delta)
	{
		starSpawnTime += Devrays.delta;

		while (stars.size() < 300 && starSpawnTime > 0.1f)
		{
			stars.add(new Star());
			starSpawnTime -= 0.1f;
		}

		uibatch.begin();
		{
			int srcBlend = uibatch.getBlendSrcFunc();
			int dstBlend = uibatch.getBlendDstFunc();
			// uibatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

			Star.rainbowPoint += Devrays.delta * 50;

			for (Star star : stars)
				star.render(false);

			uibatch.setBlendFunction(srcBlend, dstBlend);
		}
		uibatch.end();
	}

	public void render2()
	{
		uibatch.begin();
		{
			for (Star star : stars)
				star.render(true);
		}
		uibatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		Star.maxRadius = width / 2;
		logoBounds = new Rectangle(Devrays.screenWidth, 0, 102, 51);
	}

	@Override
	public void resume()
	{

	}

	@Override
	public void show()
	{
		starSpawnTime = 3;

		ui = Devrays.getNewUi();
		ui.add(new Image(LoadingScreen.logo)).width(LoadingScreen.logoBounds.width / 2).height(LoadingScreen.logoBounds.height / 2).row();

		String[] buttons = new String[] {"Start", "SDK"};
		for (int i = 0; i != buttons.length; i++)
		{
			TextButton button = new TextButton(buttons[i], Devrays.skin);
			button.setName(String.valueOf(i));
			ui.add(button).fill().row();
			button.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor)
				{
					buttonClick(Integer.valueOf(actor.getName()));
				}
			});
		}
<<<<<<< HEAD
=======

		ui.add(new NumericInput(15, 3, 7777, 0)).row();
		ui.add(new NumericInput(8.88f, 2, 655.35f, 2)).row();
>>>>>>> branch 'master' of https://github.com/Jaid/devrays.git
	}

	private void buttonClick(int button)
	{
		switch (button)
		{
			case 0:
				Devrays.instance.setScreen(new GameScreen());
			break;
			case 1:
				Devrays.instance.setScreen(new OpenScreen());
			break;
		}
	}
}
