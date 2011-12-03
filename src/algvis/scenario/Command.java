package algvis.scenario;

public interface Command extends XMLable {

	public void execute();

	public void unexecute();
}
