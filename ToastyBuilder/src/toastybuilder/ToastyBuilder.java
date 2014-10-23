/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toastybuilder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;
import org.osbot.script.MethodProvider;
import static org.osbot.script.MethodProvider.random;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.skill.Skill;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

/**
 *
 * @author Eric
 */
@ScriptManifest(author = "Toastedmeat", info = "Build shit", name = "Toasty Builder", version = 1)
public class ToastyBuilder extends Script {

    final Area castleWarsArea = new Area(2438, 3083, 2444, 3094);
    final Area castleBank = new Area(2442, 3082, 2443, 3084);
    final Area onChest = new Area(2443, 3083, 2443, 3083);
    Player player;
    Bank bank;
    String[] items = {"Saw", "Hammer", "Teleport to house", "Steel nails",
        "Ring of dueling(8)", "Ring of dueling(7)", "Ring of dueling(6)",
        "Ring of dueling(5)", "Ring of dueling(4)", "Ring of dueling(3)",
        "Ring of dueling(2)", "Ring of dueling(1)"};
    Entity portal;
    int interfaceNum = 0, chairToBuild = 0, amount = 0, plankToUse = 0;
    private long idleStartTime;
    private long lastMoveTimer;
    private int startLastMove;
    String spaceName, builtName;
    boolean doneBanking = false;
    private RectangleDestination oakLarder = new RectangleDestination(37,178,30,25);
    private RectangleDestination crudeC = new RectangleDestination(40,70,20,25);
    private RectangleDestination woodenC = new RectangleDestination(40,131,20,25);
    private RectangleDestination rocking = new RectangleDestination(40,200,20,25);
    private RectangleDestination oakC = new RectangleDestination(40,261,20,25);
    private RectangleDestination houseOptions = new RectangleDestination(680,412,30,30);
    private RectangleDestination buildingModeOn = new RectangleDestination(600,260,30,30);
    double startCon,conExp,conExpPH;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Timers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private long startTime = 0L, millis = 0L, hours = 0L;
    private long minutes = 0L, seconds = 0L, last = 0L;
    
    public void onStart() {
        startCon = client.getSkills().getExperience(Skill.CONSTRUCTION);
        bank = client.getBank();
        player = client.getMyPlayer();

        startTime = System.currentTimeMillis();
        idleStartTime = System.currentTimeMillis();
        lastMoveTimer = 0;

        spaceName = JOptionPane.showInputDialog("Enter name of space Ex: Chair space, Larder space", "Larder space");
        builtName = JOptionPane.showInputDialog("Enter name of the stucture built Ex: Chair, Larder", "Larder");
        String interfaceTemp = JOptionPane.showInputDialog("Enter Interface id\n"
                + "Chair - 396\n"
                + "Larder - 394\n", "394");
        interfaceNum = Integer.parseInt(interfaceTemp);
        String chairToBuildTemp = JOptionPane.showInputDialog("Enter Interface child id\n"
                + "Crude wooden chair - 133\n"
                + "Wooden chair - 134\n"
                + "Rocking Chair - 135\n"
                + "Oak chair - 136\n"
                + "Oak larder - 108", "108");
        chairToBuild = Integer.parseInt(chairToBuildTemp);
        String tempAmount = JOptionPane.showInputDialog("Enter amount of planks its takes", "");
        amount = Integer.parseInt(tempAmount);
        String plankToUseTemp = JOptionPane.showInputDialog("Enter plank to use\n"
                + "Plank - 960\n"
                + "Oak plank - 8778\n", "8778");
        plankToUse = Integer.parseInt(plankToUseTemp);
    }

