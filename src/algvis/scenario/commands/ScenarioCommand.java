package algvis.scenario.commands;

public class ScenarioCommand extends
		MacroCommand<MacroCommand<MacroCommand<Command>>> {

	public ScenarioCommand(String name) {
		super(name);
	}

	@Override
	public void unexecuteOne() {
		if (current.hasPrevious()) {
			current.unexecuteOne();
			if (!current.hasPrevious() && iterator.previousIndex() == position) {
				iterator.previous();
			}
		} else {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecuteOne();
		}
		if (!current.hasPrevious() && iterator.hasPrevious()) {
			position = iterator.previousIndex();
			current = iterator.previous();
		}
	}

	@Override
	public void executeOne() {
		if (current.hasNext()) {
			current.executeOne();
			if (!current.hasNext() && iterator.nextIndex() == position) {
				iterator.next();
			}
		} else {
			position = iterator.nextIndex();
			current = iterator.next();
			current.executeOne();
		}
	}

	@Override
	public void execute() {
		if (current.hasNext()) {
			current.execute();
			if (!current.hasNext() && iterator.nextIndex() == position) {
				iterator.next();
			}
		} else {
			position = iterator.nextIndex();
			current = iterator.next();
			current.execute();
		}
	}

	@Override
	public void unexecute() {
		if (current.hasPrevious()) {
			current.unexecute();
			if (!current.hasPrevious() && iterator.previousIndex() == position) {
				iterator.previous();
			}
		} else {
			position = iterator.previousIndex();
			current = iterator.previous();
			current.unexecute();
		}
		if (!current.hasPrevious() && iterator.hasPrevious()) {
			position = iterator.previousIndex();
			current = iterator.previous();
		}
	}

}
