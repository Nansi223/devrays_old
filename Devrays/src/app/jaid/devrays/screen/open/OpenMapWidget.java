package app.jaid.devrays.screen.open;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.editor.EditorScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OpenMapWidget extends Table {

	private class TabListener extends ChangeListener {

		private boolean	showExisting;

		TabListener(boolean showExisting)
		{
			this.showExisting = showExisting;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor)
		{
			updateMapEntriesWidget(showExisting);
		}
	}

	private MapEntriesWidget	mapEntries;

	public OpenMapWidget()
	{
		TextButton newMapButton = new TextButton("New", Devrays.skin);
		newMapButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				newMap();
			}

		});

		TextButton existingsMapsToggleButton = new TextButton("Projects", Devrays.skin, "toggle");
		existingsMapsToggleButton.setChecked(true);
		existingsMapsToggleButton.addListener(new TabListener(true));

		TextButton deletedMapsToggleButton = new TextButton("Trash", Devrays.skin, "toggle");
		deletedMapsToggleButton.addListener(new TabListener(false));

		new ButtonGroup(existingsMapsToggleButton, deletedMapsToggleButton);

		add(newMapButton).fillX();
		add(existingsMapsToggleButton).fillX();
		add(deletedMapsToggleButton).fillX().row();

		updateMapEntriesWidget(true);
	}

	public void newMap()
	{
		Devrays.instance.setScreen(new EditorScreen(null));
	}

	private void updateMapEntriesWidget(boolean showExisting)
	{
		if (mapEntries != null)
			mapEntries.remove();

		mapEntries = new MapEntriesWidget(showExisting);

		if (showExisting && mapEntries.getChildren().size == 0)
			newMap();

		add(mapEntries).colspan(3).row();
	}
}
