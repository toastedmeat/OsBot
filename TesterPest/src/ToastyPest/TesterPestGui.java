/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToastyPest;

/**
 *
 * @author Eric
 */
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TesterPestGui extends JFrame {
    
    public static JFrame frame;
    public static JPanel panelSettings;
    public static JPanel panelPrayer;
    public static JPanel panelDebug;
    public static JTabbedPane tabbedPane;
    
    //Main panel
    public static JLabel lblNewLabel = new JLabel("Boat");
    public static JLabel lblPaint = new JLabel("Paint");
    public static JLabel lblWeaponSpeed = new JLabel("Weapon Speed");
    public static JLabel lblItemsToBuy = new JLabel("Item to buy"); 
    //public static JLabel lblDoors = new JLabel("Door Opening");
    
    public static JTextField textWeaponSpeed;
    
    private static ButtonGroup buttonGroup = new ButtonGroup();
    private static ButtonGroup buttonGroup_1 = new ButtonGroup();
    private static ButtonGroup buttonGroup_2 = new ButtonGroup();
    private static ButtonGroup buttonGroup_3 = new ButtonGroup();
    private static ButtonGroup buttonGroup_4 = new ButtonGroup();
    private static ButtonGroup buttonGroup_5 = new ButtonGroup();
    
    public static JRadioButton rdbtnNovice = new JRadioButton("Novice");
    public static JRadioButton rdbtnMedium = new JRadioButton("Medium");
    public static JRadioButton rdbtnHard = new JRadioButton("Hard");
    
    public static JRadioButton rdbtnShowPaint = new JRadioButton("Show");
    public static JRadioButton rdbtnHidePaint = new JRadioButton("Hide");
    
    public static JRadioButton rdbtnBody = new JRadioButton("Body");
    public static JRadioButton rdbtnLegs = new JRadioButton("Legs");
    public static JRadioButton rdbtnGloves = new JRadioButton("Gloves");
    public static JRadioButton rdbtnRHelm = new JRadioButton("Range Helm");
    public static JRadioButton rdbtnMeleeHelm = new JRadioButton("Melee Helm");
    public static JRadioButton rdbtnMageHelm = new JRadioButton("Mage Helm");
    
    public static JRadioButton rdbtnAtt = new JRadioButton("Att Exp");
    public static JRadioButton rdbtnStr = new JRadioButton("Str Exp");
    public static JRadioButton rdbtnDef = new JRadioButton("Def Exp");
    public static JRadioButton rdbtnRng = new JRadioButton("Rng Exp");
    public static JRadioButton rdbtnMage = new JRadioButton("Magic Exp");
    public static JRadioButton rdbtnHP = new JRadioButton("Hp Exp");
    
    public static JCheckBox chkboxDontBuy = new JCheckBox("Dont Buy!?");
    
    //public static JRadioButton rdbtnDoor1 = new JRadioButton("Method 1");
    //public static JRadioButton rdbtnDoor2 = new JRadioButton("Method 2");
    
    public static JCheckBox chkboxPureMode = new JCheckBox("Pure mode");
    
    public static JButton btnStart = new JButton("Start/Hide GUI");  
    public static JButton btnUpdate = new JButton("Update Settings");  
    
    //prayer panel
    public static JCheckBox chkboxPrayer = new JCheckBox("Using Prayer?");
    public static JRadioButton rdbtnStr1 = new JRadioButton("Burst of Str");
    public static JRadioButton rdbtnAtt1 = new JRadioButton("Clarity of Thought");
    public static JRadioButton rdbtnStr2 = new JRadioButton("Superhuman Str");
    public static JRadioButton rdbtnAtt2 = new JRadioButton("Improved Reflex"); 
    public static JRadioButton rdbtnStr3 = new JRadioButton("Ultimate Str"); 
    public static JRadioButton rdbtnAtt3 = new JRadioButton("Incredible Reflex");
    
    
    public static JButton btnUpdatePrayer = new JButton("Update Prayer");  
    public static JButton btnStartPrayer = new JButton("Start/Hide GUI");
    
    
    
    //Debug Panel    
    public static JCheckBox chkboxStarted = new JCheckBox("Started?");
    
    public static JButton btnUpdateDebug = new JButton("Update Debug");  
    public static JButton btnStartDebug = new JButton("Start/Hide GUI");
    public static JButton btnReset = new JButton("Reset Paint");
    
    // end
    private static boolean initialized = false;

    public static void buildGUI() {
        if(initialized){
            frame.setTitle("Toasty Pest");
            return;
        }else{
            initialized = true;
        }            
        frame = new JFrame("Toasty Pest Control");
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        panelSettings = new JPanel();
        panelSettings.setLayout(null);
        tabbedPane.addTab("Settings", null, panelSettings, null);
        
        panelPrayer = new JPanel();
        panelPrayer.setLayout(null);
        tabbedPane.addTab("Prayer", null, panelPrayer, null);
        
        panelDebug = new JPanel();
        panelDebug.setLayout(null);
        tabbedPane.addTab("Debug", null, panelDebug, null);

        
        // panel Settings starts here
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setBounds(21, 12, 46, 14);
        panelSettings.add(lblNewLabel);

        rdbtnNovice.setSelected(true);
        buttonGroup.add(rdbtnNovice);
        rdbtnNovice.setBounds(6, 33, 109, 23);
        panelSettings.add(rdbtnNovice);

        buttonGroup.add(rdbtnMedium);
        rdbtnMedium.setBounds(6, 59, 109, 23);
        panelSettings.add(rdbtnMedium);

        buttonGroup.add(rdbtnHard);
        rdbtnHard.setBounds(6, 85, 109, 23);
        panelSettings.add(rdbtnHard);

        lblWeaponSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblWeaponSpeed.setBounds(10, 115, 120, 14);
        panelSettings.add(lblWeaponSpeed);
        
        textWeaponSpeed = new JTextField();
        textWeaponSpeed.setText("1.5");
        textWeaponSpeed.setToolTipText("This is the time to wait before checking "
                + "\nif your still attacking/need a new target "
                + "\nif other conditions are not fulfilled."
                + "\n This time is in seconds");
        
        textWeaponSpeed.setBounds(6, 135, 105, 20);
        panelSettings.add(textWeaponSpeed);
        textWeaponSpeed.setColumns(10);
        
        /*
        lblDoors.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDoors.setBounds(10, 160, 120, 20);
        panelSettings.add(lblDoors);
        
        buttonGroup_5.add(rdbtnDoor1);
        rdbtnDoor1.setBounds(10, 185, 75, 23);
        rdbtnDoor1.setSelected(true);
        rdbtnDoor1.setToolTipText("This one assumes theirs always a door in the way.");
        panelSettings.add(rdbtnDoor1);

        buttonGroup_5.add(rdbtnDoor2);
        rdbtnDoor2.setBounds(10, 205, 90, 23);
        rdbtnDoor1.setToolTipText("This one waits to find a door Id, Matches it "
                + "to a open or closed door and chooses the respective action to preform.");
        panelSettings.add(rdbtnDoor2);
        */

        lblPaint.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPaint.setBounds(134, 14, 46, 14);
        panelSettings.add(lblPaint);

        buttonGroup_1.add(rdbtnShowPaint);
        rdbtnShowPaint.setSelected(true);
        rdbtnShowPaint.setBounds(117, 30, 91, 23);
        panelSettings.add(rdbtnShowPaint);

        buttonGroup_1.add(rdbtnHidePaint);
        rdbtnHidePaint.setBounds(117, 50, 91, 23);
        panelSettings.add(rdbtnHidePaint);

        btnUpdate.setBounds(6, 320, 205, 35);
        panelSettings.add(btnUpdate);
        
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStart.setBounds(6, 360, 205, 35);
        panelSettings.add(btnStart);
        
        chkboxPureMode.setBounds(117, 80, 97, 23);
        chkboxPureMode.setToolTipText("Check this box if you want to cut and repair barricades for points.");
	panelSettings.add(chkboxPureMode);
        
        chkboxDontBuy.setBounds(117, 80, 97, 23);
        chkboxDontBuy.setToolTipText("Check this box if you don't want to buy items."
                + " \n This will set the script to log off after 250 points.");
	panelSettings.add(chkboxDontBuy);

        lblItemsToBuy.setBounds(6, 170, 95, 14);
        panelSettings.add(lblItemsToBuy);

        buttonGroup_2.add(rdbtnBody);
        rdbtnBody.setSelected(true);
        rdbtnBody.setBounds(6, 190, 75, 23);
        panelSettings.add(rdbtnBody);

        buttonGroup_2.add(rdbtnLegs);
        rdbtnLegs.setBounds(6, 210, 75, 23);
        panelSettings.add(rdbtnLegs);

        buttonGroup_2.add(rdbtnGloves);
        rdbtnGloves.setBounds(6, 230, 75, 23);
        panelSettings.add(rdbtnGloves);

        buttonGroup_2.add(rdbtnRHelm);
        rdbtnRHelm.setBounds(6, 250, 90, 23);
        panelSettings.add(rdbtnRHelm);
        
        buttonGroup_2.add(rdbtnMeleeHelm);
        rdbtnMeleeHelm.setBounds(6, 270, 90, 23);
        panelSettings.add(rdbtnMeleeHelm);

        buttonGroup_2.add(rdbtnMageHelm);
        rdbtnMageHelm.setBounds(117, 150, 90, 23);
        panelSettings.add(rdbtnMageHelm);
        
        buttonGroup_2.add(rdbtnAtt);
        rdbtnAtt.setBounds(117, 170, 90, 23);
        panelSettings.add(rdbtnAtt);
        
        buttonGroup_2.add(rdbtnStr);
        rdbtnStr.setBounds(117, 190, 90, 23);
        panelSettings.add(rdbtnStr);
        
        buttonGroup_2.add(rdbtnDef);
        rdbtnDef.setBounds(117, 210, 90, 23);
        panelSettings.add(rdbtnDef);
        
        buttonGroup_2.add(rdbtnRng);
        rdbtnRng.setBounds(117, 230, 90, 23);
        panelSettings.add(rdbtnRng);
        
        buttonGroup_2.add(rdbtnMage);
        rdbtnMage.setBounds(117, 250, 90, 23);
        panelSettings.add(rdbtnMage);
        
        buttonGroup_2.add(rdbtnMage);
        rdbtnMage.setBounds(117, 270, 90, 23);
        panelSettings.add(rdbtnMage);
        
        
       //Prayer Panel
        chkboxPrayer.setBounds(6, 0, 186, 23);
	panelPrayer.add(chkboxPrayer);
        
        buttonGroup_3.add(rdbtnStr1);
        rdbtnStr1.setBounds(6, 20, 186, 23);
	panelPrayer.add(rdbtnStr1);
        
        buttonGroup_3.add(rdbtnStr2);
        rdbtnStr2.setBounds(6, 40, 186, 23);
	panelPrayer.add(rdbtnStr2);
        
        buttonGroup_3.add(rdbtnStr3);
        rdbtnStr3.setBounds(6, 60, 186, 23);
	panelPrayer.add(rdbtnStr3);
        
        buttonGroup_4.add(rdbtnAtt1);
        rdbtnAtt1.setBounds(6, 80, 186, 23);
	panelPrayer.add(rdbtnAtt1);
        
        buttonGroup_4.add(rdbtnAtt2);
        rdbtnAtt2.setBounds(6, 100, 186, 23);
	panelPrayer.add(rdbtnAtt2);
        
        buttonGroup_4.add(rdbtnAtt3);
        rdbtnAtt3.setBounds(6, 120, 186, 23);
	panelPrayer.add(rdbtnAtt3);
        
        
        btnUpdatePrayer.setBounds(6, 320, 205, 35);
        panelPrayer.add(btnUpdatePrayer);
        
        btnStartPrayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStartPrayer.setBounds(6, 360, 205, 35);
        panelPrayer.add(btnStartPrayer);
        
        //Panel Debug starts here
            chkboxStarted.setBounds(6, 0, 186, 23);
	//panelDebug.add(chkboxStarted);
        // buttons
        btnReset.setBounds(6, 230, 205, 35);
        panelDebug.add(btnReset);
        
        /*btnUpdateDebug.setBounds(6, 270, 205, 35);
        panelDebug.add(btnUpdateDebug);*/
        
        btnStartDebug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
            }
        });
        btnStartDebug.setBounds(6, 360, 205, 35);
        panelDebug.add(btnStartDebug);
        
        
        
        
        
    }
    public static void CloseGUI() {
		frame.setVisible(false);
		frame.dispose();
                panelSettings = null;
                panelPrayer = null;
                panelDebug = null;
		frame = null;
                tabbedPane = null;
		initialized = false;
	}
}
