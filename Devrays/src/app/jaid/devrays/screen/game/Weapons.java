package app.jaid.devrays.screen.game;

import app.jaid.devrays.Atlas;
import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Weapons {

	public Rectangle	bounds;
	public int			touch	= -1;
	float				alpha;

	public Weapons()
	{
		resize();
	}

	public void render()
	{
		Devrays.uibatch.setColor(0.5f, 1, 0.5f, alpha);
		Devrays.uibatch.draw(Atlas.get("star"), bounds.x, bounds.y, bounds.width, bounds.height);
		Devrays.uibatch.setColor(1, 1, 1, 1);
	}

	public void resize()
	{
		bounds = new Rectangle(0, 0, Gdx.graphics.getPpcX() * 2, Gdx.graphics.getPpcY() * 2);
		bounds.x = Devrays.screenWidth - bounds.width;
	}

	public void update()
	{
		if (touch != -1) alpha = 1;
		else
			alpha = 0.5f;

	}
}