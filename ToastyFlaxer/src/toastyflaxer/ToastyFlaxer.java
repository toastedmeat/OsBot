/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toastyflaxer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.StyleConstants;
import org.osbot.script.MethodProvider;
import static org.osbot.script.MethodProvider.random;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.MouseDestination;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.randoms.RandomBehaviourHook;
import org.osbot.script.rs2.randoms.RandomManager;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author = "Toastedmeat", info = "Picks Flax",
        name = "ToastyFlaxer", version = 1)
public class ToastyFlaxer extends Script {

    final int myVersion = 1;

    //Area's
    private final Area gflaxSpot1 = new Area(2439, 3393, 2454, 3411);
    private final Area gflaxSpot2 = new Area(2465, 3402, 2472, 3408);
    private final Area gflaxSpot3 = new Area(2479, 3396, 2484, 3399);

    private final Area sFlaxSpot = new Area(2735, 3436, 2751, 3451);

    private final Area gBankArea = new Area(2444, 3415, 2447, 3431);
    private final Area gBottomArea = new Area(2442, 3411, 2448, 3418);

    private final Area sBankArea = new Area(2721, 3490, 2727, 3493);

    // Paths
    private final int[][] toGSpot1 = new int[][]{{2446, 3415}, {2448, 3404}};
    private final int[][] toGSpot2 = new int[][]{{2446, 3415}, {2459, 3409}, {2469, 3404}};
    private final int[][] toGSpot3 = new int[][]{{2446, 3415}, {2459, 3409}, {2469, 3399}, {2481, 3397}};

    private final int[][] toSSpot = new int[][]{{2725, 3491}, {2728, 3480}, {2728, 3469}, {2727, 3461}, {2729, 3452}, {2739, 3447}};
    // ID's
    private final int gBankID = 2196;
    private final int sBankID = 25808;

    //Objects
    private Entity bankBooth, stairsUp, stairsDown, flax, failStairs;

    //Rectangles
    private final Rectangle shading = new Rectangle(0, 270, 515, 68);

    //Ints
    private int spotChooser;

    //Run
    private boolean run;
    private int runInt;
    private final RectangleDestination runBtn = new RectangleDestination(625, 415, 30, 30);

    //Booleans
    private boolean walkedToFlax;

    private boolean gnome, seers;

    //Timers
    private long startTime = 0L, millis = 0L, hours = 0L;
    private long minutes = 0L, seconds = 0L, last = 0L;

    private int startLastMove;
    private long lastMoveTimer, idleStartTime;

    //Profits
    private double priceFlax, profit = 0;
    private double flaxCounter;

    private int profitPH = 0, flaxPH;

    //Others
    private enum State {

        IDLE, FAILSAFE, STAIRSDOWN, STAIRSUP, WALKTOFLAX, WALKTOBANK, PICKINGFLAX, BANK;
    }

    private State state = State.IDLE;

