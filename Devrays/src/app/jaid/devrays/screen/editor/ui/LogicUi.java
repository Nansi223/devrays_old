package app.jaid.devrays.screen.editor.ui;

import java.util.Arrays;
import java.util.Comparator;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.Meta;
import app.jaid.devrays.meta.code.CommandType;
import app.jaid.devrays.meta.code.Parameter;
import app.jaid.devrays.screen.editor.EditorScreen;
import app.jaid.devrays.screen.editor.TaskHandler;
import app.jaid.devrays.screen.editor.data.Command;
import app.jaid.devrays.screen.editor.data.Event;
import app.jaid.devrays.ui.Cards;
import app.jaid.devrays.ui.NumericInput;
import app.jaid.devrays.ui.ToggleLabel;
import app.jaid.devrays.world.logic.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Array;

public class LogicUi {

	public static class ToggleCardsButtonListener extends ChangeListener {

		@Override
		public void changed(ChangeEvent event, Actor actor)
		{
			toggleCards();
		}
	}

	public static Event	selectedEvent;
	static final String	CARDNAME_CODE			= "Code View";
	static final String	CARDNAME_EVENTDETAILS	= "Event Details";
	static final String	CARDNAME_EVENTS			= "Events";
	static final String	CARDNAME_TIMERS			= "Timers";
	static Label		codeLabel;
	static TextButton	codeToClipboardButton;
	static SelectBox	commandSelectBox;
	static Table		eventCard, eventDetailsCard, codeCard, timersCard, eventDetails, timerList;
	static List			eventList;

	public static void close()
	{
		Cards.close(CARDNAME_EVENTS);
		Cards.close(CARDNAME_EVENTDETAILS);
		Cards.close(CARDNAME_CODE);
		Cards.close(CARDNAME_TIMERS);
	}

	public static String getPseudoCode()
	{
		String code = "";
		int lastEventType = -1;

		for (Event event : EditorScreen.map.events)
		{
			if (event.type != lastEventType)
			{
				if (lastEventType != -1)
					code += "}\n\n";
				code += "listen " + Meta.sdk.events[event.type].name + "\n{";
				lastEventType = event.type;
			}

			code += "\n    " + event.toString(true) + "\n    {\n";

			// List Commands

			for (Command command : event.commands)
				code += "        " + command.toString(true) + "\n";

			code += "    }\n";
		}

		return code + "}";
	}

