package app.jaid.devrays.ui;

import java.util.HashMap;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Cards {

	static Array<Window>			cards			= new Array<Window>();
	static int						cardWidth, cardHeight;
	static HashMap<String, float[]>	positionCache	= new HashMap<String, float[]>();

	public static void add(final String title, Table content, boolean closable, int iconId)
	{
		if (get(title) == null && content != null)
		{ // Wenn aktuell noch keine Card mit diesem Namen existiert
			final Window card = new Window("  " + title, Devrays.skin, "card"); // Title Pad finden statt "  " + title
			card.setName(title);
			card.debug();
			content.debug();
			card.setTitleAlignment(Align.left);
			card.top();
			card.setKeepWithinStage(true);
			card.setColor(Color.valueOf("5FA7FF"));

			if (positionCache.containsKey(title))
				card.setPosition(positionCache.get(title)[0], positionCache.get(title)[1]);
			else
				card.setPosition(cards.size != 0 ? cards.peek().getX() + cards.peek().getWidth() + cardWidth > Devrays.screenWidth ? cards.peek().getX() - cardWidth * 1.1f : cards.peek().getX() + cards.peek().getWidth() + 10 : 10, Devrays.screenHeight / 2 - cardHeight / 2);

			ScrollPane scrollPane = new ScrollPane(content, Devrays.skin);
			// scrollPane.setOverscroll(false, true); Overscroll X abschalten?
			card.add(scrollPane).pad(0);

			if (closable)
			{
				ImageButton cancelButton = new ImageButton(Devrays.skin, "close");
				cancelButton.addListener(new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor)
					{
						close(title);
					}
				});

				card.getButtonTable().setSkin(Devrays.skin);
				card.getButtonTable().add(cancelButton).padRight(3).padBottom(9);
			}

			cards.add(card);
			Devrays.ui.addActor(card);
			resize(Devrays.screenWidth, Devrays.screenHeight);
		}
	}

	public static void clear()
	{
		for (Window card : cards)
			card.remove();
		cards.clear();
	}

	public static boolean close(String name)
	{
		Window closed = get(name);
		if (closed == null)
			return false;

		positionCache.put(name, new float[] {closed.getX(), closed.getY()});
		closed.remove();
		cards.removeValue(closed, true);
		return true;
	}

	public static Window get(String name)
	{
		for (Window card : cards)
			if (card.getName().equalsIgnoreCase(name))
				return card;
		return null;
	}

	// Wieder entfernen, falls es in der nächsten Zeit nicht danach aussieht, dass ich die brauche
	public static Table getContent(String name)
	{
		return (Table) ((ScrollPane) get("Events").getChildren().get(1)).getChildren().get(0);
	}

	public static void resize(int width, int height)
	{
		cardHeight = (int) Math.min(height * 0.8f, 650);
		cardWidth = cardHeight / 16 * 9;

		for (Window card : cards)
		{
			card.setSize(cardWidth, cardHeight);
			card.getCells().get(0).width(cardWidth - 30);
		}
	}
}
