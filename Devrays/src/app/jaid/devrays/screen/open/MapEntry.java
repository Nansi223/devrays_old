package app.jaid.devrays.screen.open;

import app.jaid.JTimeUtils;
import app.jaid.Jtil;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.world.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MapEntry extends Table {

	public MapEntry(final Map map, final FileHandle file, boolean showExisting, final FileHandle trash)
	{
		super(Devrays.skin);

		final Label filenameLabel = new Label(file.path(), Devrays.skin, "headline");
		filenameLabel.setAlignment(Align.center);
		add(filenameLabel).fillX();

		final TextButton editButton = new TextButton("Edit", Devrays.skin);
		editButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				Devrays.instance.setScreen(new EditorScreen(map));
			}
		});
		add(editButton);

		final TextButton deleteButton = new TextButton(showExisting ? "Delete" : "Restore", Devrays.skin);
		if (showExisting)
			deleteButton.setColor(Color.RED);
		add(deleteButton).row();

		TextureRegion thumbnail = new TextureRegion(MapThumbnailDrawer.get(map, 512, 256));
		thumbnail.flip(false, true);
		final Image thumbnailImage = new Image(thumbnail);
		add(thumbnailImage);

		final Table mapDetails = new Table(Devrays.skin);
		Label statsLabel = new Label("Filesize:\n" + Jtil.size(file.length()) + "\nModified:\n" + JTimeUtils.differenceToString(file.lastModified()) + "\nPoints:\n" + map.points.size + "\nRects:\n" + map.rects.size + "\nPolygons:\n" + map.polygons.size + "\nEvents:\n" + map.events.size + "\nSwarms:\n" + map.swarms.size, Devrays.skin);
		statsLabel.setColor(Color.LIGHT_GRAY);
		statsLabel.setAlignment(Align.right);
		mapDetails.add(statsLabel);

		add(mapDetails).right().colspan(2).minWidth(120);

		deleteButton.addListener(showExisting ? new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				trash.writeString(file.nameWithoutExtension() + " ", true);
				filenameLabel.setColor(Color.RED);
				filenameLabel.setText(filenameLabel.getText() + " (Deleted)");
				editButton.remove();
				deleteButton.remove();
				thumbnailImage.remove();
				mapDetails.remove();
			}
		} : new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				trash.writeString(trash.readString().replace(file.nameWithoutExtension() + " ", ""), false);
				filenameLabel.setColor(Color.GREEN);
				filenameLabel.setText(filenameLabel.getText() + " (Restored)");
				editButton.remove();
				deleteButton.remove();
				thumbnailImage.remove();
				mapDetails.remove();
			}
		});

		debug();
	}
}
