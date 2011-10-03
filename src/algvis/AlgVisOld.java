package algvis;


public class AlgVisOld { // extends JPanel implements ActionListener {
/*	private static final long serialVersionUID = -5202486006824196688L;
	String lang;

	int current_lang;
	Locale[] all_locales = new Locale[2];
	ResourceBundle[] all_msgs = new ResourceBundle[2];
	Locale locale;
	ResourceBundle msg;
	ChButton b;
	ADTTabs tabs;

	public String getString(String s) {
		return all_msgs[current_lang].getString(s);
	}

	public void init() {
		// Internationalization
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages_sk", all_locales[1]);

		lang = "en"; // getParameter("lang");
		if (lang.equals("sk")) {
			current_lang = 1;
			b = new ChButton("english");
		} else {
			current_lang = 0;
			b = new ChButton("slovensky");
		}
		b.refresh();
		locale = all_locales[current_lang];
		msg = all_msgs[current_lang];

		tabs = new ADTTabs(this);
		DSTabs dstabs = new DSTabs(this, "dictionaries");

		dstabs.addTab(new BSTPanel(this));
		dstabs.addTab(new RotPanel(this));
		dstabs.addTab(new AVLPanel(this));
		dstabs.addTab(new BPanel(this));
		dstabs.addTab(new RBPanel(this));
		dstabs.addTab(new AAPanel(this));
		dstabs.addTab(new TreapPanel(this));
		dstabs.addTab(new SkipListPanel(this));
		dstabs.addTab(new GBPanel(this));
		dstabs.addTab(new SplayPanel(this));
		tabs.addTab(dstabs);

		dstabs = new DSTabs(this, "priorityqueues");

		dstabs.addTab(new HeapPanel(this));
		dstabs.addTab(new BinHeapPanel(this));
		dstabs.addTab(new LazyBinHeapPanel(this));
		tabs.addTab(dstabs);

		// abs.setMnemonicAt(0, KeyEvent.VK_B);
		// getContentPane().setLayout(new BoxLayout(getContentPane(),
		// BoxLayout.Y_AXIS));
		add(tabs); // BorderLayout.CENTER);

		// b.setMaximumSize(new Dimension (200, 30));
		b.setActionCommand("lang");
		b.addActionListener(this);
		// b.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(Box.createHorizontalGlue());
		add(b);

		Fonts.init(this);
	}

	public void refresh() {
		tabs.refresh();
	}

	public void actionPerformed(ActionEvent e) {
		if ("lang".equals(e.getActionCommand())) {
			current_lang = 1 - current_lang;
			if (current_lang == 0) {
				b.setText("slovensky");
			} else if (current_lang == 1) {
				b.setText("english");
			}
			b.refresh();
			locale = all_locales[current_lang];
			msg = all_msgs[current_lang];
			refresh();
		}
	}*/
}

/*
 * public void keyTyped(KeyEvent e) {} public void keyReleased(KeyEvent e) {}
 * public void keyPressed(KeyEvent e) { char key = e.getKeyChar();
 * System.out.println ("ha: "+key); switch (key) { case '+': View.zoomIn();
 * break; case '-': View.zoomOut(); break; case 'a': View.moveLeft(); break;
 * case 'd': View.moveRight(); break; case 'w': View.moveUp(); break; case 's':
 * View.moveDown(); break; } }
 */
