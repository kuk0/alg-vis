package algvis.scenario;

/**
 * Scenario of Algorithm-s, which are executed on some DataStructure.
 */
public class DSScenario extends Scenario<AlgorithmScenario> {

	public DSScenario(String name) {
		super(name);
	}

	@Override
	public void previous() {
		scenario.elementAt(position).previous();
	}

	@Override
	public void next() {
		scenario.elementAt(position).next();
	}

	@Override
	public boolean hasPrevious() {
		return scenario.elementAt(position).hasPrevious();
	}

	@Override
	public boolean hasNext() {
		return scenario.elementAt(position).hasNext();
	}
}
