package app.data;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JLabel;

import app.Place;

public class Cell {
	
	private CellType type;
	private Point position;
	private JLabel heuristicLabel;
	private PlayGroundData board;
	
	public Cell(PlayGroundData board, CellType type, Point position, Place place) {
		this.board = board;
		this.type = type;
		this.position = position;
		heuristicLabel = place;
		heuristicLabel.setText("");
	}
	
	public PlayGroundData getBoard() {
		return board;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public CellType getType() {
		return type;
	}
	
	public JLabel getJObject() {
		return heuristicLabel;
	}
	
	void setType(CellType type) {
		this.type = type;
	}
	
	public void setWalkBlocked() {
		board.get(position).setType(CellType.WalkBlockiert);
		heuristicLabel.setBackground(new Color(160, 0, 0));
	}
	
	@Override
	public String toString() {
		return position+":"+type;
	}
	
	public static enum CellType {
		Frei(1), Befahren(4), Blockiert(Float.POSITIVE_INFINITY), WalkBlockiert(Float.POSITIVE_INFINITY);

		private float multiplicator;

		CellType(float multiplicator) {
			this.multiplicator = multiplicator;
		}
		
		public float getMultiplicator() {
			return multiplicator;
		}
	}
}