	public static void init()
	{
		eventCard = new Table(Devrays.skin);
		eventList = new List(new String[0], Devrays.skin);
		eventDetailsCard = new Table(Devrays.skin);
		codeCard = new Table(Devrays.skin);
		codeLabel = new Label("", Devrays.skin, "mono");
		timersCard = new Table(Devrays.skin);
		timerList = new Table(Devrays.skin);

		// Create Event Card

		String[] eventNames = new String[Meta.sdk.events.length + 1];
		eventNames[0] = "- Add Event -";

		for (int i = 0; i != eventNames.length - 1; i++)
			eventNames[i + 1] = Meta.sdk.events[i].name;
		Arrays.sort(eventNames);

		SelectBox eventSelectBox = new SelectBox(eventNames, Devrays.skin);
		eventSelectBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SelectBox select = (SelectBox) actor;
				if (select.getSelectionIndex() != 0)
				{
					for (int i = 0; i != Meta.sdk.events.length; i++)
						if (Meta.sdk.events[i].name == select.getSelection())// Custom indexOf, weil es kein meta.eventNames gibt, sondern ich auf ein Field eines Iterable zugreifen muss
							LogicUi.addEvent(i);
					select.setSelection(0);
				}
			}
		});

		eventCard.add(eventSelectBox).row();

		// commandSelectBox bauen

		String[] commandNames = new String[Meta.sdk.commands.length + 1];
		commandNames[0] = "- Add Command -";

		for (int i = 0; i != commandNames.length - 1; i++)
			commandNames[i + 1] = Meta.sdk.commands[i].name;
		Arrays.sort(commandNames);

		commandSelectBox = new SelectBox(commandNames, Devrays.skin);
		commandSelectBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SelectBox select = (SelectBox) actor;
				if (select.getSelectionIndex() != 0)
				{
					for (int i = 0; i != Meta.sdk.commands.length; i++)
						if (Meta.sdk.commands[i].name == select.getSelection())// Custom indexOf, weil es kein meta.commandNames gibt, sondern ich auf ein Field eines Iterable zugreifen muss
							LogicUi.addCommand(i);
					select.setSelection(0);
				}
			}
		});

		// eventList Listener

		eventList.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				selectedEvent = EditorScreen.map.events.get(eventList.getSelectedIndex());
				updateEventDetailsCard();
			}
		});

		eventCard.add(eventList);

		// Create Command Card

		// Create Pseudo Code Card

		codeToClipboardButton = new TextButton("Copy to clipboard", Devrays.skin);
		codeToClipboardButton.setDisabled(true);
		codeToClipboardButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				Gdx.app.getClipboard().setContents(String.valueOf(codeLabel.getText()));
			}
		});

		codeCard.add(codeToClipboardButton).center().row();
		codeCard.add(codeLabel);

		// Create Timers Card

		TextButton addTimerButton = new TextButton("Add Timer", Devrays.skin);
		addTimerButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				EditorScreen.map.timers.add(new Timer());
				updateTimersCard();
			}
		});

		timersCard.add(addTimerButton).row();
		timersCard.add(timerList);
	}

	public static void open()
	{
		Cards.add(CARDNAME_EVENTS, eventCard, false, 0);
		Cards.add(CARDNAME_EVENTDETAILS, eventDetailsCard, false, 0);
		toggleCards();
	}

	public static void updateArg(String actorName, Object newArg)
	{
		// Ein Event Arg Editing Widget sendet als Actorprops {ARG_INDEX}, ein Command Arg Editing Widget hingegen zwei, {ARG_INDEX, COMMAND_INDEX}

		Array<Integer> actorProps = new Array<Integer>();
		for (String prop : actorName.split("\n"))
			actorProps.add(Integer.valueOf(prop));

		if (actorProps.size == 1) // Ein Arg eines Events wird aktualisiert
		{
			selectedEvent.args[actorProps.get(0)] = newArg;
			updateEventList();
		}
		else
		// Ein Arg eines Commands des aktuell selektierten Events wird aktualisiert
		{
			selectedEvent.commands.get(actorProps.get(1)).args[actorProps.get(0)] = newArg;
			updateEventList();
		}
	}

	private static void addCommand(int type)
	{
		selectedEvent.commands.add(new Command(type));
		eventDetails.add(getCommandToggleLabel(selectedEvent.commands.size - 1)).colspan(2).padBottom(10).fill();
		updateCodeCard();
	}

	private static void addEvent(int type)
	{
		EditorScreen.map.events.add(new Event(type));
		updateEventList();
	}

	private static Actor getArgWidget(byte type, final int index, final int commandIndex)
	{
		Actor argWidget = null;
		boolean isEvent = commandIndex == -1;
		Object arg = isEvent ? selectedEvent.args[index] : selectedEvent.commands.get(commandIndex).args[index];

		switch (type)
		{
			case 0: // Integer
			case 1:
				argWidget = new NumericInput((Integer) arg, 0, type == 0 ? 255 : 65535, 0);
				((NumericInput) argWidget).setListener(new NumericInput.Listener() {

					@Override
					public void change(NumericInput input, int value, float floatValue)
					{
						updateArg(input.getName(), value);
					}
				});
			break;

			case 2: // sFloat
				argWidget = new NumericInput(0, 0, 655.35f, 2);
				((NumericInput) argWidget).setListener(new NumericInput.Listener() {

					@Override
					public void change(NumericInput input, int value, float floatValue)
					{
						updateArg(input.getName(), floatValue);
					}
				});
			break;

			case 3: // String
				TextField textField = new TextField("", Devrays.skin);
				textField.setMessageText(Meta.sdk.commands[selectedEvent.commands.get(commandIndex).type].parameters[index].name);
				textField.addListener(new FocusListener() {

					@Override
					public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
					{
						if (!focused)
							updateArg(index + "\n" + commandIndex, ((TextField) actor).getText());
					}
				});
				argWidget = textField;
			break;

			case 4: // Point
				if (isEvent)
					argWidget = new TaskButton(TaskHandler.TASKTYPE_SELECTED_EVENT_ARGUMENT_POINT, arg, index);
				else
					argWidget = new TaskButton(TaskHandler.TASKTYPE_COMMAND_ARGUMENT_POINT, arg, index, commandIndex);
			break;

			case 7: // Block
				// argBuilder.add(new Block());
			break;
		}

		if (argWidget != null)
			argWidget.setName(index + (isEvent ? "" : "\n" + commandIndex));
		return argWidget;
	}

	private static ToggleLabel getCommandToggleLabel(int commandIndex)
	{
		Command command = selectedEvent.commands.get(commandIndex);
		CommandType metaCommand = Meta.sdk.commands[command.type];

		Table content = new Table(Devrays.skin);

		for (int i = 0; i != command.args.length; i++)
		{
			Parameter param = Meta.sdk.commands[command.type].parameters[i];
			content.add(Meta.sdk.parameterTypes[param.type] + "\n" + param.name).left();
			content.add(getArgWidget(metaCommand.parameters[i].type, i, commandIndex)).row();
		}

		return new ToggleLabel(commandIndex + ": " + command.toString(false), content);
	}

	private static void toggleCards()
	{
		if (Toolbar.toggleCodeCardButton.isChecked())
			Cards.add(CARDNAME_CODE, codeCard, false, 0);
		else
			Cards.close(CARDNAME_CODE);

		if (Toolbar.toggleTimersCardButton.isChecked())
			Cards.add(CARDNAME_TIMERS, timersCard, false, 0);
		else
			Cards.close(CARDNAME_TIMERS);
	}

	private static void updateCodeCard()
	{
		codeLabel.setText(getPseudoCode());
	}

	private static void updateEventDetailsCard()
	{
		eventDetails = new Table(Devrays.skin);

		Label eventDetailsHeadline = new Label("Event Parameters", Devrays.skin, "headline");
		eventDetailsHeadline.setAlignment(1);
		eventDetails.add(eventDetailsHeadline).colspan(2).fillX().pad(10).row();

		for (int i = 0; i != selectedEvent.args.length; i++)
		{
			Parameter param = Meta.sdk.events[selectedEvent.type].parameters[i];
			eventDetails.add(Meta.sdk.parameterTypes[param.type] + "\n" + param.name).left();

			Actor argWidget = getArgWidget(param.type, i, -1);

			if (argWidget != null)
				argWidget.setName(String.valueOf(i)); // Name setzen, damit das Widget beim Bearbeitetwerden weiﬂ, auf welchen Parameter es sich auswirkt

			eventDetails.add(argWidget).row();
		}

		// Commands Headline

		Label commandsHeadline = new Label("Commands", Devrays.skin, "headline");
		commandsHeadline.setAlignment(1);
		eventDetails.add(commandsHeadline).colspan(2).fillX().pad(10).row();

		// Command Adding Widget

		eventDetails.add(commandSelectBox).colspan(2).row();

		eventDetailsCard.clear();
		eventDetailsCard.add(eventDetails);

		for (int i = 0; i != selectedEvent.commands.size; i++)
			eventDetails.add(getCommandToggleLabel(i)).colspan(2).padBottom(10).fill().row();
	}

	private static void updateEventList()
	{
		EditorScreen.map.events.sort(new Comparator<Event>() {

			@Override
			public int compare(Event event1, Event event2)
			{
				if (event1.type == 0) // Keep start() on top
					return Integer.MIN_VALUE;
				if (event2.type == 0) // Keep start() on top
					return Integer.MAX_VALUE;

				if (event1.type != event2.type) // If the name doesn't equal sort by name
					return Meta.sdk.events[event1.type].name.compareTo(Meta.sdk.events[event2.type].name);

				for (int i = 0; i != event1.args.length; i++)
					// Sort by the first different Byte value of the common parameter list
					if (event1.args[i] instanceof Integer)
					{
						int valueDifference = (Integer) event1.args[i] - (Integer) event2.args[i];
						if (valueDifference != 0)
							return valueDifference;
					}

				return 0; // Else, if all integer values of two Events of the same type equal, don't affect the current order
			}

		});

		Array<String> eventListItems = new Array<String>(String.class);

		for (int i = 0; i != EditorScreen.map.events.size; i++)
			eventListItems.add(i + ": " + EditorScreen.map.events.get(i).toString(false));

		eventList.setItems(eventListItems.toArray());
		eventList.setSelectedIndex(EditorScreen.map.events.indexOf(selectedEvent, true)); // Vor dem Sortieren selektiertes Event wiederfinden und an der neuen Position neu selektieren
		updateCodeCard();
		codeToClipboardButton.setDisabled(codeLabel.getText().length() == 0); // Copy To Clipboard Button abh‰ngig von der Tatsache, ob Code existiert, disablen oder enablen
	}

	private static void updateTimersCard()
	{
		timerList.clear();

		timerList.add("ID");
		timerList.add("interval (ms)");
		timerList.add("steps").row();

		for (int i = 0; i != EditorScreen.map.timers.size; i++)
		{
			timerList.add("Timer #" + i);
			timerList.add(new NumericInput(1000, 100, 600000, 0));
			timerList.add(new NumericInput(0, 0, 255, 0)).row();
		}
	}
}
