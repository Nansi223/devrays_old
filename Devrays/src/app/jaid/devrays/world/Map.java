package app.jaid.devrays.world;

import app.jaid.Point;
import app.jaid.devrays.screen.editor.data.EnemyProfile;
import app.jaid.devrays.screen.editor.data.Event;
import app.jaid.devrays.screen.editor.data.Swarm;
import app.jaid.devrays.world.logic.Timer;

import com.badlogic.gdx.utils.Array;

public class Map {

	public Array<EnemyProfile>	enemyProfiles;
	public Array<Event>			events;
	public Array<Point>			points;
	public Array<Array<Point>>	polygons;
	public Array<Point[]>		rects;
	public Array<Swarm>			swarms;
	public Tilemap				tilemap;
	public Array<Timer>			timers;
	public String				title;
	public int					type;
}
