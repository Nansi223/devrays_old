package app.jaid.devrays.entity;

import app.jaid.Jtil;
import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.debug.Schedule;
import app.jaid.devrays.screen.game.GameScreen;
import app.jaid.devrays.world.effect.Particle;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends Entity {

	public int		children;
	public Enemy	parent;
	public int		type, hp;
	float			rot;
	float			schedule;

	public Enemy(int type, Enemy parent)
	{
		this.type = type;
		sprite = Atlas.get("e" + type);

		if (parent != null)
		{
			this.parent = parent;
			parent.children++;
		}

		switch (type) {
			case 0:

				Schedule.add(this, 2);
				hp = 20;

				break;

			case 1:

				Schedule.add(this, 8);
				hp = 50;

				break;

			case 2:

				Schedule.add(this, 0.7f);
				hp = 30;

				break;

			case 3:

				Schedule.add(this, 0.1f);
				hp = 50;

				break;

			case 4:

				Schedule.add(this, 0.05f);
				hp = 500;

				break;

			default:
				break;
		}
	}

	public void damage(int damage)
	{
		blink(Color.RED);
		hp -= damage;
	}

	public void die()
	{
		Particle.spawnEffect(40, 2, position, Jtil.randomAngle(), 3, 20, sprite);
		if (parent != null)
			parent.children--;
		GameScreen.player.hp += 2;
	}

	public boolean hasChildren()
	{
		return true;
	}

	@Override
	public boolean update()
	{
		switch (type) {
			case 0:

				if (position.distanceTo(GameScreen.player.position) > 200)
					push(GameScreen.player.position.angleTo(position), 70);
				else if (Schedule.request(this))
				{
					Bullet bullet = new Bullet();
					bullet.position.set(position);
					bullet.angle = GameScreen.player.position.angleTo(position);
					bullet.sprite = Atlas.get("star");
					bullet.type = 2;
					GameScreen.bullets.add(bullet);
				}

				break;
			case 1:

				if (children != 5 && Schedule.request(this))
				{
					Enemy e = new Enemy(0, this);
					e.position.set(position);
					e.setScale(4);
					GameScreen.enemies.add(e);
				}

				break;
			case 2:

				if (Schedule.request(this))
				{
					Bullet bullet = new Bullet();
					bullet.position.set(position.x - 20, position.y);
					bullet.angle = GameScreen.player.position.angleTo(position);
					bullet.sprite = Atlas.get("e2b");
					bullet.setScale(5);
					bullet.type = 2;
					GameScreen.bullets.add(bullet);
				}

				break;
			case 3:

				rot += Devrays.delta * 50;

				if (position.distanceTo(GameScreen.player.position) < 500 && Schedule.request(this))
				{
					Bullet bullet = new Bullet();
					bullet.position.set(position.x - 20, position.y);
					bullet.angle = rot;
					bullet.sprite = Atlas.get("star");
					bullet.type = 2;
					GameScreen.bullets.add(bullet);
				}

				break;
			case 4:

				push(GameScreen.player.position.angleTo(position), 70);

				if ((rot += Devrays.delta) > 5)
				{
					blink(Color.RED);

					if (Schedule.request(this))
					{
						Bullet bullet = new Bullet();
						bullet.position.set(position);
						bullet.angle = Jtil.randomAngle();
						bullet.sprite = Atlas.get("star");
						bullet.setScale(0.3f);
						bullet.type = 2;
						GameScreen.bullets.add(bullet);
					}

					if (rot > 10)
						rot = 0;
				}

				break;

			default:
				break;
		}

		if (hp <= 0)
			die();

		return hp > 0;
	}
}
