/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toastybdragonspriv;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.osbot.script.MethodProvider;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.GroundItem;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.skill.Skill;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.EquipmentSlot;
import org.osbot.script.rs2.ui.EquipmentTab;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

/**
 *
 * @author Eric
 */
@ScriptManifest(author = "toastedmeat", info = "Kills Blue Dragons Privately",
        name = "ToastyBDragonsPriv", version = 42)
public class ToastyBDragonsPriv extends Script {

    final int myVersion = 52;

    //~~~~~~~~~~~~~~~~~~~~~~Text~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final Font regFont = new Font("Serif", 0, 12);
    private final Font statsFont = new Font("Serif", 0, 10);
    private final Font bigFont = new Font("Serif", Font.BOLD, 16);
    private final Font versionFont = new Font("Serif", Font.BOLD, 35);
    private final Color white = new Color(255, 255, 255);
    private final Color black = new Color(0, 0, 0);
    private final Color gold = new Color(255, 255, 0);
    private final Color blue = new Color(9, 9, 81);
    private final Color red = new Color(255, 0, 0);
    private final Color healthC = new Color(255, 69, 0);
    private final Color rangeC = new Color(0, 255, 0);
    private final Color attackC = new Color(0, 255, 255);
    private final Color strengthC = new Color(245, 245, 220);
    private final Color defenceC = new Color(255, 215, 0);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~Areas~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    final Area castleWarsArea = new Area(2438, 3083, 2444, 3094);
    final Area castleBank = new Area(2442, 3082, 2443, 3084);
    final Area onChest = new Area(2443, 3083, 2443, 3083);

    final Area castleBankDoor = new Area(2442, 3088, 2444, 3091);
    final Area outsideCastle = new Area(2448, 3089, 2452, 3090);

    final Area cityEntrance = new Area(2504, 3062, 2510, 3063);
    final Area onTopOfGate = new Area(2504, 3062, 2504, 3063);
    final Area enteredCity = new Area(2503, 3062, 2503, 3063);

    final Area failToCaveEnt2 = new Area(2507, 3048, 2515, 3054);
    final Area failToCaveEnt = new Area(2511, 3050, 2517, 3047);
    final Area caveEntrance = new Area(2506, 3035, 2516, 3046);
    final Area onTopOfCave = new Area(2506, 3039, 2506, 3039);

    final Area enteredCave = new Area(2588, 9408, 2589, 9411);

    final Area dragonTop = new Area(2604, 9455, 2616, 9465);
    final Area dragonBottom = new Area(2601, 9439, 2611, 9451);

    final Area safeZone = new Area(2614, 9445, 2618, 9449);

    final Area safeSpotTop = new Area(2610, 9454, 2611, 9454);
    final Area safeSpotBottom = new Area(2611, 9444, 2611, 9444);

    final Area safeSpotBotTop = new Area(2611, 9445, 2611, 9445);
    final Area safeSpotBotMid = new Area(2611, 9444, 2611, 9444);
    final Area safespotBotBot = new Area(2611, 9441, 2611, 9441);

    final Position psafeSpotTop = new Position(2613, 9455, 0);
    final Position psafeSpotBottom = new Position(2611, 9444, 0);

    final Area lumbridgeDead = new Area(3218, 3212, 3225, 3226);

