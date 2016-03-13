package alda8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ClosestPointsProblem {
	
	/**
	 * gets the Euclidean distance between param p1 and param p2
	 * @param p1 point 1
	 * @param p2 point 2
	 * @return Euclidean distance between param p1 and param p2
	 */
	public static double distance(Point p1, Point p2){

		return Math.sqrt(quadraticDistance(p1, p2));
		
	}
	/**
	 * get the quadratic distance between two points<br>
	 * @param p1 point 1 
	 * @param p2 point 2
	 * @return quadratic distance between two points
	 */
	public static double quadraticDistance(Point p1, Point p2){
		int diffX = p1.X() <= p2.X() ?		p2.X() - p1.X() : p1.X() - p2.X();
		int diffY = p1.Y() <= p2.Y() ?		p2.Y() - p1.Y() : p1.Y() - p2.Y();
		
		return Math.pow(diffY,  2) + Math.pow(diffX,  2);
	}
	/**
	 * bruteforce method to determine closest points in param list points
	 * @param points list of points to determine closest pair from
	 * @return two closest points as array
	 */
	public static Point[] getClosestPointsBruteforce(List<Point> points){
		Point[] closestPoints = new Point[2];
		double closestDist = Double.MAX_VALUE;
		
		for(int i = 0; i < points.size(); i++){
			for(int j = i+1; j < points.size(); j++){
				double currentDist = quadraticDistance(points.get(i), points.get(j)); 
				
				if (currentDist < closestDist){
					closestDist = currentDist;
					closestPoints[0] = points.get(i);
					closestPoints[1] = points.get(j);
				}
			}
		}
		return closestPoints;
	}
	/**
	 * 
	 * @param xPoints list of points sorted by x-values
	 * @param yPoints list of points sorted by y-values
	 * @return two closest points from input param as array
	 */
	private static Point[] closestPair(List<Point> xPoints, List<Point> yPoints){
		// if points list is shorter than 4 : left or right lists would contain only 1 element
		// instead bruteforce lowest distance
		if (xPoints.size() <= 3){			
			return getClosestPointsBruteforce(xPoints);		
		}
		
		// get the x-value median point
		Point xMedian = xPoints.get(xPoints.size()/2);
		
		// separate param list xPoints into left and right lists
		List<Point> xLeft 	= new ArrayList<Point>();
		List<Point> xRight 	= new ArrayList<Point>();
		for (int i = 0; i < xPoints.size()/2; i++){
			xLeft.add(xPoints.get(i));
		}
		for(int i = xPoints.size()/2; i < xPoints.size(); i++){
			xRight.add(xPoints.get(i));
		}
		
		// separate param yPoints into left and right lists
		List<Point> yLeft 	= new ArrayList<Point>();
		List<Point> yRight 	= new ArrayList<Point>();
		for (Point p : yPoints){
			if (p.X() < xMedian.X()){
				yLeft.add(p);	
			} else {
				yRight.add(p);	
			}
		}
		
		// recursively get the closest pair of the left and right x and y lists 
		Point[] dLeft 		= closestPair(xLeft, yLeft);
		Point[] dRight 		= closestPair(xRight, yRight);
		double dLeftMin 	= quadraticDistance(dLeft[0], dLeft[1]);
		double dRightMin 	= quadraticDistance(dRight[0], dRight[1]);
		
		// determine the smallest of xLefts and xRights smallest
		double curMinDist = dLeftMin;
		Point[] closestPair = dLeft;
		if (dRightMin < curMinDist){ // if right has a closer pair
			curMinDist = dRightMin;
			closestPair = dRight;
		}
		
		// get points with lower x-distance to median point.x  than current minimum distance value
		List<Point> midPoints = new ArrayList<Point>();
		for (Point p : yPoints){
			if ((xMedian.X() - p.X()) < curMinDist){
				midPoints.add(p);
			}
		}
		// compare all points from midPoints by bruteforce
		for (int i = 0; i < midPoints.size()-1; i++){
			for (int j = i+1; j < midPoints.size(); j++){
				// if two compare-points is closer in Y than current minimum distance value
				int distY = midPoints.get(j).Y() - midPoints.get(i).Y();
				if (distY < curMinDist){
					double dist = quadraticDistance(midPoints.get(j), midPoints.get(i));
					if (dist < curMinDist){ // if distance is lower than current lowest
						curMinDist = dist;
						closestPair[0] = midPoints.get(i);
						closestPair[1] = midPoints.get(j);
					}
				}
			}
		}
		
		return closestPair;
	}
	/**
	 * determine the closest pair from list of points
	 * @param points list of points to determine closest pair
	 * @return two closest points as array
	 */
	public static Point[] getClosestPair(List<Point> points){
		List<Point> xPoints = new ArrayList<Point>();
		List<Point> yPoints = new ArrayList<Point>();
		
		for(Point p : points){ // copy vals from param
			xPoints.add(p);
			yPoints.add(p);
		}
		// sort list by X values
		xPoints.sort(new Comparator<Point>(){
			public int compare(Point p1, Point p2) {
				if(p1.X() < p2.X())
					return -1;
				if(p1.X() > p2.X())
					return 1;
				return 0;
			}
		});
		// sort list by Y values
		yPoints.sort(new Comparator<Point>(){
			public int compare(Point p1, Point p2) {
				if(p1.Y() < p2.Y())
					return -1;
				if(p1.Y() > p2.Y())
					return 1;
				return 0;
			}
		});
		
		return closestPair(xPoints, yPoints);
	}
	
}
