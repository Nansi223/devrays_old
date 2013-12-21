package app.jaid.devrays.screen.game;

import static app.jaid.devrays.Devrays.batch;
import app.jaid.Jtil;
import app.jaid.Point;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.entity.Bullet;
import app.jaid.devrays.entity.Enemy;
import app.jaid.devrays.entity.Entity;
import app.jaid.devrays.entity.Player;
import app.jaid.devrays.screen.editor.EditorGestureInput;
import app.jaid.devrays.ui.Toast;
import app.jaid.devrays.world.Map;
import app.jaid.devrays.world.effect.Particle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

	public static Array<Bullet>			bullets;
	public static OrthographicCamera	camera;
	public static final float			cameraHeight	= 12;
	public static float					cameraWidth;
	public static Array<Enemy>			enemies;
	public static Table					gameUi;
	public static GameInput				input;
	public static Joystick				joystick;
	public static Map					map;
	public static Player				player;
	public static Stage					stage;
	public static Array<Toast>			toasts;
	public static Array<Weapon>			weapons;
	public static Weapons				weaponsUi;
	static float						rotation, tilt;

	public static Point screenPointOf(Point position)
	{
		return new Point((position.x - camera.position.x + cameraWidth / 2) * (Devrays.screenWidth / cameraWidth), (position.y - camera.position.y + cameraHeight / 2) * (Devrays.screenHeight / cameraHeight));
	}

	public static void setCamera(float x, float y)
	{
		// -1 übergeben, um auszusagen, dass jener Wert unverändert bleibt

		if (x != -1)
			camera.position.x = Math.min(Math.max(cameraWidth / 2, x), map.tilemap.getWidth() - cameraWidth / 2);
		if (y != -1)
			camera.position.y = Math.min(Math.max(cameraHeight / 2, y), map.tilemap.getHeight() - cameraHeight / 2);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	public static void tilt(float power)
	{
		if (rotation > 5)
		{
			camera.rotate(-rotation);
			rotation = 0;
		}
		tilt = power;
	}

	@SafeVarargs
	static void renderEntities(Array<? extends Entity>... entities)
	{
		for (Array<? extends Entity> group : entities)
			for (Entity entity : group)
				entity.render();
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void hide()
	{
		Devrays.input.removeProcessor(1);
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void render(float delta)
	{
		Simulation.update();

		if (tilt != 0)
		{
			float frameRotation = (float) Math.cos(tilt) * tilt * 0.1f;
			rotation += frameRotation;
			camera.rotate(frameRotation);
			tilt = Jtil.normalize(tilt, 1);
		}
		else if (rotation != 0)
		{
			camera.rotate(-rotation);
			rotation = 0;
		}

		batch.begin();
		{
			map.tilemap.render(camera);

			player.render();

			renderEntities(bullets, enemies);

			Particle.render();
		}
		batch.end();

		Devrays.uibatch.begin();
		{
			joystick.render();
			weaponsUi.render();

			for (Toast toast : toasts)
				if (!toast.render())
					toasts.removeValue(toast, true);
		}
		Devrays.uibatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		cameraWidth = cameraHeight * width / height;
		camera.viewportWidth = cameraWidth;
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		joystick.resize();
		weaponsUi.resize();
	}

	@Override
	public void resume()
	{

	}

	@Override
	public void show()
	{
		player = new Player();
		player.sprite = Atlas.get("s/0"); // TEMP
		player.position.set(3, 3); // TEMP
		player.hp = 100; // TEMP
		player.setScale(0.8f); // TEMP
		joystick = new Joystick();
		weaponsUi = new Weapons();
		bullets = new Array<Bullet>();
		weapons = new Array<Weapon>();

		enemies = new Array<Enemy>();
		toasts = new Array<Toast>();

		camera = new OrthographicCamera();
		camera.viewportHeight = cameraHeight;

		Weapon w = new Weapon(); // TEMP
		w.type = 1;
		w.rate = 150;
		w.angle = -90;
		w.angleVariation = 0;
		w.speed = 5;
		w.color = Color.YELLOW;
		w.use();
		weapons.add(w);

		input = new GameInput();
		Devrays.input.addProcessor(input);
		Devrays.input.addProcessor(new GestureDetector(new EditorGestureInput()));

		gameUi = Devrays.getNewUi();
	}
}
