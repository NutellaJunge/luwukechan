package app;

import java.awt.Point;

import app.astar.Pathfinder;
import app.data.Cell;
import app.data.PlayGroundData;

public class Mover {
	
	private PlayGroundData board;
	private Pathfinder pathfinder;

	public Mover(PlayGround playGround) {
		this.board = new PlayGroundData(playGround, 10);
		this.pathfinder = new Pathfinder(board);
	}
	
	public void move() {
		board.update();
		
		if (pathfinder.isFinished()) {
			Cell nextTarget = board.getNextNearestFreeCellFromCurrentPosition();
			pathfinder.setTarget(nextTarget.getPosition());
		}
		
		Point nextMoveOfPath = pathfinder.getNextMove();
		if (nextMoveOfPath != null) {
			board.move(nextMoveOfPath);
		}
	}
	
}