    // ~~~~~~~~~~~~~~~~~~~~~~~Paths~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    final int[][] FROM_CASTLE_BANK_DOOR = new int[][]{{2451, 3090},
    {2461, 3075}, {2474, 3074}, {2488, 3070}, {2501, 3071}, {2516, 3070},
    {2530, 3066}, {2544, 3063}, {2548, 3052}, {2533, 3054}, {2520, 3062},
    {2507, 3063}};
    final int[][] FROM_GATE_TO_ENT = new int[][]{{2501, 3052}, {2514, 3050},
    {2508, 3043}, {2508, 3037}};
    final int[][] FROM_ENT_TO_DRAG = new int[][]{{2588, 9421}, {2581, 9430},
    {2574, 9437}, {2571, 9435}};

    final int[][] JOHNNYS_PATH = new int[][]{{2589, 9423}, {2598, 9435}, {2608, 9442}, {2613, 9450}};

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ID's~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    final int bankChest = 4483;

    final int doorLeft = 20648, doorRight = 20130;

    final int doorOpen = 2444, doorClose = 2445;

    final int gateLeft = 2788, gateRight = 2798;

    final int caveEntrace = 2804;

    final int[] blueDragonBottomTop = new int[]{1112, 1113};
    final int blueDragonBottom = 1112;
    final int blueDragonTop = 1113;
    int dragonID;

    final int animationDead = 92;

    final int dragonShield = 1540;
    final int mithBoltId = 9142;

    final int[] rings = new int[]{2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};

    final int[] itemsInBagAtStart = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 145, 157, 163};
    final int[] itemsInBagAtStartFourDose = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 2436, 2440, 2442};

    final int[] itemsInBagAtStartRng = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 169};
    final int[] itemsInBagAtStartFourDoseRng = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 2444};

    final int[] miscItems = new int[]{561, /*449, adamantite ore*/ 1213,
        985, 987, 1249, 2366, 1149};

    int[] IDBAD = {199, 201, 203, 1917, 1971, 526, 995, 205,
        209, 211, 213, 215, 217, 219, 2485, 886, 1243};
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Loot~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    String[] toLoot = {"Nature rune, Blue dragonhide, Dragon bones"
        + "Adamantite ore, Rune dagger, half keys, dragon spear, shield left"
        + "d med"};

    double priceBones, priceHides, profit = 0;
    double miscCounter, hidesCounter, boneCounter;

    int profitPH = 0, bonesPH, hidesPH, miscPH;

    GroundItem bones, hides, misc, boltType;
    String boltToLoot;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Timers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public long startTime = 0L, millis = 0L, hours = 0L;
    public long minutes = 0L, seconds = 0L, last = 0L;

    //~~~~~~~~~~~~~~~~~~~~~~~Start Skills Lv~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double strength, attack, defence, range, hp;
    //~~~~~~~~~~~~~~~~~~~~~ Start Skill exp~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double startAtt, startStr, startDef, startRng, startHP;
    //~~~~~~~~~~~~~~~~~~~~~~Skill levels gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double attGainedLv, strGainedLv, defGainedLv, rngGainedLv, hpGainedLv;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp Gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double attExp, strExp, defExp, rngExp, hpExp;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double attExpTNL, strExpTNL, defExpTNL, rngExpTNL, hpExpTNL;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp %TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double attPercent, strPercent, defPercent, rngPercent, hpPercent;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp PH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double attExpPH, strExpPH, defExpPH, rngExpPH, hpExpPH;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp PH String~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    String attExpPHS, strExpPHS, defExpPHS, rngExpPHS, hpExpPHS;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~Time TNL for Skills~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    String timeStrTNL, timeAttTNL, timeDefTNL, timeRngTNL, timeHpTNL;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Potion Checker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    double potAtt, potStr, potDef, potRng;

    boolean fourDose = false, isRanging = false,
            usingStrPot, usingAttPot, usingDefPot, usingRngPot;
    //food to use
    String food;
    int foodID, foodAmount;

    Object[] options = {"Lobster", "Monkfish", "Shark"};

    //Camera view
    int pitch;
    int angle;

    //for running
    boolean run, runningCheck;
    int randomInt, runInt;

    // various checks
    int antiban;
    boolean itemsReady, shieldReady, ringReady, isFighting;

    // Walking checks
    boolean doneBanking, openedDoor, walkedToGate, openedGate,
            walkedToEnt, enteredCaveArea, atDragons;

    //Monsters
    NPC dragon, dragonTopNPC, dragonBottomNPC;
    int killedDragons, switcher;
    final boolean isWalkingAllowed = false;

    //Paint Image
    Image image;
    //Rectangles to hide info
    Rectangle showGUI = new Rectangle(random(10, 300), 462, 60, 12);
    Rectangle rectHp = new Rectangle(200, 280, 260, 12);
    Rectangle rectRng = new Rectangle(200, 290, 260, 12);
    Rectangle rectStr = new Rectangle(200, 300, 260, 12);
    Rectangle rectAtt = new Rectangle(200, 310, 260, 12);
    Rectangle rectDef = new Rectangle(200, 320, 260, 12);

    Rectangle rectHpColor = new Rectangle(200, 280, 0, 12);
    Rectangle rectRngColor = new Rectangle(200, 290, 0, 12);
    Rectangle rectStrColor = new Rectangle(200, 300, 0, 12);
    Rectangle rectAttColor = new Rectangle(200, 310, 0, 12);
    Rectangle rectDefColor = new Rectangle(200, 320, 0, 12);

    RectangleDestination runBtn = new RectangleDestination(625, 415, 30, 30);

    //Gui
    boolean hideE = false;
    boolean showE = true;
    boolean hideG = false;
    boolean showG = true;
    //Mouse Listener
    Point pointer;
    //idleTimer
    public int startLastMove;
    public long lastMoveTimer, idleStartTime;
    //first status
    String status = "Booting UP!!!";

    //The different States of the script
    enum State {

        IDLE, STARTING, READYTOSTART, CHECKFORSHIELD, CHECKFORITEMSRNG,
        CHECKFORITEMS, OPENCASTLEDOOR, CHECKFORRING,
        WALKTOGATE, OPENGATE, GATETODUNGEON, ENTERDUNGEON, ANTIBAN,
        WALKTODRAGONS, FIGHT, EAT, POTIONS, GOTOBANK, BANK, BANKFOURDOSE,
        BANKRNG, BANKFOURDOSERNG, FAILEDBANKING, LOOT, FIGHTRNG;
    }

    public State state = State.IDLE;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Inherited Methods from client~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onStart() {
        status = "Starting script";
        state = State.STARTING;
        //Checking for updates
        checkForUpdate();
        client.setMouseSpeed(random(12, 16));
        //Init Loot Variables
        priceBones = 0;
        boneCounter = 0;
        bonesPH = 0;
        priceHides = 0;
        hidesCounter = 0;
        hidesPH = 0;
        miscCounter = 0;
        miscPH = 0;
        // Init other Variables
        run = false;
        runningCheck = true;
        antiban = 0;
        itemsReady = false;
        shieldReady = false;
        ringReady = false;
        isFighting = false;
        killedDragons = 0;
        switcher = 1;
        dragonID = 0;
        food = "";
        foodID = 0;
        // Init Walking Checks
        doneBanking = false;
        openedDoor = false;
        walkedToGate = false;
        openedGate = false;
        walkedToEnt = false;
        enteredCaveArea = false;
        atDragons = false;

        //Starting level for skills
        attack = client.getSkills().getLevel(Skill.ATTACK);
        strength = client.getSkills().getLevel(Skill.STRENGTH);
        defence = client.getSkills().getLevel(Skill.DEFENCE);
        range = client.getSkills().getLevel(Skill.RANGED);
        hp = client.getSkills().getLevel(Skill.HITPOINTS);

        //starting exp for skills
        startAtt = client.getSkills().getExperience(Skill.ATTACK);
        startStr = client.getSkills().getExperience(Skill.STRENGTH);
        startDef = client.getSkills().getExperience(Skill.DEFENCE);
        startRng = client.getSkills().getExperience(Skill.RANGED);
        startHP = client.getSkills().getExperience(Skill.HITPOINTS);

        //start timer
        startTime = System.currentTimeMillis();
        idleStartTime = System.currentTimeMillis();
        lastMoveTimer = 0;

        //Randomize running starter
        randomInt = random(1, 10);
        runInt = random(15, 27);
        //get Prices for bones/hides
        rsItem B = new rsItem("dragon+bones");
        priceBones = B.getAveragePriceDragonBones();
        rsItem H = new rsItem("blue+dragonhide");
        priceHides = H.getAveragePrice();
        image = null;
        try {
            //get the awesome paint for loot
            image = ImageIO.read(new URL("http://i1279.photobucket.com/albums/y535/eloo12/ToastyBDragonsv1Final2_zps7434b398.png"));
        } catch (IOException e) {
            log("Couldn't get the picture :(");
            e.printStackTrace();
        }

        ToastyBDragonsGui.buildGUI();
        ToastyBDragonsGui.frame.setVisible(true);
        ToastyBDragonsGui.frame.setSize(235, 420);
        ToastyBDragonsGui.frame.setLocation(300, 300);

        while (ToastyBDragonsGui.frame.isVisible()) {
            try {
                sleep(random(500, 600));
            } catch (InterruptedException ex) {
                Logger.getLogger(ToastyBDragonsPriv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fourDose = ToastyBDragonsGui.rdbtnPotsFour.isSelected();
        isRanging = ToastyBDragonsGui.chkboxisRanging.isSelected();
        boltToLoot = ToastyBDragonsGui.textBoltsToLoot.getText();
        shieldReady = ToastyBDragonsGui.chkboxShieldReady.isSelected();
        ringReady = ToastyBDragonsGui.chkboxRingReady.isSelected();
        doneBanking = ToastyBDragonsGui.chkboxDoneBanking.isSelected();
        itemsReady = ToastyBDragonsGui.chkboxItemsReady.isSelected();
        openedDoor = ToastyBDragonsGui.chkboxOpenedDoor.isSelected();
        walkedToGate = ToastyBDragonsGui.chkboxWalkedToGate.isSelected();
        openedGate = ToastyBDragonsGui.chkboxOpenedGate.isSelected();
        walkedToEnt = ToastyBDragonsGui.chkboxWalkedToEnt.isSelected();
        enteredCaveArea = ToastyBDragonsGui.chkboxEnteredCaveArea.isSelected();
        atDragons = ToastyBDragonsGui.chkboxAtDragons.isSelected();
        isFighting = ToastyBDragonsGui.chkboxIsFighting.isSelected();
        usingStrPot = ToastyBDragonsGui.chkboxStrPot.isSelected();
        usingAttPot = ToastyBDragonsGui.chkboxAttPot.isSelected();
        usingDefPot = ToastyBDragonsGui.chkboxDefPot.isSelected();
        usingRngPot = ToastyBDragonsGui.chkboxRngPot.isSelected();

        if (ToastyBDragonsGui.rdbtnLobster.isSelected()) {
            food = "Lobster";
            foodID = 379;
        } else if (ToastyBDragonsGui.rdbtnMonkfish.isSelected()) {
            food = "Monkfish";
            foodID = 7946;
        } else {
            food = "Shark";
            foodID = 385;
        }
        if (!ToastyBDragonsGui.textAmount.getText().equals("")) {
            String foodInputAmount = ToastyBDragonsGui.textAmount.getText();
            foodAmount = Integer.parseInt(foodInputAmount);
        }

        ToastyBDragonsGui.btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ToastyBDragonsGui.frame.setVisible(false);
                String foodInputAmount = ToastyBDragonsGui.textAmount.getText();
                foodAmount = Integer.parseInt(foodInputAmount);
                if (ToastyBDragonsGui.rdbtnLobster.isSelected()) {
                    food = "Lobster";
                    foodID = 379;
                } else if (ToastyBDragonsGui.rdbtnMonkfish.isSelected()) {
                    food = "Monkfish";
                    foodID = 7946;
                } else {
                    food = "Shark";
                    foodID = 385;
                }
                isRanging = ToastyBDragonsGui.chkboxisRanging.isSelected();
                fourDose = ToastyBDragonsGui.rdbtnPotsFour.isSelected();

                log("Ranging?: " + isRanging);
                log("Using Pots(4): " + Boolean.toString(fourDose));
                log("Using: " + foodAmount + " " + food + "(s)");
                log("Script Updated");
            }
        });

        ToastyBDragonsGui.btnUpdateDebug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ToastyBDragonsGui.frame.setVisible(false);
                shieldReady = ToastyBDragonsGui.chkboxShieldReady.isSelected();
                ringReady = ToastyBDragonsGui.chkboxRingReady.isSelected();
                doneBanking = ToastyBDragonsGui.chkboxDoneBanking.isSelected();
                itemsReady = ToastyBDragonsGui.chkboxItemsReady.isSelected();
                openedDoor = ToastyBDragonsGui.chkboxOpenedDoor.isSelected();
                walkedToGate = ToastyBDragonsGui.chkboxWalkedToGate.isSelected();
                openedGate = ToastyBDragonsGui.chkboxOpenedGate.isSelected();
                walkedToEnt = ToastyBDragonsGui.chkboxWalkedToEnt.isSelected();
                enteredCaveArea = ToastyBDragonsGui.chkboxEnteredCaveArea.isSelected();
                atDragons = ToastyBDragonsGui.chkboxAtDragons.isSelected();
                isFighting = ToastyBDragonsGui.chkboxIsFighting.isSelected();
                log("Updated booleans");
            }
        });

        ToastyBDragonsGui.btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //Loot/Kill variables
                killedDragons = 0;
                boneCounter = 0;
                bonesPH = 0;
                hidesCounter = 0;
                hidesPH = 0;
                miscCounter = 0;
                miscPH = 0;

                //Starting level for skills
                attack = client.getSkills().getLevel(Skill.ATTACK);
                strength = client.getSkills().getLevel(Skill.STRENGTH);
                defence = client.getSkills().getLevel(Skill.DEFENCE);
                range = client.getSkills().getLevel(Skill.RANGED);
                hp = client.getSkills().getLevel(Skill.HITPOINTS);

                //starting exp for skills
                startAtt = client.getSkills().getExperience(Skill.ATTACK);
                startStr = client.getSkills().getExperience(Skill.STRENGTH);
                startDef = client.getSkills().getExperience(Skill.DEFENCE);
                startRng = client.getSkills().getExperience(Skill.RANGED);
                startHP = client.getSkills().getExperience(Skill.HITPOINTS);

                //start timer
                startTime = System.currentTimeMillis();

                //Idle Timer
                idleStartTime = System.currentTimeMillis();
                lastMoveTimer = 0;

            }
        });

        state = State.READYTOSTART;

        log("Ranging?: " + isRanging);
        log("Using Pots(4): " + Boolean.toString(fourDose));
        log("Using: " + foodAmount + " " + food + "(s)");

        log("Toasty BDragons v" + myVersion);
        log("Price of Bones: " + Double.toString(priceBones));
        log("Price of Hides: " + Double.toString(priceHides));
        log("Easily 100k+");
    }

    @Override
    public void onLogin(int code) {
        if (code == 6) {
            idleStartTime = System.currentTimeMillis();
            lastMoveTimer = 0;
        }
    }

    @Override
    public int onLoop() throws InterruptedException {
        status = "Checking States";

        startLastMove = client.getLastMovementTime();

        //First Check to see what to do
        state = checkStates();
        // Does different things depending on what is happening
        switch (state) {
            //Ready check for shield
            case CHECKFORSHIELD:
                checkForShield();
                break;
            //Ready check for ring of life
            case CHECKFORRING:
                checkForRing();
                break;
            //Ready check with items
            case CHECKFORITEMS:
                checkForItems();
                break;
            case CHECKFORITEMSRNG:
                checkForItemsRng();
                break;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // door open/closed check
            case OPENCASTLEDOOR:
                openCastleDoor();
                break;
            // walk to ogres gate
            case WALKTOGATE:
                walkToGate();
                break;
            // open the citys gate
            case OPENGATE:
                openGate();
                break;
            // walk to dungeon entrance
            case GATETODUNGEON:
                gateToDungeon();
                break;
            // enter the dungeon
            case ENTERDUNGEON:
                enterDungeon();
                break;
            // walk to the blue dragons
            case WALKTODRAGONS:
                walkToDragons();
                break;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // fight
            case FIGHT:
                fight();
                break;
            case FIGHTRNG:
                fightRng();
                break;
            //check to use super potions
            case POTIONS:
                potions();
                break;
            // banking time
            case GOTOBANK:
                goToBank();
                break;
            // bank stuffs gained
            case BANK:
                bank();
                break;
            case BANKFOURDOSE:
                bankFourDose();
                break;
            case BANKRNG:
                bankRng();
                break;
            case BANKFOURDOSERNG:
                bankFourDoseRng();
                break;
            // loot items
            case LOOT:
                loot();
                break;
            case EAT:
                eat();
                break;
            case ANTIBAN:
                AntiBan();
                break;
        }
        // checks to make sure we're running
        runningCheck();

        if (client.getInventory().contains("Coins") || client.getInventory().contains("Herb")
                || client.getInventory().contains("Steel arrow")) {
            client.getInventory().dropAllForIds(IDBAD);
            sleep(random(900, 1000));
        } else {
        }

        return random(600, 900);
    }

    @Override
    public void onMessage(String message) throws InterruptedException {
        if (message.contains("There is no ammo left in your quiver.")) {
            log("Out of Arrows/Bolts Stopping");
            stop();
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void onPaint(Graphics g) throws NullPointerException {
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

        // Client view
        pitch = client.getCameraPitch();
        angle = client.getCameraPitchAngle();

        //~~~~~~~~~~~~~~~~~~~~~~Skill levels gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attGainedLv = (client.getSkills().getLevel(Skill.ATTACK) - attack);
        strGainedLv = (client.getSkills().getLevel(Skill.STRENGTH) - strength);
        defGainedLv = (client.getSkills().getLevel(Skill.DEFENCE) - defence);
        rngGainedLv = (client.getSkills().getLevel(Skill.RANGED) - range);
        hpGainedLv = (client.getSkills().getLevel(Skill.HITPOINTS) - hp);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp Gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attExp = (client.getSkills().getExperience(Skill.ATTACK) - startAtt);
        strExp = (client.getSkills().getExperience(Skill.STRENGTH) - startStr);
        defExp = (client.getSkills().getExperience(Skill.DEFENCE) - startDef);
        rngExp = (client.getSkills().getExperience(Skill.RANGED) - startRng);
        hpExp = (client.getSkills().getExperience(Skill.HITPOINTS) - startHP);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attExpTNL = experienceToNextLevel(Skill.ATTACK);
        strExpTNL = experienceToNextLevel(Skill.STRENGTH);
        defExpTNL = experienceToNextLevel(Skill.DEFENCE);
        rngExpTNL = experienceToNextLevel(Skill.RANGED);
        hpExpTNL = experienceToNextLevel(Skill.HITPOINTS);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp %~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attPercent = 100 * (experiencePrevLevel(Skill.ATTACK) / experienceTotalNeeded(Skill.ATTACK));
        strPercent = 100 * (experiencePrevLevel(Skill.STRENGTH) / experienceTotalNeeded(Skill.STRENGTH));
        defPercent = 100 * (experiencePrevLevel(Skill.DEFENCE) / experienceTotalNeeded(Skill.DEFENCE));
        rngPercent = 100 * (experiencePrevLevel(Skill.RANGED) / experienceTotalNeeded(Skill.RANGED));
        hpPercent = 100 * (experiencePrevLevel(Skill.HITPOINTS) / experienceTotalNeeded(Skill.HITPOINTS));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp PH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attExpPH = ((int) (attExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        strExpPH = ((int) (strExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        defExpPH = ((int) (defExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        rngExpPH = ((int) (rngExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        hpExpPH = ((int) (hpExp * 3600000.0D / (System.currentTimeMillis() - startTime)));

        //~~~~~~~~~~~~~~~~~~~~~~~~~Skills time TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        timeAttTNL = formatTime(timeTnl(attExpTNL, attExpPH));
        timeStrTNL = formatTime(timeTnl(strExpTNL, strExpPH));
        timeDefTNL = formatTime(timeTnl(defExpTNL, defExpPH));
        timeRngTNL = formatTime(timeTnl(rngExpTNL, rngExpPH));
        timeHpTNL = formatTime(timeTnl(hpExpTNL, hpExpPH));

        // ~~~~~~~~~~ profit ~~~~~~~~~~~
        profit = ((priceBones * boneCounter) + (priceHides * hidesCounter)) / 1000;
        profitPH = ((int) (profit * 3600000.0D / (System.currentTimeMillis() - startTime)));
        bonesPH = ((int) (boneCounter * 3600000.0D / (System
                .currentTimeMillis() - startTime)));
        hidesPH = ((int) (hidesCounter * 3600000.0D / (System
                .currentTimeMillis() - startTime)));
        miscPH = ((int) (miscCounter * 3600000.0D / (System
                .currentTimeMillis() - startTime)));
        // Fill in color of Skills percentage till next level
        int hpPercentI = (int) (hpPercent * 2.6);
        rectHpColor.setSize(hpPercentI, 12);
        int attPercentI = (int) (attPercent * 2.6);
        rectAttColor.setSize(attPercentI, 12);
        int strPercentI = (int) (strPercent * 2.6);
        rectStrColor.setSize(strPercentI, 12);
        int defPercentI = (int) (defPercent * 2.6);
        rectDefColor.setSize(defPercentI, 12);
        int rngPercentI = (int) (rngPercent * 2.6);
        rectRngColor.setSize(rngPercentI, 12);

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(regFont);
        if (ToastyBDragonsGui.frame != null) {
            if (ToastyBDragonsGui.rdbtnHideExp.isSelected()) {
            } else {
                // Rectangles for Stats
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        0.60f));
                g2.setColor(black);
                g2.fill(rectHp);
                g2.setColor(healthC);
                g2.fill(rectHpColor);

                g2.setColor(black);
                g2.fill(rectAtt);
                g2.setColor(attackC);
                g2.fill(rectAttColor);

                g2.setColor(black);
                g2.fill(rectStr);
                g2.setColor(strengthC);
                g2.fill(rectStrColor);

                g2.setColor(black);
                g2.fill(rectDef);
                g2.setColor(defenceC);
                g2.fill(rectDefColor);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        1.00f));

                // HP Paint Starts
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 288);
                g2.setColor(white);
                g2.drawString("HP:", 206, 290);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                        + "/" + client.getSkills().getLevel(Skill.HITPOINTS), 230, 290);
                g2.setColor(red);
                g2.drawString("|", 253, 289);
                g2.setColor(gold);
                String newHpPercent = Double.toString(hpPercent);
                g2.drawString(newHpPercent.substring(0, 4), 263, 290);
                g2.setColor(white);
                g2.drawString("%", 283, 290);
                g2.setColor(red);
                g2.drawString("|", 291, 289);
                g2.setColor(gold);
                g2.drawString(hpExpPH / 1000 + "", 305, 290);
                g2.setColor(white);
                g2.drawString("k xp/h", 333, 290);
                g2.setColor(red);
                g2.drawString("|", 363, 289);
                g2.setColor(white);
                g2.drawString("TTL:", 369, 290);
                g2.setColor(gold);
                g2.drawString(timeHpTNL, 399, 290);
                g2.setColor(red);
                g2.drawString("] ", 458, 289);
                g2.setColor(black);
                g2.drawLine(201, 291, 459, 291);

                // Strength Paint Start
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 308);
                g2.setColor(white);
                g2.drawString("Str:", 208, 310);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.STRENGTH)
                        + "/" + client.getSkills().getLevel(Skill.STRENGTH), 228, 310);
                g2.setColor(red);
                g2.drawString("|", 257, 309);
                g2.setColor(gold);
                String newStrPercent = Double.toString(strPercent);
                g2.drawString(newStrPercent.substring(0, 3), 267, 310);

                g2.setColor(white);
                g2.drawString("%", 287, 310);
                g2.setColor(red);
                g2.drawString("|", 295, 309);
                g2.setColor(gold);
                g2.drawString(strExpPH / 1000 + "", 309, 310);
                g2.setColor(white);
                g2.drawString("k xp/h", 343, 310);
                g2.setColor(red);
                g2.drawString("|", 373, 309);
                g2.setColor(white);
                g2.drawString("TTL:", 375, 310);
                g2.setColor(gold);
                g2.drawString(timeStrTNL, 405, 310);
                g2.setColor(red);
                g2.drawString("] ", 458, 309);
                g2.setColor(black);
                g2.drawLine(201, 311, 459, 311);

                // Attack Paint Start
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 319);
                g2.setColor(white);
                g2.drawString("Att:", 208, 320);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.ATTACK)
                        + "/" + client.getSkills().getLevel(Skill.ATTACK), 228, 320);
                g2.setColor(red);
                g2.drawString("|", 257, 319);
                g2.setColor(gold);
                String newAttPercent = Double.toString(attPercent);
                g2.drawString(newAttPercent.substring(0, 3), 267, 320);
                g2.setColor(white);
                g2.drawString("%", 287, 320);
                g2.setColor(red);
                g2.drawString("|", 295, 319);
                g2.setColor(gold);
                g2.drawString(attExpPH / 1000 + "", 309, 320);
                g2.setColor(white);
                g2.drawString("k xp/h", 343, 320);
                g2.setColor(red);
                g2.drawString("|", 373, 319);
                g2.setColor(white);
                g2.drawString("TTL:", 375, 320);
                g2.setColor(gold);
                g2.drawString(timeAttTNL, 405, 320);
                g2.setColor(red);
                g2.drawString("] ", 458, 319);
                g2.setColor(black);
                g2.drawLine(201, 321, 459, 321);

                // Defence Paint Start
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 329);
                g2.setColor(white);
                g2.drawString("Def:", 208, 330);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.DEFENCE)
                        + "/" + client.getSkills().getLevel(Skill.DEFENCE), 228, 330);
                g2.setColor(red);
                g2.drawString("|", 257, 329);
                g2.setColor(gold);
                String newDefPercent = Double.toString(defPercent);
                g2.drawString(newDefPercent.substring(0, 4), 267, 330);
                g2.setColor(white);
                g2.drawString("%", 287, 330);
                g2.setColor(red);
                g2.drawString("|", 295, 329);
                g2.setColor(gold);
                g2.drawString(defExpPH / 1000 + "", 309, 330);
                g2.setColor(white);
                g2.drawString("k xp/h", 343, 330);
                g2.setColor(red);
                g2.drawString("|", 373, 329);
                g2.setColor(white);
                g2.drawString("TTL:", 375, 330);
                g2.setColor(gold);
                g2.drawString(timeDefTNL, 405, 330);
                g2.setColor(red);
                g2.drawString("] ", 458, 329);
                g2.setColor(black);
                g2.drawLine(201, 331, 459, 331);

                // Other Paint Stuff 
                g2.setFont(regFont);
                g2.setColor(white);
                g2.drawString("Running at: " + runInt, 200, 240);
                g2.drawString("Fighting: " + isFighting, 200, 250);
                // status
                g2.drawString("Status: " + status, 200, 260);

            }
        }

        if (ToastyBDragonsGui.frame != null) {
            if (ToastyBDragonsGui.rdbtnHideRng.isSelected()) {
            } else {
                // Rectangle for stats
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        0.60f));
                g2.setColor(black);
                g2.fill(rectHp);
                g2.setColor(healthC);
                g2.fill(rectHpColor);
                g2.setColor(black);
                g2.fill(rectRng);
                g2.setColor(rangeC);
                g2.fill(rectRngColor);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        1.00f));

                // HP Paint Starts
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 288);
                g2.setColor(white);
                g2.drawString("HP:", 206, 290);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                        + "/" + client.getSkills().getLevel(Skill.HITPOINTS), 228, 290);
                g2.setColor(red);
                g2.drawString("|", 253, 289);
                g2.setColor(gold);
                String newHpPercent = Double.toString(hpPercent);
                g2.drawString(newHpPercent.substring(0, 4), 263, 290);
                g2.setColor(white);
                g2.drawString("%", 283, 290);
                g2.setColor(red);
                g2.drawString("|", 291, 289);
                g2.setColor(gold);
                g2.drawString(hpExpPH / 1000 + "", 305, 290);
                g2.setColor(white);
                g2.drawString("k xp/h", 333, 290);
                g2.setColor(red);
                g2.drawString("|", 363, 289);
                g2.setColor(white);
                g2.drawString("TTL:", 369, 290);
                g2.setColor(gold);
                g2.drawString(timeHpTNL, 399, 290);
                g2.setColor(red);
                g2.drawString("] ", 458, 289);
                g2.setColor(black);
                g2.drawLine(201, 291, 459, 291);

                // Range Paint Start
                g2.setFont(statsFont);
                g2.setColor(red);
                g2.drawString("[ ", 200, 299);
                g2.setColor(white);
                g2.drawString("Rng:", 206, 300);
                g2.setColor(gold);
                g2.drawString(client.getSkills().getCurrentLevel(Skill.RANGED)
                        + "/" + client.getSkills().getLevel(Skill.RANGED), 230, 300);
                g2.setColor(red);
                g2.drawString("|", 257, 299);
                g2.setColor(gold);
                String newRngPercent = Double.toString(rngPercent);
                g2.drawString(newRngPercent.substring(0, 4), 267, 300);
                g2.setColor(white);
                g2.drawString("%", 287, 300);
                g2.setColor(red);
                g2.drawString("|", 295, 299);
                g2.setColor(gold);
                g2.drawString(rngExpPH / 1000 + "", 309, 300);
                g2.setColor(white);
                g2.drawString("k xp/h", 343, 300);
                g2.setColor(red);
                g2.drawString("|", 373, 299);
                g2.setColor(white);
                g2.drawString("TTL:", 375, 300);
                g2.setColor(gold);
                g2.drawString(timeRngTNL, 405, 300);
                g2.setColor(red);
                g2.drawString("] ", 458, 299);
                g2.setColor(black);
                g2.drawLine(201, 301, 459, 301);

                // Other Paint Stuff 
                g2.setFont(regFont);
                g2.setColor(white);
                g2.drawString("Running at: " + runInt, 200, 240);
                g2.drawString("Fighting: " + isFighting, 200, 250);
                // status
                g2.drawString("Status: " + status, 200, 260);
            }
        }
        //state in the color black
        g2.setColor(black);
        g2.drawString("State: " + state, 360, 458);
        g2.setColor(rangeC);
        g2.drawString("World: " + client.getCurrentWorld(), 10, 14);
        g2.setColor(white);
        g2.drawString("Time Elapsed: " + hours + " hours " + minutes
                + " minutes " + seconds + " seconds", 100, 14);
        g2.drawString("Idle Time: " + (lastMoveTimer / 1000) + " Seconds", 360, 14);

        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
        // 1.00f));
        if (ToastyBDragonsGui.frame != null) {
            if (ToastyBDragonsGui.rdbtnHideGp.isSelected()) {
            } else {
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //~~~~~~~~~~~~~~~~~~~Awsome sauce image for loot~~~~~~~~~~~~~~~~~~~~
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                g2.drawImage(image, 547, 207, null);

                // Stuff inside the box
                g2.setFont(bigFont);
                g2.setColor(gold);

                g2.drawString("Bones collected: " + boneCounter, 570, 270);
                g2.drawString("Bones per hour: " + bonesPH, 570, 290);
                g2.drawString("Hides collected: " + hidesCounter, 570, 314);
                g2.drawString("Hides per hour: " + hidesPH, 570, 335);
                //~~~~~~~~~~~~~~~Misc Loot~~~~~~~
                g2.drawString("Misc Items Looted: " + miscCounter, 570, 355);
                g2.drawString("Misc Items/H: " + miscPH, 570, 375);
                g2.drawString("Profit: " + profit + " k", 570, 395);
                g2.drawString("Profit/h: " + profitPH + " k/h", 570, 415);
                g2.drawString("Dragons Killed: " + killedDragons, 570, 435);

                //Version
                g2.setFont(versionFont);
                g2.drawString(Integer.toString(myVersion), 690, 245);
            }
        }
        //Hide Stat Rectangles

        g2.setFont(regFont);
        g2.setColor(blue);
        g2.fill(showGUI);
        g2.setColor(gold);
        g2.drawString("GUI", (int) showGUI.getX() + 20, (int) showGUI.getY() + 11);

    }

    @Override
    public void onExit() {
        ToastyBDragonsGui.CloseGUI();
        if (isRanging) {
            log("Ranging Exp Gained: " + rngExp);
        } else {
            log("Strength Exp Gained: " + strExp);
            log("Attack Exp Gained: " + attExp);
            log("Defence Exp Gained: " + defExp);
        }
        log("Bones collected: " + boneCounter);
        log("Hides collected: " + hidesCounter);
        log("Profit gained: " + profit + " k");
        log("Report all bugs to toastedmeat");
    }
