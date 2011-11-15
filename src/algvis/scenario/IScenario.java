package algvis.scenario;

import org.jdom.Element;

public interface IScenario<T> {
	public void add(T t);

	public boolean previous();

	public boolean next();

	public int getPosition(); // maybe don't need this

	public int length();

	public Element getXML();

	public String name();
}
