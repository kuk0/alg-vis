package algvis.internationalization;

import algvis.core.StringUtils;

public class IParamString implements Stringable {
    String s;
    String[] param;

    public IParamString(String s, String... par) {
        this.s = s;
        this.param = par;
    }

    @Override
    public String getString() {
        return StringUtils.subst(Languages.getString(s), param);
    }
}