    @Override
    public void onStart() {

        client.setMouseSpeed(random(12, 16));
        //Init Loot Variables
        priceFlax = 0;
        flaxCounter = 0;
        flaxPH = 0;
        profit = 0;
        profitPH = 0;

        //Run
        run = false;
        runInt = random(15, 27);

        //Booleans
        walkedToFlax = false;

        //start timer
        startTime = System.currentTimeMillis();
        idleStartTime = System.currentTimeMillis();
        lastMoveTimer = 0;

        rsItem f = new rsItem("flax");
        priceFlax = f.getAveragePrice();

        FlaxerGui.buildGUI();
        FlaxerGui.frame.setVisible(true);
        FlaxerGui.frame.setSize(140, 250);
        FlaxerGui.frame.setLocation(300, 300);

        while (FlaxerGui.frame.isVisible()) {
            try {
                sleep(random(500, 600));
            } catch (InterruptedException ex) {
                Logger.getLogger(FlaxerGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        gnome = FlaxerGui.rdbtnGnome.isSelected();
        seers = FlaxerGui.rdbtnSeers.isSelected();
        int tempSpot = Integer.parseInt(FlaxerGui.textSpot.getText());
        spotChooser = tempSpot;

        if (gnome) {
            log("Picking in the Gnome Stronghold");
        } else if (seers) {
            log("Picking in the flax field in Seers village");
        }
        log("Price of flax: " + priceFlax);
        log("Starting ToastyFlaxer v" + myVersion);
    }

    @Override
    public int onLoop() throws InterruptedException {

        state = checkState();

        switch (state) {
            case STAIRSDOWN:
                log("Going down the stairs.");
                walk(new Position(2445, 3416, 1));
                sleep(random(1000, 1200));
                stairsDown = closestObjectForName("Staircase");
                stairsDown.interact("Climb-down");
                sleep(random(600, 700));
                break;
            case STAIRSUP:
                log("Going up the stairs.");
                stairsUp = closestObjectForName("Staircase");
                stairsUp.interact("Climb-Up");
                sleep(random(800, 900));
                if (myPlayer().getZ() == 1) {
                    walk(new Position(2445, 3424, 1));
                    sleep(random(1000, 1200));
                }
                break;
            case WALKTOFLAX:
                walkToFlax(spotChooser);
                break;
            case WALKTOBANK:
                walkToBank(spotChooser);
                break;
            case PICKINGFLAX:
                pickFlax(spotChooser);
                break;
            case BANK:
                bank();
                break;
        }
        runningCheck();
        return 300;
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("You pick some flax")) {
            flaxCounter++;
        }
    }

    @Override
    public void onPaint(Graphics g) {

        if (startLastMove == client.getLastMovementTime()) {
            lastMoveTimer = (System.currentTimeMillis() - idleStartTime);
        } else {
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

        flaxPH = ((int) (flaxCounter * 3600000.0D / (System
                .currentTimeMillis() - startTime)));

        profit = ((priceFlax * flaxCounter) / 1000);
        profitPH = ((int) (profit * 3600000.0D / (System.currentTimeMillis() - startTime)));

        Graphics2D g2 = (Graphics2D) g;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                0.40f));
        g2.setColor(Color.BLACK);
        g2.fill(shading);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                1.00f));

        g2.setColor(Color.GREEN);
        g2.drawString("Flax picked: " + flaxCounter, 125, 300);
        g2.drawString("Flax p/h: " + flaxPH, 125, 320);
        g2.drawString("Profit: " + profit + "k", 325, 300);
        g2.drawString("Profit p/h: " + profitPH + "k", 325, 320);

