package algvis.scenario;

public class AlgorithmScenario extends Scenario<Command> {
	public AlgorithmScenario(String name) {
		super(name);
	}

	@Override
	public void previous() {
		canAdd = false;
		scenario.elementAt(position).unexecute();
		while (--position > 0
				&& !(scenario.elementAt(position) instanceof PauseCommand)) {
			scenario.elementAt(position).unexecute();
		}
		canAdd = true;
	}

	@Override
	public void next() {
		canAdd = false;
		++position;
		do {
			scenario.elementAt(position).execute();
			++position;
		} while (position < length()
				&& !(scenario.elementAt(position) instanceof PauseCommand));
		if (position == length()) {
			--position;
		}
		canAdd = true;
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
