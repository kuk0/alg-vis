package algvis.transitions;

/**
 * The class TInt
 * Integer value with a transition effect -- the value changes gradually.
 */
public class TInt {
	int value;  // the current value
	int target; // the target value (the current value gradually approaches the target value)  
	int steps;  // the number of steps left for the transition
	
	public void set(int x) {
		set(x, 10);
	}

	public void set(int x, int s) {
		target = x;
		steps = s;
	}
	
	public int get() {
		return value;
	}
	
	public void step() {
		if (steps > 0) {
			value += (target - value) / steps;
			--steps;
		}
	}
}