    public int onLoop() throws InterruptedException {
        Entity space = closestObjectForName(spaceName);
        Entity built = closestObjectForName(builtName);
        if (client.getInventory().getAmount(plankToUse) >= amount) {
            if (!myPlayer().isAnimating() && lastMoveTimer > 2) {
                if (space != null) {
                    if (new RS2Interface(client.getBot(), interfaceNum).getChild(0) != null) {
                        //log("done?");
                        switch (chairToBuild){
                            case 108:
                                client.moveMouseTo(oakLarder, false, true, false);
                                break;
                            case 133:
                                client.moveMouseTo(crudeC, false, true, false);
                                break;
                            case 134:
                                client.moveMouseTo(woodenC, false, true, false);
                                break;
                            case 135:
                                client.moveMouseTo(rocking, false, true, false);
                                break;
                            case 136:
                                client.moveMouseTo(oakC, false, true, false);
                                break;
                            default:
                                client.getInterface(interfaceNum).getChild(chairToBuild).interact("Build");
                                break;
                        }
                    } else if (new RS2Interface(client.getBot(), interfaceNum).getChild(0) == null) {
                        space.interact("Build");
                        sleep(random(2000, 3000));
                        //log("Opened");
                        return 50;
                    }
                } else if (built != null) {
                    built.interact("Remove");
                    sleep(random(1700, 1900));
                    
                    if (new RS2Interface(client.getBot(), 228) == null) {
                        log("Null waiting");
                    } else {
                        client.getInterface(228).getChild(1).interact("Yes");
                        sleep(random(900, 1000));
                        client.clickMouse(false);
                        sleep(random(1200, 1500));
                        
                    }
                }
            } else {
                sleep(200);
            }
        } else {
            int name = client.getInventory().getSlotForNameThatContains("Ring of dueling");
            client.getInventory().interactWithSlot(name, "Rub");
            sleep(random(4300, 4800));
            client.getInterface(230).getChild(2).interact("Continue");
            sleep(random(2900, 3300));
            doneBanking = false;
            if (myPlayer().isInArea(castleWarsArea)) {
                walk(onChest);
                sleep(random(1600, 2200));
                bank();
            } else {
            }
        }
        return 400;
    }

    public void onPaint(Graphics g) throws NullPointerException {
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
        // Timer Variables
        millis = (System.currentTimeMillis() - startTime);
        hours = (millis / 3600000L);
        millis -= hours * 3600000L;
        minutes = (millis / 60000L);
        millis -= minutes * 60000L;
        seconds = (millis / 1000L);
        
        conExp = (client.getSkills().getExperience(Skill.CONSTRUCTION) - startCon);
        conExpPH = ((int) (conExp * 3600000.0D / (System.currentTimeMillis() - startTime)));

        Graphics2D g2 = (Graphics2D) g;
        g2.drawString("Time Elapsed: " + hours + " hours " + minutes
                + " minutes " + seconds + " seconds", 125, 286);
        g2.drawString("Exp PH: " + conExpPH / 1000 +"k", 125, 306);
        g2.drawString("idle: " + lastMoveTimer / 1000, 125, 326);
    }

    public void onExit() {
    }

    public void bank() throws InterruptedException {
        if (castleBank.contains(player)) {
            Entity bankBox = closestObject(4483);
            if (bank.isOpen()) {
                bank.depositAllExcept(items);
                sleep(random(1000, 1500));
                if (!client.getInventory().contains("Ring of dueling(8)")
                        && !client.getInventory().contains("Ring of dueling(7)")
                        && !client.getInventory().contains("Ring of dueling(6)")
                        && !client.getInventory().contains("Ring of dueling(5)")
                        && !client.getInventory().contains("Ring of dueling(4)")
                        && !client.getInventory().contains("Ring of dueling(3)")
                        && !client.getInventory().contains("Ring of dueling(2)")
                        && !client.getInventory().contains("Ring of dueling(1)")) {
                    bank.withdraw1(2552);
                    sleep(random(800, 900));
                }
                bank.withdrawAll(plankToUse);
                sleep(random(800, 900));
                if (client.getInventory().isFull()) {
                    bank.close();
                    sleep(random(600, 700));
                    client.getInventory().interactWithName("Teleport to house", "Break");
                    log("Waiting to load");
                    sleep(random(5000, 6000));
                    portal = closestObjectForName("Portal");
                    if (portal != null) {
                        log("Portal?");
                        openTab(Tab.SETTINGS);
                        sleep(random(200, 300));
                        client.moveMouseTo(houseOptions, false, true, false);
                        sleep(random(500, 600));
                        while(!settingsTab.isHouseBuildingModeOn()){
                        client.moveMouseTo(buildingModeOn, false, true, false);
                        sleep(random(200, 300));
                        }
                    }
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Use");
                        sleep(random(700, 900));
                        bank();
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        } else {
            walk(castleBank);
        }
    }
}
