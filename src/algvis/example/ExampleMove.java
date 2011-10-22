package algvis.example;

import algvis.core.Algorithm;

public class ExampleMove extends Algorithm {
	ExampleDS D;
	int x, y;
	
	public ExampleMove(ExampleDS D, int x, int y) {
		super(D.M);
		this.D = D;
		this.x = x;
		this.y = y;
		setHeader("move");
	}
	
	@Override
	public void run() {
		D.v.goTo(x,y);
		setText("done");
	}
}
