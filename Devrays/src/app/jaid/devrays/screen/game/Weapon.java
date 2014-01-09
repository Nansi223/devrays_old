package app.jaid.devrays.screen.game;

import app.jaid.Jtil;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.debug.Schedule;
import app.jaid.devrays.entity.Bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Weapon {

	public Color	color;
	public int		rate, type;
	public float	speed, angle, angleVariation;

	public void fire()
	{
		Bullet bullet = new Bullet();
		bullet.position.set(GameScreen.player.position.x, GameScreen.player.position.y);
		bullet.angle = MathUtils.random(angle - angleVariation, angle + angleVariation);
		bullet.sprite = Atlas.get("star");
		bullet.setScale(0.2f);
		bullet.type = type;
		bullet.speed = speed;
		bullet.harmless = true;
		bullet.colliding = false;
		bullet.moveManually = true;
		if (Gdx.input.isKeyPressed(Keys.E))
			bullet.type = 4;
		bullet.color = bullet.type == 4 ? Color.RED.cpy() : color.cpy();
		GameScreen.bullets.add(bullet);
	}

	public void use()
	{
		Schedule.add("shoot", Jtil.spmToPeriod(rate)); // TEMP
	}
}
