package app.jaid.devrays.ui;

import static app.jaid.devrays.Devrays.uibatch;
import app.jaid.devrays.Devrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Interpolation;

public class Tooltip {

	private static String	text	= "";

	public static void render()
	{
		if (text.length() != 0)
		{
			BitmapFont font = Devrays.skin.getFont("small");
			TextBounds bounds = font.getMultiLineBounds(text);

			float minX = Gdx.input.getX() - 20 - bounds.width;
			float maxX = Gdx.input.getX() + 20;
			float x = Interpolation.linear.apply(maxX, minX, (float) Gdx.input.getX() / (float) Devrays.screenWidth);
			float y = Devrays.screenHeight - Gdx.input.getY() + (Gdx.input.getY() > Devrays.screenHeight / 2 ? 20 + bounds.height : -20);

			Devrays.skin.getDrawable("background").draw(uibatch, x - 20, y - bounds.height - 20, bounds.width + 40, bounds.height + 40);

			font.drawMultiLine(uibatch, text, x, y);
			text = "";
		}
	}

	public static void set(String addition)
	{
		text += text.length() != 0 ? "\n-------\n" + addition : addition;
	}

}
