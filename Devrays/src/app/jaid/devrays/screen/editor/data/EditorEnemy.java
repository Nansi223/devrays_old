package app.jaid.devrays.screen.editor.data;

import app.jaid.Point;

public class EditorEnemy {

	public Point		position;
	public EnemyProfile	profile;

	public EditorEnemy(EnemyProfile profile)
	{
		this.profile = profile;
	}

	@Override
	public String toString()
	{
		return profile.toString() + " @ " + "p";
	}

}
