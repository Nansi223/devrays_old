package app.jaid.devrays.screen.game;

public class Controller {

	public static boolean	movingUp, movingLeft, movingDown, movingRight, shooting;

	public static int direction()
	{
		if (movingUp && movingRight) return -3;
		if (movingRight && movingDown) return -1;
		if (movingDown && movingLeft) return 1;
		if (movingLeft && movingUp) return 3;

		if (movingUp) return -4;
		if (movingRight) return -2;
		if (movingDown) return 0;
		if (movingLeft) return 2;

		return -10;
	}

}
