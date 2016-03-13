package alda8;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.junit.Test;

public class testPoint {

	public boolean isBetween(double val, int min, int max){
		return (min <= val && val <= max);
	}
	
	public void testAllSides(int x1, int y1, int x2, int y2, int expMin, int expMax){
		Point p1;
		Point p2;
		for (int i = -10; i <= 50; i=i+5){
			for (int j = -10; j <= 10; j=j+5){
				
				try{
					p1 = new Point(x1+i, y1+j);
					p2 = new Point(x2+i, y2+j);
					double dist = ClosestPointsProblem.distance(p1, p2);
					assertTrue(isBetween(dist, expMin, expMax));
				} catch(AssertionError e){
					throw new AssertionError("x:"+x1+" y:"+y1+"    x:"+x2+" y:"+y2+"\n is not between "+expMin+" and "+expMax);
				}
			}
		}
		
	}
	
	
	@Test
	public void testDistance() {
		
		testAllSides(0, 3, 3, 0, 4, 5);
		testAllSides(0, 3, 0, 3, 0, 0);
		testAllSides(3, 0, 3, 0, 0, 0);
		testAllSides(3, 0, 0, 3, 4, 5);
		
		testAllSides(0, 2, 0, 10, 8, 11);
		testAllSides(0, 2, 0, 2, 0, 0);
		testAllSides(2, 0, 10, 0, 8, 11);
		testAllSides(10, 0, 0, 2, 10, 11);
		
		testAllSides(4,3, 4,1, 2, 2);
		testAllSides(4,3, 1, 3, 3,3);
		
	}
	

	
	@Test
	public void multitestClosest(){
		for(int i = 0; i < 10; i++)
			testClosest();
	}
	public void testClosest(){
	
		ClosestPointsProblem cpp = new ClosestPointsProblem();
		Random rand =  new Random();
	
		int nPoints 	= rand.nextInt(10_000) + 5_000;
		int max 		= rand.nextInt(10_000) + 1_000;
		max *= rand.nextBoolean()? -1: 1;
		
		int min;
		do{
			min = rand.nextInt(10_000) + 1_000;
			min *= rand.nextBoolean()? -1: 1;
		} while( min > max);

		
		
		System.out.format("running %d points\n", nPoints);
		System.out.println("max: "+max);
		System.out.println("min: "+min);
		int range = max - min;
		range *= range < 1?-1: 1;
		
		//setup
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < nPoints; i++){
			int x = rand.nextInt(range) + min;
			int y = rand.nextInt(range) + min;
			points.add(new Point(x,y));
			try{
				assertTrue(x <= max && x >= min);
				assertTrue(y <= max && y >= min);
			} catch(AssertionError e){
				fail(x + ", "+y+" : not in range");
			}
			
		}
		
		// bruteforce for comparison
		long startBruteforce 		= System.nanoTime();
		Point[] closestPointsBF 	= cpp.getClosestPointsBruteforce(points);
		long stopBruteforce 		= System.nanoTime();
		// closestpair algorithm
		long startClosestPair 		= System.nanoTime();
		Point[] closestPoints 		= cpp.getClosestPair(points);
		long stopClosestPair 		= System.nanoTime();
		
		long deltaTimeBruteforce = stopBruteforce - startBruteforce;
		long deltaTimeClosestPair = stopClosestPair - startClosestPair;
		
		double distBruteforce 	= cpp.distance(closestPointsBF[0], closestPointsBF[1]);
		double distClosestPair 	= cpp.distance(closestPoints[0], closestPoints[1]);

		if(nPoints > 1_000)
			assertTrue(deltaTimeBruteforce >= deltaTimeClosestPair);
		assertTrue(distBruteforce == distClosestPair);

	}

}
