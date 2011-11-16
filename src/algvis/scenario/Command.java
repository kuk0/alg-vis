package algvis.scenario;

import org.jdom.Element;

public interface Command extends XMLable {

	public abstract void execute();

	public abstract void unexecute();

	public abstract Element getXML();
}
