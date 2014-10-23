/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static org.osbot.script.MethodProvider.random;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.model.RS2Object;
import org.osbot.script.rs2.skill.Skill;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.EquipmentSlot;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

/**
 *
 *
 * @author Eric
 */
@ScriptManifest(author = "toastedmeat", info = "tester",
        name = "Testing", version = 1)
public class Tester extends Script {

    RectangleDestination exchangeBottom = new RectangleDestination(475, 220, 10, 25);
    RectangleDestination confirm = new RectangleDestination(225, 285, 45, 10);

    final Area castleWarsArea = new Area(2438, 3083, 2444, 3094);
    final Area castleBank = new Area(2442, 3082, 2443, 3084);
    final Area onChest = new Area(2443, 3083, 2443, 3083);
    Area outsideBoat = new Area(2657, 2638, 2657, 2644);
    Area insideBoat = new Area(2660, 2638, 2663, 2643);

    Area outsideBoatMed = new Area(2644, 2642, 2644, 2651);
    Area insideBoatMed = new Area(2638, 2642, 2641, 2647);

    //Paths
    private int[][] toEast, toWest, toSouth, toSW, toSE;
    //portal id's
    private final int westPortal = 2058, eastPortal = 2059,
            sEastPortal = 2060, sWestPortal = 2061;
    //inGame NPC's
    private final int squire = 2085, voidKnight = 2089;
    //doors
    private final int doorO = 14236;
    //players positon
    private int x, y, voidKx, voidKy;
    boolean setPaths, playingPC, wdropped, sedropped, edropped, swdropped, doneBanking;
    private NPC voidK;
    Rectangle hidePaint = new Rectangle(random(10, 300), 462, 60, 12);

    RectangleDestination crude = new RectangleDestination(45, 70, 11, 21);
    Point pointer;
    public Area initialWest;

    Player player;
    Bank bank;
    String[] items = {"Saw", "Hammer", "Teleport to house", "Steel nails",
        "Ring of dueling(8)", "Ring of dueling(7)", "Ring of dueling(6)",
        "Ring of dueling(5)", "Ring of dueling(4)", "Ring of dueling(3)",
        "Ring of dueling(2)", "Ring of dueling(1)"};
    String[] rings = {"Ring of dueling(8)", "Ring of dueling(7)", "Ring of dueling(6)",
        "Ring of dueling(5)", "Ring of dueling(4)", "Ring of dueling(3)",
        "Ring of dueling(2)", "Ring of dueling(1)"};

    BufferedImage image;
    Robot robot;
    Calendar now;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
    String fs = System.getProperty("file.separator");
    String filename = fs + "OSBot" + fs + "scripts" + fs + formatter.format(Calendar.getInstance().getTime()) + ".png";
    Entity portal;
    int chairToBuild = 0, amount = 0, plankToUse = 0;
    private long idleStartTime;
    private long lastMoveTimer;
    private int startLastMove;
    String spaceName, builtName;

    public void onStart() {
        bank = client.getBank();
        player = client.getMyPlayer();

        playingPC = false;
        wdropped = false;
        edropped = false;
        swdropped = false;
        sedropped = false;
        setPaths = false;
        doneBanking = false;

        idleStartTime = System.currentTimeMillis();
        lastMoveTimer = 0;

        chairToBuild += 1;
        amount = +1;
        /*
         spaceName = JOptionPane.showInputDialog("Enter name of space Ex: Chair space", "");
         builtName = JOptionPane.showInputDialog("Enter name of the stucture built Ex: Chair", "");
         String chairToBuildTemp = JOptionPane.showInputDialog("Enter Interface id\n "
         + "Crude wooden chair - 133\n"
         + "Wooden chair - 134\n"
         + "Rocking Chair - 135\n"
         + "Oak chair - 136", "");
         chairToBuild = Integer.parseInt(chairToBuildTemp);
         String tempAmount = JOptionPane.showInputDialog("Enter amount of planks its takes", "");
         amount = Integer.parseInt(tempAmount);
         String plankToUseTemp = JOptionPane.showInputDialog("Enter plank to use\n "
         + "Plank - 960\n"
         + "Oak plank - 8778\n", "");
         plankToUse = Integer.parseInt(plankToUseTemp);
         */
        /*TesterGui.buildGUI();
         TesterGui.frame.setVisible(true);
         TesterGui.frame.setSize(235, 140);
         TesterGui.frame.setLocation(300, 300);
         TesterGui.btnReset.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
         log("i tried");
         now = Calendar.getInstance();

         BufferedImage screenShot = robot.createScreenCapture(new Rectangle(0, 0, 750, 500));
         try {
         ImageIO.write(screenShot, "png", new File(formatter.format(System.getProperty("user.home") + filename)));
         } catch (IOException ex) {
         Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         });*/
    }

