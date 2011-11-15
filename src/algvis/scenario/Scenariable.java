package algvis.scenario;

import org.jdom.Element;

public interface Scenariable<T> {
	public void add(T t);

	public boolean previous();

	public boolean next();

	public int length();

	public Element getXML();

	public String name();
}
