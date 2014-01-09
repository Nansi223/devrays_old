package app.jaid.devrays.screen.editor;

import app.jaid.Point;
import app.jaid.devrays.Meta;
import app.jaid.devrays.meta.code.CommandType;
import app.jaid.devrays.screen.editor.ui.LogicUi;
import app.jaid.devrays.screen.editor.ui.TaskButton;

public class TaskHandler {

	public static int[]			args;
	public static int			goBackTool, goBackSubtool;
	public static final int		OBJECTTYPE_BLOCK							= 3;
	public static final int		OBJECTTYPE_POINT							= 0;
	public static final int		OBJECTTYPE_POLYGON							= 2;
	public static final int		OBJECTTYPE_RECT								= 1;
	public static int			taskObjectType, taskType;
	public static final int		TASKTYPE_COMMAND_ARGUMENT_BLOCK				= 8;
	public static final int		TASKTYPE_COMMAND_ARGUMENT_POINT				= 5;
	public static final int		TASKTYPE_COMMAND_ARGUMENT_POLYGON			= 6;
	public static final int		TASKTYPE_COMMAND_ARGUMENT_RECT				= 7;
	public static final int		TASKTYPE_SELECTED_EVENT_ARGUMENT_BLOCK		= 4;
	public static final int		TASKTYPE_SELECTED_EVENT_ARGUMENT_POINT		= 1;
	public static final int		TASKTYPE_SELECTED_EVENT_ARGUMENT_POLYGON	= 3;
	public static final int		TASKTYPE_SELECTED_EVENT_ARGUMENT_RECT		= 2;
	public static final int		TASKTYPE_SWARM_MEMBER_SPAWNPOINT			= 9;
	private static TaskButton	requestSender;

	public static void endTask(Object returnObject)
	{
		requestSender.updateText(returnObject);

		switch (taskType)
		{
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_POINT:
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_RECT:
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_POLYGON:
				LogicUi.updateArg(String.valueOf(args[0]), returnObject);
				break;

			case TASKTYPE_COMMAND_ARGUMENT_POINT:
			case TASKTYPE_COMMAND_ARGUMENT_RECT:
			case TASKTYPE_COMMAND_ARGUMENT_POLYGON:
				LogicUi.updateArg(args[0] + "\n" + args[1], returnObject);
				break;

			case TASKTYPE_SWARM_MEMBER_SPAWNPOINT:
				EditorScreen.map.swarms.get(args[0]).enemies.get(args[1]).position = (Point) returnObject;
				break;
		}
	}

	public static int getObjectTypeByType(int type)
	{
		switch (type)
		{
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_RECT:
				return OBJECTTYPE_RECT;
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_POLYGON:
				return OBJECTTYPE_POLYGON;
			default: // if Object Type -> Point then default, return 0
				return OBJECTTYPE_POINT;
		}
	}

	public static void startTask(TaskButton actor, int type, int tool, int subtool, int[] arguments)
	{
		requestSender = actor;
		taskType = type;
		args = arguments;
		goBackTool = tool;
		goBackSubtool = subtool;
		EditorScreen.startTask(getObjectTypeByType(type), "Task #" + taskType + ": \"Set " + getTaskMessage() + ".\"");
	}

	private static String getTaskMessage()
	{
		switch (taskType)
		{
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_POINT:
				return "";
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_RECT:
				return "";
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_POLYGON:
				return "";
			case TASKTYPE_SELECTED_EVENT_ARGUMENT_BLOCK:
				return "";

			case TASKTYPE_COMMAND_ARGUMENT_POINT:
			case TASKTYPE_COMMAND_ARGUMENT_RECT:
			case TASKTYPE_COMMAND_ARGUMENT_POLYGON:
			case TASKTYPE_COMMAND_ARGUMENT_BLOCK:
				CommandType commandType = Meta.sdk.commands[LogicUi.selectedEvent.commands.get(args[1]).type];
				return "Point for Argument '" + commandType.parameters[args[0]].name + "' of Command '" + commandType.name + "'";

			case TASKTYPE_SWARM_MEMBER_SPAWNPOINT:
				return "Spawn Point for Enemy #" + args[0] + "-" + args[1];
			default:
				return "UNKNOWN";
		}
	}

}
