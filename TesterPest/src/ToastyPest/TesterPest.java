/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToastyPest;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.osbot.script.MethodProvider;
import static org.osbot.script.MethodProvider.random;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.MainScreenTileDestination;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.RS2Object;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author = "toastedmeat", name = "TesterPest",
        info = "Testing version only", version = 3)
public class TesterPest extends Script {
    //fonts

    private final Font regFont = new Font("Serif", 0, 12);
    private final Font statsFont = new Font("Serif", 0, 10);

    //Colors
    private final Color gold = new Color(255, 255, 0);

    //Areas
    final Area outsideBoat = new Area(2657, 2638, 2657, 2644);
    final Area insideBoat = new Area(2660, 2638, 2663, 2643);

    final Area outsideBoatMed = new Area(2644, 2642, 2644, 2651);
    final Area insideBoatMed = new Area(2638, 2642, 2641, 2647);

    final Area outsideBoatHigh = new Area(2638, 2652, 2638, 2655);
    final Area insideBoatHigh = new Area(2632, 2649, 2635, 2654);

    final Area buyingSpot = new Area(2658, 2646, 2664, 2655);
    //Normal
    private Area startArea, middleArea, eGate, wGate, sGate;
    //pures
    private Area initialEast, initialWest, eTree, midTree, westTree, westTreeTop, eastTreeTop,
            westBar1, westBar2, westBar3, westBar4, eastBar1, eastBar2, eastBar3, eastBar4;

    private Area newStuffs, area1, area2, area3, area4, area5;
    private Position pos1, pos2, pos3, pos4, pos5;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Timers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public long startTime = 0L, millis = 0L, hours = 0L;
    public long minutes = 0L, seconds = 0L, last = 0L;

    //move timers
    private long idleStartTime;
    private long lastMoveTimer;

    //Paths
    private int[][] toEastG, toEastP, toWestG, toWestP, toSouth, toSW, toSE, switchSWToW, switchSEToSW, switchEToSE;
    //For pures
    private int[][] toWestTreeTop, toEastTreeTop, toWestBar2, toWestBar3, toWestBar4, toEastBar2, toEastBar3, toEastBar4;

    //portal
    private int westPortal = 2058, eastPortal = 2059,
            sEastPortal = 2060, sWestPortal = 2061;

    private int begPortal = 1, begTree = 1;

    //players positon
    private int x, y, voidKx, voidKy, strPrayer = 5, attPrayer = 5;
    //Booleans
    private boolean playingPC, isFighting, fightingMisc, portalDead, setPaths,
            firstMove, wonGame, prayerActivated, usingPrayer;
    // Booleans for pures
    private boolean initialWalk, doneCutting, eastTreeDone, midTreeDone, westTreeDone, westTreeTopDone, eastTreeTopDone,
            bar1Done, bar2Done, bar3Done, bar4Done, bar1Donew, bar2Donew, bar3Donew, bar4Donew, walkedPassedGate,
            deathWalk;

    private boolean newStuff, area1Done, area2Done, area3Done, area4Done, area5Done;

    //names for misc mobs
    private final String[] mobs = {"Torcher", "Defiler", "Brawler", "Ravager"};
    private NPC spinner, Portal, miscMobs, voidK;
    private Entity tree, fence;

    private int[] Barricades = new int[]{14227, 14228, 14229, 14230, 14231, 14232};

    //doors
    private final int[] gates = new int[]{14233, 14237, 14241, 14234, 14238, 14242, 14245, 14246};
    int[] gatesO = new int[]{14234, 14238, 14242, 14245, 14246};
    int[] gatesC = new int[]{14233, 14237, 14241};

    private final int doorO1 = 14234, doorO2 = 14238, doorO3 = 14242, doorO4 = 14245, doorO5 = 14246;
    private RS2Object gate;

    // Rectangles for buying
    final RectangleDestination exchangeBottom = new RectangleDestination(475, 220, 10, 25);
    final RectangleDestination confirm = new RectangleDestination(225, 285, 45, 10);
    private int startLastMove, itemToBuy;
    String buyingItem = "";

    //weapons speed
    private double weaponSpeed;

