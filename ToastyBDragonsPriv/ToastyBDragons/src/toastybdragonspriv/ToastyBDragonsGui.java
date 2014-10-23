/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toastybdragonspriv;

/**
 *
 * @author Eric
 */
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class ToastyBDragonsGui extends JFrame {
    
    public static JFrame frame;
    public static JPanel panelSettings;
    public static JPanel panelPotions;
    public static JPanel panelDebug;
    public static JTabbedPane tabbedPane;
    
    //Main panel
    public static JLabel lblNewLabel = new JLabel("Food");
    public static JLabel lblAmount = new JLabel("Amount");
    public static JLabel lblPots = new JLabel("Pots");
    public static JLabel lblBoltType = new JLabel("Bolts");
    public static JLabel lblShowhide = new JLabel("Show/Hide"); 
    public static JTextField textAmount;
    public static JTextField textBoltsToLoot;
    private static ButtonGroup buttonGroup = new ButtonGroup();
    private static ButtonGroup buttonGroup_1 = new ButtonGroup();
    private static ButtonGroup buttonGroup_2 = new ButtonGroup();
    private static ButtonGroup buttonGroup_3 = new ButtonGroup();
    private static ButtonGroup buttonGroup_4 = new ButtonGroup();
    public static JRadioButton rdbtnLobster = new JRadioButton("Lobster");
    public static JRadioButton rdbtnMonkfish = new JRadioButton("Monkfish");
    public static JRadioButton rdbtnShark = new JRadioButton("Shark");    
    public static JRadioButton rdbtnPotsThree = new JRadioButton("Pots (3)");
    public static JRadioButton rdbtnPotsFour = new JRadioButton("Pots (4)");
    public static JRadioButton rdbtnShowExp = new JRadioButton("Show EXP");
    public static JRadioButton rdbtnHideExp = new JRadioButton("Hide EXP");
    public static JRadioButton rdbtnShowRng = new JRadioButton("Show RNG");
    public static JRadioButton rdbtnHideRng = new JRadioButton("Hide RNG");
    public static JRadioButton rdbtnShowGp = new JRadioButton("Show GP");
    public static JRadioButton rdbtnHideGp = new JRadioButton("Hide GP");
    public static JButton btnStart = new JButton("Start/Hide GUI");  
    public static JButton btnUpdate = new JButton("Update Settings");   
    public static JCheckBox chkboxisRanging = new JCheckBox("Ranging?");
    //Potions Panel
    public static JCheckBox chkboxStrPot = new JCheckBox("Use Super strength");
    public static JCheckBox chkboxAttPot = new JCheckBox("Use Super attack");
    public static JCheckBox chkboxDefPot = new JCheckBox("Use Super defence");
    public static JCheckBox chkboxRngPot = new JCheckBox("Use Ranging potion");
    public static JButton btnUpdatePot = new JButton("Update Potions");  
    public static JButton btnStartPot = new JButton("Start/Hide GUI");
    //Debug Panel    
    public static JCheckBox chkboxItemsReady = new JCheckBox("Items ready");
    public static JCheckBox chkboxShieldReady = new JCheckBox("Shields Equiped");
    public static JCheckBox chkboxRingReady = new JCheckBox("Rings Equiped");
    public static JCheckBox chkboxDoneBanking = new JCheckBox("Done banking"); 
    public static JCheckBox chkboxOpenedDoor = new JCheckBox("Opened door"); 
    public static JCheckBox chkboxWalkedToGate = new JCheckBox("Walked to gate"); 
    public static JCheckBox chkboxOpenedGate = new JCheckBox("Opened gate"); 
    public static JCheckBox chkboxWalkedToEnt = new JCheckBox("Walked to cave entrance"); 
    public static JCheckBox chkboxEnteredCaveArea = new JCheckBox("Enter the cave"); 
    public static JCheckBox chkboxAtDragons = new JCheckBox("Walked to Dragons");
    public static JCheckBox chkboxIsFighting = new JCheckBox("Is fighting");
    public static JButton btnUpdateDebug = new JButton("Update Debug");  
    public static JButton btnStartDebug = new JButton("Start/Hide GUI");
    public static JButton btnReset = new JButton("Reset Paint");
    
    
    // end
    private static boolean initialized = false;

    public static void buildGUI() {
        if(initialized){
            frame.setTitle("Toasty BDragonsK");
            return;
        }else{
            initialized = true;
        }            
        frame = new JFrame("Toasty BDragons");
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        panelSettings = new JPanel();
        panelSettings.setLayout(null);
        tabbedPane.addTab("Settings", null, panelSettings, null);
        
        panelPotions = new JPanel();
        panelPotions.setLayout(null);
        tabbedPane.addTab("Potions", null, panelPotions, null);
        
        panelDebug = new JPanel();
        panelDebug.setLayout(null);
        tabbedPane.addTab("Debug", null, panelDebug, null);

        
        // panel Settings starts here
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setBounds(21, 12, 46, 14);
        panelSettings.add(lblNewLabel);

        rdbtnLobster.setSelected(true);
        buttonGroup.add(rdbtnLobster);
        rdbtnLobster.setBounds(6, 33, 109, 23);
        panelSettings.add(rdbtnLobster);

        buttonGroup.add(rdbtnMonkfish);
        rdbtnMonkfish.setBounds(6, 59, 109, 23);
        panelSettings.add(rdbtnMonkfish);

        buttonGroup.add(rdbtnShark);
        rdbtnShark.setBounds(6, 85, 109, 23);
        panelSettings.add(rdbtnShark);

        lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblAmount.setBounds(10, 115, 65, 14);
        panelSettings.add(lblAmount);

        textAmount = new JTextField();
        textAmount.setText("4");
        textAmount.setBounds(6, 135, 86, 20);
        panelSettings.add(textAmount);
        textAmount.setColumns(10);
        
        lblBoltType.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblBoltType.setBounds(10, 165, 75, 14);
        panelSettings.add(lblBoltType);
        
        textBoltsToLoot = new JTextField();
        textBoltsToLoot.setText("Mithril bolts");
        textBoltsToLoot.setBounds(6, 185, 105, 20);
        panelSettings.add(textBoltsToLoot);
        textBoltsToLoot.setColumns(10);

        lblPots.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPots.setBounds(134, 14, 46, 14);
        panelSettings.add(lblPots);

        buttonGroup_1.add(rdbtnPotsThree);
        rdbtnPotsThree.setSelected(true);
        rdbtnPotsThree.setBounds(117, 33, 91, 23);
        panelSettings.add(rdbtnPotsThree);

        buttonGroup_1.add(rdbtnPotsFour);
        rdbtnPotsFour.setBounds(117, 59, 91, 23);
        panelSettings.add(rdbtnPotsFour);

        btnUpdate.setBounds(6, 270, 205, 35);
        panelSettings.add(btnUpdate);
        
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStart.setBounds(6, 310, 205, 35);
        panelSettings.add(btnStart);
        
        chkboxisRanging.setBounds(6, 240, 97, 23);
        chkboxisRanging.setSelected(true);
	panelSettings.add(chkboxisRanging);

        lblShowhide.setBounds(127, 89, 77, 14);
        panelSettings.add(lblShowhide);

        buttonGroup_3.add(rdbtnShowExp);
        rdbtnShowExp.setBounds(117, 110, 75, 23);
        panelSettings.add(rdbtnShowExp);

        buttonGroup_3.add(rdbtnHideExp);
        rdbtnHideExp.setSelected(true);
        rdbtnHideExp.setBounds(117, 136, 75, 23);
        panelSettings.add(rdbtnHideExp);

        buttonGroup_2.add(rdbtnShowGp);
        rdbtnShowGp.setBounds(117, 162, 75, 23);
        panelSettings.add(rdbtnShowGp);

        buttonGroup_2.add(rdbtnHideGp);
        rdbtnHideGp.setSelected(true);
        rdbtnHideGp.setBounds(117, 188, 75, 23);
        panelSettings.add(rdbtnHideGp);
        
        buttonGroup_4.add(rdbtnShowRng);
        rdbtnShowRng.setBounds(117, 214, 80, 23);
        panelSettings.add(rdbtnShowRng);

        buttonGroup_4.add(rdbtnHideRng);
        rdbtnHideRng.setSelected(true);
        rdbtnHideRng.setBounds(117, 240, 75, 23);
        panelSettings.add(rdbtnHideRng);
        //Panel Potions 
        chkboxStrPot.setBounds(6, 0, 186, 23);
        chkboxStrPot.setSelected(true);
	panelPotions.add(chkboxStrPot);
        
        chkboxAttPot.setBounds(6, 20, 186, 23);
        chkboxAttPot.setSelected(true);
	panelPotions.add(chkboxAttPot);
        
        chkboxDefPot.setBounds(6, 40, 186, 23);
        chkboxDefPot.setSelected(true);
	panelPotions.add(chkboxDefPot);
        
        chkboxRngPot.setBounds(6, 60, 186, 23);
        chkboxRngPot.setSelected(true);
	panelPotions.add(chkboxRngPot);
        
        btnUpdatePot.setBounds(6, 270, 205, 35);
        panelPotions.add(btnUpdatePot);
        
        btnStartPot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStartPot.setBounds(6, 310, 205, 35);
        panelPotions.add(btnStartPot);
        
        
        //Panel Debug starts here
             
        chkboxShieldReady.setBounds(6, 0, 186, 23);
	panelDebug.add(chkboxShieldReady);
        
        chkboxRingReady.setBounds(6, 20, 186, 23);
	panelDebug.add(chkboxRingReady);
        
        chkboxDoneBanking.setBounds(6, 40, 186, 23);
	panelDebug.add(chkboxDoneBanking);
        
        chkboxItemsReady.setBounds(6, 60, 186, 23);
	panelDebug.add(chkboxItemsReady);
        
        chkboxOpenedDoor.setBounds(6, 80, 186, 23);
	panelDebug.add(chkboxOpenedDoor);
        
        chkboxWalkedToGate.setBounds(6, 100, 186, 23);
	panelDebug.add(chkboxWalkedToGate);
        
        chkboxOpenedGate.setBounds(6, 120, 186, 23);
	panelDebug.add(chkboxOpenedGate);
        
        chkboxWalkedToEnt.setBounds(6, 140, 186, 23);
	panelDebug.add(chkboxWalkedToEnt);
        
        chkboxEnteredCaveArea.setBounds(6, 160, 186, 23);
	panelDebug.add(chkboxEnteredCaveArea);
        
        chkboxIsFighting.setBounds(6, 200, 186, 23);
	panelDebug.add(chkboxIsFighting);
        
        
        // buttons
        btnReset.setBounds(6, 230, 205, 35);
        panelDebug.add(btnReset);
        
        btnUpdateDebug.setBounds(6, 270, 205, 35);
        panelDebug.add(btnUpdateDebug);
        
        btnStartDebug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStartDebug.setBounds(6, 310, 205, 35);
        panelDebug.add(btnStartDebug);
        
        
        
        
        
    }
    public static void CloseGUI() {
		frame.setVisible(false);
		frame.dispose();
                panelSettings = null;
                panelDebug = null;
		frame = null;
                tabbedPane = null;
		initialized = false;
	}
}
