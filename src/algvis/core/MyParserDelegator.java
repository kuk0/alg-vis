package algvis.core;

import javax.swing.text.html.parser.ParserDelegator;

public class MyParserDelegator extends ParserDelegator {
    private static final long serialVersionUID = -2997009746844410526L;

    public static void workaround() {
        setDefaultDTD();
    }
}
