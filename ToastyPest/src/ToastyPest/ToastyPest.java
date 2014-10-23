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
import javax.swing.JOptionPane;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.RS2Object;
import org.osbot.script.rs2.skill.Combat;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author = "toastedmeat", name = "ToastyPest",
        info = "Plays Pest Control New features everyday! check the forum to request whatever you want!!", version = 1)
public class ToastyPest extends Script {

    //abuda
    private final double Version = 1.70;

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

    final Area pestControlIsland = new Area(2638, 2638, 2670, 2663);

    final Area bankArea = new Area(2665, 2653, 2669, 2655);
    //Normal
    private Area startArea, middleArea, eGate, wGate, sGate;
    //pures
    private Area eastBar1, eastBar2, eastBar3, eastBar4, eastBar5, eastBar6,
            area1, area2, area3, area4, area5;
    private Position pos1, pos2, pos3, pos4, pos5;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Timers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private long startTime = 0L, millis = 0L, hours = 0L;
    private long minutes = 0L, seconds = 0L;

    //move timers
    private long idleStartTime;
    private long lastMoveTimer;

    //Paths
    private int[][] toEastG, toEastP, toWestG, toWestP, toSouth, toSW, toSE, switchSWToW, switchSEToSW, switchEToSE;

    private final int[][] toBankNov = new int[][]{{2657, 2639}, {2657, 2643}, {2666, 2653}};
    private final int[][] toBankMed = new int[][]{{2644, 2644}, {2654, 2646}, {2666, 2653}};
    private final int[][] toBankHard = new int[][]{{2638, 2653}, {2644, 2647}, {2658, 2649}, {2666, 2653}};
    //For pures
    private int[][] toEastBar2, toEastBar3, toEastBar4, toEastBar5, toEastBar6;

    //portal
    private int westPortal = 554, eastPortal = 280,
            sEastPortal = 372, sWestPortal = 962;
    
    // East = 280, SE = 372, SW = 962, W = 554
    // SEO = 612 SWO, WO, EO = 520

    private int begPortal = 1;

    //players positon
    private int x, y, voidKx, voidKy, strPrayer = 5, attPrayer = 5;
    //Booleans
    private boolean playingPC, isFighting, fightingMisc, portalDead, setPaths,
            wonGame, prayerActivated, usingPrayer, usingSpec;

    private boolean atkPort, purpleWest, blueEast, yellowSouthE, redSouthW;
    // Booleans for pures
    private boolean firstBoatMove, initialWalk, doneCutting, bar1Done, bar2Done, bar3Done,
            bar4Done, bar5Done, bar6Done, walkedPassedGate, area1Done, area2Done, area3Done,
            area4Done, area5Done, doneBanking;

    //names for misc mobs
    private final String[] mobs = {"Torcher", "Defiler", "Brawler", "Ravager"};
    private NPC spinner, brawler, Portal, miscMobs, voidK;
    private Entity tree, fence, plank;

    private final int[] Barricades = new int[]{14227, 14228, 14229, 14230, 14231, 14232};

    //doors
    private final int[] gates = new int[]{14233, 14237, 14241, 14234, 14238, 14242, 14245, 14246};
    final int[] gatesO = new int[]{14234, 14238, 14242, 14245, 14246};

    private final int doorO1 = 14234, doorO2 = 14238, doorO3 = 14242, doorO4 = 14245, doorO5 = 14246;
    private RS2Object gate;

    // Rectangles for buying
    final RectangleDestination exchangeBottom = new RectangleDestination(475, 220, 10, 25);
    final RectangleDestination confirm = new RectangleDestination(225, 285, 45, 10);
    private int startLastMove, itemToBuy;
    private String buyingItem = "";

    //weapons speed/spec
    private double weaponSpeed;
    private int specPerc, specTemp;
    final RectangleDestination specButton = new RectangleDestination(575, 415, 130, 13);

    // for PAINT
    private int winCounter, currentPoints;
    private double winPH, pointsPercent;
    private String timeTM;
    private Image image, imageTwo;
    final Rectangle hidePaint = new Rectangle(random(10, 500), 462, 60, 12);
    final Rectangle mainPanels = new Rectangle(493, 302, 15, 15);
    final Rectangle infoPanels = new Rectangle(493, 302, 15, 15);

    boolean change;
    //Mouse Listener
    private Point pointer;

    //running checks
    private boolean run;
    private int randomInt, runInt;
    RectangleDestination runBtn = new RectangleDestination(625, 415, 30, 30);

    // Death Anim
    final int deathAnim = 836;

    //ClippingPlanes bot.getClient().getClippingPlanes()
    public final int WALL_NORTH_WEST = 0x1, WALL_NORTH = 0x2;
    public final int WALL_NORTH_EAST = 0x4, WALL_EAST = 0x8;
    public final int WALL_SOUTH_EAST = 0x10, WALL_SOUTH = 0x20;
    public final int WALL_SOUTH_WEST = 0x40, WALL_WEST = 0x80;
    public final int BLOCKED = 0x100, WATER = 0x200000;

    //Rectangles
    final Rectangle rectAtt = new Rectangle(125, 303, 260, 12);
    final Rectangle rectAttColor = new Rectangle(109, 375, 0, 18);
    private State state = State.IDLE;

    //~~~~~~~~~~~~~~~~~~~~~~~~ Inherited overrides ~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onStart() {

        try {
            //get the awesome paint for loot
            image = ImageIO.read(new URL("http://i1279.photobucket.com/albums/y535/eloo12/OP03Gea_zpsa507804b.png"));
            imageTwo = ImageIO.read(new URL("http://i1279.photobucket.com/albums/y535/eloo12/qTgCWYe_zps6d698735.png"));
        } catch (IOException e) {
            log("Couldn't get the picture :(");
            e.printStackTrace();
        }

        //for running check
        run = false;
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
        specTemp = specPerc;
        setPaths = false;
        wonGame = false;
        doneBanking = false;
        change = false;

        //Tree's 
        initialWalk = false;
        doneCutting = false;
        walkedPassedGate = false;

        //Barricades
        bar1Done = false;
        bar2Done = false;
        bar3Done = false;
        bar4Done = false;

        //Initialization 
        ToastyPestGui.buildGUI();
        ToastyPestGui.frame.setVisible(true);
        ToastyPestGui.frame.setSize(235, 470);
        ToastyPestGui.frame.setLocation(300, 300);
        ToastyPestGui.frame.setTitle(ToastyPestGui.frame.getTitle() + " V" + Version);

