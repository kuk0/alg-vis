package algvis.scenario.commands;

import algvis.scenario.XMLable;

public interface Command extends XMLable {

	public void execute();

	public void unexecute();
}
