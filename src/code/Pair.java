package code;

public class Pair {
	int x;
	int y;
	public Pair(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int compareTo(Pair o) {
        if (o.x == this.x) {
            return this.y - o.y;
        }
        return this.x - o.x;
    }
}