        while (ToastyPestGui.frame.isVisible()) {
            try {
                sleep(random(500, 600));
            } catch (InterruptedException ex) {
                Logger.getLogger(ToastyPest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        weaponSpeed = (Double.parseDouble(ToastyPestGui.textWeaponSpeed.getText()) * 1000);
        specPerc = Integer.parseInt(ToastyPestGui.textSpecPercent.getText());
        usingPrayer = ToastyPestGui.chkboxPrayer.isSelected();
        usingSpec = ToastyPestGui.chkboxSpec.isSelected();
        atkPort = ToastyPestGui.chkboxAtkPort.isSelected();

        if (!ToastyPestGui.chkboxDontBuy.isSelected()) {
            if (ToastyPestGui.rdbtnBody.isSelected()) {
                itemToBuy = 94;
                buyingItem = "Void Knight body";
            } else if (ToastyPestGui.rdbtnLegs.isSelected()) {
                itemToBuy = 95;
                buyingItem = "Void Knight legs";
            } else if (ToastyPestGui.rdbtnGloves.isSelected()) {
                itemToBuy = 96;
                buyingItem = "Void Knight gloves";
            } else if (ToastyPestGui.rdbtnRHelm.isSelected()) {
                itemToBuy = 120;
                buyingItem = "Void Ranging Helm";
            } else if (ToastyPestGui.rdbtnMeleeHelm.isSelected()) {
                itemToBuy = 121;
                buyingItem = "Void Melee Helm";
            } else if (ToastyPestGui.rdbtnMageHelm.isSelected()) {
                itemToBuy = 119;
                buyingItem = "Void Mage Helm";
            } else if (ToastyPestGui.rdbtnAtt.isSelected()) {
                itemToBuy = 108;
                buyingItem = "Void Att Exp";
            } else if (ToastyPestGui.rdbtnStr.isSelected()) {
                itemToBuy = 109;
                buyingItem = "Void Str Exp";
            } else if (ToastyPestGui.rdbtnDef.isSelected()) {
                itemToBuy = 110;
                buyingItem = "Void Def Exp";
            } else if (ToastyPestGui.rdbtnRng.isSelected()) {
                itemToBuy = 111;
                buyingItem = "Void Rng Exp";
            } else if (ToastyPestGui.rdbtnMage.isSelected()) {
                itemToBuy = 112;
                buyingItem = "Void Mage Exp";
            } else if (ToastyPestGui.rdbtnHP.isSelected()) {
                itemToBuy = 113;
                buyingItem = "Void HP Exp";
            }
        } else {
            itemToBuy = 0;
            buyingItem = "XP Shutting down at 250 points!";
        }

        if (ToastyPestGui.rdbtnAtt1.isSelected()) {
            attPrayer = 6;
        } else if (ToastyPestGui.rdbtnAtt2.isSelected()) {
            attPrayer = 9;
        } else if (ToastyPestGui.rdbtnAtt3.isSelected()) {
            attPrayer = 15;
        }
        if (ToastyPestGui.rdbtnStr1.isSelected()) {
            strPrayer = 5;
        } else if (ToastyPestGui.rdbtnStr2.isSelected()) {
            strPrayer = 8;
        } else if (ToastyPestGui.rdbtnStr3.isSelected()) {
            strPrayer = 14;
        }
        if (ToastyPestGui.rdbtnChiv.isSelected()) {
            attPrayer = 28;
            strPrayer = 0;
        } else if (ToastyPestGui.rdbtnPiety.isSelected()) {
            attPrayer = 29;
            strPrayer = 0;
        }

        if (ToastyPestGui.rdbtnRng1.isSelected()) {
            attPrayer = 22;
            strPrayer = 0;
        } else if (ToastyPestGui.rdbtnRng2.isSelected()) {
            attPrayer = 24;
            strPrayer = 0;
        } else if (ToastyPestGui.rdbtnRng3.isSelected()) {
            attPrayer = 26;
            strPrayer = 0;
        }
        String boat = "";
        if (ToastyPestGui.rdbtnNovice.isSelected()) {
            boat = ToastyPestGui.rdbtnNovice.getText();
        } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
            boat = ToastyPestGui.rdbtnMedium.getText();
        } else if (ToastyPestGui.rdbtnHard.isSelected()) {
            boat = ToastyPestGui.rdbtnHard.getText();
        }

        ToastyPestGui.btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                weaponSpeed = (Double.parseDouble(ToastyPestGui.textWeaponSpeed.getText()) * 1000);
                atkPort = ToastyPestGui.chkboxAtkPort.isSelected();

                if (!ToastyPestGui.chkboxDontBuy.isSelected()) {
                    if (ToastyPestGui.rdbtnBody.isSelected()) {
                        itemToBuy = 94;
                        buyingItem = "Void Knight body";
                    } else if (ToastyPestGui.rdbtnLegs.isSelected()) {
                        itemToBuy = 95;
                        buyingItem = "Void Knight legs";
                    } else if (ToastyPestGui.rdbtnGloves.isSelected()) {
                        itemToBuy = 96;
                        buyingItem = "Void Knight gloves";
                    } else if (ToastyPestGui.rdbtnRHelm.isSelected()) {
                        itemToBuy = 120;
                        buyingItem = "Void Ranging Helm";
                    } else if (ToastyPestGui.rdbtnMeleeHelm.isSelected()) {
                        itemToBuy = 121;
                        buyingItem = "Void Melee Helm";
                    } else if (ToastyPestGui.rdbtnMageHelm.isSelected()) {
                        itemToBuy = 119;
                        buyingItem = "Void Mage Helm";
                    } else if (ToastyPestGui.rdbtnAtt.isSelected()) {
                        itemToBuy = 108;
                        buyingItem = "Void Att Exp";
                    } else if (ToastyPestGui.rdbtnStr.isSelected()) {
                        itemToBuy = 109;
                        buyingItem = "Void Str Exp";
                    } else if (ToastyPestGui.rdbtnDef.isSelected()) {
                        itemToBuy = 110;
                        buyingItem = "Void Def Exp";
                    } else if (ToastyPestGui.rdbtnRng.isSelected()) {
                        itemToBuy = 111;
                        buyingItem = "Void Rng Exp";
                    } else if (ToastyPestGui.rdbtnMage.isSelected()) {
                        itemToBuy = 112;
                        buyingItem = "Void Mage Exp";
                    }
                } else {
                    itemToBuy = 0;
                    buyingItem = "XP Shutting down at 250 points!";
                }
                if (ToastyPestGui.rdbtnNovice.isSelected()) {
                    log("Novice boat selected");
                } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                    log("Medium boat selected");
                } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                    log("Hard boad selected");
                }

