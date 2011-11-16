package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public abstract class Scenario<T extends XMLable> implements XMLable {
	protected Vector<T> scenario;
	protected int position;
	protected String name;
	protected boolean canAdd; // when traverse scenario backwards, scenario can
								// call methods, which call scenario.add();

	public Scenario(String name) {
		scenario = new Vector<T>();
		position = -1;
		this.name = name;
		canAdd = true;
	}

	public void add(T item) {
		if (canAdd) {
			scenario.add(item);
			++position;
		}
	}

	public void removeLast() {
		scenario.setSize(position--);
	}

	public int length() {
		return scenario.size();
	}

	@Override
	public Element getXML() {
		Element root = new Element(name);
		for (int i = 0; i < length(); ++i) {
			root.addContent(scenario.elementAt(i).getXML());
		}
		return root;
	}

	public void saveXML(String path) {
		Document doc = new Document(getXML());
		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(path));
			outp.output(doc, outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void previous();

	public abstract void next();

	public abstract boolean hasPrevious();

	public abstract boolean hasNext();
}