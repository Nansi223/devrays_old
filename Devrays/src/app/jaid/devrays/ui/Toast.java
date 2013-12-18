package app.jaid.devrays.ui;

import app.jaid.Point;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.game.GameScreen;

import com.badlogic.gdx.graphics.Color;

public class Toast {

	Color	color;
	float	lifetime	= 1, angle;
	Point	position;
	String	text;

	public Toast(String text, Point position, float angle, Color color)
	{
		this.text = text;
		this.position = new Point(position.x - Devrays.toastFont.getBounds(text).width / 2, position.y + Devrays.toastFont.getLineHeight() / 2);
		this.angle = angle;
		this.color = color;
		this.position.move(angle, 20);
		GameScreen.toasts.add(this);
	}

	public boolean render()
	{
		if ((lifetime -= Devrays.delta) < 0) return false;
		position.move(angle, Devrays.delta * 50);
		Devrays.toastFont.setColor(color.r, color.g, color.b, lifetime);
		Devrays.toastFont.draw(Devrays.uibatch, text, position.x, position.y);
		return true;
	}

}
