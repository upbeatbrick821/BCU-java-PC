package page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import event.EventBase;
import event.EventReader;
import io.BCMusic;
import io.Reader;
import main.MainBCU;
import page.view.ViewBox;
import util.ImgCore;

public class ConfigPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN(0, "back");
	private final JBTN filt = new JBTN(0, "filter" + MainBCU.FILTER_TYPE);
	private final JBTN rlla = new JBTN(0, "rllang");
	private final JTG prel = new JTG(0, "preload");
	private final JTG whit = new JTG(0, "white");
	private final JTG refe = new JTG(0, "axis");
	private final JTG musc = new JTG(0, "musc");
	private final JTG exla = new JTG(0, "exlang");
	private final JTG extt = new JTG(0, "extip");
	private final JBTN[] left = new JBTN[4];
	private final JBTN[] right = new JBTN[4];
	private final JLabel[] name = new JLabel[4];
	private final JLabel[] vals = new JLabel[4];
	private final JLabel jlmin = new JLabel(get("opamin"));
	private final JLabel jlmax = new JLabel(get("opamax"));
	private final JSlider jsmin = new JSlider(0, 100);
	private final JSlider jsmax = new JSlider(0, 100);
	private final JList<String> jls = new JList<>(MainLocale.strs);
	private final JScrollPane jsps = new JScrollPane(jls);
	private final JList<String> jll = new JList<>(EventReader.LOC_NAME);
	private final JScrollPane jspl = new JScrollPane(jll);

	private boolean changing = false;

	protected ConfigPage(Page p) {
		super(p);

		ini();
		resized();
	}

	@Override
	protected void renew() {
		jlmin.setText(get("opamin"));
		jlmax.setText(get("opamax"));
		for (int i = 0; i < 4; i++) {
			name[i].setText(get(ImgCore.NAME[i]));
			vals[i].setText(get(ImgCore.VAL[ImgCore.ints[i]]));
		}
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(back, x, y, 0, 0, 200, 50);
		set(prel, x, y, 50, 200, 200, 50);
		set(whit, x, y, 50, 300, 200, 50);
		set(refe, x, y, 50, 400, 200, 50);
		for (int i = 0; i < 4; i++) {
			set(name[i], x, y, 300, 100 + i * 100, 200, 50);
			set(left[i], x, y, 550, 100 + i * 100, 100, 50);
			set(vals[i], x, y, 650, 100 + i * 100, 200, 50);
			set(right[i], x, y, 850, 100 + i * 100, 100, 50);
		}
		set(jsps, x, y, 1100, 100, 200, 400);
		set(jsmin, x, y, 50, 550, 1000, 50);
		set(jsmax, x, y, 50, 650, 1000, 50);
		set(jlmin, x, y, 50, 500, 400, 50);
		set(jlmax, x, y, 50, 600, 400, 50);
		set(filt, x, y, 1100, 550, 200, 50);
		set(jspl, x, y, 1350, 100, 200, 400);
		set(musc, x, y, 1350, 550, 200, 50);
		set(exla, x, y, 1100, 650, 450, 50);
		set(extt, x, y, 1100, 750, 450, 50);
		set(rlla, x, y, 1100, 850, 450, 50);
	}

	private void addListeners() {
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changePanel(getFront());
			}
		});

		prel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainBCU.preload = prel.isSelected();
			}
		});

		exla.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainLocale.exLang = exla.isSelected();
				Page.renewLoc(getThis());
			}
		});

		extt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainLocale.exTTT = extt.isSelected();
				Page.renewLoc(getThis());
			}
		});

		rlla.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Reader.readLang();
				Page.renewLoc(getThis());
			}
		});

		whit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ViewBox.white = whit.isSelected();
			}
		});

		refe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImgCore.ref = refe.isSelected();
			}
		});

		for (int i = 0; i < 4; i++) {
			int I = i;

			left[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ImgCore.ints[I]--;
					vals[I].setText(get(ImgCore.VAL[ImgCore.ints[I]]));
					left[I].setEnabled(ImgCore.ints[I] > 0);
					right[I].setEnabled(ImgCore.ints[I] < 2);
				}

			});

			right[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ImgCore.ints[I]++;
					vals[I].setText(get(ImgCore.VAL[ImgCore.ints[I]]));
					left[I].setEnabled(ImgCore.ints[I] > 0);
					right[I].setEnabled(ImgCore.ints[I] < 2);
				}

			});

			jsmin.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					ImgCore.deadOpa = jsmin.getValue();
				}
			});

			jsmax.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					ImgCore.fullOpa = jsmax.getValue();
				}
			});

		}

		jls.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (changing)
					return;
				changing = true;
				if (jls.getSelectedIndex() == -1) {
					jls.setSelectedIndex(MainLocale.lang);
				}
				MainLocale.lang = jls.getSelectedIndex();
				Page.renewLoc(getThis());
				changing = false;
			}
		});

		filt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainBCU.FILTER_TYPE = 1 - MainBCU.FILTER_TYPE;
				filt.setText(0, "filter" + MainBCU.FILTER_TYPE);
			}
		});

		jll.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				EventReader.loc = jll.getSelectedIndex();
				EventBase.clear();
			}
		});

		musc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BCMusic.play = musc.isSelected();
			}
		});

	}

	private void ini() {
		add(back);
		add(prel);
		add(refe);
		add(whit);
		add(jsps);
		add(jsmin);
		add(jsmax);
		add(jlmin);
		add(jlmax);
		add(filt);
		add(jspl);
		add(musc);
		add(rlla);
		add(exla);
		add(extt);
		jls.setSelectedIndex(MainLocale.lang);
		jll.setSelectedIndex(EventReader.loc);
		jsmin.setMajorTickSpacing(10);
		jsmin.setMinorTickSpacing(5);
		jsmin.setPaintTicks(true);
		jsmin.setPaintLabels(true);
		jsmax.setMajorTickSpacing(10);
		jsmax.setMinorTickSpacing(5);
		jsmax.setPaintTicks(true);
		jsmax.setPaintLabels(true);
		jsmin.setValue(ImgCore.deadOpa);
		jsmax.setValue(ImgCore.fullOpa);
		for (int i = 0; i < 4; i++) {
			left[i] = new JBTN("<");
			right[i] = new JBTN(">");
			name[i] = new JLabel(get(ImgCore.NAME[i]));
			vals[i] = new JLabel(get(ImgCore.VAL[ImgCore.ints[i]]));
			add(left[i]);
			add(right[i]);
			add(name[i]);
			add(vals[i]);
			name[i].setHorizontalAlignment(SwingConstants.CENTER);
			vals[i].setHorizontalAlignment(SwingConstants.CENTER);
			name[i].setBorder(BorderFactory.createEtchedBorder());
			vals[i].setBorder(BorderFactory.createEtchedBorder());
			left[i].setEnabled(ImgCore.ints[i] > 0);
			right[i].setEnabled(ImgCore.ints[i] < 2);
		}
		exla.setSelected(MainLocale.exLang);
		extt.setSelected(MainLocale.exTTT);
		prel.setSelected(MainBCU.preload);
		whit.setSelected(ViewBox.white);
		refe.setSelected(ImgCore.ref);
		musc.setSelected(BCMusic.play);
		addListeners();
	}

}
