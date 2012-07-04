package algvis.intervaltree;

import algvis.gui.InputField;

public class IntervalChangeKey extends IntervalAlg{
	private int value;

	public IntervalChangeKey(IntervalTree T, IntervalNode v, int value) {
		super(T);
		this.v = v;
		this.value = value;
		setHeader("changekey");
		//addStep("changekeyv");
	}
	
	
	@Override
	public void run() {
		v.setKey(value);
		if (v.getKey() < 1)
			v.setKey(1);
		if (v.getKey() > InputField.MAX)
			v.setKey(InputField.MAX);
		
		//if (H.minHeap) { //<<------
		addNote("intervalchangev");
		v.mark();
		mysuspend();
		v.unmark();
		v = v.getParent();
		adjustValues(v);
		addNote("done");
	}


}
