package app.astar;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import app.data.Cell;
import app.data.Cell.CellType;
import app.data.PlayGroundData;
import app.utils.MathHelper;
import app.utils.MathHelper.CellTester;

public class Pathfinder {
	
	private PlayGroundData board;
	
	private Cell target;
	private List<Cell> testedCells = new ArrayList<>();
	private Queue<Cell> path = new LinkedList<>();

	public Pathfinder(PlayGroundData board) {
		this.board = board;
	}
	
	public void setTarget(Point target) {
		this.target = board.get(target);
		testedCells = new ArrayList<>();
		
		calculatePath(new PathCell(board.getCurrentPosition(), null));
		
		if (path.isEmpty()) {
			this.target.setWalkBlocked();
			this.target = null;
			return;
		}
		
		isFinished();
	}
	
	private void calculatePath(PathCell currentCell) {
		testedCells.add(currentCell.getCell());
		
		if (MathHelper.equals(currentCell.getCell().getPosition(), target.getPosition())) {
			while (currentCell != null) {
				path.add(currentCell.getCell());
				currentCell = currentCell.getParent();
			}
			reverseAndCleanPath();
			return;
		}
		
		List<Cell> childs = MathHelper.getSuroundings(currentCell.getCell(), new CellTester() {
			
			@Override
			public boolean test(Cell cell) {
				return cell.getType() != CellType.Blockiert && cell.getType() != CellType.WalkBlockiert && !testedCells.contains(cell);
			}
		});
		
		List<PathCell> cells = new ArrayList<PathCell>();
		
		for (Cell childCell : childs) {
			float heuristic = currentCell.getHeuristic() + 1;
			heuristic += childCell.getPosition().distanceSq(target.getPosition());
			PathCell childPathCell = new PathCell(childCell, currentCell);
			childPathCell.setHeuristic(heuristic * childCell.getType().getMultiplicator());
			cells.add(childPathCell);
		}
		
		cells.sort(new Comparator<PathCell>() {

			@Override
			public int compare(PathCell cell1, PathCell cell2) {
				if (cell1.getHeuristic() > cell2.getHeuristic()) {
					return 1;
				} else if (cell2.getHeuristic() > cell1.getHeuristic()) {
					return -1;
				}
				return 0;
			}
		});
		
		for (PathCell pathCell : cells) {
			calculatePath(pathCell);
		}
	}
	
	private void reverseAndCleanPath() {
        Stack<Cell> stack = new Stack<>();
        while (!path.isEmpty()) {
            stack.add(path.poll());
        }
        while (!stack.isEmpty()) {
        	path.add(stack.pop());
        }
        path.poll();
	}

	public boolean isFinished() {
		if (target != null) {
			for (Cell cell : path) {
				cell.getJObject().setBackground(Color.YELLOW);
			}
			
			target.getJObject().setBackground(Color.GREEN);
			return false;
		}
		return true;
	}
	
	public Point getNextMove() {
		if (path.isEmpty()) {
			target = null;
			return null;
		}
		Cell moveCell = path.poll();
		Cell currentCell = board.getCurrentPosition();
		return MathHelper.subtract(moveCell.getPosition(), currentCell.getPosition());
	}
	
	private static class PathCell {
		private float heuristic;
		private Cell cell;
		private PathCell parent;

		public PathCell(Cell cell, PathCell parent) {
			this.cell = cell;
			this.parent = parent;
		}
		
		public Cell getCell() {
			return cell;
		}
		
		public PathCell getParent() {
			return parent;
		}
		
		public float getHeuristic() {
			return heuristic;
		}
		
		public void setHeuristic(float heuristic) {
			this.heuristic = heuristic;
			//cell.getJObject().setText(""+heuristic);
		}
	}
}
