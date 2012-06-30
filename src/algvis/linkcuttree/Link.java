package algvis.linkcuttree;

public class Link extends LinkCutAlg {
	LinkCutDS D;
	LinkCutDSNode v, w;
	
	public Link(LinkCutDS D, LinkCutDSNode v, LinkCutDSNode w) {
		super(D);
		this.D = D;
		this.v = v;
		this.w = w;
	}

	@Override
	public void run() {
		if (v==null || w==null) {return;}
		link(v,w);
	}
}
