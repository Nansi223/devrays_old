package app.jaid.devrays.client;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {

	@Override
	public ApplicationListener getApplicationListener()
	{
		return new Devrays();
	}

	@Override
	public GwtApplicationConfiguration getConfig()
	{
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 450);

		return cfg;
	}
}