package algvis.internationalization;

import algvis.core.StringUtils;

public class IIntParamString implements Stringable {
    String s;
    int[] param;

    public IIntParamString(String s, int... par) {
        this.s = s;
        this.param = par;
    }

    @Override
    public String getString() {
        return StringUtils.subst(Languages.getString(s), param);
    }
}
