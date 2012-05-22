package algvis.intervaltree;

import algvis.core.InputField;

public class IntervalChangeKey extends IntervalAlg{
	int value;

	public IntervalChangeKey(IntervalTree T, IntervalNode v, int value) {
		super(T);
		this.v = v;
		this.value = value;
		setHeader("changekey");
		//addStep("changekeyv");
	}
	
	
	@Override
	public void run() {
		v.key = value;
		if (v.key < 1)
			v.key = 1;
		if (v.key > InputField.MAX)
			v.key = InputField.MAX;
		
		//if (H.minHeap) { //<<------
		addStep("intervalchangev");
		v.mark();
		mysuspend();
		v.unmark();
		v = v.getParent();
		adjustValues(v);
		addNote("done");
	}


}
