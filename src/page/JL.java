package page;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import common.util.lang.LocaleCenter.Binder;

public class JL extends JLabel implements LocComp {

	private static final long serialVersionUID = 1L;

	private final LocSubComp lsc;

	public JL() {
		lsc = new LocSubComp(this);
		setBorder(BorderFactory.createEtchedBorder());
	}

	public JL(int i, String str) {
		this();
		lsc.init(i, str);
	}

	public JL(String str) {
		this(-1, str);
	}

	public JL(Binder binder) {
		this();
		lsc.init(binder);
	}

	@Override
	public LocSubComp getLSC() {
		return lsc;
	}

}
