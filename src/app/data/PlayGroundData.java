package app.data;

import java.awt.Point;
import java.lang.reflect.Field;

import app.PlayGround;
import app.data.Cell.CellType;

public class PlayGroundData {
	
	private Field currentX, currentY;
	private PlayGround playGround;
	
	private Cell[][] board = new Cell[10][10];
	
	private Cell currentPosition;
	
	public PlayGroundData(PlayGround playGround, int startBlockCount) {
		this.playGround = playGround;
		
		try {
			currentX = PlayGround.class.getDeclaredField("currentCol");
			currentY = PlayGround.class.getDeclaredField("currentRow");
			currentX.setAccessible(true);
			currentY.setAccessible(true);
	
			Field startBlocks = PlayGround.class.getDeclaredField("maxBlocks");
			startBlocks.setAccessible(true);
			startBlocks.setInt(playGround, startBlockCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				CellType type = CellType.values()[playGround.getCellInfo(x, y)];
				board[x][y] = new Cell(this, type, new Point(x, y), playGround.getCell(x, y));
			}
		}
	}
	
	public void update() {
		updatePosition();
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				Cell cell = get(x, y);
				if (cell.getType() != CellType.WalkBlockiert) {
					CellType type = CellType.values()[playGround.getCellInfo(x, y)];
					cell.setType(type);
				}
			}
		}
		
		hasMoved = false;
	}
	
	private void updatePosition() {
		try {
			currentPosition = get(currentX.getInt(playGround), currentY.getInt(playGround));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Cell getCurrentPosition() {
		return currentPosition;
	}

	public Cell get(int x, int y) {
		return board[x][y];
	}

	public Cell get(Point p) {
		return get(p.x, p.y);
	}
	
	private boolean hasMoved = false;
	
	public void move(Point p) {
		if (hasMoved)
			return;
		if (p.y != 0) {
			if (p.y > 0) {
				playGround.moveDown();
			} else {
				playGround.moveUp();
			}
			hasMoved = true;
		} else if (p.x != 0) {
			if (p.x > 0) {
				playGround.moveRight();
			} else {
				playGround.moveLeft();
			}
			hasMoved = true;
		}
		updatePosition();
	}
	
	public Cell getNextNearestFreeCellFromCurrentPosition() {
		Point currentPosition = this.currentPosition.getPosition();
		for (int r = 1; currentPosition.x + r < 10 || currentPosition.y + r < 10 || currentPosition.x - r >= 0 || currentPosition.y - r >= 0; r++) {
			for (int x = r * -1; x <= r; x++) {
				for (int y = r; y >= r * -1; y--) {
					if (!(x == 0 && y == 0)) {
						Point p = new Point(currentPosition.x + x, currentPosition.y + y);
						if (p.x >= 0 && p.x < 10) {
							if (p.y >= 0 && p.y < 10) {
								Cell cell = get(p);
								if (cell.getType() == CellType.Frei) {
									return cell;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public Cell getNextFarestFreeCellFromCurrentPosition() {
		Point currentPosition = this.currentPosition.getPosition();
		for (int r = 10; r > 0; r--) {
			for (int x = r; x >= r * -1; x--) {
				for (int y = r; y >= r * -1; y--) {
					if (!(x == 0 && y == 0)) {
						Point p = new Point(currentPosition.x + x, currentPosition.y + y);
						if (p.x >= 0 && p.x < 10) {
							if (p.y >= 0 && p.y < 10) {
								Cell cell = get(p);
								if (cell.getType() == CellType.Frei) {
									return cell;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
}
