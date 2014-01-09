package app.jaid.devrays.screen.editor.data;

import app.jaid.Point;
import app.jaid.devrays.Meta;
import app.jaid.devrays.meta.code.Parameter;
import app.jaid.devrays.screen.editor.EditorScreen;

import com.badlogic.gdx.utils.Array;

public class Function {

	public static final int	PARAMETERTYPE_BLOCK		= 7;
	public static final int	PARAMETERTYPE_BYTE		= 0;
	public static final int	PARAMETERTYPE_COLOR		= 8;
	public static final int	PARAMETERTYPE_POINT		= 4;
	public static final int	PARAMETERTYPE_POLYGON	= 6;
	public static final int	PARAMETERTYPE_RECT		= 5;
	public static final int	PARAMETERTYPE_SFLOAT	= 2;
	public static final int	PARAMETERTYPE_SHORT		= 1;
	public static final int	PARAMETERTYPE_STRING	= 3;
	public Object[]			args;
	public int				type;

	public Function(int type)
	{
		this.type = type;
		Array<Object> argBuilder = new Array<Object>();

		for (Parameter param : this instanceof Event ? Meta.sdk.events[type].parameters : Meta.sdk.commands[type].parameters)
			switch (param.type)
			{
				case PARAMETERTYPE_BYTE:
				case PARAMETERTYPE_SHORT:
					argBuilder.add(new Integer((byte) 0));
				break;
				case PARAMETERTYPE_SFLOAT:
					argBuilder.add(new Float(0f));
				break;
				case PARAMETERTYPE_STRING:
					argBuilder.add("");
				break;
				case PARAMETERTYPE_POINT:
					argBuilder.add(null);
				break;
				case PARAMETERTYPE_RECT:
					argBuilder.add(null);
				break;
				case PARAMETERTYPE_POLYGON:
					argBuilder.add(null);
				break;
				case PARAMETERTYPE_BLOCK:
					argBuilder.add(new Block());
				break;
				case PARAMETERTYPE_COLOR:
					argBuilder.add(null);
				break;
			}

		args = argBuilder.toArray();
	}

	public String toString(boolean writeParamNames)
	{
		Parameter[] params = this instanceof Event ? Meta.sdk.events[type].parameters : Meta.sdk.commands[type].parameters;
		String string = (this instanceof Event ? Meta.sdk.events[type].name : Meta.sdk.commands[type].name) + " (";

		for (int i = 0; i != args.length; i++)
		{
			if (writeParamNames)
				string += params[i].name + "=";

			Object arg = args[i];

			if (arg instanceof Integer)
				string += arg;
			else if (arg instanceof Point)
				string += "Point#" + EditorScreen.map.points.indexOf((Point) arg, false);
			else if (arg instanceof Float)
				string += String.valueOf(arg);
			else if (arg instanceof String)
				string += "\"" + (String) arg + "\"";
			else
				string += "?";

			if (i != args.length - 1)
				string += ", ";
		}

		return string + ")";
	}
}
