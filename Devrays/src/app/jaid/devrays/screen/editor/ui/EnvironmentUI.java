package app.jaid.devrays.screen.editor.ui;

import static app.jaid.devrays.screen.editor.EditorScreen.map;
import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.editor.tools.EnvironmentHandler;
import app.jaid.devrays.ui.Cards;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class EnvironmentUI {

	public static class ToggleCardsButtonListener extends ChangeListener {

		@Override
		public void changed(ChangeEvent event, Actor actor)
		{
			toggleCards();
		}
	}

	private static class TabChangeListener extends ChangeListener {

		int	objectType;

		public TabChangeListener(int objectType)
		{
			this.objectType = objectType;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor)
		{
			selectedTab = objectType;
			Toolbar.environmentTabs.getButtons().get(selectedTab).setChecked(true);
			updateList();
		}
	}

	public static int	selectedTab;

	static final String	CARDNAME_ENVIRONMENT_OVERVIEW	= "Environment Overview";
	static List			list;
	static Table		overviewCard;
	static TextButton	pointTab, rectTab, polygonTab;
	static final int	TAB_POINT						= 0;
	static final int	TAB_POLYGON						= 2;
	static final int	TAB_RECT						= 1;

	public static void addListSelection(int index)
	{}

	public static void close()
	{
		Cards.close(CARDNAME_ENVIRONMENT_OVERVIEW);
	}

	public static void init()
	{
		overviewCard = new Table(Devrays.skin);

		pointTab = new TextButton("", Devrays.skin, "toggle");
		pointTab.addListener(new TabChangeListener(TAB_POINT));

		rectTab = new TextButton("", Devrays.skin, "toggle");
		rectTab.addListener(new TabChangeListener(TAB_RECT));

		polygonTab = new TextButton("", Devrays.skin, "toggle");
		polygonTab.addListener(new TabChangeListener(TAB_POLYGON));

		overviewCard.add(pointTab);
		overviewCard.add(rectTab);
		overviewCard.add(polygonTab).row();

		new ButtonGroup(pointTab, rectTab, polygonTab);

		list = new List(new String[0], Devrays.skin);

		overviewCard.add(list).colspan(3);
		updateEnvironmentTabNames();
		pointTab.setChecked(true);
	}

	public static void open()
	{
		toggleCards();
	}

	public static void updateEnvironmentTabNames()
	{
		pointTab.setText(map.points.size + " Point" + (map.points.size == 1 ? "" : "s")); // Point Tab Label + Plural Logic
		rectTab.setText(map.rects.size + " Rect" + (map.rects.size == 1 ? "" : "s")); // Rect Tab Label + Plural Logic
		polygonTab.setText(map.polygons.size + " Polygon" + (map.polygons.size == 1 ? "" : "s")); // Poly Tab Label + Plural Logic
	}

	public static void updateList()
	{
		list.getListeners().truncate(1);

		switch (selectedTab)
		{
			case TAB_POINT:
				updatePointList();
				break;
			case TAB_RECT:
				updateRectList();
				break;
			case TAB_POLYGON:
				updatePolygonList();
				break;
		}
	}

	private static void toggleCards()
	{
		if (Toolbar.toggleEnvironmentOverviewCardButton.isChecked())
			Cards.add(CARDNAME_ENVIRONMENT_OVERVIEW, overviewCard, false, 0);
		else
			Cards.close(CARDNAME_ENVIRONMENT_OVERVIEW);
	}

	private static void updatePointList()
	{
		String[] listItems = new String[map.points.size];
		for (int i = 0; i != map.points.size; i++)
			listItems[i] = i + ": " + map.points.get(i).toString();
		list.setItems(listItems);

		if (EnvironmentHandler.selectedPoints.size == 0)
			list.setSelectedIndex(-1);
		else
			list.setSelectedIndex(map.points.indexOf(EnvironmentHandler.selectedPoints.peek(), false));

		list.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				EnvironmentHandler.selectedPoints.clear();
				EnvironmentHandler.selectedPoints.add(map.points.get(((List) actor).getSelectedIndex()));
				EnvironmentHandler.updateSelection();
			}
		});
	}

	private static void updatePolygonList()
	{
		list.setItems(new String[0]);
	}

	private static void updateRectList()
	{
		list.setItems(new String[0]);
	}
}
