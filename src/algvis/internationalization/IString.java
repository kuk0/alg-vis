package algvis.internationalization;

public class IString implements Stringable {
	String s;

	public IString(String s) {
		this.s = s;
	}

	@Override
	public String getString() {
		return Languages.getString(s);
	}
}