                log("Buying: " + buyingItem);
                log("Weapon Speed: " + ToastyPestGui.textWeaponSpeed.getText());
                log("Script Updated");
            }
        });

        ToastyPestGui.btnUpdatePrayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                usingPrayer = ToastyPestGui.chkboxPrayer.isSelected();
                usingSpec = ToastyPestGui.chkboxSpec.isSelected();
                specPerc = Integer.parseInt(ToastyPestGui.textSpecPercent.getText());
                specTemp = specPerc;
                if (ToastyPestGui.rdbtnAtt1.isSelected()) {
            attPrayer = 6;
        } else if (ToastyPestGui.rdbtnAtt2.isSelected()) {
            attPrayer = 9;
        } else if (ToastyPestGui.rdbtnAtt3.isSelected()) {
            attPrayer = 15;
        }
        if (ToastyPestGui.rdbtnStr1.isSelected()) {
            strPrayer = 5;
        } else if (ToastyPestGui.rdbtnStr2.isSelected()) {
            strPrayer = 8;
        } else if (ToastyPestGui.rdbtnStr3.isSelected()) {
            strPrayer = 14;
        }
        if (ToastyPestGui.rdbtnChiv.isSelected()) {
            attPrayer = 28;
            strPrayer = 0;
        } else if (ToastyPestGui.rdbtnPiety.isSelected()) {
            attPrayer = 29;
            strPrayer = 0;
        }

                if (ToastyPestGui.rdbtnRng1.isSelected()) {
                    attPrayer = 22;
                    strPrayer = 0;
                } else if (ToastyPestGui.rdbtnRng2.isSelected()) {
                    attPrayer = 24;
                    strPrayer = 0;
                } else if (ToastyPestGui.rdbtnRng3.isSelected()) {
                    attPrayer = 26;
                    strPrayer = 0;
                }

                if (ToastyPestGui.chkboxPrayer.isSelected()) {
                    log("AttPrayer: " + attPrayer);
                    log("StrPrayer: " + strPrayer);
                }
                if (ToastyPestGui.chkboxSpec.isSelected()) {
                    log("Specing at: " + specPerc);
                }
                log("Script Updated");
            }
        });

        ToastyPestGui.btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //start timer
                startTime = System.currentTimeMillis();

                //Idle Timer
                idleStartTime = System.currentTimeMillis();
                lastMoveTimer = 0;
                winCounter = 0;
                log("Paint was reset!");
            }
        });

        specTemp = specPerc;

        /*
        JOptionPane.showMessageDialog(null, "Please support scripters and help them to get paid. \n"
                + "The administrators have not been paying us and when they do, Its always less\n"
                + "Then what it should be or insanely late\n"
                + "This will lead to no updates, if it continues.");
        */
        log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log("Toasty Pest Control Beta release");
        log("On the " + boat + " boat");
        log("Buying: " + buyingItem);
        log("Using Prayer: " + usingPrayer);
        log("Using Specs: " + usingSpec + " @ " + specPerc);
        log("Pure Mode is released!!");
        log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public int onLoop() throws InterruptedException {
        state = checkStates();
        if (!playingPC) {
            x = myPlayer().getX();
            y = myPlayer().getY();
            // random portal
            begPortal = random(1, 4);
        }
        //log("starting switch");
        switch (state) {
            case OUTSIDEBOAT:
                outsideBoat();
                break;
            case INSIDEBOAT:
                insideBoat();
                break;
            case GAMESTARTED:
                wonGame = true;
                gameStarted2(begPortal);
                break;
            case FIGHTING:
                fight();
                break;
            case NEXTPORTAL:
                nextPortal();
                break;
            case BUYITEMS:
                buyItems();
                break;
            case BANK:
                bank();
                break;
            case GAMESTARTEDPUREMODE:
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
            default:
                log("Idle");
                break;
        }

        // checks to make sure we're running
        runningCheck();
        //log("runn done/ loop done");
        return random(300, 500);
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
        timeTM = formatTime(timeTnl((250 - currentPoints), (2 * (winCounter * 3600000.0D / (System.currentTimeMillis() - startTime)))));
        pointsPercent = (100 * ((double) currentPoints / 250));
        int pointsPercentI = (int) (pointsPercent * 2.97);
        rectAttColor.setSize(pointsPercentI, 12);

        if (ToastyPestGui.frame != null) {
            if (ToastyPestGui.rdbtnHidePaint.isSelected()) {
            } else if (ToastyPestGui.rdbtnShowPaint.isSelected()) {
                if (ToastyPestGui.rdbtnHidePaintInfo.isSelected()) {
                    // awesome paint ^_^
                    g2.drawImage(image, 4, 268, null);
                    
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    0.00f));
                    g2.fill(infoPanels);

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.00f));
                    
                    g2.drawString("" + winCounter, 78, 357);
                    g2.drawString("" + state, 405, 357);
                    g2.drawString("V " + Version, 475, 384);
                    g2.drawString(hours + " hrs " + minutes
                            + " mins " + seconds + " secs", 205, 357);
                    
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            0.60f));
                    g2.setColor(Color.BLUE);
                    g2.fill(rectAttColor);
                    int one = 384;
                    int two = 385;
                    g2.setFont(statsFont);

                    g2.setColor(Color.WHITE);
                    g2.drawString("Pts:", 131, two);
                    g2.setColor(gold);
                    g2.drawString(currentPoints + "/250", 149, two);
                    g2.setColor(Color.RED);

                    g2.drawString("|", 184, one);

                    g2.setColor(gold);
                    String newPointsPercent = Double.toString(pointsPercent);
                    g2.drawString(newPointsPercent.substring(0, 3), 194, two);
                    g2.setColor(Color.WHITE);
                    g2.drawString("%", 212, two);
                    g2.setColor(Color.RED);

                    g2.drawString("|", 220, one);

                    g2.setColor(gold);
                    g2.drawString(winPH * 2 + "", 237, two);
                    g2.setColor(Color.WHITE);
                    g2.drawString("Pts/h", 270, two);
                    g2.setColor(Color.RED);

                    g2.drawString("|", 296, one);

                    g2.setColor(Color.WHITE);
                    g2.drawString("TTM:", 299, two);
                    g2.setColor(gold);
                    g2.drawString(timeTM, 333, two);
                } else {
                    g2.drawImage(imageTwo, 4, 268, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    0.00f));
                    g2.fill(mainPanels);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.00f));
                    
                    if (ToastyPestGui.chkboxPureMode.isSelected()) {
                        g2.drawString("Pure mode!", 55, 357);
                        if(ToastyPestGui.chkboxBanking.isSelected()){
                            g2.drawString("Banking", 58, 384);
                        }
                    }
                    if (ToastyPestGui.chkboxSpec.isSelected()) {
                        g2.drawString("Spec: " + new Combat(provideBot(client.getBot())).getSpecialPercentage(), 62, 357);
                        g2.drawString("Specing @: " + specPerc, 50, 384);
                    }
                    g2.drawString("" + lastMoveTimer / 1000, 430, 358);
                    g2.drawString("" + isFighting, 240, 358);
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
    public void onMessage(String message) throws InterruptedException {
        if (message.contains("You have been given priority level")) {
            startLastMove = client.getLastMovementTime();
            idleStartTime = System.currentTimeMillis();
        }
        if (message.contains("Your inventory is too full")) {
            doneCutting = true;
        }
        if (message.contains("It's too damaged to be moved")) {
            gate.interact("Repair");
            sleep(random(700, 800));
        }
        if (message.equalsIgnoreCase("The purple, western portal shield has dropped!")) {
            purpleWest = true;
        }
        if (message.equalsIgnoreCase("The blue, eastern portal shield has dropped!")) {
            blueEast = true;
        }
        if (message.equalsIgnoreCase("The yellow, south-eastern portal shield has dropped!")) {
            yellowSouthE = true;
        }
        if (message.equalsIgnoreCase("The red, south-western portal shield has dropped!")) {
            redSouthW = true;
        }
    }

    @Override
    public void onExit() throws InterruptedException {
        ToastyPestGui.CloseGUI();
        
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~ Main Methods ~~~~~~~~~~~~~~~~~
    @Override
    public void mouseClicked(MouseEvent e) {
        pointer = e.getPoint();

        if (hidePaint.contains(pointer)) {
            ToastyPestGui.frame.setVisible(true);
        }
        if(infoPanels.contains(pointer) && !change){
            ToastyPestGui.rdbtnShowPaintInfo.setSelected(true);
            change = true;
        }else if(mainPanels.contains(pointer) && change){
           ToastyPestGui.rdbtnHidePaintInfo.setSelected(true);
           change = false;
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
        if (ToastyPestGui.chkboxPureMode.isSelected() && ToastyPestGui.chkboxBanking.isSelected()
                && ((pestControlIsland != null && myPlayer().isInArea(pestControlIsland)
                && client.getInventory().getAmount("Willow Logs") < 10) || !doneBanking)) {
            log("Getting logs");
            return State.BANK;
        } else if (myPlayer().isInArea(outsideBoat) || myPlayer().isInArea(outsideBoatMed)
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
                    && ToastyPestGui.rdbtnGloves.isSelected())
                    || (client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 200")
                    && (ToastyPestGui.rdbtnRHelm.isSelected()
                    || ToastyPestGui.rdbtnMeleeHelm.isSelected()
                    || ToastyPestGui.rdbtnMageHelm.isSelected()))
                    || (client.getInterface(407).getChild(14).getMessage().equalsIgnoreCase("Pest Points: 100")
                    && (ToastyPestGui.rdbtnAtt.isSelected() || ToastyPestGui.rdbtnDef.isSelected()
                    || ToastyPestGui.rdbtnStr.isSelected() || ToastyPestGui.rdbtnRng.isSelected()
                    || ToastyPestGui.rdbtnMage.isSelected()))) {
                log("Buying items");
                return State.BUYITEMS;
            } else {
                //log("Inside the boat, Waiting");
                return State.INSIDEBOAT;
            }
        } else if (playingPC && ToastyPestGui.chkboxPureMode.isSelected()
                && doneCutting && !walkedPassedGate) {
            //log("Died walking back");
            return State.TOGATEPURE;
        } else if (playingPC && ToastyPestGui.chkboxPureMode.isSelected()
                && !doneCutting && initialWalk) {
            return State.CUTTING;
        } else if (playingPC && ToastyPestGui.chkboxPureMode.isSelected()
                && doneCutting && walkedPassedGate) {
            //log("Pure mode repairing barricades");
            return State.REPAIR;
        } else if ((voidK != null && myPlayer().isInArea(startArea))
                || new RS2Interface(client.getBot(), 244).getChild(6) != null
                || (voidK != null && myPlayer().isInArea(middleArea))
                || (ToastyPestGui.chkboxPureMode.isSelected() && !doneCutting)) {
            if (ToastyPestGui.chkboxPureMode.isSelected()) {
                //log("Games started walking to tree's");
                return State.GAMESTARTEDPUREMODE;
            } else {
                log("Games started walking to first portal");
                return State.GAMESTARTED;
            }
        } else if (playingPC && (portalDead && !ToastyPestGui.chkboxPureMode.isSelected())) {
            log("Switching portals, current is dead");
            return State.NEXTPORTAL;
        } else if (playingPC && (spinner != null || Portal != null || miscMobs != null)
                && !ToastyPestGui.chkboxPureMode.isSelected()) {
            //log("Fighting");
            return State.FIGHTING;
        }
        log("hmm..");
        return State.IDLE;
    }

    public void runningCheck() throws InterruptedException {
        run = isRunning();
        //log("isRunning: " + Boolean.toString(run));
        if ((run == false) && (client.getRunEnergy() >= runInt)) {
            settingsTab.open();
            random(900, 1279);
            client.moveMouseTo(runBtn, false, true, false);
            openTab(Tab.INVENTORY);
        }
        if (client.getRunEnergy() < 10) {
            runInt = random(15, 47);
        }
    }

    public void insideBoat() throws InterruptedException {
        if (new RS2Interface(client.getBot(), 407).getChild(14) != null) {
            currentPoints = Integer.parseInt(new RS2Interface(client.getBot(), 407).getChild(14).getMessage().substring(13));
            //log("pointers meter");
        }
        if (!firstBoatMove && (myPlayer().isInArea(insideBoat)
                || myPlayer().isInArea(insideBoatHigh) || myPlayer().isInArea(insideBoatMed))) {
            //log("First move");
            if (ToastyPestGui.rdbtnNovice.isSelected()) {
                if (myPlayer().isInArea(insideBoat)) {
                    //client.moveMouseTo(new MainScreenTileDestination(client.getBot(),                        new Position(random(2660, 2663), random(2638, 2643), 0)), false, true, false);
                    firstBoatMove = true;
                }
            } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                if (myPlayer().isInArea(insideBoatMed)) {
                    //client.moveMouseTo(new MainScreenTileDestination(client.getBot(),                        new Position(random(2638, 2641), random(2642, 2647), 0)), false, true, false);
                    firstBoatMove = true;
                }
            } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                if (myPlayer().isInArea(insideBoatHigh)) {
                    // client.moveMouseTo(new MainScreenTileDestination(client.getBot(),                        new Position(random(2632, 2635), random(2649, 2654), 0)), false, true, false);
                    firstBoatMove = true;
                }
            }
        }
        //log("first moves done/ anti");
        if (random(1, 900) == 6) {
            log("Rotations");
            client.rotateCameraToAngle(random(10, 290));
            sleep(random(500, 600));
            moveMouseOutsideScreen();
        } else if (random(1, 550) == 2) {
            log("Move mouse");
            client.moveMouse((this.client.getLocalPlayers()
                    .get(random(0, this.client.getLocalPlayers()
                    .size() - 1))).getMouseDestination(), false);
            //client.clickMouse(false);
        } else if (random(1, 600) == 8) {
            log("Move to skills");
            openTab(Tab.SKILLS);
            sleep(random(1000, 1200));
            openTab(Tab.INVENTORY);
        } else if (random(1, 500) == 5) {
            log("Move to friends");
            openTab(Tab.FRIENDS);
            sleep(random(1000, 1200));
            openTab(Tab.INVENTORY);
        }
        //log("antis done");
    }

    public void outsideBoat() throws InterruptedException {
        if (wonGame) {
            if (new RS2Interface(client.getBot(), 243).getChild(5) != null) {
                log("Won game!");
                winCounter++;
                wonGame = false;
            }
        }
        prayerActivated = false;
        // log("1");
        playingPC = false;
        portalDead = false;
        isFighting = false;
        fightingMisc = false;
        setPaths = false;
        doneCutting = false;
        initialWalk = false;
        walkedPassedGate = false;
        firstBoatMove = false;

        purpleWest = false;
        blueEast = false;
        yellowSouthE = false;
        redSouthW = false;
        //log("10");

        plank = closestObjectForName("Gangplank");
        //log("11");
        if (plank != null) {
            plank.interact("Cross", true);
            // log("13");
        }
    }

    public void gameStarted2(int portals) throws InterruptedException {
        playingPC = true;
        if (!setPaths) {
            sleep(random(1000, 1500));
            voidK = closestNPCForName("Squire");
            voidKx = voidK.getX();
            voidKy = voidK.getY();
            startArea = new Area(voidKx + 1, voidKy - 1, voidKx + 4, voidKy + 7);
            middleArea = new Area(voidKx - 12, voidKy - 22, voidKx + 15, voidKy - 1);
            eGate = new Area(voidKx + 13, voidKy - 16, voidKx + 15, voidKy - 12);
            wGate = new Area(voidKx - 12, voidKy - 17, voidKx - 10, voidKy - 12);
            sGate = new Area(voidKx, voidKy - 22, voidKx + 3, voidKy - 21);
            // pathing for begining portals
            toEastG = new int[][]{{voidKx + 5, voidKy - 5}, {voidKx + 15, voidKy - 14}};
            toEastP = new int[][]{{voidKx + 22, voidKy - 17}};
            toWestG = new int[][]{{voidKx - 5, voidKy - 6}, {voidKx - 12, voidKy - 15}};
            toWestP = new int[][]{{voidKx - 22, voidKy - 16}};
            toSouth = new int[][]{{voidKx + 2, voidKy - 13}, {voidKx + 2, voidKy - 22}};
            toSW = new int[][]{{voidKx - 6, voidKy - 32}};
            toSE = new int[][]{{voidKx + 11, voidKy - 33}};
            // switch portals
            switchSWToW = new int[][]{{voidKx - 10, voidKy - 32}, {voidKx - 13, voidKy - 24}, {voidKx - 20, voidKy - 15}};
            switchSEToSW = new int[][]{{voidKx + 14, voidKy - 32}, {voidKx + 2, voidKy - 36}, {voidKx - 9, voidKy - 34}};
            switchEToSE = new int[][]{{voidKx + 22, voidKy - 19}, {voidKx + 20, voidKy - 27}, {voidKx + 14, voidKy - 32}};
            setPaths = true;

        }
        prayerActivated = false;
        switch (portals) {
            //East
            case 1:
                //playingPC = true;
                if ((myPlayer().getX() == x && myPlayer().getY() == y)
                        || !myPlayer().isInArea(eGate)) {
                log("Walking to East Gate");
                client.rotateCameraToAngle(random(278, 283));
                WalkAlongPath(toEastG, true);
                sleep(random(600, 800));
            }
                if (myPlayer().isInArea(eGate)) {
                    if (gate != null) {
                        if (gate.getId() == doorO1 || gate.getId() == doorO2
                                || gate.getId() == doorO3
                                || gate.getId() == doorO4
                                || gate.getId() == doorO5) {
                            walkMiniMap(new Position(voidKx + 22, voidKy - 17, 0));
                        } else {
                            gate.interact("Open");
                            sleep(random(200, 400));
                            walkMiniMap(new Position(voidKx + 22, voidKy - 17, 0));
                            //WalkAlongPath(toEastP, true);
                        }
                    }
                }
                break;
            //South-East
            case 2:
                //playingPC = true;
                if ((myPlayer().getX() == x && myPlayer().getY() == y)
                        || !myPlayer().isInArea(sGate)) {
                log("Walking to South Gate");
                client.rotateCameraToAngle(random(178, 183));
                WalkAlongPath(toSouth, true);
                sleep(random(600, 800));
            }
                if (myPlayer().isInArea(sGate)) {
                    if (gate != null) {
                        if (gate.getId() == doorO1 || gate.getId() == doorO2
                                || gate.getId() == doorO3
                                || gate.getId() == doorO4
                                || gate.getId() == doorO5) {
                            walkMiniMap(new Position(voidKx + 11, voidKy - 33, 0));
                            //WalkAlongPath(toSE, true);
                        } else {
                            gate.interact("Open");
                            sleep(random(200, 400));
                            walkMiniMap(new Position(voidKx + 11, voidKy - 33, 0));
                            //WalkAlongPath(toSE, true);
                        }
                    }
                }
                break;
            //South-West
            case 3:
                //playingPC = true;
                if ((myPlayer().getX() == x && myPlayer().getY() == y)
                        || !myPlayer().isInArea(sGate)) {
                log("Walking to South Gate");
                client.rotateCameraToAngle(random(178, 183));
                WalkAlongPath(toSouth, true);
                sleep(random(600, 800));
            }
                if (myPlayer().isInArea(sGate)) {
                    if (gate != null) {
                        if (gate.getId() == doorO1 || gate.getId() == doorO2
                                || gate.getId() == doorO3
                                || gate.getId() == doorO4
                                || gate.getId() == doorO5) {
                            walkMiniMap(new Position(voidKx - 6, voidKy - 32, 0));
                            //WalkAlongPath(toSW, true);
                        } else {
                            gate.interact("Open");
                            sleep(random(200, 400));
                            walkMiniMap(new Position(voidKx - 6, voidKy - 32, 0));
                            //WalkAlongPath(toSW, true);
                        }
                    }
                }
                break;
            //West
            case 4:
                //playingPC = true;
                if ((myPlayer().getX() == x && myPlayer().getY() == y)
                        || !myPlayer().isInArea(wGate)) {
                log("Walking to West Gate");
                client.rotateCameraToAngle(random(88, 93));
                WalkAlongPath(toWestG, true);
                sleep(random(600, 800));
            }
                if (myPlayer().isInArea(wGate)) {
                    if (gate != null) {
                        if (gate.getId() == doorO1 || gate.getId() == doorO2
                                || gate.getId() == doorO3
                                || gate.getId() == doorO4
                                || gate.getId() == doorO5) {
                            walkMiniMap(new Position(voidKx - 22, voidKy - 16, 0));
                            //WalkAlongPath(toWestP, true);
                        } else {
                            gate.interact("Open");
                            sleep(random(200, 400));
                            walkMiniMap(new Position(voidKx - 22, voidKy - 16, 0));
                            //WalkAlongPath(toWestP, true);
                        }
                    }
                }
                break;
        }
    }

    public void bank() throws InterruptedException {
        if (wonGame) {
            if (new RS2Interface(client.getBot(), 243).getChild(5) != null) {
                winCounter++;
                wonGame = false;
            }
        }
        Bank bank = client.getBank();
        if (bankArea != null
                && myPlayer().isInArea(bankArea) && !client.getInventory().isFull()) {
            Entity bankBox = closestObject(14367);
            if (bank.isOpen()) {
                if (bank.contains("Willow logs")) {
                    bank.withdrawAll(1519);
                    sleep(random(800, 900));
                } else {
                    log("Ran out of logs Shutting down!");
                    stop();
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Bank");
                        sleep(random(700, 900));
                        bank();
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        } else if (!myPlayer().isInArea(bankArea) && !client.getInventory().isFull()) {
            if (ToastyPestGui.rdbtnNovice.isSelected()) {
                WalkAlongPath(toBankNov, true);
            } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                WalkAlongPath(toBankMed, true);
            } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                WalkAlongPath(toBankHard, true);
            }
        }
        if (client.getInventory().isFull()) {
            if (ToastyPestGui.rdbtnNovice.isSelected()) {
                WalkAlongPath(toBankNov, false);
            } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                WalkAlongPath(toBankMed, false);
            } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                WalkAlongPath(toBankHard, false);
            }
            if (ToastyPestGui.rdbtnNovice.isSelected()) {
                if (myPlayer().isInArea(outsideBoat)) {
                    doneBanking = true;
                }
            } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                if (myPlayer().isInArea(outsideBoatMed)) {
                    doneBanking = true;
                }
            } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                if (myPlayer().isInArea(outsideBoatHigh)) {
                    doneBanking = true;
                }
            }

        }
    }

    public void gameStartedPureRetry() throws InterruptedException {
        playingPC = true;
        Inventory bag = client.getInventory();
        if (!setPaths) {
            sleep(random(1000, 1500));
            voidK = closestNPCForName("Squire");
            voidKx = voidK.getX();
            voidKy = voidK.getY();
            startArea = new Area(voidKx + 1, voidKy - 1, voidKx + 4, voidKy + 7);
            middleArea = new Area(voidKx - 12, voidKy - 22, voidKx + 15, voidKy - 1);

            eGate = new Area(voidKx + 13, voidKy - 16, voidKx + 15, voidKy - 12);

            eastBar1 = new Area(voidKx + 17, voidKy - 18, voidKx + 20, voidKy - 13);
            eastBar2 = new Area(voidKx + 19, voidKy - 24, voidKx + 23, voidKy - 19);
            eastBar3 = new Area(voidKx + 19, voidKy - 37, voidKx + 23, voidKy - 32);
            eastBar4 = new Area(voidKx + 10, voidKy - 31, voidKx + 15, voidKy - 27);
            eastBar5 = new Area(voidKx, voidKy - 34, voidKx + 5, voidKy - 30);
            eastBar6 = new Area(voidKx - 9, voidKy - 31, voidKx - 4, voidKy - 27);

            // pathing
            toEastG = new int[][]{{voidKx + 5, voidKy - 5}, {voidKx + 15, voidKy - 14}};

            // Barriers
            toEastBar2 = new int[][]{{voidKx + 17, voidKy - 16}, {voidKx + 20, voidKy - 22}};
            toEastBar3 = new int[][]{{voidKx + 20, voidKy - 22}, {voidKx + 22, voidKy - 29}, {voidKx + 22, voidKy - 35}};
            toEastBar4 = new int[][]{{voidKx + 22, voidKy - 35}, {voidKx + 18, voidKy - 30}, {voidKx + 12, voidKy - 28}};
            toEastBar5 = new int[][]{{voidKx + 12, voidKy - 28}, {voidKx + 3, voidKy - 31}};
            toEastBar6 = new int[][]{{voidKx + 2, voidKy - 31}, {voidKx - 6, voidKy - 28}};

            pos1 = new Position(voidKx - 11, voidKy + 7, 0);
            pos2 = new Position(voidKx - 9, voidKy - 1, 0);
            pos3 = new Position(voidKx - 9, voidKy - 11, 0);
            pos4 = new Position(voidKx - 3, voidKy - 4, 0);
            pos5 = new Position(voidKx + 11, voidKy - 1, 0);

            area1 = new Area(voidKx - 14, voidKy + 1, voidKx - 11, voidKy + 9);
            area2 = new Area(voidKx - 10, voidKy - 4, voidKx - 7, voidKy - 1);
            area3 = new Area(voidKx - 9, voidKy - 12, voidKx - 5, voidKy - 6);
            area4 = new Area(voidKx - 3, voidKy - 7, voidKx + 8, voidKy - 3);
            area5 = new Area(voidKx + 8, voidKy - 12, voidKx + 13, voidKy - 1);

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
            bar5Done = false;
            bar6Done = false;

            //Done
            walkedPassedGate = false;
            setPaths = true;
        }

        if (bag.isFull() || ToastyPestGui.chkboxBanking.isSelected()) {
            doneCutting = true;
        } else if (!bag.isFull() && ((myPlayer().isInArea(startArea))
                || (!myPlayer().isInArea(area1) && !initialWalk))) {
            log("Walking to East Tree's");
            walkMiniMap(pos1);
            sleep(random(1000, 1500));
            //tree = closestObjectForName("Tree");
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
        if (!myPlayer().isInArea(middleArea) && !myPlayer().isInArea(startArea)) {
            walkedPassedGate = true;
        }
        if (eGate != null && myPlayer().isInArea(eGate) && !walkedPassedGate) {
            if (gate != null) {
                if (gate.getId() == doorO1 || gate.getId() == doorO2
                        || gate.getId() == doorO3
                        || gate.getId() == doorO4
                        || gate.getId() == doorO5) {
                    walk(eastBar1);
                    walkedPassedGate = true;
                } else {
                    gate.interact("Open");
                    sleep(random(400, 700));
                    if (gate.getId() == doorO1 || gate.getId() == doorO2
                            || gate.getId() == doorO3
                            || gate.getId() == doorO4
                            || gate.getId() == doorO5) {
                        walk(eastBar1);
                        walkedPassedGate = true;
                    }
                }
            }

        } else if (!myPlayer().isInArea(eGate) && !walkedPassedGate && !myPlayer().isMoving()) {
            log("Didn't walk past gate and not near the gate");
            WalkAlongPath(toEastG, true);
        }
    }

    public void repairPure() throws InterruptedException {
        if (myPlayer().isInArea(startArea)) {
            walkedPassedGate = false;
        }
        if (!bar1Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1200, 2100));
            } else if (eastBar1 != null && myPlayer().isInArea(eastBar1)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar1)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
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
            } else if (eastBar2 != null && myPlayer().isInArea(eastBar2)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar2)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
                    fence = closestObject(Barricades);
                } else if (!eastBar2.contains(fence)) {
                    bar2Done = true;
                    WalkAlongPath(toEastBar3, true);
                }
            } else if (eastBar2 != null && !myPlayer().isInArea(eastBar2)) {
                walk(eastBar2);
            }

        } else if (!bar3Done) {
            if (client.getInterface(408).getChild(11).getMessage() != null
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 1200));
            } else if (eastBar3 != null && myPlayer().isInArea(eastBar3)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar3)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
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
            } else if (eastBar4 != null && myPlayer().isInArea(eastBar4)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar4)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
                    fence = closestObject(Barricades);
                } else if (!eastBar4.contains(fence)) {
                    bar4Done = true;
                    WalkAlongPath(toEastBar5, true);
                }
            } else if (!myPlayer().isInArea(eastBar4)) {
                bar4Done = true;
                WalkAlongPath(toEastBar4, true);
            }

        } else if (!bar5Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 2000));
            } else if (eastBar5 != null && myPlayer().isInArea(eastBar5)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar5)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
                    fence = closestObject(Barricades);
                } else if (!eastBar5.contains(fence)) {
                    bar5Done = true;
                    WalkAlongPath(toEastBar6, true);
                }
            } else if (eastBar5 != null && !myPlayer().isInArea(eastBar5)) {
                walk(eastBar5);
            }

        } else if (!bar6Done) {
            if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
                sleep(random(1000, 2000));
            } else if (eastBar6 != null && myPlayer().isInArea(eastBar6)
                    && Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) < 50) {
                fence = closestObject(Barricades);
                if (fence.interact("Repair", "Barricade", false, 9, true, true) && fence.isInArea(eastBar6)) {
                    fence.interact("Repair");
                    sleep(random(500, 600));
                    fence = closestObject(Barricades);
                } else if (!eastBar6.contains(fence)) {
                    bar6Done = true;
                }
            } else if (eastBar6 != null && !myPlayer().isInArea(eastBar6)) {
                walk(eastBar6);
            }

        } else if (Integer.parseInt(client.getInterface(408).getChild(11).getMessage()) >= 50) {
            sleep(random(1000, 2000));
        }
    }

    public void fight() throws InterruptedException {
        spinner = closestNPCForName("Spinner");
        Portal = closestNPCForName("Portal");
        miscMobs = closestNPCForName(mobs);
        brawler = closestNPCForName("Brawler");
        if (myPlayer().getAnimation() != deathAnim) {
            if (Portal != null) {
            } else {
                portalDead = true;
            }
            if ((Portal != null && (Math.abs(Portal.getX() - myPlayer().getX()) > 7))) {
                if (Portal.getId() == eastPortal) {
                    walkMiniMap(new Position(voidKx + 22, voidKy - 17, 0));
                } else if (Portal.getId() == westPortal) {
                    walkMiniMap(new Position(voidKx - 22, voidKy - 16, 0));
                } else if (Portal.getId() == sEastPortal) {
                    walkMiniMap(new Position(voidKx + 11, voidKy - 33, 0));
                } else if (Portal.getId() == sWestPortal) {
                    walkMiniMap(new Position(voidKx - 6, voidKy - 32, 0));
                }
            }

            if (myPlayer().getAnimation() != -1
                    && (myPlayer().isFacing(Portal) || myPlayer().isFacing(spinner)
                    || myPlayer().isFacing(miscMobs) || myPlayer().isFacing(brawler))) {
                startLastMove = client.getLastMovementTime();
                idleStartTime = System.currentTimeMillis();
            }

            if (!isFighting) {
                if (spinner != null && Math.abs(spinner.getPosition().distance(myPlayer().getPosition())) < 8) {
                    log("Attacking Spinners");
                    client.moveCameraToEntity(spinner);
                    //client.rotateCameraPitch(random(10, 60));
                    spinner.interact("Attack");
                    isFighting = true;
                } else if (Portal != null && atkPort && ((Portal.getModel().getVerticeCount() == eastPortal)
                        || (Portal.getModel().getVerticeCount() == sWestPortal)
                        || (Portal.getModel().getVerticeCount() == sEastPortal)
                        || (Portal.getModel().getVerticeCount() == westPortal))) {
                    log("Attacking Portal");
                    client.moveCameraToEntity(Portal);
                    Portal.interact("Attack");
                    isFighting = true;
                } else if (miscMobs != null && Math.abs(miscMobs.getPosition().distance(myPlayer().getPosition())) < 8) {
                    log("Attacking misc");
                    miscMobs.interact("Attack");
                    fightingMisc = true;
                    isFighting = true;
                }
            } else {
                if (fightingMisc) {
                    if (spinner != null) {
                        isFighting = false;
                        fightingMisc = false;
                    } else if (Portal != null && atkPort && ((Portal.getModel().getVerticeCount() == eastPortal)
                        || (Portal.getModel().getVerticeCount() == sWestPortal)
                        || (Portal.getModel().getVerticeCount() == sEastPortal)
                        || (Portal.getModel().getVerticeCount() == westPortal))) {
                        isFighting = false;
                        fightingMisc = false;
                    } else if ((lastMoveTimer) >= weaponSpeed && !myPlayer().isAnimating()
                            && (!myPlayer().isFacing(spinner)
                            || !myPlayer().isFacing(Portal) || !myPlayer().isFacing(miscMobs)
                            || !myPlayer().isFacing(brawler))) {
                        startLastMove = client.getLastMovementTime();
                        idleStartTime = System.currentTimeMillis();
                        isFighting = false;
                    }
                } else if (myPlayer().isFacing(Portal) && isFighting && spinner != null
                        && Math.abs(spinner.getPosition().distance(myPlayer().getPosition())) < 8) {
                    isFighting = false;
                } else if ((lastMoveTimer) >= weaponSpeed && !myPlayer().isAnimating()
                        && (!myPlayer().isFacing(spinner)
                        || !myPlayer().isFacing(Portal) || !myPlayer().isFacing(miscMobs)
                        || !myPlayer().isFacing(brawler))) {
                    startLastMove = client.getLastMovementTime();
                    idleStartTime = System.currentTimeMillis();
                    isFighting = false;
                }
            }

            if (usingPrayer) {
                if (!prayerActivated) {
                    openTab(Tab.PRAYER);
                    client.getInterface(271).getChild(attPrayer).hover();
                    sleep(random(500, 600));
                    client.clickMouse(false);
                    sleep(random(500, 600));
                    if (strPrayer != 0) {
                        client.getInterface(271).getChild(strPrayer).hover();
                        sleep(random(500, 600));
                        client.clickMouse(false);
                        sleep(random(500, 600));
                    }
                    openTab(Tab.INVENTORY);
                    prayerActivated = true;
                }
            }
            if (usingSpec) {
                if (combat.getSpecialPercentage() >= specPerc) {
                    if (!combat.isSpecialActivated()) {
                        openTab(Tab.ATTACK);
                        sleep(random(500, 600));
                        client.moveMouseTo(specButton, false, true, false);
                        sleep(random(500, 600));
                        openTab(Tab.INVENTORY);
                    }
                }
                specPerc = random(specTemp, 100);
            }
        }
    }

    public void nextPortal() throws InterruptedException {
        //east client.getInterface(408).getChild(13).getMessage() // w
        // client.getInterface(408).getChild(14).getMessage() e
        // client.getInterface(408).getChild(15).getMessage() se
        // client.getInterface(408).getChild(16).getMessage() sw
        brawler = closestNPCForName("Brawler");
        if (brawler != null && brawler.getPosition().distance(myPlayer().getPosition()) < 2) {
            if (!brawler.isUnderAttack()) {
                brawler.interact("Attack");
            }
        } else if (playingPC) {
            if (myPlayer().getX() > (x + 14)) {
                log("East to SE");
                //E To SE
                WalkAlongPath(switchEToSE, true);
                portalDead = false;

            } else if (myPlayer().getX() > x && myPlayer().getX() <= (x + 14)
                    && !client.getInterface(408).getChild(14).getMessage().equalsIgnoreCase("0")) {
                //SE TO E
                WalkAlongPath(switchEToSE, false);
                portalDead = false;
                log("SE to East");

            } else if (myPlayer().getX() > x && myPlayer().getX() <= (x + 14)
                    && client.getInterface(408).getChild(14).getMessage().equalsIgnoreCase("0")) {
                //SE TO SW
                WalkAlongPath(switchSEToSW, true);
                portalDead = false;
                log("SE to SW");

            } else if (myPlayer().getX() > (x - 14) && myPlayer().getX() <= x
                    && !client.getInterface(408).getChild(13).getMessage().equalsIgnoreCase("0")) {
                //SW TO W
                WalkAlongPath(switchSWToW, true);
                portalDead = false;
                log("SW to W");

            } else if (myPlayer().getX() > (x - 14) && myPlayer().getX() <= x) {
                //SW TO SE
                WalkAlongPath(switchSEToSW, false);
                portalDead = false;
                log("SW to SE");

            } else if (myPlayer().getX() <= (x - 15)) {
                //W TO SW
                WalkAlongPath(switchSWToW, false);
                portalDead = false;
                log("W To SW");
            }
        }
    }

    public void nextPortalTest() throws InterruptedException {
        //east client.getInterface(408).getChild(13).getMessage() // w
        // client.getInterface(408).getChild(14).getMessage() e
        // client.getInterface(408).getChild(15).getMessage() se
        // client.getInterface(408).getChild(16).getMessage() sw
        NPC port = closestNPCForName("Portal");
        brawler = closestNPCForName("Brawler");
        if (brawler != null && brawler.getPosition().distance(myPlayer().getPosition()) < 2.5) {
            if (!brawler.isUnderAttack()) {
                brawler.interact("Attack");
            }
        } else if (playingPC) {
            if (client.getInterface(408).getChild(14).getMessage().equalsIgnoreCase("0")
                    && !client.getInterface(408).getChild(15).getMessage().equalsIgnoreCase("0")) {
                log("East to SE");
                //E To SE
                WalkAlongPath(switchEToSE, true);
                if (Portal != null) {
                    portalDead = false;
                }

            } else if (!client.getInterface(408).getChild(14).getMessage().equalsIgnoreCase("0")) {
                //SE TO E
                WalkAlongPath(switchEToSE, false);
                if (Portal != null) {
                    portalDead = false;
                }
                log("SE to East");

            } else if (client.getInterface(408).getChild(14).getMessage().equalsIgnoreCase("0")
                    && client.getInterface(408).getChild(15).getMessage().equalsIgnoreCase("0")
                    && !client.getInterface(408).getChild(16).getMessage().equalsIgnoreCase("0")) {
                //SE TO SW
                WalkAlongPath(switchSEToSW, true);
                if (port != null) {
                    portalDead = false;
                }
                log("SE to SW");

            } else if (!client.getInterface(408).getChild(13).getMessage().equalsIgnoreCase("0")) {
                //SW TO W
                WalkAlongPath(switchSWToW, true);
                if (port != null) {
                    portalDead = false;
                }
                log("SW to W");

            } else if (client.getInterface(408).getChild(13).getMessage().equalsIgnoreCase("0")
                    && client.getInterface(408).getChild(16).getMessage().equalsIgnoreCase("0")) {
                //SW TO SE w e se sw 13 14 15 16
                WalkAlongPath(switchSEToSW, false);
                if (port != null) {
                    portalDead = false;
                }
                log("SW to SE");

            } else if (client.getInterface(408).getChild(13).getMessage().equalsIgnoreCase("0")) {
                //W TO SW
                WalkAlongPath(switchSWToW, false);
                if (port != null) {
                    portalDead = false;
                }
                log("W To SW");
            }
        }
    }

    public void buyItems() throws InterruptedException {
        if (!ToastyPestGui.chkboxDontBuy.isSelected()) {
            if (myPlayer().isInArea(insideBoat)) {
                Entity ladder = closestObjectForName("Ladder");
                ladder.interact("Climb");
                sleep(random(800, 900));
            }
            if (myPlayer().isInArea(outsideBoat)) {
                walkMiniMap(new Position(2662, 2650, 0));
                sleep(random(2500, 2700));
            }

            NPC exchanger = closestNPCForName("Void Knight");
            exchanger.interact("Exchange");
            sleep(random(1200, 1500));
            if (ToastyPestGui.rdbtnBody.isSelected() || ToastyPestGui.rdbtnLegs.isSelected()
                    || ToastyPestGui.rdbtnGloves.isSelected() || ToastyPestGui.rdbtnMageHelm.isSelected()
                    || ToastyPestGui.rdbtnRHelm.isSelected() || ToastyPestGui.rdbtnMeleeHelm.isSelected()) {
                client.moveMouseTo(exchangeBottom, true, true, false);
                sleep(random(900, 1000));
            }

            client.getInterface(267).getChild(itemToBuy).hover();
            sleep(random(900, 1000));
            client.clickMouse(false);
            sleep(random(900, 1000));
            client.moveMouseTo(confirm, true, true, false);
            sleep(random(900, 1000));
            client.clickMouse(false);
            sleep(random(900, 1000));
            if (ToastyPestGui.rdbtnNovice.isSelected()) {
                walkMiniMap(new Position(2657, 2639, 0));
                sleep(random(1500, 1800));
            } else if (ToastyPestGui.rdbtnMedium.isSelected()) {
                walkMiniMap(new Position(2650, 2646, 0));
                sleep(random(1500, 1800));
                walkMiniMap(new Position(2644, 2643, 0));
                sleep(random(1500, 1800));
            } else if (ToastyPestGui.rdbtnHard.isSelected()) {
                walkMiniMap(new Position(2650, 2646, 0));
                sleep(random(1500, 1800));
                walkMiniMap(new Position(2638, 2653, 0));
                sleep(random(1500, 1800));
            }
        } else {
            stop();
        }
    }

    //~~~~~~~~~~~~~~~~~~ HELPERS ~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
            //log("Loop");
            //sleep(100);
            if (distanceToPoint(path[i][0], path[i][1]) < distanceToPoint(
                    path[destination][0], path[destination][1])) {
                destination = i;
            }
        }

        if (client.getMyPlayer().isMoving() && (distanceToPoint(path[destination][0], path[destination][1]) > (isRunning() ? 3
                : 2))) {
            log("Walk nothing");
            return;
        }
        if (AscendThroughPath && destination != path.length - 1
                || !AscendThroughPath && destination != 0) {
            destination += (AscendThroughPath ? 1 : -1);
        }
        log("Walking to point:" + destination);
        if (new Position(path[destination][0], path[destination][1], 0).distance(myPlayer().getPosition()) < 35) {
            walk(new Position(path[destination][0], path[destination][1], 0));
        }
        log("Done");
        sleep(random(100, 200));
        log("sleep Done");

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