    // for PAINT
    private int winCounter, currentPoints;
    private double winPH, pointsPercent;
    private String timeTM;
    Image image;
    Rectangle hidePaint = new Rectangle(random(10, 500), 462, 60, 12);

    //Mouse Listener
    Point pointer;

    //running checks
    boolean run, runningCheck;
    int randomInt, runInt;

    //Rectangles
    Rectangle rectAtt = new Rectangle(125, 303, 260, 12);
    Rectangle rectAttColor = new Rectangle(125, 303, 0, 12);

    private enum State {

        IDLE, OUTSIDEBOAT, INSIDEBOAT, GAMESTARTED, FIGHTING, NEXTPORTAL,
        BUYITEMS, GAMESTARTEDPUREMODE, CUTTING, TOGATEPURE, REPAIR;
    }
    private State state = State.IDLE;

    //~~~~~~~~~~~~~~~~~~~~~~~~ Inherited overrides ~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onStart() {

        try {
            //get the awesome paint for loot
            image = ImageIO.read(new URL("http://i1279.photobucket.com/albums/y535/eloo12/ToastyPests_zps85a56b82.png"));
        } catch (IOException e) {
            log("Couldn't get the picture :(");
            e.printStackTrace();
        }

        //for running check
        run = false;
        runningCheck = true;
        randomInt = random(1, 10);
        runInt = random(15, 27);

        //playing
        playingPC = false;
        //fighting
        isFighting = false;
        fightingMisc = false;
        //dead portals
        portalDead = false;

        //start timer
        startTime = System.currentTimeMillis();
        idleStartTime = System.currentTimeMillis();
        lastMoveTimer = 0;
        // others
        winCounter = 0;
        currentPoints = 0;
        setPaths = false;
        firstMove = false;
        wonGame = false;

        //Tree's 
        initialWalk = false;
        doneCutting = false;
        eastTreeDone = false;
        eastTreeTopDone = false;
        midTreeDone = false;
        westTreeDone = false;
        westTreeTopDone = false;
        walkedPassedGate = false;

        deathWalk = false;

        //Barricades
        bar1Done = false;
        bar2Done = false;
        bar3Done = false;
        bar4Done = false;

        //Initialization 
        TesterPestGui.buildGUI();
        TesterPestGui.frame.setVisible(true);
        TesterPestGui.frame.setSize(235, 470);
        TesterPestGui.frame.setLocation(300, 300);
        TesterPestGui.chkboxPureMode.setSelected(true);
        while (TesterPestGui.frame.isVisible()) {
            try {
                sleep(random(500, 600));
            } catch (InterruptedException ex) {
            }
        }
        weaponSpeed = (Double.parseDouble(TesterPestGui.textWeaponSpeed.getText()) * 1000);
        usingPrayer = TesterPestGui.chkboxPrayer.isSelected();
        itemToBuy = 94;
        buyingItem = "Void Knight body";

        if (TesterPestGui.rdbtnNovice.isSelected()) {
            westPortal = 2058;
            eastPortal = 2059;
            sEastPortal = 2060;
            sWestPortal = 2061;
        }

        log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log("Toasty tester");
        log("Buying: " + buyingItem);
        log("Report all bugs!");
        log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public int onLoop() throws InterruptedException {
        state = checkStates();
        if (!playingPC) {

            x = myPlayer().getX();
            y = myPlayer().getY();

            // random portal
            begTree = random(1, 2);

        }
        switch (state) {
            case OUTSIDEBOAT:
                if (wonGame) {
                if (new RS2Interface(client.getBot(), 243).getChild(5) != null) {
                    winCounter++;
                    wonGame = false;
                }
            }
                prayerActivated = false;
                playingPC = false;
                portalDead = false;
                isFighting = false;
                fightingMisc = false;
                setPaths = false;
                firstMove = false;
                doneCutting = false;

                Entity plank = closestObjectForName("Gangplank");
                plank.interact("Cross", true);
                sleep(random(250, 290));
                plank.interact("Cross", true);
                sleep(random(100, 200));
                break;
            case INSIDEBOAT:
                currentPoints = Integer.parseInt(new RS2Interface(client.getBot(), 407).getChild(14).getMessage().substring(13));
                break;
            case GAMESTARTED:

                break;
            case FIGHTING:

                break;
            case NEXTPORTAL:

                break;
            case BUYITEMS:

                break;
            case GAMESTARTEDPUREMODE:
                if (!firstMove) {
                log("Walking the first move");
                sleep(random(600, 700));
                walk(new Position(x, y - 7, 0));
                sleep(random(2000, 2300));
                firstMove = true;
                log("Tree Area: " + begTree);
            }
                wonGame = true;
                gameStartedPureRetry();
                break;
            case CUTTING:
                cutting();
                break;
            case TOGATEPURE:
                walkToGatePure();
                break;
            case REPAIR:
                wonGame = true;
                repairPure();
                break;
        }
        // checks to make sure we're running
        runningCheck();

        return random(400, 700);
    }