        g2.setColor(Color.BLACK);
        g2.drawString("State: " + state, 360, 458);
        g2.setColor(Color.CYAN);
        g2.drawString("World: " + client.getCurrentWorld(), 10, 14);
        g2.drawString("Time Elapsed: " + hours + " hours " + minutes
                + " minutes " + seconds + " seconds", 100, 14);
        g2.drawString("Idle Time: " + (lastMoveTimer / 1000) + " Seconds", 375, 14);
        g2.drawString("Toasty's Flaxer", 0, 270);
        g2.drawString("Made by Toastedmeat V" + myVersion, 365, 337);

    }

    @Override
    public void onExit() {
        FlaxerGui.CloseGUI();
        log("Picked: " + flaxCounter + " Flax");
        log("Profit gained: " + (flaxCounter * priceFlax) / 1000 + "k");
    }
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public State checkState() throws InterruptedException {
        flax = closestObjectForName("Flax");
        if (gnome) {
            if (myPlayer().isInArea(new Area(2440, 3404, 2440, 3404)) && myPlayer().getZ() == 1) {
                failStairs = closestObjectForName("Staircase");
                failStairs.interact("Climb-down");
                sleep(random(600, 700));
                walk(new Position(2449, 3404, 0));
                sleep(random(1200, 1500));
                return State.FAILSAFE;
            } else if (client.getInventory().isFull() && (myPlayer().isInArea(gBankArea) || myPlayer().isInArea(gBottomArea)) && myPlayer().getZ() == 0) {
                return State.STAIRSUP;
            } else if (client.getInventory().isFull() && !myPlayer().isInArea(gBankArea) && !myPlayer().isInArea(gBottomArea)) {
                return State.WALKTOBANK;
            } else if (client.getInventory().isFull() && myPlayer().isInArea(gBankArea)) {
                return State.BANK;
            } else if (!client.getInventory().isFull() && myPlayer().isInArea(gBankArea) && myPlayer().getZ() == 1) {
                return State.STAIRSDOWN;
            } else if (!client.getInventory().isFull() && (myPlayer().isInArea(gBottomArea) || !walkedToFlax)) {
                return State.WALKTOFLAX;
            } else if (flax != null && (myPlayer().isInArea(gflaxSpot1) || myPlayer().isInArea(gflaxSpot2)
                    || myPlayer().isInArea(gflaxSpot3))) {
                return State.PICKINGFLAX;
            }
        } else if (seers) {
            if (myPlayer().isInArea(new Area(2725, 3490, 2729, 3494)) && myPlayer().getZ() == 1) {
                failStairs = closestObjectForName("Ladder");
                failStairs.interact("Climb-down");
                sleep(random(600, 700));
                walk(new Position(2724, 3493, 0));
                sleep(random(1200, 1500));
                return State.FAILSAFE;
            } else if (client.getInventory().isFull() && !myPlayer().isInArea(sBankArea)) {
                return State.WALKTOBANK;
            } else if (client.getInventory().isFull() && myPlayer().isInArea(sBankArea)) {
                return State.BANK;
            } else if (!client.getInventory().isFull() && !walkedToFlax) {
                return State.WALKTOFLAX;
            } else if (flax != null && myPlayer().isInArea(sFlaxSpot)) {
                return State.PICKINGFLAX;
            }
        }
        return State.IDLE;
    }

    public void runningCheck() throws InterruptedException {
        run = isRunning();
        //log("isRunning: " + Boolean.toString(run));
        if ((run == false) && (client.getRunEnergy() >= runInt)) {
            settingsTab.open();
            random(900, 1279);
            client.moveMouseTo(runBtn, false, true, false);
            sleep(random(700, 900));
            openTab(Tab.INVENTORY);
        }
        if (client.getRunEnergy() < 10) {
            runInt = random(15, 47);
        }
    }

    public void walkToFlax(int spot) throws InterruptedException {
        if (gnome) {
            switch (spot) {
                case 1:
                    WalkAlongPath(toGSpot1, true);
                    if (myPlayer().isInArea(gflaxSpot1)) {
                        walkedToFlax = true;
                    }
                    break;
                case 2:
                    WalkAlongPath(toGSpot2, true);
                    if (myPlayer().isInArea(gflaxSpot2)) {
                        walkedToFlax = true;
                    }
                    break;
                case 3:
                    WalkAlongPath(toGSpot3, true);
                    if (myPlayer().isInArea(gflaxSpot3)) {
                        walkedToFlax = true;
                    }
                    break;
            }
        } else if (seers) {
            switch (spot) {
                case 1:
                    WalkAlongPath(toSSpot, true);
                    if (myPlayer().isInArea(sFlaxSpot) && !myPlayer().isMoving()) {
                        walkedToFlax = true;
                    }
                    break;
            }
        }
    }

    public void walkToBank(int spot) throws InterruptedException {
        if (gnome) {
            switch (spot) {
                case 1:
                    WalkAlongPath(toGSpot1, false);
                    break;
                case 2:
                    WalkAlongPath(toGSpot2, false);
                    break;
                case 3:
                    WalkAlongPath(toGSpot3, false);
                    break;
            }
        } else if (seers) {
            switch (spot) {
                case 1:
                    WalkAlongPath(toSSpot, false);
                    break;
            }
        }
    }

    public void pickFlax(int spot) throws InterruptedException {
        if (myPlayer().getAnimation() != -1) {
            startLastMove = client.getLastMovementTime();
            idleStartTime = System.currentTimeMillis();
        }
        if (gnome) {
            switch (spot) {
                case 1:
                    if (myPlayer().isInArea(gflaxSpot1)) {
                    flax = closestObjectForName("Flax");
                    if (flax != null) {
                        flax.interact("Pick");
                        sleep(random(300, 600));
                    }

                } else {
                    walk(gflaxSpot1);
                }
                    break;
                case 2:
                    if (myPlayer().isInArea(gflaxSpot2)) {
                    if (!myPlayer().isAnimating()) {
                        flax = closestObjectForName("Flax");
                        if (flax != null) {
                            flax.interact("Pick");
                            sleep(random(300, 600));
                        }
                    }
                } else {
                    walk(gflaxSpot2);
                }
                    break;
                case 3:
                    if (myPlayer().isInArea(gflaxSpot3)) {
                    if (!myPlayer().isAnimating()) {
                        flax = closestObjectForName("Flax");
                        if (flax != null) {
                            flax.interact("Pick");
                            sleep(random(300, 600));
                        }
                    }
                } else {
                    walk(gflaxSpot2);
                }
                    break;
            }
        } else if (seers) {
            switch (spot) {
                case 1:
                    if (myPlayer().isInArea(sFlaxSpot)) {
                    flax = closestObjectForName("Flax");
                    if (flax != null) {
                        flax.interact("Pick");
                        sleep(random(300, 600));
                    }

                } else {
                    walk(sFlaxSpot);
                }
                    break;
            }
        }

    }

    public void bank() throws InterruptedException {
        if (gnome) {
            bankBooth = closestObject(gBankID);
        } else if (seers) {
            bankBooth = closestObject(sBankID);
        }
        walkedToFlax = false;
        if (client.getBank().isOpen()) {
            if (client.getInventory().isFull()) {
                client.getBank().depositAll();
                sleep(random(800, 900));
                client.getBank().close();
                sleep(random(800, 900));
            }
        } else {
            if (bankBooth != null) {
                if (bankBooth.getPosition().distance(myPlayer().getPosition()) < 5) {
                    if (bankBooth.isVisible()) {
                        if (!myPlayer().isMoving()) {
                            bankBooth.interact("Bank");
                            sleep(random(700, 900));
                            bank();
                        }
                    } else {
                        client.moveCameraToEntity(bankBooth);
                    }
                } else if (bankBooth.getPosition().distance(myPlayer().getPosition()) > 5 && gnome) {
                    log("Walked");
                    walk(new Position(2445, 3424, 1));
                } else if (bankBooth.getPosition().distance(myPlayer().getPosition()) > 5 && seers) {
                    log("Walked");
                    walk(new Position(2724, 3493, 0));
                }
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public boolean WalkAlongPath(int[][] path, boolean AscendThroughPath,
                                 int distanceFromEnd) throws InterruptedException {
        if (distanceToPoint(AscendThroughPath ? path[path.length - 1][0]
                : path[0][0], AscendThroughPath ? path[path.length - 1][1]
                : path[0][1]) <= distanceFromEnd) {
            return true;
        } else {
            WalkAlongPath(path, AscendThroughPath);
            return false;
        }
    }

    public void WalkAlongPath(int[][] path, boolean AscendThroughPath) throws InterruptedException {
        int destination = 0;
        for (int i = 0; i < path.length; i++) {
            if (distanceToPoint(path[i][0], path[i][1]) < distanceToPoint(
                    path[destination][0], path[destination][1])) {
                destination = i;
            }
        }
        if (myPlayer().isMoving()
                && distanceToPoint(path[destination][0], path[destination][1]) > (isRunning() ? 3
                : 2)) {
            return;
        }
        if (AscendThroughPath && destination != path.length - 1
                || !AscendThroughPath && destination != 0) {
            destination += (AscendThroughPath ? 1 : -1);
        }
        log("Walking to point:" + destination);
        if (new Area(path[destination][0], path[destination][1], path[destination][0], path[destination][1]) != null) {
            walk(new Area(path[destination][0], path[destination][1], path[destination][0], path[destination][1]));
        }

    }

    private int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math
                .pow(client.getMyPlayer().getX() - pointX, 2)
                + Math.pow(client.getMyPlayer().getY() - pointY, 2));
    }
}
