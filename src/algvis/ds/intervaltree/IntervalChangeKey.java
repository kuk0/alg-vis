package algvis.ds.intervaltree;

import algvis.gui.InputField;

import java.util.HashMap;

public class IntervalChangeKey extends IntervalAlg{
	private final int value;

	public IntervalChangeKey(IntervalTree T, IntervalNode v, int value) {
		super(T);
		this.v = v;
		this.value = value;
		setHeader("changekey");
		//addStep("changekeyv");
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
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
