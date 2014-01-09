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

		addEvent("start");
		addEvent("timerTick", newParameter(PARAM_BYTE, "TimerID"));
		addEvent("timerStep", newParameter(PARAM_BYTE, "TimerID"), newParameter(PARAM_BYTE, "CurrentStep"));
		addEvent("inRect", newParameter(PARAM_RECT, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		addEvent("inPolygon", newParameter(PARAM_POLYGON, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		addEvent("swarmDie", newParameter(PARAM_BYTE, "SwarmID"));
		addEvent("blockHit", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "BulletTeamID"));

		addCommand("teleportPlayers", newParameter(PARAM_POINT, "NewPosition"));
		addCommand("stopListening");
		addCommand("startTimer", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("resumeTimer", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("stopTimer", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("setBlock", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInRect", newParameter(PARAM_RECT, "IntersectingRect"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInPolygon", newParameter(PARAM_POLYGON, "IntersectingPolygon"), newParameter(PARAM_BYTE, "Type"));
		addCommand("spawnSwarm", newParameter(PARAM_BYTE, "SwarmID"));
		addCommand("spawnBullet", newParameter(PARAM_POINT, "Position"));
		addCommand("explode", newParameter(PARAM_POINT, "Position"), newParameter(PARAM_SFLOAT, "Power"));
		addCommand("displayTempText", newParameter(PARAM_SHORT, "LengthMs"), newParameter(PARAM_STRING, "Text"));
		addCommand("displayText", newParameter(PARAM_BYTE, "LabelID"), newParameter(PARAM_STRING, "Text"));
		addCommand("removeText", newParameter(PARAM_BYTE, "LabelID"));
		addCommand("exitDeath");
		addCommand("exitSuccess");
		addCommand("vibrate", newParameter(PARAM_SHORT, "LengthMs"));
		addCommand("displayTimer", newParameter(PARAM_BYTE, "TimerID"));
		addCommand("hideTimer", newParameter(PARAM_BYTE, "Timer ID"));
		addCommand("removeSwarm", newParameter(PARAM_BYTE, "SwarmID"));
		addCommand("forbidAction", newParameter(PARAM_BYTE, "ActionID"));
		addCommand("allowAction", newParameter(PARAM_BYTE, "ActionID"));
		addCommand("addEnvironmentEffect", newParameter(PARAM_BYTE, "EnvironmentEffectID"));
		addCommand("removeEnvironmentEffect", newParameter(PARAM_BYTE, "EnvironmentEffectID"));

		addEnemyPreset("Eye", 0, 30, Colors.WHITE);
		addEnemyPreset("Power Eye", 0, 100, Colors.RED);

		sdk.parameterTypes = parameterTypes.toArray();
		sdk.commands = commands.toArray();
		sdk.events = events.toArray();
		sdk.enemies = enemyPresets.toArray();
		return json.toJson(sdk);
	}

	private void addCommand(String name, Parameter... parameters)
	{
		CommandType command = new CommandType();
		command.name = name;
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

	private void addEvent(String name, Parameter... parameters)
	{
		EventType event = new EventType();
		event.name = name;
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
