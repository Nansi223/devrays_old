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

		// 0: 8-Bit unsigned
		addParameterType(PARAM_BYTE);
		// 1: 16-Bit unsigned
		addParameterType(PARAM_SHORT);
		// 2: 16-Bit 0,00 to 655,36
		addParameterType(PARAM_SFLOAT);
		// 3: 2-Byte + n Bytes
		addParameterType(PARAM_STRING);
		// 4: 4-Byte Vector {x/4, y/4}
		addParameterType(PARAM_POINT);
		// 6: 8-Byte Vector {Point, Point}
		addParameterType(PARAM_RECT);
		// 7: 4n-Byte Vector {n Points}
		addParameterType(PARAM_POLYGON);
		// 5: 2-Byte Vector {x, y}
		addParameterType(PARAM_BLOCK);

		// 0
		addEvent("start");
		// 1
		addEvent("timerTick", newParameter(PARAM_BYTE, "TimerID"));
		addEvent("timerStep", newParameter(PARAM_BYTE, "TimerID"), newParameter(PARAM_BYTE, "CurrentStep"));
		// 2
		addEvent("inRect", newParameter(PARAM_RECT, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		// 3
		addEvent("inPolygon", newParameter(PARAM_POLYGON, "Area"), newParameter(PARAM_BYTE, "TeamID"), newParameter(PARAM_BYTE, "Count"));
		// 4
		addEvent("swarmDie", newParameter(PARAM_BYTE, "EnemyGroupID"));
		// 5
		addEvent("blockHit", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "BulletTeamID"));

		// 0
		addCommand("stopListening");
		addCommand("teleportPlayers", newParameter(PARAM_POINT, "NewPosition"));
		// 1
		addCommand("startTimer", newParameter(PARAM_BYTE, "TimerID"));
		// 2
		addCommand("resumeTimer", newParameter(PARAM_BYTE, "TimerID"));
		// 3
		addCommand("stopTimer", newParameter(PARAM_BYTE, "TimerID"));
		// 4
		addCommand("setBlock", newParameter(PARAM_BLOCK, "BlockPosition"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInRect", newParameter(PARAM_RECT, "IntersectingRect"), newParameter(PARAM_BYTE, "Type"));
		addCommand("setBlocksInPolygon", newParameter(PARAM_POLYGON, "IntersectingPolygon"), newParameter(PARAM_BYTE, "Type"));
		// 5
		addCommand("spawnSwarm", newParameter(PARAM_BYTE, "EnemyGroupID"));
		// 6
		addCommand("explode", newParameter(PARAM_POINT, "Position"), newParameter(PARAM_SFLOAT, "Power"));

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