    public int onLoop() throws InterruptedException {
        int[][] toChurch = new int[][]{{3130, 3124}, {3134,3122}, {3134, 3116}, {3130, 3109}, {3130, 3107}};
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Prayer.") && client.getInterface(372).getChild(0).isVisible()) {
            Entity door = closestObjectForName("Large door");
            NPC monk = closestNPCForName("Brother Brace");
            if ((door != null && door.isVisible()) || (monk != null && canReach(monk) && monk.isVisible())) {
                if (myPlayer().getPosition().distance(new Position(3130,3107,0)) < 4
                        || (monk != null && canReach(monk))) {
                    if (canReach(monk)) {
                        if (monk.getPosition().distance(myPlayer().getPosition()) < 3) {
                            monk.interact("Talk-to");
                            sleep(random(900));

                        } else {
                            walk(monk.getPosition());
                            sleep(random(600, 700));
                        }
                    } else {
                        door.interact("Open");
                        sleep(random(900));
                    }
                } else {
                    WalkAlongPath(toChurch, true);
                    sleep(random(900));
                }
            } else if (door != null && !door.isVisible()) {
                if (myPlayer().getPosition().distance(new Position(3130,3107,0)) < 3) {
                    //client.moveCameraToEntity(door);
                    client.rotateCameraToAngle(random(80, 120));
                    sleep(random(900));
                } else {
                    WalkAlongPath(toChurch, true);
                    sleep(random(900));
                }
            } else if (door == null) {
                WalkAlongPath(toChurch, true);
                sleep(random(900));
            } else if (!monk.isVisible()) {
                client.moveCameraToEntity(monk);
                sleep(random(900));

            }
        }
        //client.rotateCameraPitch(random(10,60)); 
        // pitch angle 
        // 22 - 30
        //client.rotateCameraToAngle(random(15,65)); 
        // yaw angle 
        // 242 - 250
        //openTab(Tab.CLANCHAT);
        //sleep(random(500,600));
        //sleep(random(500,600));
        //client.clickMouse(false);
        //sleep(random(500,600));
        //client.typeString("iemz");
        return 1000;
    }

    public void onPaint(Graphics g) throws NullPointerException {
        int[] gates = new int[]{14233, 14237, 14241, 14234, 14238, 14242, 14245, 14246};
        int[] gatesO = new int[]{14234, 14238, 14242, 14245, 14246};
        int[] gatesC = new int[]{14233, 14237, 14241};
        // 34,36,46,48,
        // 142 base closed 33, 35, 37, 41 43 
        //34 open 38, 42 , 46
        //33, 37, 41
        NPC Portal = closestNPCForName("Portal");
        Entity fence = closestObjectForName("Barricade");
        NPC mining = closestNPCForName("Mining Instructor");
        Entity furn = closestObjectForName("Furnace");
        NPC monk = closestNPCForName("Brother Brace");
        

        if (startLastMove == client.getLastMovementTime()) {
            lastMoveTimer = (System.currentTimeMillis() - idleStartTime);
        } else {
            startLastMove = client.getLastMovementTime();
            idleStartTime = System.currentTimeMillis();
        }

        if (myPlayer().getAnimation() != -1) {
            startLastMove = client.getLastMovementTime();
            idleStartTime = System.currentTimeMillis();
        }
        
        

        Graphics2D g2 = (Graphics2D) g;
        Entity door = closestObjectForName("Large door");
        if(monk != null){
            g2.drawString("Y: " + monk.getPosition().toString(), 10, 210);
        }
        g2.drawString("Y: " + (myPlayer().getPosition().distance(new Position(3130,3107,0))), 10, 200);
        //g2.drawString("Y " + (myPlayer().getX() == 3097 && myPlayer().getY() == 3107 && myPlayer().getZ() == 0), 10, 100);
        //g2.drawString("D" + closestObjectForName("Large door").getPosition().distance(myPlayer().getPosition()), 15, 60);
        //g2.drawString("Y " + furn.getPosition().distance(myPlayer().getPosition()).toString(), 10, 100);
        g2.drawString("idle: " + lastMoveTimer / 1000, 125, 326);

        RS2Object gate = closestObjectInRange(6, gates);
        /*
         g2.drawString(new RS2Interface(client.getBot(), 407).getChild(14).getMessage().substring(13,15), 10, 150);
         g2.drawString(" " + (Integer.parseInt(new RS2Interface(client.getBot(), 407).getChild(14).getMessage().substring(13,15)) == 82), 10, 160);
         if (Portal != null && (Portal.getId() == eastPortal
         || Portal.getId() == westPortal
         || Portal.getId() == sEastPortal
         || Portal.getId() == sWestPortal)) {
         g2.drawString("east: " + Portal.exists(), 10, 160);
         }
         */
        g2.drawString("gate: " + (gate == null), 10, 260);
        if (gate != null) {
            g2.drawString("gate = id: " + (gate.getId() == 14236), 10, 270);
        }
        final Area safeZone = new Area(2587, 9449, 2589, 9450);
        if (safeZone != null) {
            g2.drawString("d: " + myPlayer().getPosition().distance(safeZone.getRandomPosition(0)), 10, 270);
        }

        if (!setPaths) {
            //   voidKx = voidK.getX();
            //  voidKy = voidK.getY();
            setPaths = true;
        }
        //g2.drawString("Logs: " + client.getInventory().getAmount("Logs"), 10, 150);
        //g2.drawString("VLx: " + voidKx, 125, 313);
        //g2.drawString("VKy: " + voidKy, 225, 313);
        //g2.drawString("myPosx: " + (myPlayer().getX() - voidKx), 125, 323);
        //g2.drawString("myPosy: " + (myPlayer().getY() - voidKy), 225, 323);

        g2.fill(hidePaint);
        g2.setColor(Color.RED);
        g2.drawString("GUI", (int) hidePaint.getX() + 20, (int) hidePaint.getY() + 11);

        //g2.drawString("m: "+ client.getInterface(407).getChild(14).getMessage(), 10, 280); // Pest Points: x
        //g2.drawString("equals: "+ client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 44"), 10, 290);
    }

    public void onExit() {
        //TesterGui.CloseGUI();
    }

    private NPC closestRat() {
        for (int i = 0; i < closestNPCListForName("Giant rat").size(); i++) {
            if (closestNPCListForName("Giant rat").get(i).getPosition().distance(myPlayer().getPosition()) <= 7) {
                return closestNPCListForName("Giant rat").get(i);
            }
        }
        return null;
    }

    public boolean WalkAlongPath(int[][] path, boolean AscendThroughPath,
                                 int distanceFromEnd) {
        if (distanceToPoint(AscendThroughPath ? path[path.length - 1][0]
                : path[0][0], AscendThroughPath ? path[path.length - 1][1]
                : path[0][1]) <= distanceFromEnd) {
            return true;
        } else {
            WalkAlongPath(path, AscendThroughPath);
            return false;
        }
    }

    public void WalkAlongPath(int[][] path, boolean AscendThroughPath) {
        int destination = 0;
        for (int i = 0; i < path.length; i++) {
            if (distanceToPoint(path[i][0], path[i][1]) < distanceToPoint(
                    path[destination][0], path[destination][1])) {
                destination = i;
            }
        }
        if (distanceToPoint(path[destination][0], path[destination][1]) > (isRunning() ? 3
                : 2)) {
            return;
        }
        if (AscendThroughPath && destination != path.length - 1
                || !AscendThroughPath && destination != 0) {
            destination += (AscendThroughPath ? 1 : -1);
        }
        try {
            log("Walking to Spot:" + destination);
            if (new Area(path[destination][0], path[destination][1], path[destination][0], path[destination][1]) != null) {
                walk(new Area(path[destination][0], path[destination][1], path[destination][0], path[destination][1]));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math
                .pow(client.getMyPlayer().getX() - pointX, 2)
                + Math.pow(client.getMyPlayer().getY() - pointY, 2));
    }

}
