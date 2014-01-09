package app.jaid.devrays;

import app.jaid.devrays.world.effect.Particle;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Devrays";
		cfg.width = 1024;
		cfg.height = 600;

		cfg.useGL20 = true;
		// cfg.fullscreen = true;
		cfg.addIcon("atlas/icon32.png", FileType.Internal);

		Particle.setQuality(3);

		new LwjglApplication(new Devrays(), cfg);
	}
}
