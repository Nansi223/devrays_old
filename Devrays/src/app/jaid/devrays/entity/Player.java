package app.jaid.devrays.entity;

import app.jaid.devrays.Devrays;
import app.jaid.devrays.screen.game.GameScreen;

public class Player extends Ship {

	public int	hp;

	@Override
	public boolean update()
	{
		float cameraNewX = -1, cameraNewY = -1;

		if (rect.x < Devrays.screenWidth * 0.2f) cameraNewX = position.x;
		// GameScreen.map.cameraX -= Math.max(0,
		// Math.sin(Math.toRadians(angle)) * speed * Devrays.delta *
		// Map.tilesize);
		if (rect.x > GameScreen.cameraWidth / 2 - rect.x) cameraNewX = position.x;
		// Devrays.batch.setProjectionMatrix(Devrays.getCamera(16,
		// 12).combined);
		// GameScreen.map.cameraX -= Math.min(0, Math.sin(Math.toRadians(angle))
		// * speed * Devrays.delta * Map.tilesize);
		if (rect.y < Devrays.screenHeight * 0.35f) cameraNewY = position.y;
		// GameScreen.map.cameraY -= Math.max(0, Math.cos(Math.toRadians(angle))
		// * speed * Devrays.delta * Map.tilesize);
		if (rect.y > Devrays.screenHeight - Devrays.screenHeight * 0.35f) cameraNewY = position.y;
		// GameScreen.map.cameraY -= Math.min(0, Math.cos(Math.toRadians(angle))
		// * speed * Devrays.delta * Map.tilesize);

		if (cameraNewX != -1 || cameraNewY != -1) GameScreen.setCamera(cameraNewX, cameraNewY);

		return false;
	}
}
