package app.jaid.devrays.ui;

import app.jaid.devrays.Devrays;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ToggleLabel extends Table {

	public Table	content;
	public Label	headline;

	public ToggleLabel(String text, Table content)
	{
		this.content = content;
		// Wie mache ich Left Pad bei Label?
		headline = new Label(" " + text + " ", Devrays.skin, "untoggled");
		headline.setAlignment(Align.right);
		headline.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				toggle();
			}
		});
		add(headline).fill();
	}

	public void toggle()
	{
		if (getChildren().contains(content, true))
		{
			headline.setStyle(Devrays.skin.get("untoggled", LabelStyle.class));
			removeActor(content);
		}
		else
		{
			headline.setStyle(Devrays.skin.get("toggled", LabelStyle.class));
			add(content);
		}
	}
}