    @Override
    public void onPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        gate = closestObjectInRange(6, gates);

        millis = (System.currentTimeMillis() - startTime);
        hours = (millis / 3600000L);
        millis -= hours * 3600000L;
        minutes = (millis / 60000L);
        millis -= minutes * 60000L;
        seconds = (millis / 1000L);

        if (startLastMove == client.getLastMovementTime()) {
            lastMoveTimer = (System.currentTimeMillis() - idleStartTime);
        } else {
            startLastMove = client.getLastMovementTime();
            idleStartTime = System.currentTimeMillis();
        }

        winPH = ((int) (winCounter * 3600000.0D / (System.currentTimeMillis() - startTime)));
        timeTM = formatTime(timeTnl(250 - currentPoints, winPH));
        pointsPercent = (100 * ((double) currentPoints / 250));
        int pointsPercentI = (int) (pointsPercent * 2.6);
        rectAttColor.setSize(pointsPercentI, 12);

        if (TesterPestGui.frame != null) {
            if (TesterPestGui.rdbtnHidePaint.isSelected()) {
            } else if (TesterPestGui.rdbtnShowPaint.isSelected()) {

                // awesome paint ^_^
                g2.drawImage(image, 9, 268, null);

                g2.drawString("idle: " + lastMoveTimer / 1000, 125, 326);
                g2.drawString("Games won: " + winCounter, 125, 336);
                g2.drawString("Tester Version", 225, 336);
                g2.drawString("State: " + state, 325, 336);
                g2.drawString("Time Elapsed: " + hours + " hours " + minutes
                        + " minutes " + seconds + " seconds", 150, 293);
                if (TesterPestGui.chkboxPureMode.isSelected()) {
                    g2.drawString("Pure mode activated!", 210, 303);
                }
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.00f));
            g2.fill(hidePaint);
            g2.setColor(Color.RED);
            g2.drawString("GUI", (int) hidePaint.getX() + 20, (int) hidePaint.getY() + 11);

        }
    }

    @Override
    public void onExit() throws InterruptedException {
        TesterPestGui.CloseGUI();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ Main Methods ~~~~~~~~~~~~~~~~~
    public void mouseClicked(MouseEvent e) {
        pointer = e.getPoint();

        if (hidePaint.contains(pointer)) {
            TesterPestGui.frame.setVisible(true);
        }

    }

    public State checkStates() throws InterruptedException {
        spinner = closestNPCForName("Spinner");
        Portal = closestNPCForName("Portal");
        miscMobs = closestNPCForName(mobs);

        if (lastMoveTimer > 360000 + random(-30000, 30000)) {
            log("Idle for too long logging out");
            stop();
        }
        if (minutes > 30) {
            log("30 minutes have passed shutting down");
            stop();
        }
        if (myPlayer().isInArea(outsideBoat) || myPlayer().isInArea(outsideBoatMed)
                || myPlayer().isInArea(outsideBoatHigh)) {
            if (new RS2Interface(client.getBot(), 213).getChild(4) != null) {
                return State.BUYITEMS;
            } else {
                log("Outside the boat");
                return State.OUTSIDEBOAT;
            }
        } else if (myPlayer().isInArea(insideBoat)
                || myPlayer().isInArea(insideBoatMed)
                || myPlayer().isInArea(insideBoatHigh)) {
            if ((client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 150")
                    && TesterPestGui.rdbtnGloves.isSelected())
                    || client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 200")
                    && (TesterPestGui.rdbtnRHelm.isSelected()
                    || TesterPestGui.rdbtnMeleeHelm.isSelected()
                    || TesterPestGui.rdbtnMageHelm.isSelected())
                    || (client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 100")
                    && (TesterPestGui.rdbtnAtt.isSelected() || TesterPestGui.rdbtnDef.isSelected()
                    || TesterPestGui.rdbtnStr.isSelected() || TesterPestGui.rdbtnRng.isSelected()
                    || TesterPestGui.rdbtnMage.isSelected()))) {
                log("Buying items");
                return State.BUYITEMS;
            } else {
                //log("Inside the boat, Waiting");
                return State.INSIDEBOAT;
            }
        } else if (TesterPestGui.chkboxPureMode.isSelected() && doneCutting && deathWalk) {
            //log("Died walking back");
            return State.TOGATEPURE;
        } else if (TesterPestGui.chkboxPureMode.isSelected() && !doneCutting && initialWalk) {
            return State.CUTTING;
        } else if (TesterPestGui.chkboxPureMode.isSelected() && doneCutting) {
            //log("Pure mode repairing barricades");
            return State.REPAIR;
        } else if ((voidK != null && myPlayer().isInArea(startArea))
                || new RS2Interface(client.getBot(), 244).getChild(0) != null
                || (voidK != null && myPlayer().isInArea(middleArea))
                || (TesterPestGui.chkboxPureMode.isSelected() && !doneCutting)
                || (westTreeTop != null && myPlayer().isInArea(westTreeTop))
                || (eastTreeTop != null && myPlayer().isInArea(eastTreeTop))) {
            if (TesterPestGui.chkboxPureMode.isSelected()) {
                //log("Games started walking to tree's");
                return State.GAMESTARTEDPUREMODE;
            } else {
                log("Games started walking to first portal");
                return State.GAMESTARTED;
            }
        } else if (portalDead && !TesterPestGui.chkboxPureMode.isSelected()) {
            log("Switching portals, current is dead");
            return State.NEXTPORTAL;
        } else if ((spinner != null || Portal != null || miscMobs != null)
                && !TesterPestGui.chkboxPureMode.isSelected()) {
            //log("Fighting");
            return State.FIGHTING;
        }
        log("hmm..");
        return State.IDLE;
    }

    public void runningCheck() throws InterruptedException {
    }

    public void gameStarted2(int portals) throws InterruptedException {
    }

    public void gameStartedPureRetry() throws InterruptedException {
        playingPC = true;
        Inventory bag = client.getInventory();
        if (!setPaths) {
            log("Setting Paths");
            walkMiniMap(new Position(x, y - 7, 0));
            voidK = closestNPCForName("Void Knight");
            voidKx = voidK.getX();
            voidKy = voidK.getY();
            startArea = new Area(voidKx, voidKy + 15, voidKx + 3, voidKy + 22);
            middleArea = new Area(voidKx - 13, voidKy - 7, voidKx + 14, voidKy + 16);

            eGate = new Area(voidKx + 12, voidKy - 2, voidKx + 14, voidKy + 3);

            eastBar1 = new Area(voidKx + 15, voidKy - 3, voidKx + 19, voidKy + 2);
            eastBar2 = new Area(voidKx + 18, voidKy - 9, voidKx + 22, voidKy - 4);
            eastBar3 = new Area(voidKx + 18, voidKy - 22, voidKx + 22, voidKy - 17);
            eastBar4 = new Area(voidKx + 8, voidKy - 16, voidKx + 14, voidKy - 12);

            // pathing
            toEastG = new int[][]{{voidKx + 6, voidKy + 7}, {voidKx + 14, voidKy}};

            // Barriers
            toEastBar2 = new int[][]{{voidKx + 16, voidKy}, {voidKx + 19, voidKy - 6}};
            toEastBar3 = new int[][]{{voidKx + 19, voidKy - 7}, {voidKx + 21, voidKy - 12}, {voidKx + 21, voidKy - 19}};
            toEastBar4 = new int[][]{{voidKx + 21, voidKy - 19}, {voidKx + 17, voidKy - 15}, {voidKx + 11, voidKy - 13}};

            initialWest = new Area(voidKx - 15, voidKy + 16, voidKx - 12, voidKy - 23);

            pos1 = new Position(voidKx - 12, voidKy + 21, 0);
            pos2 = new Position(voidKx - 11, voidKy + 13, 0);
            pos3 = new Position(voidKx - 10, voidKy + 4, 0);
            pos4 = new Position(voidKx - 4, voidKy + 10, 0);
            pos5 = new Position(voidKx + 10, voidKy + 14, 0);
            
            area1 = new Area(voidKx - 15, voidKy + 16, voidKx - 12, voidKy + 23);
            area2 = new Area(voidKx - 11, voidKy + 11, voidKx - 7, voidKy + 14);
            area3 = new Area(voidKx - 11, voidKy + 3, voidKx - 6, voidKy + 8);
            area4 = new Area(voidKx - 4, voidKy + 8, voidKx + 7, voidKy + 12);
            area5 = new Area(voidKx + 7, voidKy + 3, voidKx + 12, voidKy + 14);
            
            // Booleans
            doneCutting = false;
            initialWalk = false;
            
            area1Done = false;
            area2Done = false;
            area3Done = false;
            area4Done = false;
            area5Done = false;

            bar1Done = false;
            bar2Done = false;
            bar3Done = false;
            bar4Done = false;

            //Done
            setPaths = true;
        }

        if (bag.isFull()) {
            doneCutting = true;
        } else if (!bag.isFull() && ((myPlayer().isInArea(startArea))
                || (!myPlayer().isInArea(area1) && !initialWalk))) {
            log("Walking to East Tree's");
            walkMiniMap(pos1);
            sleep(random(1000, 1500));
            //tree = closestObjectForName("Tree");
            //walkedPassedGate = false;
            initialWalk = true;
        }
    }

    public void cutting() throws InterruptedException {
        if (area1 != null && myPlayer().isInArea(area1)) {
            if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                tree = closestObjectForName("Tree");
                if (area1.contains(tree)) {
                    tree.interact("Chop down");
                    sleep(random(600, 700));
                } else if (!tree.isInArea(area1)) {
                    area1Done = true;
                    walk(pos2);
                }
            }
        } else if (area1Done && !area2Done) {
            if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                tree = closestObjectForName("Tree");
                if (area2.contains(tree)) {
                    tree.interact("Chop down");
                    sleep(random(600, 700));
                } else if (!tree.isInArea(area2)) {
                    area2Done = true;
                    walk(pos3);
                }
            }
        } else if (area2Done && !area3Done) {
            if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                tree = closestObjectForName("Tree");
                if (area3.contains(tree)) {
                    tree.interact("Chop down");
                    sleep(random(600, 700));
                } else if (!tree.isInArea(area3)) {
                    area3Done = true;
                    walk(pos4);
                }
            }
        } else if (area3Done && !area4Done) {
            if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                tree = closestObjectForName("Tree");
                if (area4.contains(tree)) {
                    tree.interact("Chop down");
                    sleep(random(600, 700));
                } else if (!tree.isInArea(area4)) {
                    area4Done = true;
                    walk(pos5);
                }
            }
        } else if (area4Done && !area5Done) {
            if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                tree = closestObjectForName("Tree");
                if (area5.contains(tree)) {
                    tree.interact("Chop down");
                    sleep(random(600, 700));
                } else if (tree == null || !tree.isInArea(area4)) {
                    area5Done = true;
                    doneCutting = true;
                }
            }
        }
    }

    public void walkToGatePure() throws InterruptedException {
        if (myPlayer().isInArea(wGate) && !walkedPassedGate) {
            if (gate != null) {
                if (gate.getId() == doorO1 || gate.getId() == doorO2
                        || gate.getId() == doorO3
                        || gate.getId() == doorO4
                        || gate.getId() == doorO5) {
                    walk(westBar1);
                    walkedPassedGate = true;
                } else {
                    gate.interact("Open");
                    sleep(random(400, 700));
                    walk(westBar1);
                    walkedPassedGate = true;
                }
            }

        } else if (!myPlayer().isInArea(eGate) && !walkedPassedGate && !myPlayer().isMoving()) {
            log("Didn't walk past gate and not near the gate");
            WalkAlongPath(toEastG, true);
        }
    }

    public void repairPure() throws InterruptedException {
        if (myPlayer().isInArea(startArea)) {
            deathWalk = true;
        }
        if (!bar1Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1200, 2100));
            } else if (myPlayer().isInArea(eastBar1)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) <= 45) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar1)) {
                    fence.interact("Repair");
                    sleep(random(2000, 3000));
                    fence = closestObject(Barricades);
                } else if (!eastBar1.contains(fence)) {
                    bar1Done = true;
                    WalkAlongPath(toEastBar2, true);
                }
            } else if (eastBar1 != null && !myPlayer().isInArea(eastBar1)) {
                walk(eastBar1);
            }

        } else if (!bar2Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 2000));
            } else if (myPlayer().isInArea(eastBar2)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) <= 45) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar2)) {
                    fence.interact("Repair");
                    sleep(random(2000, 3000));
                    fence = closestObject(Barricades);
                } else if (!eastBar2.contains(fence)) {
                    bar2Done = true;
                    WalkAlongPath(toEastBar3, true);
                }
            } else if (eastBar2 != null && !myPlayer().isInArea(eastBar2)) {
                walk(eastBar2);
            }

        } else if (!bar3Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 1200));
            } else if (myPlayer().isInArea(eastBar3)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) <= 45) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar3)) {
                    fence.interact("Repair");
                    sleep(random(2000, 3000));
                    fence = closestObject(Barricades);
                } else if (!eastBar3.contains(fence)) {
                    bar3Done = true;
                    WalkAlongPath(toEastBar4, true);
                }
            } else if (!myPlayer().isInArea(eastBar3)) {
                WalkAlongPath(toEastBar3, true);
            }

        } else if (!bar4Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 1200));
            } else if (myPlayer().isInArea(eastBar4)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) <= 45) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar4)) {
                    fence.interact("Repair");
                    sleep(random(2000, 3000));
                    fence = closestObject(Barricades);
                } else {
                    bar4Done = true;
                }
            } else if (!myPlayer().isInArea(eastBar4)) {
                bar4Done = true;
                WalkAlongPath(toEastBar4, true);
            }

        } else if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
            sleep(random(1000, 2000));
        }
    }

    public void fight() throws InterruptedException {
    }

    public void nextPortal() {
    }

    public void buyItems() throws InterruptedException {
        stop();
    }

    //~~~~~~~~~~~~~~~~~~ HELPERS ~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
        if (client.getMyPlayer().isMoving()
                && distanceToPoint(path[destination][0], path[destination][1]) > (isRunning() ? 3
                : 2)) {
            return;
        }
        if (AscendThroughPath && destination != path.length - 1
                || !AscendThroughPath && destination != 0) {
            destination += (AscendThroughPath ? 1 : -1);
        }
        try {
            log("Walking to node:" + destination);
            walk(new Position(path[destination][0], path[destination][1], 0));
            Thread.sleep(700 + MethodProvider.random(600));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math
                .pow(client.getMyPlayer().getX() - pointX, 2)
                + Math.pow(client.getMyPlayer().getY() - pointY, 2));
    }

    long timeTnl(double xpTNL, double xpPH) {

        if (xpPH > 0) {
            long timeTNL = (long) ((xpTNL / xpPH) * 3600000.0D);
            return timeTNL;
        }
        return 0;
    }

    private String formatTime(long time) {
        int sec = (int) (time / 1000L), d = sec / 86400, h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return new StringBuilder()
                .append(d < 10 ? new StringBuilder().append("0").append(d)
                .toString() : Integer.valueOf(d))
                .append(":")
                .append(h < 10 ? new StringBuilder().append("0").append(h)
                .toString() : Integer.valueOf(h))
                .append(":")
                .append(m < 10 ? new StringBuilder().append("0").append(m)
                .toString() : Integer.valueOf(m))
                .append(":")
                .append(s < 10 ? new StringBuilder().append("0").append(s)
                .toString() : Integer.valueOf(s)).toString();
    }
}
