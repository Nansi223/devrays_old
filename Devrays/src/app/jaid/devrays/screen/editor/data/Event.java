package app.jaid.devrays.screen.editor.data;

import com.badlogic.gdx.utils.Array;

public class Event extends Function {

	public Array<Command>	commands;

	public Event(int type)
	{
		super(type);
		commands = new Array<Command>();
	}
}
