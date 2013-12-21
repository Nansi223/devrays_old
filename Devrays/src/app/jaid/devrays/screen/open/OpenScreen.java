package app.jaid.devrays.screen.open;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class OpenScreen implements Screen {

	private static Table	ui;

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
	{}

	@Override
	public void resize(int width, int height)
	{}

	@Override
	public void resume()
	{}

	@Override
	public void show()
	{
		ui = Devrays.getNewUi().top();
		ui.add(new OpenMapWidget());
	}
}
