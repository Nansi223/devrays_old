package app.jaid.devrays.packer;

import app.jaid.devrays.meta.code.CommandType;
import app.jaid.devrays.meta.code.EnemyPreset;
import app.jaid.devrays.meta.code.EventType;
import app.jaid.devrays.meta.code.Parameter;
import app.jaid.devrays.meta.code.SDKData;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class CodePacker {

	public static final String			PARAM_BLOCK		= "Block";
	public static final String			PARAM_BYTE		= "Byte";
	public static final String			PARAM_COLOR		= "Color";
	public static final String			PARAM_POINT		= "Point";
	public static final String			PARAM_POLYGON	= "Polygon";
	public static final String			PARAM_RECT		= "Rect";
	public static final String			PARAM_SFLOAT	= "sFloat";
	public static final String			PARAM_SHORT		= "Short";
	public static final String			PARAM_STRING	= "String";
	private static Array<CommandType>	commands		= new Array<CommandType>(CommandType.class);
	private static Array<EnemyPreset>	enemyPresets	= new Array<EnemyPreset>(EnemyPreset.class);
	private static Array<EventType>		events			= new Array<EventType>(EventType.class);
	private static Array<String>		parameterTypes	= new Array<String>(String.class);
	private static SDKData				sdk				= new SDKData();

	public String get()
	{
		Json json = new Json();

		addParameterType(PARAM_BYTE);
		addParameterType(PARAM_SHORT);
		addParameterType(PARAM_SFLOAT);
		addParameterType(PARAM_STRING);
		addParameterType(PARAM_POINT);
		addParameterType(PARAM_RECT);
		addParameterType(PARAM_POLYGON);
		addParameterType(PARAM_BLOCK);
		addParameterType(PARAM_COLOR);

		addEvent("start", "(empty)");
		addEvent("timerTick", "(empty)", newParameter(PARAM_BYTE, "TimerID"));
		addEvent("timerStep", "(empty)", newParameter(PARAM_BYTE, "TimerID"), newParameter(PARAM_BYTE, "CurrentStep"));
		addEvent("inRect", "(empty)", newParameter(PARAM_RECT, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		addEvent("inPolygon", "(empty)", newParameter(PARAM_POLYGON, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		addEvent("swarmDie", "(empty)", newParameter(PARAM_BYTE, "SwarmID"));
		addEvent("blockHit", "(empty)", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "BulletTeamID"));

		addCommand("stopListening", "Entfernt das Event, dem dieser Command angehört, irreversibel aus der Session.");
		addCommand("teleportPlayers", "(empty)", newParameter(PARAM_POINT, "NewPosition"));
		addCommand("startTimer", "(empty)", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("resumeTimer", "(empty)", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("stopTimer", "(empty)", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("setBlock", "(empty)", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInRect", "(empty)", newParameter(PARAM_RECT, "IntersectingRect"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInPolygon", "(empty)", newParameter(PARAM_POLYGON, "IntersectingPolygon"), newParameter(PARAM_BYTE, "Type"));
		addCommand("spawnSwarm", "(empty)", newParameter(PARAM_BYTE, "SwarmID"));
		addCommand("explode", "(empty)", newParameter(PARAM_POINT, "Position"), newParameter(PARAM_SFLOAT, "Power"));
		addCommand("displayTempText", "(empty)", newParameter(PARAM_SHORT, "LengthMs"), newParameter(PARAM_STRING, "Text"));
		addCommand("displayText", "(empty)", newParameter(PARAM_BYTE, "LabelID"), newParameter(PARAM_STRING, "Text"));
		addCommand("removeText", "(empty)", newParameter(PARAM_BYTE, "LabelID"));
		addCommand("die", "(empty)");
		addCommand("displayTimer", "(empty)", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("hideTimer", "(empty)", newParameter(PARAM_BYTE, "Timer ID"));
		addCommand("setEnvironmentColor", "(empty)", newParameter(PARAM_BYTE, "Red"), newParameter(PARAM_BYTE, "Green"), newParameter(PARAM_BYTE, "Blue"));
		addCommand("removeSwarm", "(empty)", newParameter(PARAM_BYTE, "SwarmID"));

		addEnemyPreset("Eye", 0, 30, Colors.WHITE);
		addEnemyPreset("Power Eye", 0, 100, Colors.RED);

		sdk.parameterTypes = parameterTypes.toArray();
		sdk.commands = commands.toArray();
		sdk.events = events.toArray();
		sdk.enemies = enemyPresets.toArray();
		return json.toJson(sdk);
	}

	private void addCommand(String name, String description, Parameter... parameters)
	{
		CommandType command = new CommandType();
		command.name = name;
		command.description = description;
		command.parameters = parameters;
		commands.add(command);
	}

	private void addEnemyPreset(String name, int sprite, int hp, String rrggbbaa)
	{
		EnemyPreset enemyPreset = new EnemyPreset();
		enemyPreset.name = name;
		enemyPreset.sprite = sprite;
		enemyPreset.hp = hp;
		enemyPreset.color = rrggbbaa;
		enemyPresets.add(enemyPreset);
	}

	private void addEvent(String name, String description, Parameter... parameters)
	{
		EventType event = new EventType();
		event.name = name;
		event.description = description;
		event.parameters = parameters;
		events.add(event);
	}

	private void addParameterType(String name)
	{
		parameterTypes.add(name);
	}

	private Parameter newParameter(String type, String name)
	{
		Parameter parameter = new Parameter();
		parameter.type = (byte) parameterTypes.indexOf(type, false);
		parameter.name = name;
		return parameter;
	}
}
