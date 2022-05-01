package app.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import app.data.Cell;

public class MathHelper {
	
	public static Point add(Point a, Point b) {
		return new Point(a.x+b.x, a.y+b.y);
	}
	
	public static Point subtract(Point a, Point b) {
		return new Point(a.x-b.x, a.y-b.y);
	}
	
	public static boolean equals(Point a, Point b) {
		return a.x == b.x && a.y == b.y;
	}
	
	public static List<Cell> getSuroundings(Cell cell, CellTester tester) {
		List<Cell> soroundings = new ArrayList<>();
		addRelativeToListIfTester(soroundings, cell, new Point(1, 0), tester);
		addRelativeToListIfTester(soroundings, cell, new Point(-1, 0), tester);
		addRelativeToListIfTester(soroundings, cell, new Point(0, 1), tester);
		addRelativeToListIfTester(soroundings, cell, new Point(0, -1), tester);
		return soroundings;
	}
	
	private static void addRelativeToListIfTester(List<Cell> list, Cell origin, Point relative, CellTester tester) {
		Point p = add(origin.getPosition(), relative);
		if (p.x >= 0 && p.x < 10) {
			if (p.y >= 0 && p.y < 10) {
				Cell cell = origin.getBoard().get(p);
				if (tester.test(cell)) {
					list.add(cell);
				}
			}
		}
	}

	public static interface CellTester {
		boolean test(Cell cell);
	}
	
}