//~~~~~~~~~~~~~~~ All the methods called by this Class~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override

    public void mouseClicked(MouseEvent e) {
        pointer = e.getPoint();

        if (showGUI.contains(pointer)) {
            ToastyBDragonsGui.frame.setVisible(true);
        }

    }

    /**
     *
     * @return the state the bot has to go into;
     * @throws InterruptedException
     */
    public State checkStates() throws InterruptedException {
        Player player = client.getMyPlayer();
        //anti ban counter
        antiban = random(1, 2500);

        // checks for items on the floor
        bones = closestGroundItemForName(new String[]{"Dragon bones"});
        hides = closestGroundItemForName(new String[]{"Blue dragonhide"});
        misc = closestGroundItem(miscItems);
        boltType = closestGroundItemForName(new String[]{boltToLoot});

        //NPC
        dragon = closestNPC(blueDragonBottomTop);
        //Potting Levels
        potStr = client.getSkills().getCurrentLevel(Skill.STRENGTH);
        potAtt = client.getSkills().getCurrentLevel(Skill.ATTACK);
        potDef = client.getSkills().getCurrentLevel(Skill.DEFENCE);
        potRng = client.getSkills().getCurrentLevel(Skill.RANGED);

        //Fail Safes
        if (lastMoveTimer > 280000 + random(-60000, 30000)) {
            log("Got stuck or Died or got Saved by Ring, Starting over!!");
            return State.GOTOBANK;
        } else if (myPlayer().isInArea(lumbridgeDead)
                && (client.getInventory().contains(2552) || client.getInventory().contains(2554)
                || client.getInventory().contains(2556) || client.getInventory().contains(2558)
                || client.getInventory().contains(2560) || client.getInventory().contains(2562)
                || client.getInventory().contains(2566) || client.getInventory().contains(2566))) {
            log("Somehow im in lumbridge Starting Over!!");
            return State.GOTOBANK;
        } else if (myPlayer().isInArea(lumbridgeDead)
                && (!client.getInventory().contains(2552) && !client.getInventory().contains(2554)
                && !client.getInventory().contains(2556) && !client.getInventory().contains(2558)
                && !client.getInventory().contains(2560) && !client.getInventory().contains(2562)
                && !client.getInventory().contains(2566) && !client.getInventory().contains(2566))) {
            log("Well i got KO'ed GG Logging out");
            stop();
            return State.IDLE;
        }
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~ SHEILD CHECK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
        if (!shieldReady) {
            log("Checking for Shield");
            return State.CHECKFORSHIELD;

        } else if (!ringReady) {
            log("Checking for ring");
            return State.CHECKFORRING;

        } else if (antiban < 46) {
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ANTI BAN ~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
            return State.ANTIBAN;

        } else if ((!client.getInventory().contains(food) && player.isInArea(castleBank))
                || (player.isInArea(onChest) && !doneBanking)
                || (player.isInArea(castleWarsArea) && !doneBanking)) {
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~BANKING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if (!isRanging) {
                if (fourDose == true) {
                    log("banking fourDose");
                    return State.BANKFOURDOSE;
                } else {
                    log("banking");
                    return State.BANK;
                }
            } else {
                if (fourDose == true) {
                    log("banking fourDose");
                    return State.BANKFOURDOSERNG;
                } else {
                    log("banking");
                    return State.BANKRNG;
                }
            }
            // ~~~~~~~~~~~~~~~~~~~~~~~~ CHECK ITEMS IN BAG ~~~~~~~~~~~~~~~~~~~~~ 
        } else if ((doneBanking && shieldReady && ringReady && !itemsReady) && player.isInArea(castleWarsArea)) {
            log("Check for Items");
            if (!isRanging) {
                return State.CHECKFORITEMS;
            } else {
                return State.CHECKFORITEMSRNG;
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~GO TO BANK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if ((!client.getInventory().contains(food)
                && client.getSkills().getCurrentLevel(Skill.HITPOINTS) <= 30)
                || (client.getInventory().isFull() && !client.getInventory().contains(food)
                && !client.getInventory().contains(229))) {
            log("back to the bank");
            return State.GOTOBANK;
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOOTING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
        } else if ((bones != null || hides != null
                || misc != null || (boltType != null && boltType.getAmount() > 3))
                && isFighting == false) {
            log("Looting the dragon");
            return State.LOOT;
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~EATTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
        } else if (dragon != null
                && client.getInventory().contains(food)
                && (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                < ((client.getSkills().getLevel(Skill.HITPOINTS) / 2) - random(-5, 5)))) {
            log("Eatting");
            return State.EAT;
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ SUPER POTS ~~~~~~~~~~~~~~~~~~~~~~~~~~ 
        } else if (((Math.abs(potStr - strength) < (strength * .08)
                && ((client.getInventory().contains(157))
                || (client.getInventory().contains(159))
                || (client.getInventory().contains(161))
                || (client.getInventory().contains(2440)))
                && usingStrPot)
                || (Math.abs(potAtt - attack) < (attack * .08)
                && ((client.getInventory().contains(145))
                || (client.getInventory().contains(147))
                || (client.getInventory().contains(149))
                || (client.getInventory().contains(2436)))
                && usingAttPot)
                || (Math.abs(potDef - defence) < (defence * .08)
                && ((client.getInventory().contains(163))
                || (client.getInventory().contains(165))
                || (client.getInventory().contains(167))
                || (client.getInventory().contains(2442)))
                && usingDefPot)
                || (Math.abs(potRng - range) < ((range * .03) + 4)
                && ((client.getInventory().contains(169))
                || (client.getInventory().contains(171))
                || (client.getInventory().contains(173))
                || (client.getInventory().contains(2444)))
                && usingRngPot)) && dragon != null) {
            log("Drinking pots");
            return State.POTIONS;
            //~~~~~~~~~~~~~~~~~~~~~~~~~~ FIGHTING FINALLY ~~~~~~~~~~~~~~~~~~~~~~ 
        } else if (dragon != null
                && (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                >= (client.getSkills().getLevel(Skill.HITPOINTS) / 2) - 15)
                && (client.getInventory().contains(food)
                || client.getSkills().getCurrentLevel(Skill.HITPOINTS) > 30)) {
            if (!isRanging) {
                return State.FIGHT;
            } else {
                return State.FIGHTRNG;
            }

            //~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~
            // ~~~~~~~~~~~~~~~~ ALL THE WALKING DONE IN THE SCRIPT  ~~~~~~~~~~~~
            //~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~
        } else if ((player.isInArea(castleWarsArea))
                || (shieldReady && ringReady && itemsReady && doneBanking && !openedDoor)) {
            log("Castle door check");
            return State.OPENCASTLEDOOR;
        } else if ((player.isInArea(outsideCastle)) || (openedDoor && !walkedToGate)) {
            log("Walking to gate");
            return State.WALKTOGATE;
        } else if ((player.isInArea(cityEntrance)) || (walkedToGate && !openedGate)) {
            log("Opened gate");
            return State.OPENGATE;
        } else if ((player.isInArea(enteredCity)) || (openedGate && !walkedToEnt)) {
            log("To the Dungeon entrance!");
            return State.GATETODUNGEON;
        } else if (player.isInArea(caveEntrance) || (walkedToEnt && !enteredCaveArea)) {
            log("Entering the scary cave");
            return State.ENTERDUNGEON;
        } else if ((player.isInArea(enteredCave)) || (enteredCaveArea && !atDragons)) {
            log("Time to profit!");
            return State.WALKTODRAGONS;
        }

        log("uhh ohh");
        return State.IDLE;
    }

    public void runningCheck() throws InterruptedException {
        run = isRunning();
        //log("isRunning: " + Boolean.toString(run));
        if ((run == false) && (client.getRunEnergy() >= runInt)) {
            status = "Toggling running";
            settingsTab.open();
            sleep(random(900, 1279));
            client.moveMouseTo(runBtn, false, true, false);
            //setRunning(true);
            sleep(random(400, 500));
            openTab(Tab.INVENTORY);
            run = true;
        } else if ((run == false) && (client.getRunEnergy() == 100)) {
            status = "Toggling running";
            settingsTab.open();
            sleep(random(986, 1393));
            client.moveMouseTo(runBtn, false, true, false);
            //setRunning(true);
            sleep(random(400, 500));
            openTab(Tab.INVENTORY);
            run = true;
        }
        if (client.getRunEnergy() < 10) {
            runInt = random(15, 47);
        }
    }

    public void checkForShield() throws InterruptedException {
        status = "Checking for anti-dragon shield";

        Bank bank = client.getBank();

        if (client.getInventory().contains("Anti-dragon shield")) {
            client.getInventory().interactWithId(dragonShield, "Wear");
            shieldReady = true;
            ToastyBDragonsGui.chkboxShieldReady.setSelected(true);
        } else if (!client.getInventory().contains("Anti-dragon shield")) {
            if (equipmentTab.isWearingItem(EquipmentSlot.SHIELD, "Anti-dragon shield")) {
                shieldReady = true;
                ToastyBDragonsGui.chkboxShieldReady.setSelected(true);
            } else {
                shieldReady = false;
                ToastyBDragonsGui.chkboxShieldReady.setSelected(false);
            }
        } else {
            if (myPlayer().isInArea(castleWarsArea)) {
                Entity chest = closestObject(bankChest);
                if (bank.isOpen()) {
                    bank.withdraw1(dragonShield);
                    sleep(random(1000, 1500));
                    bank.close();
                    client.getInventory().interactWithId(dragonShield, "Wear");
                    shieldReady = true;
                    ToastyBDragonsGui.chkboxShieldReady.setSelected(true);
                } else {
                    if (chest != null) {
                        if (chest.isVisible()) {
                            chest.interact("Use");
                            sleep(random(50, 100));
                        } else {
                            client.moveCameraToEntity(chest);
                        }
                    }
                }
            } else {
                goToBank();
            }
        }

    }

    public void checkForRing() throws InterruptedException {
        status = "Checking for Ring of Life";

        Bank bank = client.getBank();

        if (client.getInventory().contains("Ring of life")) {
            client.getInventory().interactWithName("Ring of life", "Wear");
            ringReady = true;
            ToastyBDragonsGui.chkboxRingReady.setSelected(true);
        } else if (!client.getInventory().contains("Ring of life")) {
            if (equipmentTab.isWearingItem(EquipmentSlot.RING, "Ring of life")) {
                ringReady = true;
                ToastyBDragonsGui.chkboxRingReady.setSelected(true);
            } else {
                ringReady = false;
                ToastyBDragonsGui.chkboxRingReady.setSelected(false);
                if (myPlayer().isInArea(castleWarsArea)) {
                    Entity chest = closestObject(bankChest);
                    if (bank.isOpen()) {
                        bank.withdraw1(2570);
                        sleep(random(1000, 1500));
                        bank.close();
                        client.getInventory().interactWithName("Ring of life", "Wear");
                        ringReady = true;
                        ToastyBDragonsGui.chkboxRingReady.setSelected(true);
                    } else {
                        if (chest != null) {
                            if (chest.isVisible()) {
                                chest.interact("Use");
                                sleep(random(50, 100));
                            } else {
                                client.moveCameraToEntity(chest);
                            }
                        }
                    }
                } else {
                    goToBank();
                }
            }

        }
    }

    public void checkForItems() throws InterruptedException {
        status = "Items check!";
        log("Items Check");
        client.getBank().close();
        if (!fourDose) {
            if (client.getInventory().isEmptyExcept(itemsInBagAtStart)) {
                status = "Ready to go";
                itemsReady = true;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(true);
            } else {
                itemsReady = false;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(false);
                bank();
            }
        } else {
            if (client.getInventory().isEmptyExcept(itemsInBagAtStartFourDose)) {
                status = "Ready to go";
                itemsReady = true;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(true);
            } else {
                itemsReady = false;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(false);
                bankFourDose();
            }
        }
    }

    public void checkForItemsRng() throws InterruptedException {
        status = "Items check!";
        log("Items Check");
        client.getBank().close();
        if (!fourDose) {
            if (client.getInventory().isEmptyExcept(itemsInBagAtStartRng)) {
                status = "Ready to go";
                itemsReady = true;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(true);
            } else {
                itemsReady = false;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(false);
                bankRng();
            }
        } else {
            if (client.getInventory().isEmptyExcept(itemsInBagAtStartFourDoseRng)) {
                status = "Ready to go";
                itemsReady = true;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(true);
            } else {
                itemsReady = false;
                ToastyBDragonsGui.chkboxItemsReady.setSelected(false);
                bankFourDoseRng();
            }
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    public void openCastleDoor() throws InterruptedException {
        status = "Checking if door is open";
        Entity door = closestObjectForName("Large door");
        log(Integer.toString(door.getX()));
        if (door.getX() != doorOpen) {
            door.interact("Open");
        } else if (myPlayer().isInArea(outsideCastle)) {
            openedDoor = true;
            ToastyBDragonsGui.chkboxOpenedDoor.setSelected(true);
        } else {
            walk(outsideCastle);
        }
    }

    public void walkToGate() {
        status = "Walking to Ogre City gate";
        WalkAlongPath(FROM_CASTLE_BANK_DOOR, true);
        if (!myPlayer().isInArea(cityEntrance)) {
        } else {
            walkedToGate = true;
            ToastyBDragonsGui.chkboxWalkedToGate.setSelected(true);
        }
    }

    public void openGate() throws InterruptedException {
        status = "Opening Ogre City gate";
        walkedToGate = true;
        Entity gate = closestObjectForName("City gate");
        if (gate != null && !myPlayer().isInArea(enteredCity)) {
            gate.interact("Open");
        }
        if (myPlayer().isInArea(enteredCity)) {
            openedGate = true;
            ToastyBDragonsGui.chkboxOpenedGate.setSelected(true);
        }

    }

    public void gateToDungeon() throws InterruptedException {
        status = "Walking to dungeon entrance";
        WalkAlongPath(FROM_GATE_TO_ENT, true);
        walk(caveEntrance);
        sleep(random(1000, 2000));
        walk(onTopOfCave);
        Entity entra = closestObject(caveEntrace);
        if (entra == null) {
        } else {
            walkedToEnt = true;
            ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(true);
        }
    }

    public void enterDungeon() throws InterruptedException {
        status = "Entering dungeon";
        walk(onTopOfCave);
        Entity caveEnt = closestObject(caveEntrace);
        if (caveEnt != null && caveEnt.isVisible()) {
            caveEnt.interact("Enter");
            sleep(random(6000, 7000));
        } else if (myPlayer().isInArea(enteredCave)) {
            enteredCaveArea = true;
            ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(true);
        } else {
            client.moveCameraToEntity(caveEnt);
        }

    }

    public void walkToDragons() throws InterruptedException {
        status = "Walking to dragons";
        WalkAlongPath(JOHNNYS_PATH, true);
        sleep(random(1000, 1200));
        walk(dragonBottom);
        sleep(random(2000, 3000));
        if (dragon != null) {
            atDragons = true;
            //ToastyBDragonsGui.chkboxAtDragons.setSelected(true);
        }

    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void fight() throws InterruptedException {
        status = "Fighting";

        if (isFighting == false) {
            dragon = closestAttackableNPC(blueDragonBottomTop);
            dragonID = dragon.getId();
            if (dragon != null && !dragon.isUnderAttack()) {
                if (dragon.isVisible()) {
                    if (state != State.LOOT) {
                        while (!dragon.isUnderAttack()) {
                            sleep(random(500, 800));
                            if (dragon.interact("Attack")) {
                                isFighting = true;
                                ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                            }
                        }
                        
                    } else {
                        if (myPlayer().isAnimating()) {
                            sleep(random(500, 800));
                        }
                    }
                } else {
                    switchDragonSpots();
                    client.moveCameraToEntity(dragon);
                }
            } else {
                switchDragonSpots();
                dragon = closestAttackableNPC(blueDragonBottomTop);
                dragonID = dragon.getId();
            }

        } else {

            if (isFighting == true && !myPlayer().isAnimating()
                    && (!dragon.isFacing(myPlayer()) || !myPlayer().isFacing(dragon))
                    && dragon != null && !dragon.isAnimating()) {
                dragon.interact("Attack");
                sleep(random(600, 700));
            } else if (isFighting == true && !myPlayer().isAnimating()
                    && (dragon.isFacing(myPlayer()) && !myPlayer().isFacing(dragon))
                    && dragon != null && dragon.isAnimating()) {
                dragon.interact("Attack");
                sleep(random(600, 700));
            }

            if (dragon.getAnimation() == animationDead) {
                sleep(random(3000, 3400));
                killedDragons++;
                isFighting = false;
                ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                checkStates();
            }
        }
    }

    public void fightRng() throws InterruptedException {
        status = "Fighting";

        if (isFighting == false) {
            dragon = closestAttackableNPC(blueDragonBottomTop);
            if (dragon != null && !dragon.isUnderAttack()) {
                dragonID = dragon.getId();
                log("Dragon Id is: " + Integer.toString(dragonID));
                log("Dragon underattack: " + Boolean.toString(dragon.isUnderAttack()));
                if (dragon.isVisible()) {
                    log("Dragon is visible: " + dragon.isVisible());
                    if (state != State.LOOT) {
                        if (dragonID == blueDragonBottom) {
                            if (myPlayer().isInArea(safeSpotBottom)) {
                                //client.rotateCameraPitch(random(22, 30));
                                sleep(random(500, 600));
                                log("First pitch turn bottom");
                                client.rotateCameraToAngle(random(50, 96));
                                sleep(random(1000, 1100));
                                dragon.interact("Attack", isWalkingAllowed, 33);
                                isFighting = true;
                                ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                sleep(random(500, 800));
                            } else {
                                log("Not in safe Spot bottom");
                                walkMainScreen(psafeSpotBottom, true);
                                log("Walking on the main screen bot");
                                sleep(random(500, 600));
                                //client.rotateCameraPitch(random(22, 30));
                                sleep(random(500, 600));
                                client.rotateCameraToAngle(random(50, 96));
                                sleep(random(1000, 1100));
                                dragon.interact("Attack", isWalkingAllowed, 33);
                                isFighting = true;
                                ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                sleep(random(500, 800));
                            }
                        } else {
                            if (myPlayer().isInArea(safeSpotTop)) {
                                //client.rotateCameraPitch(random(25, 35));
                                client.rotateCameraToAngle(random(0, 35));
                                sleep(random(1000, 1100));
                                dragon.interact("Attack", isWalkingAllowed, 33);
                                isFighting = true;
                                ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                sleep(random(500, 800));
                            } else {
                                log("Not in Safe spot top");// froze
                                walkMainScreen(psafeSpotTop, true);
                                sleep(random(500, 600));
                                //client.rotateCameraPitch(random(25, 35));
                                client.rotateCameraToAngle(random(0, 35));
                                sleep(random(1000, 1100));
                                dragon.interact("Attack", isWalkingAllowed, 33);
                                isFighting = true;
                                ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                sleep(random(500, 800));
                            }
                        }

                    } else {
                        if (myPlayer().isAnimating()) {
                            sleep(random(500, 800));
                        }
                    }
                } else {
                    log("Dragon is visible: " + dragon.isVisible());
                    log("Froze 1?");
                    switchDragonSpotsRng();
                }
            } else if (dragon == null) {
                switchDragonSpotsRng();
                log("Froze 33?");
                dragon = closestAttackableNPC(blueDragonBottomTop);
                log("Froze 34?");
                dragonID = dragon.getId();
                log("Froze 35?");
            }

        } else {
            if (dragonID == blueDragonBottom) {
                if (dragon.getY() <= 9442) {
                    if (!myPlayer().isInArea(safespotBotBot)) {
                        log("Not in safe spot bottom else");
                        log("Froze 2?");
                        walkExact(safespotBotBot);
                        sleep(random(500, 600));
                        client.rotateCameraToAngle(random(50, 96));
                        sleep(random(1000, 1100));
                        dragon.interact("Attack", isWalkingAllowed, 33);
                        log("Froze 36?");// froze?
                        sleep(random(800, 1200));
                    }
                } else if (dragon.getY() >= 9443 && dragon.getY() <= 9445) {
                    if (!myPlayer().isInArea(safeSpotBotMid)) {
                        log("Not in safe spot bottom else");
                        log("Froze 2?");
                        walkExact(safeSpotBotMid);
                        sleep(random(500, 600));
                        client.rotateCameraToAngle(random(50, 96));
                        sleep(random(1000, 1100));
                        dragon.interact("Attack", isWalkingAllowed, 33);
                        log("Froze 36?");// froze?
                        sleep(random(800, 1200));
                    }
                } else if (dragon.getY() >= 9446) {
                    if (!myPlayer().isInArea(safeSpotBotTop)) {
                        log("Not in safe spot bottom else");
                        log("Froze 2?");
                        walkExact(safeSpotBotTop);
                        sleep(random(500, 600));
                        client.rotateCameraToAngle(random(50, 96));
                        sleep(random(1000, 1100));
                        dragon.interact("Attack", isWalkingAllowed, 33);
                        log("Froze 36?");// froze?
                        sleep(random(800, 1200));
                    }
                }
            } else {
                if (myPlayer().isInArea(safeSpotTop)) {
                } else {
                    log("Not in safe spot top else");
                    log("Froze 6?");
                    walkExact(safeSpotTop, 8);
                    log("Froze 7?");
                    sleep(random(500, 600));
                    //client.rotateCameraPitch(random(25, 35));
                    client.rotateCameraToAngle(random(0, 35));
                    sleep(random(1000, 1100));
                    log("Froze 8?");
                    dragon.interact("Attack", isWalkingAllowed, 33);
                    sleep(random(800, 1200));
                }
            }
            if (isFighting == true && !myPlayer().isAnimating()
                    && (!dragon.isFacing(myPlayer()) || !myPlayer().isFacing(dragon))
                    && dragon != null && !dragon.isAnimating()) {
                log("Not Attacking 1");
                log("Froze 9?");
                dragon.interact("Attack", isWalkingAllowed, 33);
                log("Froze 11?");
                sleep(random(1200, 1300));
                log("Froze 12?");
            } else if (isFighting == true && !myPlayer().isAnimating()
                    && (dragon.isFacing(myPlayer()) && !myPlayer().isFacing(dragon))
                    && dragon != null && dragon.isAnimating()) {
                log("Not Attacking 2");
                log("Froze 14?");
                dragon.interact("Attack", isWalkingAllowed, 33);
                log("Froze 15?");
                sleep(random(1200, 1300));
                log("Froze 16?");
            } else if (isFighting == true && !myPlayer().isAnimating()
                    && (dragon.isFacing(myPlayer()) && myPlayer().isFacing(dragon))
                    && dragon != null && !dragon.isAnimating() && !dragon.isUnderAttack()) {
                log("Not Attacking 3");
                log("Froze 18?");
                dragon.interact("Attack", isWalkingAllowed, 33);
                log("Froze 19?");
                sleep(random(1200, 1300));
                log("Froze 20?");
            }
            if (dragon.getAnimation() == animationDead) {
                sleep(random(3000, 3400));
                killedDragons++;
                isFighting = false;
                ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                checkStates();
            }
        }
    }

    public void switchDragonSpots() throws InterruptedException {
        if (dragonID == blueDragonTop) {
            log("To top dragon");
            walk(dragonTop);
        } else {
            log("To bottom dragon");
            walk(dragonBottom);
        }
    }

    public void switchDragonSpotsRng() throws InterruptedException {
        if (dragonID == blueDragonTop) {
            log("To top dragon");
            walkExact(safeSpotTop, 8);
            log("Froze 22?");
            sleep(random(2000, 3000));
            //client.rotateCameraPitch(random(25, 35));
            client.rotateCameraToAngle(random(0, 35));
            sleep(random(400, 500));
        } else {
            log("To bottom dragon");
            walkExact(safeSpotBottom, 8);
            log("Froze 28?");
            sleep(random(2000, 3000));
            //client.rotateCameraPitch(random(10, 20));
            client.rotateCameraToAngle(random(50, 96));
            sleep(random(400, 500));
        }
    }

    public void eat() throws InterruptedException {
        status = "HP is low Eatting";
        openTab(Tab.INVENTORY);
        client.getInventory().interactWithId(foodID, "Eat");
    }

    public void loot() throws InterruptedException {
        status = "Looting";
        // checks for items on the floor
        bones = closestGroundItemForName(new String[]{"Dragon bones"});
        hides = closestGroundItemForName(new String[]{"Blue dragonhide"});
        misc = closestGroundItem(miscItems);
        boltType = closestGroundItemForName(new String[]{boltToLoot});

        if (client.getInventory().contains(food) && client.getInventory().isFull()) {
            client.getInventory().interactWithId(foodID, "Eat");
            sleep(random(400, 500));
        } else if ((client.getInventory().contains(229) && client.getInventory().isFull())) {
            client.getInventory().interactWithId(229, "Drop");
        }

        if (boltType != null && canReach(boltType) && boltType.getAmount() > 3) {
            boltType.interact("Take");
            sleep(random(800, 1100));
        } else if (misc != null && canReach(misc)) {
            misc.interact("Take");
            sleep(random(1000, 1100));
            if (!misc.exists()) {
                miscCounter++;
            } else {
                log("Nothing to count");
                sleep(random(1000, 1200));
                if (!misc.exists()) {
                    miscCounter++;
                } else {
                    log("Still nothing T.T");
                }
            }
        } else if (hides != null && canReach(hides)) {
            hides.interact("Take");
            sleep(random(1800, 2100));
            if (!hides.exists()) {
                hidesCounter++;
            } else {
                log("Nothing to count");
                sleep(random(1000, 1200));
                if (!hides.exists()) {
                    hidesCounter++;
                } else {
                    log("Still nothing T.T");
                }
            }

        } else if (bones != null && canReach(bones)) {
            bones.interact("Take");
            sleep(random(1200, 1700));
            if (!bones.exists()) {
                boneCounter++;
            } else {
                log("Nothing to count");
            }
        } else {
            log("Nothing to loot");
            if (!isRanging) {
                switchDragonSpots();
            } else {
                switchDragonSpotsRng();
            }

        }

    }

    public void potions() throws InterruptedException {
        status = "Repotting";
        if (!isRanging) {
            if (Math.abs(potStr - strength) < (strength * .08)) {
                log("Str Pot");
                if (client.getInventory().contains(2440)) {
                    client.getInventory().interactWithId(2440, "Drink");
                } else if (client.getInventory().contains(157)) {
                    client.getInventory().interactWithId(157, "Drink");
                } else if (client.getInventory().contains(159)) {
                    client.getInventory().interactWithId(159, "Drink");
                } else if (client.getInventory().contains(161)) {
                    client.getInventory().interactWithId(161, "Drink");
                }
            } else if (Math.abs(potAtt - attack) < (attack * .08)) {
                log("Att Pot");
                if (client.getInventory().contains(2436)) {
                    client.getInventory().interactWithId(2436, "Drink");
                } else if (client.getInventory().contains(145)) {
                    client.getInventory().interactWithId(145, "Drink");
                } else if (client.getInventory().contains(147)) {
                    client.getInventory().interactWithId(147, "Drink");
                } else if (client.getInventory().contains(149)) {
                    client.getInventory().interactWithId(149, "Drink");
                }
            } else if (Math.abs(potDef - defence) < (defence * .08)) {
                log("Def Pot");
                if (client.getInventory().contains(2442)) {
                    client.getInventory().interactWithId(2442, "Drink");
                } else if (client.getInventory().contains(163)) {
                    client.getInventory().interactWithId(163, "Drink");
                } else if (client.getInventory().contains(165)) {
                    client.getInventory().interactWithId(165, "Drink");
                } else if (client.getInventory().contains(167)) {
                    client.getInventory().interactWithId(167, "Drink");
                }
            }
        } else {
            if (Math.abs(potRng - range) < ((range * .03) + 4)) {
                log("Ranging Pot");
                if (client.getInventory().contains(2444)) {
                    client.getInventory().interactWithId(2444, "Drink");
                } else if (client.getInventory().contains(169)) {
                    client.getInventory().interactWithId(169, "Drink");
                } else if (client.getInventory().contains(171)) {
                    client.getInventory().interactWithId(171, "Drink");
                } else if (client.getInventory().contains(173)) {
                    client.getInventory().interactWithId(173, "Drink");
                }
            }
        }
    }

    public void goToBank() throws InterruptedException {
        status = "Teleporting to bank";
        dragon = closestNPC(blueDragonBottomTop);
        if (dragon != null) {
            walk(safeZone);
        }
        int name = client.getInventory().getSlotForNameThatContains("Ring of dueling");
        doneBanking = false;
        ToastyBDragonsGui.chkboxDoneBanking.setSelected(false);
        shieldReady = false;
        ToastyBDragonsGui.chkboxShieldReady.setSelected(false);
        ringReady = false;
        ToastyBDragonsGui.chkboxRingReady.setSelected(false);
        itemsReady = false;
        ToastyBDragonsGui.chkboxItemsReady.setSelected(false);
        client.getInventory().interactWithSlot(name, "Rub");
        sleep(random(4300, 4800));
        client.getInterface(230).getChild(2).interact("Continue");
        sleep(random(2900, 3300));
        if (myPlayer().isInArea(castleWarsArea)) {
            if (client.getInventory().contains(boltToLoot)) {
                client.getInventory().interactWithName(boltToLoot, "Wield");
            }
            walk(onChest);
            sleep(random(1600, 2200));
            openedDoor = false;
            walkedToGate = false;
            openedGate = false;
            walkedToEnt = false;
            enteredCaveArea = false;
            atDragons = false;
            ToastyBDragonsGui.chkboxOpenedDoor.setSelected(false);
            ToastyBDragonsGui.chkboxWalkedToGate.setSelected(false);
            ToastyBDragonsGui.chkboxOpenedGate.setSelected(false);
            ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(false);
            ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(false);
            ToastyBDragonsGui.chkboxAtDragons.setSelected(false);
        } else {
        }
    }

    public void bank() throws InterruptedException {
        status = "banking";
        Player player = client.getMyPlayer();
        Bank bank = client.getBank();
        if (castleBank.contains(player)) {
            Entity bankBox = closestObject(bankChest);
            if (bank.isOpen()) {
                bank.depositAllExcept(rings);
                sleep(random(1000, 1500));
                if (client.getInventory().isEmpty()) {
                    bank.withdraw1(2552);
                    if (usingAttPot && bank.contains(145)) {
                        bank.withdraw1(145);
                    }
                    if (usingStrPot && bank.contains(157)) {
                        bank.withdraw1(157);
                    }
                    if (usingDefPot && bank.contains(163)) {
                        bank.withdraw1(163);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    if (usingAttPot && bank.contains(145)) {
                        bank.withdraw1(145);
                    }
                    if (usingStrPot && bank.contains(157)) {
                        bank.withdraw1(157);
                    }
                    if (usingDefPot && bank.contains(163)) {
                        bank.withdraw1(163);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    if (usingAttPot && bank.contains(145)) {
                        bank.withdraw1(145);
                    }
                    if (usingStrPot && bank.contains(157)) {
                        bank.withdraw1(157);
                    }
                    if (usingDefPot && bank.contains(163)) {
                        bank.withdraw1(163);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                }
                if (client.getInventory().isEmptyExcept(itemsInBagAtStart)) {
                    if (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                            < client.getSkills().getLevel(Skill.HITPOINTS) - 10) {
                        int needToEat = (client.getSkills().getLevel(Skill.HITPOINTS) - client.getSkills().getCurrentLevel(Skill.HITPOINTS)) / 12;
                        if (bank.isOpen()) {
                            bank.withdrawX(foodID, needToEat);
                            sleep(random(500, 800));
                            bank.close();
                            sleep(random(500, 800));
                            if (bank.isOpen()) {
                                bank.close();
                                for (int i = 0; i < needToEat; i++) {
                                    client.getInventory().interactWithId(foodID, "Eat");
                                    sleep(random(1522, 1700));
                                }
                            } else {
                                for (int i = 0; i < needToEat; i++) {
                                    client.getInventory().interactWithId(foodID, "Eat");
                                    sleep(random(1522, 1700));
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
                    }
                    status = "Done banking";
                    doneBanking = true;
                    ToastyBDragonsGui.chkboxDoneBanking.setSelected(true);
                } else {
                    itemsReady = false;
                    state = State.FAILEDBANKING;
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

    public void bankFourDose() throws InterruptedException {
        status = "banking";
        Player player = client.getMyPlayer();
        Bank bank = client.getBank();
        if (castleBank.contains(player)) {
            Entity bankBox = closestObject(bankChest);
            if (bank.isOpen()) {
                bank.depositAllExcept(rings);
                sleep(random(1000, 1500));
                if (client.getInventory().isEmpty()) {
                    bank.withdraw1(2552);
                    if (usingAttPot && bank.contains(2436)) {
                        bank.withdraw1(2436);
                    }
                    if (usingStrPot && bank.contains(2440)) {
                        bank.withdraw1(2440);
                    }
                    if (usingDefPot && bank.contains(2442)) {
                        bank.withdraw1(2442);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    if (usingAttPot && bank.contains(2436)) {
                        bank.withdraw1(2436);
                    }
                    if (usingStrPot && bank.contains(2440)) {
                        bank.withdraw1(2440);
                    }
                    if (usingDefPot && bank.contains(2442)) {
                        bank.withdraw1(2442);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    if (usingAttPot && bank.contains(2436)) {
                        bank.withdraw1(2436);
                    }
                    if (usingStrPot && bank.contains(2440)) {
                        bank.withdraw1(2440);
                    }
                    if (usingDefPot && bank.contains(2442)) {
                        bank.withdraw1(2442);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                }
                if (client.getInventory().isEmptyExcept(itemsInBagAtStartFourDose)) {
                    if (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                            < client.getSkills().getLevel(Skill.HITPOINTS) - 10) {
                        int needToEat = (client.getSkills().getLevel(Skill.HITPOINTS) - client.getSkills().getCurrentLevel(Skill.HITPOINTS)) / 12;
                        if (bank.isOpen()) {
                            bank.withdrawX(foodID, needToEat);
                            sleep(random(500, 800));
                            bank.close();
                            sleep(random(500, 800));
                            for (int i = 0; i < needToEat; i++) {
                                client.getInventory().interactWithId(foodID, "Eat");
                                sleep(random(1522, 1700));
                            }
                        } else {
                            if (bankBox != null) {
                                if (bankBox.isVisible()) {
                                    bankBox.interact("Use");
                                    sleep(random(700, 900));
                                    bankFourDose();
                                } else {
                                    client.moveCameraToEntity(bankBox);
                                }
                            }
                        }
                    }
                    status = "Done banking";
                    doneBanking = true;
                    ToastyBDragonsGui.chkboxDoneBanking.setSelected(true);
                } else {
                    itemsReady = false;
                    state = State.FAILEDBANKING;
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Use");
                        sleep(random(700, 900));
                        bankFourDose();
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        } else {
            walk(castleBank);
        }
    }

    public void bankRng() throws InterruptedException {
        status = "banking";
        Player player = client.getMyPlayer();
        Bank bank = client.getBank();
        if (castleBank.contains(player)) {
            Entity bankBox = closestObject(bankChest);
            if (bank.isOpen()) {
                bank.depositAllExcept(rings);
                sleep(random(1000, 1500));
                if (client.getInventory().isEmpty()) {
                    bank.withdraw1(2552);
                    if (usingRngPot && bank.contains(169)) {
                        bank.withdraw1(169);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    if (usingRngPot && bank.contains(169)) {
                        bank.withdraw1(169);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    if (usingRngPot && bank.contains(169)) {
                        bank.withdraw1(169);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                }
                if (client.getInventory().isEmptyExcept(itemsInBagAtStartRng)) {
                    if (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                            < client.getSkills().getLevel(Skill.HITPOINTS) - 10) {
                        int needToEat = (client.getSkills().getLevel(Skill.HITPOINTS) - client.getSkills().getCurrentLevel(Skill.HITPOINTS)) / 12;
                        if (bank.isOpen()) {
                            bank.withdrawX(foodID, needToEat);
                            sleep(random(500, 800));
                            bank.close();
                            sleep(random(500, 800));
                            for (int i = 0; i < needToEat; i++) {
                                client.getInventory().interactWithId(foodID, "Eat");
                                sleep(random(1522, 1700));
                            }
                        } else {
                            if (bankBox != null) {
                                if (bankBox.isVisible()) {
                                    bankBox.interact("Use");
                                    sleep(random(700, 900));
                                    bankRng();
                                } else {
                                    client.moveCameraToEntity(bankBox);
                                }
                            }
                        }
                    }
                    status = "Done banking";
                    doneBanking = true;
                    ToastyBDragonsGui.chkboxDoneBanking.setSelected(true);
                } else {
                    itemsReady = false;
                    state = State.FAILEDBANKING;
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Use");
                        sleep(random(700, 900));
                        bankRng();
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        } else {
            walk(castleBank);
        }
    }

    public void bankFourDoseRng() throws InterruptedException {
        status = "banking";
        Player player = client.getMyPlayer();
        Bank bank = client.getBank();
        if (castleBank.contains(player)) {
            Entity bankBox = closestObject(bankChest);
            if (bank.isOpen()) {
                bank.depositAllExcept(rings);
                sleep(random(1000, 1500));
                if (client.getInventory().isEmpty()) {
                    bank.withdraw1(2552);
                    if (usingRngPot && bank.contains(2444)) {
                        bank.withdraw1(2444);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    if (usingRngPot && bank.contains(2444)) {
                        bank.withdraw1(2444);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    if (usingRngPot && bank.contains(2444)) {
                        bank.withdraw1(2444);
                    }
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                }
                if (client.getInventory().isEmptyExcept(itemsInBagAtStartFourDoseRng)) {
                    if (client.getSkills().getCurrentLevel(Skill.HITPOINTS)
                            < client.getSkills().getLevel(Skill.HITPOINTS) - 10) {
                        int needToEat = (client.getSkills().getLevel(Skill.HITPOINTS) - client.getSkills().getCurrentLevel(Skill.HITPOINTS)) / 12;
                        if (bank.isOpen()) {
                            bank.withdrawX(foodID, needToEat);
                            sleep(random(500, 800));
                            bank.close();
                            sleep(random(500, 800));
                            for (int i = 0; i < needToEat; i++) {
                                client.getInventory().interactWithId(foodID, "Eat");
                                sleep(random(1522, 1700));
                            }
                        } else {
                            if (bankBox != null) {
                                if (bankBox.isVisible()) {
                                    bankBox.interact("Use");
                                    sleep(random(700, 900));
                                    bankFourDoseRng();
                                } else {
                                    client.moveCameraToEntity(bankBox);
                                }
                            }
                        }
                    }
                    status = "Done banking";
                    doneBanking = true;
                    ToastyBDragonsGui.chkboxDoneBanking.setSelected(true);
                } else {
                    itemsReady = false;
                    state = State.FAILEDBANKING;
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Use");
                        sleep(random(700, 900));
                        bankFourDoseRng();
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        } else {
            walk(castleBank);
        }
    }

    // Anti ban
    public void AntiBan() throws InterruptedException {
        status = "Anti-Ban";
        int skillToHover;
        //for antiban to hover over a skill
        if (!isRanging) {
            skillToHover = 123;
            switch (random(1, 4)) {
                case 1:
                    skillToHover = 123;
                    break;
                case 2:
                    skillToHover = 124;
                    break;
                case 3:
                    skillToHover = 126;
                    break;
                case 4:
                    skillToHover = 129;
                    break;
            }
        } else {
            skillToHover = 132;
        }
        // random Rectangle to move mouse to
        Rectangle k = new Rectangle(1 + random(489), 1 + random(328), 10, 10);

        // antiban functions
        switch (antiban) {
            case 1:
                status = "Anti-Ban rotate camera";
                //client.rotateCameraPitch(random(55, 60));
                log("case 1");
                break;
            case 2:
                status = "Anti-Ban Check Buddy List";
                openTab(Tab.FRIENDS);
                log("case 2");
                sleep(random(500, 600));
                openTab(Tab.INVENTORY);
                break;
            case 3:
                status = "Anti-Ban rotate camera";
                //client.rotateCameraPitch(random(67, 90));
                client.rotateCameraToAngle(random(43, 68));
                log("case 3");
                break;
            case 5:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(100).hover();
                log("case 5");
                break;
            case 7:
                status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);
                // Looks at Skill Exp Must change Child for different Skills!!
                if (client.getInterface(320).getChild(skillToHover).isVisible()) {
                    client.getInterface(320).getChild(skillToHover).hover();
                    sleep(random(1394, 1867));
                    openTab(Tab.INVENTORY);
                }
                log("case 7");
                break;
            case 8:
                status = "Anti-Ban rotate camera";
                client.rotateCameraToAngle(random(44, 58));
                log("case 8");
                break;
            case 11:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 11");
                break;
            case 13:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 13");
                break;
            case 14:
                status = "Anti-Ban sleepy";
                sleep(random(4279, 6732));
                log("case 14");
                break;
            case 16:
                status = "Anti-Ban rotate camera";
                //client.rotateCameraPitch(random(84, 100));
                log("case 16");
                break;
            case 17:
                status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);
                // Looks at Skill Exp Must change Child for different Skills!!
                if (client.getInterface(320).getChild(skillToHover).isVisible()) {
                    client.getInterface(320).getChild(skillToHover).hover();
                    sleep(random(1200, 1500));
                    openTab(Tab.INVENTORY);
                }
                log("case 17");
                break;
            case 19:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(84).hover();
                log("case 19");
                break;
            case 21:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 21");
                break;
            case 22:
                status = "Antiban mouse move rec";
                client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 22");
                break;
            case 23:
                status = "Anti-Ban rotate Camera";
                //client.rotateCameraPitch(random(45, 65));
                log("case 23");
                break;
            case 24:
                status = "Anti-Ban Check Buddy List";
                openTab(Tab.FRIENDS);
                log("case 24");
                sleep(random(400, 500));
                openTab(Tab.INVENTORY);
                break;
            case 26:
                status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);
                // Looks at Skill Exp Must change Child for different Skills!!
                if (client.getInterface(320).getChild(skillToHover).isVisible()) {
                    client.getInterface(320).getChild(skillToHover).hover();
                    sleep(random(1442, 1867));
                    openTab(Tab.INVENTORY);
                }
                log("case 26");
                break;
            case 28:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 28");
                break;
            case 30:
                status = "Anti-Ban rotate camera";
                client.rotateCameraToAngle(random(41, 66));
                log("case 30");
                break;
            case 31:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(75).hover();
                log("case 31");
                break;
            case 32:
                status = "Anti-Ban move mouse out 3";
                moveMouseOutsideScreen();
                log("case 32");
                break;
            case 34:
                status = "Antiban mouse move rec";
                client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 34");
                break;
            case 35:
                status = "Anti-Ban sleepy";
                sleep(random(4000, 5000));
                log("case 35");
                break;
            case 38:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 38");
                break;
            case 39:
                status = "Anti-Ban rotate camera";
                //client.rotateCameraPitch(random(45, 60));
                log("case 39");
                break;
            case 41:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(44).hover();
                log("case 41");
                break;
            case 42:
                status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);
                // Looks at Skill Exp Must change Child for different Skills!!
                if (client.getInterface(320).getChild(skillToHover).isVisible()) {
                    client.getInterface(320).getChild(skillToHover).hover();
                    sleep(random(1481, 1957));
                    openTab(Tab.INVENTORY);
                }
                log("case 42");
                break;
            case 43:
                status = "Antiban mouse move rec";
                client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 43");
                break;
            case 44:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 44");
                break;
            case 45:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(109).hover();
                log("case 45");
                break;
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ----------------------------Experience Table----------------------------
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private int experienceToNextLevel(Skill skill) {
        int[] xpForLevels = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
            1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470,
            5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
            14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648,
            37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
            91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040,
            203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015,
            449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257,
            992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808,
            1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
            3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431};
        int xp = client.getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xpForLevels[i] - xp;
            }
        }
        return 200000000 - xp;
    }

    private double experiencePrevLevel(Skill skill) {
        int[] xpForLevels = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
            1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470,
            5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
            14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648,
            37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
            91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040,
            203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015,
            449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257,
            992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808,
            1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
            3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431};
        int xp = client.getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xp - xpForLevels[i - 1];
            }
        }
        return 200000000 - xp;
    }

    private double experienceTotalNeeded(Skill skill) {
        int[] xpForLevels = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
            1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470,
            5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
            14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648,
            37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014,
            91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040,
            203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015,
            449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257,
            992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808,
            1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792,
            3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431};
        int xp = client.getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xpForLevels[i] - xpForLevels[i - 1];
            }
        }
        return 200000000 - xp;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~Other Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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

    private void checkForUpdate() {

        try {
            URL url = new URL("http://pastebin.com/raw.php?i=bk0JFncD");
            Scanner s = new Scanner(url.openStream());
            String latestVersion = s.next();
            log("[Updater] My version: " + myVersion);
            log("[Updater] Latest version: " + latestVersion);
            int latest = Integer.parseInt(latestVersion);
            if (myVersion >= latest) {
                log("[Toasty BDragons Killer] is up to date!");
            } else {
                updateScript();
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Updater~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void updateScript() {

        try {
            URL urlDL = new URL(
                    "https://dl-web.dropbox.com/get/ChaosDruidJar/ToastyBDragonsPriv.jar");
            InputStream in = urlDL.openStream();

            byte[] buf = new byte[4096];
            String fs = System.getProperty("file.separator");
            String filename = fs + "OSBot" + fs + "scripts" + fs
                    + "ToastyBDragonsPriv.jar";
            OutputStream os = new FileOutputStream(
                    System.getProperty("user.home") + filename);
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                os.write(buf, 0, bytesRead);
            }
            os.flush();
            os.close();
            in.close();
            JOptionPane.showMessageDialog(null,
                    "Script has been automatically updated!"
                    + " Please refresh your scripts list"
                    + " and run the script again.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
