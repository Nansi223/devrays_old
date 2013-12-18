package app.jaid.devrays.debug;

import static app.jaid.devrays.Devrays.delta;
import static app.jaid.devrays.Devrays.uibatch;

import java.sql.Connection;
import java.util.TreeMap;
import java.util.TreeSet;

import app.jaid.Jtil;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.InputCore;
import app.jaid.devrays.screen.game.GameScreen;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class Log {

	public static BitmapFont				arial			= new BitmapFont();
	public static Array<Exception>			exceptions		= new Array<Exception>();
	public static Array<String>				last100lines	= new Array<String>();
	public static TreeMap<String, Property>	props			= new TreeMap<String, Property>();
	public static boolean					show;
	private static float					fpsSince;

	public static void e(Exception e)
	{
		e.printStackTrace();
		exceptions.add(e);
	}

	public static void m(Object message)
	{
		String text = String.valueOf(message);
		Gdx.app.log("Devrays.Log", text);

		if (last100lines.size == 100)
			last100lines.removeIndex(0);
		last100lines.add(text);
	}

	public static void p(String name, Object value)
	{
		props.put(name, new Property(value));
	}

	public static void render()
	{
		update();

		if (show)
		{
			uibatch.begin();

			int y = Devrays.screenHeight - 20;

			for (String name : new TreeSet<String>(props.keySet()))
			{
				Property p = props.get(name);
				p.lifetime += Devrays.delta;

				if (p.lifetime > 5)
				{
					props.remove(name);
					break;
				}

				arial.setColor(1, 1, 1, p.lifetime > 4 ? 1 - (p.lifetime - 4) : 1);

				arial.drawMultiLine(uibatch, name + ": " + p.value, p.lifetime > 4 ? 20 - (p.lifetime - 4) * 100 : 20, y);
				y -= arial.getBounds(p.value).height + 5;
			}

			uibatch.end();
		}
	}

	private static void update()
	{
		if (Gdx.input.isTouched())
		{

			for (int i = 0; i != 10; i++)
				if (Gdx.input.isTouched(i))
				{
					p("Touch[" + i + "] X", Gdx.input.getX(i));
					p("Touch[" + i + "] Y", Devrays.screenHeight - Gdx.input.getY(i));
				}

		}
		else if (Gdx.app.getType() == ApplicationType.Desktop)
		{
			p("Mouse X", Gdx.input.getX());
			p("Mouse Y", Devrays.screenHeight - Gdx.input.getY());

			if (GameScreen.map != null)
			{

			}
		}

		if (Gdx.app.getType() == ApplicationType.Android)
			p("Roll", InputCore.angleY);

		p("Density", Gdx.graphics.getDensity());
		p("RAM Usage", Jtil.size(Gdx.app.getJavaHeap()) + ", " + Jtil.size(Gdx.app.getNativeHeap()));
		fpsSince = Gdx.graphics.getFramesPerSecond() >= 55 ? fpsSince + delta : 0;
		p("FPS", Gdx.graphics.getFramesPerSecond() + (fpsSince > 0 ? " (since " + (int) fpsSince + "s)" : " (unstable)"));
		p("Render Calls", Devrays.batch.renderCalls + uibatch.renderCalls);
	}
}
