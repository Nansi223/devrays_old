package app.jaid.devrays;

import android.os.Bundle;
import app.jaid.devrays.world.effect.Particle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		cfg.useAccelerometer = true;
		cfg.useGL20 = true;

		Particle.setQuality(1);

		initialize(new Devrays(), cfg);
	}
}