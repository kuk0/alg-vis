package algvis.scenario;

import org.jdom.Element;

public interface IScenario<Type> {
	public void add(Type t);

	public boolean previous();

	public boolean next();

	public int getPosition(); // maybe don't need this

	public int length();

	public Element getXML();

	public String name();
}
