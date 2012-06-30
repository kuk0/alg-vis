package algvis.linkcuttree;

public class Cut extends LinkCutAlg {
	LinkCutDSNode v;

	public Cut(LinkCutDS D, LinkCutDSNode v) {
		super(D);
		this.v = v;
	}
	
	public void run() {
		if (v == null) { return; }
		cut(v);
	}

}
