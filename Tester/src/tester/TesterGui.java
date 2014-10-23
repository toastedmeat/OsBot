/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

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
public class TesterGui extends JFrame {
    
    public static JFrame frame;

    public static JPanel panelDebug;
    public static JTabbedPane tabbedPane;
    
   
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

        panelDebug = new JPanel();
        panelDebug.setLayout(null);
        tabbedPane.addTab("Debug", null, panelDebug, null);

        
       
        //Panel Debug starts here

        btnReset.setBounds(6, 10, 205, 35);
        panelDebug.add(btnReset);

        
        
        
        
        
    }
    public static void CloseGUI() {
		frame.setVisible(false);
		frame.dispose();
                panelDebug = null;
		frame = null;
                tabbedPane = null;
		initialized = false;
	}
}
