package alda8;

public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int X(){
		return x;
	}
	public int Y(){
		return y;
	}
	public String toString(){
		return "("+x+","+y+")";
	}
	public boolean equalTo(Point p2){
		return x == p2.x && y == p2.y;
	}
}
