package algvis.scenario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

// TODO add some comments
public class Scenario implements IScenario<SubScenario> {
	private Vector<SubScenario> scenario;
	private int position;
	public SubScenario current;
	private static final String name = "scenario"; // this will be (maybe) the
													// name of the DS

	public Scenario() {
		scenario = new Vector<SubScenario>();
		position = -1;
	}

	public void add(SubScenario ss) {
		// if (scenario.size()-1 > position) scenario.setSize(position);
		scenario.add(ss);
		++position;
	}

	public boolean previous() {
		if (position == -1)
			return false;
		else
			return scenario.elementAt(position).previous();
	}

	public boolean next() {
		if (position == -1)
			return false;
		else
			return scenario.elementAt(position).next();
	}

	public int getPosition() {
		return position;
	}

	@Override
	public int length() {
		return scenario.size();
	}

	@Override
	public Element getXML() {
		Element root = new Element("scenario");
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

	@Override
	public String name() {
		return name;
	}

}
