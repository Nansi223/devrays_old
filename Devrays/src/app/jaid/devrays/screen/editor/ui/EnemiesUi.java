package app.jaid.devrays.screen.editor.ui;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.Meta;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.screen.editor.TaskHandler;
import app.jaid.devrays.screen.editor.data.EditorEnemy;
import app.jaid.devrays.screen.editor.data.EnemyProfile;
import app.jaid.devrays.screen.editor.data.Swarm;
import app.jaid.devrays.ui.Cards;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class EnemiesUi {

	public static class ToggleCardsButtonListener extends ChangeListener {

		@Override
		public void changed(ChangeEvent event, Actor actor)
		{
			toggleCards();
		}

	}

	static final String	CARDNAME_ENEMYPROFILEDETAILS	= "Enemy Profile Details";
	static final String	CARDNAME_ENEMYPROFILES			= "Enemy Profiles";
	static final String	CARDNAME_SWARMDETAILS			= "Swarm Details";
	static final String	CARDNAME_SWARMS					= "Swarms";
	static Table		enemyProfilesCard, swarmsCard, enemyProfileDetailsCard, swarmDetailsCard;
	static List			profileList, swarmList;
	static EnemyProfile	selectedProfile;
	static Swarm		selectedSwarm;

	public static void close()
	{
		Cards.close(CARDNAME_ENEMYPROFILES);
		Cards.close(CARDNAME_SWARMS);
		Cards.close(CARDNAME_SWARMDETAILS);
		Cards.close(CARDNAME_ENEMYPROFILEDETAILS);
	}

	public static void init()
	{
		profileList = new List(new String[0], Devrays.skin);
		swarmList = new List(new String[0], Devrays.skin);
		enemyProfilesCard = new Table(Devrays.skin);
		enemyProfileDetailsCard = new Table(Devrays.skin);
		swarmsCard = new Table(Devrays.skin);
		swarmDetailsCard = new Table(Devrays.skin);

		String[] presetNames = new String[Meta.sdk.enemies.length + 1];
		presetNames[0] = "- Add Profile -";

		for (int i = 0; i != presetNames.length - 1; i++)
			presetNames[i + 1] = "Based on Preset #" + i + ": " + Meta.sdk.enemies[i].name;

		SelectBox presetSelectBox = new SelectBox(presetNames, Devrays.skin);
		presetSelectBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SelectBox select = (SelectBox) actor;
				if (select.getSelectionIndex() != 0)
				{
					addProfile(select.getSelectionIndex() - 1);
					select.setSelection(0);
				}
			}
		});

		enemyProfilesCard.add(presetSelectBox).row();
		enemyProfilesCard.add(profileList);

		profileList.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				// selectedEvent = EditorScreen.map.events.get(eventList.getSelectedIndex());
				// updateCommandCard();
			}
		});

		// Swarms Card

		TextButton addSwarmButton = new TextButton("Add Swarm", Devrays.skin);
		addSwarmButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				addSwarm();
			}
		});

		swarmList.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				selectedSwarm = EditorScreen.map.swarms.get(swarmList.getSelectedIndex());
				updateSwarmDetailsCard();
			}
		});

		swarmsCard.add(addSwarmButton).row();
		swarmsCard.add(swarmList);
	}

	public static void open()
	{
		toggleCards();
	}

	private static void addEnemyToSelectedSwarm(EnemyProfile profile)
	{
		selectedSwarm.enemies.add(new EditorEnemy(profile));
		updateSwarmDetailsCard();
		updateSwarmList();
	}

	private static void addProfile(int presetId)
	{
		EditorScreen.map.enemyProfiles.add(new EnemyProfile(Meta.sdk.enemies[presetId]));
		updateProfileList();
		updateSwarmDetailsCard();
	}

	private static void addSwarm()
	{
		EditorScreen.map.swarms.add(new Swarm());
		updateSwarmList();
	}

	private static void toggleCards()
	{
		if (Toolbar.toggleSwarmsCardButton.isChecked())
		{
			Cards.add(CARDNAME_SWARMS, swarmsCard, false, 0);
			Cards.add(CARDNAME_SWARMDETAILS, swarmDetailsCard, false, 0);
		}
		else
		{
			Cards.close(CARDNAME_SWARMS);
			Cards.close(CARDNAME_SWARMDETAILS);
		}

		if (Toolbar.toggleEnemyProfilesCardButton.isChecked())
		{
			Cards.add(CARDNAME_ENEMYPROFILES, enemyProfilesCard, false, 0);
			Cards.add(CARDNAME_ENEMYPROFILEDETAILS, enemyProfileDetailsCard, false, 0);
		}
		else
		{
			Cards.close(CARDNAME_ENEMYPROFILES);
			Cards.close(CARDNAME_ENEMYPROFILEDETAILS);
		}
	}

	private static void updateProfileDetailsCard()
	{

	}

	private static void updateProfileList()
	{
		Array<String> profileListItems = new Array<String>(String.class);

		for (int i = 0; i != EditorScreen.map.enemyProfiles.size; i++)
			profileListItems.add(i + ": " + EditorScreen.map.enemyProfiles.get(i).toString());

		profileList.setItems(profileListItems.toArray());
	}

	private static void updateSwarmDetailsCard()
	{
		if (selectedSwarm != null)
		{
			Table swarmDetails = new Table(Devrays.skin);

			String[] profileNames = new String[EditorScreen.map.enemyProfiles.size + 1];
			profileNames[0] = "- Add Enemy -";

			for (int i = 0; i != profileNames.length - 1; i++)
				profileNames[i + 1] = i + ": " + EditorScreen.map.enemyProfiles.get(i).toString();

			SelectBox profileSelectBox = new SelectBox(profileNames, Devrays.skin);
			profileSelectBox.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor)
				{
					SelectBox select = (SelectBox) actor;
					if (select.getSelectionIndex() != 0)
					{
						addEnemyToSelectedSwarm(EditorScreen.map.enemyProfiles.get(select.getSelectionIndex() - 1));
						select.setSelection(0);
					}
				}
			});

			swarmDetails.add(profileSelectBox).row();

			for (int i = 0; i != selectedSwarm.enemies.size; i++)
			{
				Table enemyBox = new Table(Devrays.skin);
				enemyBox.setBackground("background_hole");
				enemyBox.add(new Label("Enemy #" + EditorScreen.map.swarms.indexOf(selectedSwarm, true) + "-" + i + " (Profile #" + EditorScreen.map.enemyProfiles.indexOf(selectedSwarm.enemies.get(i).profile, true) + ")", Devrays.skin)).colspan(2).row();
				enemyBox.add("Position");
				enemyBox.add(new TaskButton(TaskHandler.TASKTYPE_SWARM_MEMBER_SPAWNPOINT, selectedSwarm.enemies.get(i).position, EditorScreen.map.swarms.indexOf(selectedSwarm, true), i));
				swarmDetails.add(enemyBox).row().padTop(12);
			}

			swarmDetailsCard.clear();
			swarmDetailsCard.add(swarmDetails);
		}
	}

	private static void updateSwarmList()
	{
		Array<String> swarmListItems = new Array<String>(String.class);

		for (int i = 0; i != EditorScreen.map.swarms.size; i++)
		{
			int swarmSize = EditorScreen.map.swarms.get(i).enemies.size;
			swarmListItems.add("Swarm #" + i + " (" + swarmSize + (swarmSize == 1 ? " enemy" : " enemies") + ")");
		}

		swarmList.setItems(swarmListItems.toArray());
		swarmList.setSelectedIndex(EditorScreen.map.swarms.indexOf(selectedSwarm, true));
	}
}
