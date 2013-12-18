package app.jaid.devrays.screen.editor.data;

import app.jaid.devrays.meta.code.EnemyPreset;

import com.badlogic.gdx.graphics.Color;

public class EnemyProfile {

	Color	color;
	int		sprite, hp;

	public EnemyProfile(EnemyPreset preset)
	{
		sprite = preset.sprite;
		hp = preset.hp;
		color = Color.valueOf(preset.color);
	}

	@Override
	public String toString()
	{
		return "[" + sprite + ", " + hp + "]";
	}

}
