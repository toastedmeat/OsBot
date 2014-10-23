package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GUIPaint {

	/*
	 * 
	 * GUI Based Paint
	 * 
	 * Developed by Divine at OSBot.org 100% Loyalty Free, and available for use
	 * to all users.
	 */

	/*
	 * Variables
	 */

	private final static int DEFAULT_MAX_CELLS = 20;
	private final static int DEFAULT_MAX_PANELS = 8;
	private final static int DEFAULT_REPAINT_RATE_IN_MILLISECONDS = 1000;

	private static JLabel[] jlabels;
	private static JPanel[] jpanels;
	private static Border[] borders;
	private static int[] labelPanelIDs;
	private static boolean[] activePanels;

	private static int lastCategory = -1;

	private static boolean initialized = false;
	private static JFrame frame;
	private static int currentLabel = 0, currentPanel = 0, repaintRate;
	private static long paintTimer;

	/*
	 * Initialization
	 */

	public static void buildFrame(String Name, int MaxCells, int MaxPanels, int RepaintRateInMilliseconds) {
		if (initialized) {
			frame.setTitle(Name);
			return;
		} else
			initialized = true;
		repaintRate = RepaintRateInMilliseconds;
		jlabels = new JLabel[MaxCells];
		labelPanelIDs = new int[MaxCells];
		activePanels = new boolean[MaxPanels];
		jpanels = new JPanel[MaxPanels];
		borders = new Border[MaxPanels];
		frame = new JFrame(Name);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		for (int i = 0; i < jlabels.length; i++) {
			jlabels[i] = new JLabel("");
			// jlabels[i].setPreferredSize(new Dimension(340, 20));
		}
		for (int i = 0; i < jpanels.length; i++) {
			jpanels[i] = new JPanel(new GridLayout(0, 1));
			jpanels[i].setPreferredSize(new Dimension(350, 33));
		}
	}

	public static void buildFrame(String Name, int MaxCells, int MaxPanels) {
		buildFrame(Name, MaxCells, MaxPanels, DEFAULT_REPAINT_RATE_IN_MILLISECONDS);
	}

	public static void buildFrame(String Name, int MaxCells) {
		buildFrame(Name, MaxCells, DEFAULT_MAX_PANELS, DEFAULT_REPAINT_RATE_IN_MILLISECONDS);
	}

	public static void buildFrame(String Name) {
		buildFrame(Name, DEFAULT_MAX_CELLS, DEFAULT_MAX_PANELS, DEFAULT_REPAINT_RATE_IN_MILLISECONDS);
	}

	/*
	 * Add to GUI.
	 */

	public static void addCell(String name, String value) {
		if (System.currentTimeMillis() - paintTimer > repaintRate) {
			jlabels[currentLabel].setText(name + ": " + value);
			labelPanelIDs[currentLabel] = currentPanel - 1;
			jpanels[currentPanel - 1].setPreferredSize(new Dimension(350, (currentLabel - lastCategory + 2) * 23));
			activePanels[currentPanel - 1] = true;
			currentLabel++;
		}
	}

	public static void setCategory(String name) {
		if (System.currentTimeMillis() - paintTimer > repaintRate) {
			lastCategory = currentLabel;
			borders[currentPanel] = BorderFactory.createTitledBorder(name);
			jpanels[currentPanel].setBorder(borders[currentPanel]);
			currentPanel++;
		}
	}

	public static void addCell(String name, int value) {
		addCell(name, value + "");
	}

	public static void addCell(String name, double value) {
		addCell(name, value + "");
	}

	public static void addCell(String name, long value) {
		addCell(name, value + "");
	}

	public static void addCell(String name, boolean value) {
		addCell(name, (value ? "True" : "False"));
	}

	/*
	 * Paint + Refresh
	 */

	public static void RefreshPaint() {
		if (System.currentTimeMillis() - paintTimer > repaintRate) {

			for (int i = 0; i < jpanels.length; i++)
				if (activePanels[i])
					frame.add(jpanels[i]);
				else
					frame.remove(jpanels[i]);

			for (int i = 0; i < jlabels.length; i++) {
				if (jlabels[i] == null || i >= currentLabel || jlabels[i].getText() == null
						|| jlabels[i].getText().length() < 1)
					jpanels[labelPanelIDs[i]].remove(jlabels[i]);
				else
					jpanels[labelPanelIDs[i]].add(jlabels[i]);
			}
			if (!frame.isVisible())
				frame.setVisible(true);

			// frame.setSize(365, currentLabel * 25 + (currentPanel * 30));
			frame.pack();
			currentLabel = 0;
			currentPanel = 0;
			for(int i = 0; i < activePanels.length; i++)
				activePanels[i] = false;
			paintTimer = System.currentTimeMillis();
		}
	}

	/*
	 * On Exit
	 */

	public static void CloseGUI() {
		frame.setVisible(false);
		frame.dispose();
		frame = null;
		initialized = false;
	}

}
