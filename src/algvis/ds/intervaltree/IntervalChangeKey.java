package algvis.ds.intervaltree;

import algvis.ui.InputField;

import java.util.HashMap;

public class IntervalChangeKey extends IntervalAlg{
	private final int value;

	public IntervalChangeKey(IntervalTree T, IntervalNode v, int value) {
		super(T);
		this.v = v;
		this.value = value;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("changekey");
		//addStep("changekeyv");
		
		v.setKey(value);
		if (v.getKey() < 1)
			v.setKey(1);
		if (v.getKey() > InputField.MAX)
			v.setKey(InputField.MAX);

		//if (H.minHeap) { //<<------
		addNote("intervalchangev");
		v.mark();
		pause();
		v.unmark();
		v = v.getParent();
		adjustValues(v);
		addNote("done");
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
