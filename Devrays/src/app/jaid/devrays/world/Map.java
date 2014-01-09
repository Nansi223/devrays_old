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

	public Map()
	{
		tilemap = new Tilemap();
		points = new Array<Point>(Point.class);
		rects = new Array<Point[]>(Point[].class);
		polygons = new Array<Array<Point>>();
		events = new Array<Event>(Event.class);
		enemyProfiles = new Array<EnemyProfile>(EnemyProfile.class);
		swarms = new Array<Swarm>(Swarm.class);
		timers = new Array<Timer>(Timer.class);
	}
}
