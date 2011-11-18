package algvis.scenario;

public class AlgorithmScenario extends Scenario<Command> {
	public AlgorithmScenario(String name) {
		super(name);
	}

	@Override
	public void previous() {
		scenario.elementAt(position).unexecute();
		while (--position > -1
				&& !(scenario.elementAt(position) instanceof PauseCommand)) {
			scenario.elementAt(position).unexecute();
		}
	}

	@Override
	public void next() {
		++position;
		do {
			scenario.elementAt(position).execute();
			++position;
		} while (position < length()
				&& !(scenario.elementAt(position) instanceof PauseCommand));
		if (position == length()) {
			--position;
		}
	}

	@Override
	public boolean hasPrevious() {
		if (position == -1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean hasNext() {
		if (position >= length() - 1) {
			return false;
		} else {
			return true;
		}
	}
}
