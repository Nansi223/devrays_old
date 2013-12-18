package app.jaid.devrays.debug;

import java.util.HashMap;

import com.badlogic.gdx.utils.Json;

public class Session {

	// Exception[] exceptions;
	String[]				log;
	HashMap<String, String>	loggedProperties;
	long					start, runtime;
}
