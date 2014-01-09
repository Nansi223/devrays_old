package app.jaid.devrays.debug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.io.BinWriter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

public class SessionBuilder {

	public static void save()
	{
		String logfileName = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
		FileHandle file = Devrays.file("log/sessions/" + logfileName);
		file.writeString("", false);
		BinWriter.write(serialize(), file.file().getAbsolutePath());
		Devrays.file("log.txt").writeString(logfileName + " ", true);
	}

	public static String serialize()
	{
		try
		{
			Session session = new Session();

			HashMap<String, String> properties = new HashMap<String, String>();
			for (String key : Log.props.keySet())
				properties.put(key, Log.props.get(key).value);
			session.loggedProperties = properties;

			// session.exceptions = Log.exceptions.toArray(Exception.class);
			session.start = Devrays.start;
			session.runtime = TimeUtils.millis() - Devrays.start;
			session.log = Log.last100lines.toArray(String.class);

			return new Json().toJson(session);
		} catch (Exception e)
		{
			// return new Json().toJson(e);
			return null;
		}
	}
}
