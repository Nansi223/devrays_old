package app.jaid.devrays.screen.editor.data;

import app.jaid.Point;
import app.jaid.devrays.Meta;
import app.jaid.devrays.meta.code.Parameter;
import app.jaid.devrays.screen.editor.EditorScreen;

import com.badlogic.gdx.utils.Array;

public class Function {

	public Object[]	args;
	public int		type;

	public Function(int type)
	{
		this.type = type;
		Array<Object> argBuilder = new Array<Object>();

		for (Parameter param : this instanceof Event ? Meta.sdk.events[type].parameters : Meta.sdk.commands[type].parameters)
			switch (param.type)
			{
				case 0: // Byte (0-255)
				case 1: // Short (0-65535)
					argBuilder.add(new Integer((byte) 0));
				break;

				case 2: // sFloat (0.00-655.35)
					argBuilder.add(new Float(0f));
				break;

				case 3: // String
					argBuilder.add("");
				break;

				case 7: // Block (<0-255, 0-255>)
					argBuilder.add(new Block());
				break;

				case 4: // EditorPoint
					argBuilder.add(null);
				break;
				case 5: // Rect
					argBuilder.add(null);
				break;
				case 6: // Polygon
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
			else
				string += "?";

			if (i != args.length - 1)
				string += ", ";
		}

		return string + ")";
	}

}
