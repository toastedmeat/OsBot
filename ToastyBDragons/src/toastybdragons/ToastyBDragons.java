package toastybdragons;

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
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

/**
 *
 * @author Eric Loo
 */
@SuppressWarnings("ALL")
@ScriptManifest(author = "Toastedmeat", info = "Kills Lots of Blue Dragons in Ogre Enclave",
        name = "ToastyBDragons", version = 61)
public class ToastyBDragons extends Script {

    private final int myVersion = 60;

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
    private final Area castleWarsArea = new Area(2438, 3082, 2444, 3094);
    private final Area castleBank = new Area(2442, 3082, 2443, 3084);
    private final Area onChest = new Area(2443, 3083, 2443, 3083);

    final Area castleBankDoor = new Area(2442, 3088, 2444, 3091);
    private final Area outsideCastle = new Area(2448, 3089, 2452, 3090);

    private final Area cityEntrance = new Area(2504, 3062, 2510, 3063);
    final Area onTopOfGate = new Area(2504, 3062, 2504, 3063);
    private final Area enteredCity = new Area(2503, 3062, 2503, 3063);

    final Area failToCaveEnt2 = new Area(2507, 3048, 2515, 3054);
    final Area failToCaveEnt = new Area(2511, 3050, 2517, 3047);
    private final Area caveEntrance = new Area(2506, 3035, 2516, 3046);
    private final Area onTopOfCave = new Area(2506, 3039, 2506, 3039);

    private final Area enteredCave = new Area(2588, 9408, 2589, 9411);
    private final Area dragonTop = new Area(2579, 9444, 2583, 9451);
    private final Area dragonBottom = new Area(2569, 9436, 2575, 9442);

    private final Area dragonsArea = new Area(2565, 9428, 2582, 9456);

    private final Area safeZone = new Area(2587, 9449, 2589, 9450);

    private final Area safeSpotTop = new Area(2575, 9445, 2575, 9445);
    private final Area safeSpotBottom = new Area(2564, 9442, 2564, 9442);

    private final Area lumbridgeDead = new Area(3218, 3212, 3225, 3226);

    private final Position middleArea = new Position(2576, 9442, 0);
    private final Position psafeSpotTop = new Position(2575, 9445, 0);
    private final Position psafeSpotBottom = new Position(2564, 9442, 0);

    // ~~~~~~~~~~~~~~~~~~~~~~~Paths~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    final int[][] FROM_CASTLE_BANK_DOOR = new int[][]{{2450, 3089}, {2455, 3078}, {2455, 3076},
    {2461, 3075}, {2474, 3074}, {2488, 3070}, {2501, 3071}, {2516, 3070},
    {2530, 3066}, {2544, 3063}, {2548, 3052}, {2533, 3054}, {2520, 3062},
    {2507, 3063}};

    private final int[][] FROM_CASTLE_BANK_DOOR2 = new int[][]{{2449, 3090}, {2455, 3088}, {2455, 3084},
    {2455, 3076}, {2463, 3073}, {2471, 3072}, {2479, 3072}, {2487, 3072}, {2495, 3072},
    {2503, 3071}, {2511, 3071}, {2519, 3070}, {2527, 3069}, {2535, 3068},
    {2543, 3065}, {2554, 3062}, {2553, 3055}, {2546, 3051}, {2538, 3048},
    {2532, 3054}, {2526, 3062}, {2518, 3062}, {2510, 3060}, {2505, 3063}};

    private final int[][] FROM_GATE_TO_ENT = new int[][]{{2501, 3052}, {2514, 3050},
    {2508, 3043}, {2508, 3037}};
    private final int[][] FROM_ENT_TO_DRAG = new int[][]{{2588, 9409}, {2588, 9421}, {2581, 9430},
    {2574, 9437}, {2571, 9435}};

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ID's~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final int bankChest = 4483;

    final int doorLeft = 20648, doorRight = 20130;

    private final int doorOpen = 2444;
    final int doorClose = 2445;

    final int gateLeft = 2788, gateRight = 2798;

    private final int caveEntrace = 2804;

    //final int[] blueDragonBottomTop = new int[]{1115, 1114};
    private final int blueDragonBottom = 1009;
    private final int blueDragonTop = 1101;
    private int dragonVert;

    private final int animationDead = 92;

    private final int dragonShield = 1540;
    final int mithBoltId = 9142;

    private final int[] rings = new int[]{2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};

    String[] ringNames = {"Ring of dueling(8)", "Ring of dueling(7)", "Ring of dueling(6)",
        "Ring of dueling(5)", "Ring of dueling(4)", "Ring of dueling(3)",
        "Ring of dueling(2)", "Ring of dueling(1)"};

    private final int[] itemsInBagAtStart = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 145, 157, 163};
    private final int[] itemsInBagAtStartFourDose = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 2436, 2440, 2442};

    private final int[] itemsInBagAtStartRng = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 169};
    private final int[] itemsInBagAtStartFourDoseRng = new int[]{2552, 2554, 2556, 2558, 2560,
        2562, 2564, 2566, 379, 385, 7946, 2444};

    private final int[] miscItems = new int[]{561, /*449, adamantite ore*/ 1213,
        985, 987, 1249, 2366, 1149};

    private final int[] IDBAD = {199, 201, 203, 1917, 1971, 526, 995, 205,
        209, 211, 213, 215, 217, 219, 2485, 886, 1243};
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Loot~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final String[] toLoot = {"Nature rune, Blue dragonhide, Dragon bones"
        + "Adamantite ore, Rune dagger, half keys, dragon spear, shield left"
        + "d med"};

    private double priceBones, priceHides, profit = 0;
    private double miscCounter, hidesCounter, boneCounter;
    private double bonesTemp, hidesTemp;

    private int bonesPH;
    private int hidesPH;
    private int miscPH;

    private GroundItem bones, hides, misc, boltType;
    private String boltToLoot;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Timers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private long startTime = 0L;
    private long last = 0L;

    //~~~~~~~~~~~~~~~~~~~~~~~Start Skills Lv~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private double strength, attack, defence, range, hp;
    //~~~~~~~~~~~~~~~~~~~~~ Start Skill exp~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private double startAtt, startStr, startDef, startRng, startHP;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp Gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private double attExp;
    private double strExp;
    private double defExp;
    private double rngExp;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp PH String~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String attExpPHS, strExpPHS, defExpPHS, rngExpPHS, hpExpPHS;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Potion Checker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private double potAtt, potStr, potDef, potRng;

    private boolean fourDose = false, isRanging = false,
            usingStrPot, usingAttPot, usingDefPot, usingRngPot;
    //food to use
    private String food;
    private int foodID, foodAmount;

    private final Object[] options = {"Lobster", "Monkfish", "Shark"};

    //for running
    private boolean run;
    private int runInt;

    // various checks
    private int antiban;
    private boolean itemsReady, shieldReady, ringReady, isFighting;

    // Walking checks
    private boolean doneBanking, openedDoor, walkedToGate, openedGate,
            walkedToEnt, enteredCaveArea, atDragons;

    //Monsters
    private NPC dragon, dragonTopNPC, dragonBottomNPC;
    private int killedDragons;
    private final boolean isWalkingAllowed = false;

    //Paint Image
    private Image image;
    //Rectangles to hide info
    private final Rectangle showGUI = new Rectangle(random(10, 300), 462, 60, 12);
    private final Rectangle rectHp = new Rectangle(200, 280, 260, 12);
    private final Rectangle rectRng = new Rectangle(200, 290, 260, 12);
    private final Rectangle rectStr = new Rectangle(200, 300, 260, 12);
    private final Rectangle rectAtt = new Rectangle(200, 310, 260, 12);
    private final Rectangle rectDef = new Rectangle(200, 320, 260, 12);

    private Rectangle rectHpColor = new Rectangle(200, 280, 0, 12);
    private Rectangle rectRngColor = new Rectangle(200, 290, 0, 12);
    private Rectangle rectStrColor = new Rectangle(200, 300, 0, 12);
    private Rectangle rectAttColor = new Rectangle(200, 310, 0, 12);
    private Rectangle rectDefColor = new Rectangle(200, 320, 0, 12);

    private RectangleDestination runBtn = new RectangleDestination(625, 415, 30, 30);

    //Gui
    private boolean hideE = false;
    private boolean showE = true;
    private boolean hideG = false;
    private boolean showG = true;
    //idleTimer
    private int startLastMove;
    private long lastMoveTimer, idleStartTime;
    //first status
    private String status = "Booting UP!!!";

    //The different States of the script
    enum State {

        IDLE, STARTING, READYTOSTART, CHECKFORSHIELD, CHECKFORITEMSRNG,
        CHECKFORITEMS, OPENCASTLEDOOR, CHECKFORRING,
        WALKTOGATE, OPENGATE, GATETODUNGEON, ENTERDUNGEON, ANTIBAN,
        WALKTODRAGONS, FIGHT, EAT, POTIONS, GOTOBANK, BANK, BANKFOURDOSE,
        BANKRNG, BANKFOURDOSERNG, FAILEDBANKING, LOOT, FIGHTRNG
    }

    private State state = State.IDLE;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Inherited Methods from client~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onStart() {
        status = "Starting script";
        state = State.STARTING;
        //Checking for updates
        //checkForUpdate();
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
        boolean runningCheck = true;
        antiban = 0;
        itemsReady = false;
        shieldReady = false;
        ringReady = false;
        isFighting = false;
        killedDragons = 0;
        int switcher = 1;
        dragonVert = 0;
        food = "";
        foodID = 0;
        hidesTemp = client.getInventory().getAmount("Blue dragonhide");
        bonesTemp = client.getInventory().getAmount("Dragon bones");
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
        int randomInt = random(1, 10);
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
                Logger.getLogger(ToastyBDragons.class.getName()).log(Level.SEVERE, null, ex);
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
                openedDoor = false;
                log("open");
                walkedToGate = false;
                log("gate");
                openedGate = false;
                log("oGate");
                walkedToEnt = false;
                log("wEnt");
                enteredCaveArea = false;
                log("eCave");
                atDragons = false;
                log("aDragon");
                ToastyBDragonsGui.chkboxOpenedDoor.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToGate.setSelected(false);
                ToastyBDragonsGui.chkboxOpenedGate.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(false);
                ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(false);
                ToastyBDragonsGui.chkboxAtDragons.setSelected(false);
                bank();
                break;
            case BANKFOURDOSE:
                openedDoor = false;
                log("open");
                walkedToGate = false;
                log("gate");
                openedGate = false;
                log("oGate");
                walkedToEnt = false;
                log("wEnt");
                enteredCaveArea = false;
                log("eCave");
                atDragons = false;
                log("aDragon");
                ToastyBDragonsGui.chkboxOpenedDoor.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToGate.setSelected(false);
                ToastyBDragonsGui.chkboxOpenedGate.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(false);
                ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(false);
                ToastyBDragonsGui.chkboxAtDragons.setSelected(false);
                bank();
                bankFourDose();
                break;
            case BANKRNG:
                openedDoor = false;
                log("open");
                walkedToGate = false;
                log("gate");
                openedGate = false;
                log("oGate");
                walkedToEnt = false;
                log("wEnt");
                enteredCaveArea = false;
                log("eCave");
                atDragons = false;
                log("aDragon");
                ToastyBDragonsGui.chkboxOpenedDoor.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToGate.setSelected(false);
                ToastyBDragonsGui.chkboxOpenedGate.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(false);
                ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(false);
                ToastyBDragonsGui.chkboxAtDragons.setSelected(false);
                bank();
                bankRng();
                break;
            case BANKFOURDOSERNG:
                openedDoor = false;
                log("open");
                walkedToGate = false;
                log("gate");
                openedGate = false;
                log("oGate");
                walkedToEnt = false;
                log("wEnt");
                enteredCaveArea = false;
                log("eCave");
                atDragons = false;
                log("aDragon");
                ToastyBDragonsGui.chkboxOpenedDoor.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToGate.setSelected(false);
                ToastyBDragonsGui.chkboxOpenedGate.setSelected(false);
                ToastyBDragonsGui.chkboxWalkedToEnt.setSelected(false);
                ToastyBDragonsGui.chkboxEnteredCaveArea.setSelected(false);
                ToastyBDragonsGui.chkboxAtDragons.setSelected(false);
                bank();
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

    @Override
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
        if (lastMoveTimer / 1000 > 5 && myPlayer().getFacing() == null
                && (state == State.FIGHT || state == State.FIGHTRNG)) {
            isFighting = false;
        }

        if (client.getInventory().getAmount("Blue dragonhide") != hidesTemp && (state == State.LOOT || state == State.FIGHT)) {
            ++hidesCounter;
            hidesTemp = client.getInventory().getAmount("Blue dragonhide");
        }

        if (client.getInventory().getAmount("Dragon bones") != bonesTemp && (state == State.LOOT || state == State.FIGHT)) {
            ++boneCounter;
            bonesTemp = client.getInventory().getAmount("Dragon bones");
        }

        // Timer Variables
        long millis = (System.currentTimeMillis() - startTime);
        long hours = (millis / 3600000L);
        millis -= hours * 3600000L;
        long minutes = (millis / 60000L);
        millis -= minutes * 60000L;
        long seconds = (millis / 1000L);

        // Client view
        int pitch = client.getCameraPitch();
        int angle = client.getCameraPitchAngle();

        //~~~~~~~~~~~~~~~~~~~~~~Skill levels gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        double attGainedLv = (client.getSkills().getLevel(Skill.ATTACK) - attack);
        double strGainedLv = (client.getSkills().getLevel(Skill.STRENGTH) - strength);
        double defGainedLv = (client.getSkills().getLevel(Skill.DEFENCE) - defence);
        double rngGainedLv = (client.getSkills().getLevel(Skill.RANGED) - range);
        double hpGainedLv = (client.getSkills().getLevel(Skill.HITPOINTS) - hp);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp Gained~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        attExp = (client.getSkills().getExperience(Skill.ATTACK) - startAtt);
        strExp = (client.getSkills().getExperience(Skill.STRENGTH) - startStr);
        defExp = (client.getSkills().getExperience(Skill.DEFENCE) - startDef);
        rngExp = (client.getSkills().getExperience(Skill.RANGED) - startRng);
        double hpExp = (client.getSkills().getExperience(Skill.HITPOINTS) - startHP);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        double attExpTNL = experienceToNextLevel(Skill.ATTACK);
        double strExpTNL = experienceToNextLevel(Skill.STRENGTH);
        double defExpTNL = experienceToNextLevel(Skill.DEFENCE);
        double rngExpTNL = experienceToNextLevel(Skill.RANGED);
        double hpExpTNL = experienceToNextLevel(Skill.HITPOINTS);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp %~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        double attPercent = 100 * (experiencePrevLevel(Skill.ATTACK) / experienceTotalNeeded(Skill.ATTACK));
        double strPercent = 100 * (experiencePrevLevel(Skill.STRENGTH) / experienceTotalNeeded(Skill.STRENGTH));
        double defPercent = 100 * (experiencePrevLevel(Skill.DEFENCE) / experienceTotalNeeded(Skill.DEFENCE));
        double rngPercent = 100 * (experiencePrevLevel(Skill.RANGED) / experienceTotalNeeded(Skill.RANGED));
        double hpPercent = 100 * (experiencePrevLevel(Skill.HITPOINTS) / experienceTotalNeeded(Skill.HITPOINTS));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~Exp PH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        double attExpPH = ((int) (attExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        double strExpPH = ((int) (strExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        double defExpPH = ((int) (defExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        double rngExpPH = ((int) (rngExp * 3600000.0D / (System.currentTimeMillis() - startTime)));
        double hpExpPH = ((int) (hpExp * 3600000.0D / (System.currentTimeMillis() - startTime)));

        //~~~~~~~~~~~~~~~~~~~~~~~~~Skills time TNL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String timeAttTNL = formatTime(timeTnl(attExpTNL, attExpPH));
        String timeStrTNL = formatTime(timeTnl(strExpTNL, strExpPH));
        String timeDefTNL = formatTime(timeTnl(defExpTNL, defExpPH));
        String timeRngTNL = formatTime(timeTnl(rngExpTNL, rngExpPH));
        String timeHpTNL = formatTime(timeTnl(hpExpTNL, hpExpPH));

        // ~~~~~~~~~~ profit ~~~~~~~~~~~
        profit = ((priceBones * boneCounter) + (priceHides * hidesCounter)) / 1000;
        int profitPH = ((int) (profit * 3600000.0D / (System.currentTimeMillis() - startTime)));
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
                g2.drawString(newHpPercent.substring(0, 3), 263, 290);
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

            }
        }
        //state in the color black
        g2.setFont(regFont);
        g2.setColor(black);
        g2.drawString("State: " + state, 360, 458);
        g2.setColor(Color.CYAN);
        g2.drawString("World: " + client.getCurrentWorld(), 10, 14);
        if (dragon != null) {
            g2.drawString("Dragon: " + dragon.getModel().getVerticeCount(), 200, 220);
            g2.drawString("Saved: " + dragonVert, 200, 210);
        }
        // Other Paint Stuff 
        g2.drawString("Running at: " + runInt, 200, 240);
        g2.drawString("Fighting: " + isFighting, 200, 250);
        // status
        g2.drawString("Status: " + status, 200, 260);
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
                //g2.drawString("Dragons Killed: " + killedDragons, 570, 435);

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
        Point pointer = e.getPoint();

        if (showGUI.contains(pointer)) {
            ToastyBDragonsGui.frame.setVisible(true);
        }

    }

    /**
     *
     * @return the state the bot has to go into;
     * @throws InterruptedException
     */
    State checkStates() throws InterruptedException {
        Player player = client.getMyPlayer();
        //anti ban counter
        antiban = random(1, 2500);

        // checks for items on the floor
        bones = closestGroundItemForName("Dragon bones");
        hides = closestGroundItemForName("Blue dragonhide");
        misc = closestGroundItem(miscItems);
        boltType = closestGroundItemForName(boltToLoot);

        //NPC
        NPC dragon = closestNPCForName("Blue dragon");
        //Potting Levels
        potStr = client.getSkills().getCurrentLevel(Skill.STRENGTH);
        potAtt = client.getSkills().getCurrentLevel(Skill.ATTACK);
        potDef = client.getSkills().getCurrentLevel(Skill.DEFENCE);
        potRng = client.getSkills().getCurrentLevel(Skill.RANGED);

        //Fail Safes
        if (lastMoveTimer > 280000 + random(-60000, 30000)
                && (client.getInventory().contains(2552) || client.getInventory().contains(2554)
                || client.getInventory().contains(2556) || client.getInventory().contains(2558)
                || client.getInventory().contains(2560) || client.getInventory().contains(2562)
                || client.getInventory().contains(2566) || client.getInventory().contains(2566))) {
            log("Idle for too long Starting Over!!");
            return State.GOTOBANK;
        } else if (lastMoveTimer > 280000 + random(-60000, 30000)
                && (!client.getInventory().contains(2552) && !client.getInventory().contains(2554)
                && !client.getInventory().contains(2556) && !client.getInventory().contains(2558)
                && !client.getInventory().contains(2560) && !client.getInventory().contains(2562)
                && !client.getInventory().contains(2566) && !client.getInventory().contains(2566))) {
            log("Idle for too long, No ring in inventory Logging out");
            stop();
            return State.IDLE;
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
                if (fourDose) {
                    log("banking fourDose");
                    return State.BANKFOURDOSE;
                } else {
                    log("banking");
                    return State.BANK;
                }
            } else {
                if (fourDose) {
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
                && !isFighting) {
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
        } else if (dragon != null && atDragons
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

    //~~~~~~~~ Checks ~~~~~~~~~
    void runningCheck() throws InterruptedException {
        run = isRunning();
        //log("isRunning: " + Boolean.toString(run));
        if ((!run) && (client.getRunEnergy() >= runInt)) {
            status = "Toggling running";
            settingsTab.open();
            sleep(random(900, 1279));
            client.moveMouseTo(runBtn, false, true, false);
            //setRunning(true);
            sleep(random(400, 500));
            openTab(Tab.INVENTORY);
            run = true;
        } else if ((!run) && (client.getRunEnergy() == 100)) {
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

    void checkForShield() throws InterruptedException {
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

    void checkForRing() throws InterruptedException {
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

    void checkForItems() throws InterruptedException {
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

    void checkForItemsRng() throws InterruptedException {
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
    void openCastleDoor() throws InterruptedException {
        status = "Checking if door is open";
        Entity door = closestObjectForName("Large door");
        hidesTemp = 0;
        bonesTemp = 0;
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

    void walkToGate() {
        status = "Walking to Ogre City gate";
        WalkAlongPath(FROM_CASTLE_BANK_DOOR2, true);
        if (!myPlayer().isInArea(cityEntrance)) {
        } else {
            walkedToGate = true;
            ToastyBDragonsGui.chkboxWalkedToGate.setSelected(true);
        }
    }

    void openGate() throws InterruptedException {
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

    void gateToDungeon() throws InterruptedException {
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

    void enterDungeon() throws InterruptedException {
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

    void walkToDragons() throws InterruptedException {
        status = "Walking to dragons";
        WalkAlongPath(FROM_ENT_TO_DRAG, true);
        sleep(random(1000, 1200));
        if (myPlayer().isInArea(dragonsArea)) {
            walk(dragonBottom);
            sleep(random(2000, 3000));
            atDragons = true;
            ToastyBDragonsGui.chkboxAtDragons.setSelected(true);
        }

    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    void fight() throws InterruptedException {
        status = "Fighting";
        bones = closestGroundItemForName("Dragon bones");
        hides = closestGroundItemForName("Blue dragonhide");

        if (bones == null && hides == null) {
            if (!isFighting) {
                dragon = closestAttackableNPCForName("Blue dragon");
                if (dragon != null) {
                    dragonVert = dragon.getModel().getVerticeCount();
                    if (!dragon.isUnderAttack()) {
                        if (dragon.isInArea(dragonsArea)) {
                            if (dragon.isVisible()) {
                                while (!dragon.isUnderAttack() && dragon.exists()) {
                                    if (!myPlayer().isMoving()) {
                                        log("Attacking / Couldn't click");
                                        if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                                            isFighting = true;
                                            ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                            sleep(random(500, 800));
                                        }
                                    }
                                }
                            } else {
                                log("Can't see");
                                //switchDragonSpots();
                                walk(middleArea);
                                sleep(random(500, 800));
                                client.moveCameraToEntity(dragon);
                            }
                        } else {
                            log("switching spots");
                            //switchDragonSpots();
                            walk(middleArea);
                        }
                    }
                } else {
                    log("Dragon null");
                    walk(middleArea);
                    /*
                     if (dragonVert == blueDragonTop) {
                     log("To bottom dragon"); 12803 = attacking
                     walk(dragonBottom);
                     } else if (dragonVert == blueDragonBottom) {
                     log("To top dragon");
                     walk(dragonTop);
                     }
                     */
                }

            } else {
                if (lastMoveTimer / 1000 > 4.5) {
                    dragon.interact("Attack");
                    sleep(random(600, 700));
                } else if (isFighting && !myPlayer().isAnimating() && dragon != null
                        && (!dragon.isFacing(myPlayer()) || !myPlayer().isFacing(dragon))
                        && !dragon.isAnimating()) {
                    while (!dragon.isUnderAttack() && dragon.exists()) {
                        if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                            log("ReAttacked");
                        }
                    }
                    sleep(random(600, 700));
                } else if (isFighting && !myPlayer().isAnimating() && dragon != null
                        && (dragon.isFacing(myPlayer()) && !myPlayer().isFacing(dragon))
                        && dragon.isAnimating()) {
                    while (!dragon.isUnderAttack() && dragon.exists()) {
                        if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                            log("ReAttacked");
                        }
                    }
                    sleep(random(600, 700));
                }

                if (dragon != null && dragon.getAnimation() == animationDead) {
                    log("Dead1");
                    sleep(random(3000, 3400));
                    killedDragons++;
                    isFighting = false;
                    ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                } else if (dragon != null && dragon.getHealth() == 0) {
                    log("Dead2");
                    sleep(random(3000, 3400));
                    killedDragons++;
                    isFighting = false;
                    ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                } else if (dragon != null && dragon.getModel().getVerticeCount() != dragonVert && myPlayer().getFacing() == null) {
                    log("Dead3");
                    sleep(random(3000, 3400));
                    killedDragons++;
                    isFighting = false;
                    ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                }
            }
        }
    }

    void fightRng() throws InterruptedException {
        status = "Fighting";
        if (!isFighting) {
            dragon = closestAttackableNPCForName("Blue dragon");
            dragonVert = dragon.getModel().getVerticeCount();
            log("Dragon Vert is: " + Integer.toString(dragonVert));
            if (dragon != null && !dragon.isUnderAttack()) {
                log("Dragon is null: " + Boolean.toString(dragon == null));
                log("Dragon underattack: " + Boolean.toString(dragon.isUnderAttack()));
                if (dragon.isVisible()) {
                    log("Dragon is visible: " + dragon.isVisible());
                    if (state != State.LOOT) {
                        if (dragonVert == blueDragonBottom) {
                            if (myPlayer().isInArea(safeSpotBottom)) {
                                //client.rotateCameraPitch(random(22, 30));
                                sleep(random(500, 600));
                                log("First pitch turn bottom");
                                client.rotateCameraToAngle(random(242, 250));
                                sleep(random(1000, 1100));
                                while (!dragon.isUnderAttack() && dragon.exists()) {
                                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                                        isFighting = true;
                                        ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                        sleep(random(500, 800));
                                    }
                                }
                                //dragon.interact("Attack", isWalkingAllowed, 33);

                            } else {
                                log("Not in safe Spot bottom");
                                walkMainScreen(psafeSpotBottom, true);
                                log("Walking on the main screen bot");
                                sleep(random(500, 600));
                                //client.rotateCameraPitch(random(22, 30));
                                sleep(random(500, 600));
                                client.rotateCameraToAngle(random(242, 250));
                                sleep(random(1000, 1100));
                                //dragon.interact("Attack", isWalkingAllowed, 33);
                                while (!dragon.isUnderAttack() && dragon.exists()) {
                                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                                        isFighting = true;
                                        ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                        sleep(random(500, 800));
                                    }
                                }
                            }
                        } else {
                            if (myPlayer().isInArea(safeSpotTop)) {
                                //client.rotateCameraPitch(random(30, 42));
                                log("First pitch turn top");
                                sleep(random(500, 600));
                                client.rotateCameraToAngle(random(285, 315));
                                sleep(random(1000, 1100));
                                while (!dragon.isUnderAttack() && dragon.exists()) {
                                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                                        isFighting = true;
                                        ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                        sleep(random(500, 800));
                                    }
                                }
                            } else {
                                log("Not in Safe spot top");// froze
                                walkMainScreen(psafeSpotTop, true);
                                log("Walking on the main screen top");
                                sleep(random(500, 600));
                                //client.rotateCameraPitch(random(30, 42));
                                sleep(random(500, 600));
                                client.rotateCameraToAngle(random(285, 315));
                                sleep(random(1000, 1100));
                                while (!dragon.isUnderAttack() && dragon.exists()) {
                                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                                        isFighting = true;
                                        ToastyBDragonsGui.chkboxIsFighting.setSelected(true);
                                        sleep(random(500, 800));
                                    }
                                }
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
                dragon = closestAttackableNPCForName("Blue dragon");
                log("Froze 34?");
                dragonVert = dragon.getModel().getVerticeCount();
                log("Froze 35?");
            } else if (!myPlayer().isFacing(dragon) && dragon.isFacing(myPlayer())) {
                isFighting = true;
            } else if (myPlayer().isFacing(dragon) && dragon.isFacing(myPlayer())) {
                isFighting = true;
            }

        } else {
            if (dragonVert == blueDragonBottom) {
                if (myPlayer().isInArea(safeSpotBottom)) {
                } else {
                    log("Not in safe spot bottom else");
                    log("Froze 2?");
                    walkExact(safeSpotBottom, 8);
                    sleep(random(500, 600));
                    //client.rotateCameraPitch(random(22, 30));
                    client.rotateCameraToAngle(random(242, 250));
                    sleep(random(1000, 1100));
                    while (!dragon.isUnderAttack() && dragon.exists()) {
                        if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                            log("ReAttacked");
                        }
                    }
                    log("Froze 36?");// froze?
                    sleep(random(800, 1200));
                }
            } else {
                if (myPlayer().isInArea(safeSpotTop)) {
                } else {
                    log("Not in safe spot top else");
                    log("Froze 6?");
                    walkExact(safeSpotTop, 8);
                    log("Froze 7?");
                    sleep(random(500, 600));
                    //client.rotateCameraPitch(random(30, 42));
                    //sleep(random(500, 600));
                    client.rotateCameraToAngle(random(285, 315));
                    sleep(random(1000, 1100));
                    log("Froze 8?");
                    while (!dragon.isUnderAttack() && dragon.exists()) {
                        if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                            log("ReAttacked");
                        }
                    }
                    sleep(random(800, 1200));
                }
            }
            if (isFighting && !myPlayer().isAnimating()
                    && (!dragon.isFacing(myPlayer()) || !myPlayer().isFacing(dragon))
                    && dragon != null && !dragon.isAnimating()) {
                log("Not Attacking 1");
                log("Froze 9?");
                while (!dragon.isUnderAttack() && dragon.exists()) {
                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                        log("ReAttacked");
                    }
                }
                log("Froze 11?");
                sleep(random(1200, 1300));
                log("Froze 12?");
            } else if (isFighting && !myPlayer().isAnimating()
                    && (dragon.isFacing(myPlayer()) && !myPlayer().isFacing(dragon))
                    && dragon != null && dragon.isAnimating()) {
                log("Not Attacking 2");
                log("Froze 14?");
                while (!dragon.isUnderAttack() && dragon.exists()) {
                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                        log("ReAttacked");
                    }
                }
                log("Froze 15?");
                sleep(random(1200, 1300));
                log("Froze 16?");
            } else if (isFighting && !myPlayer().isAnimating()
                    && (dragon.isFacing(myPlayer()) && myPlayer().isFacing(dragon))
                    && dragon != null && !dragon.isAnimating() && !dragon.isUnderAttack()) {
                log("Not Attacking 3");
                log("Froze 18?");
                while (!dragon.isUnderAttack() && dragon.exists()) {
                    if (dragon.interact("Attack", "Blue dragon", false, 40, false, false)) {
                        log("ReAttacked");
                    }
                }
                log("Froze 19?");
                sleep(random(1200, 1300));
                log("Froze 20?");
            }
            if (dragon.getAnimation() == animationDead) {
                sleep(random(3000, 3400));
                killedDragons++;
                isFighting = false;
                ToastyBDragonsGui.chkboxIsFighting.setSelected(false);
                dragon = closestAttackableNPCForName("Blue dragon");
                checkStates();
            }
        }
    }

    void switchDragonSpots() throws InterruptedException {
        if (dragonVert == blueDragonTop) {
            log("To top dragon");
            walkMiniMap(dragonTop.getRandomPosition(0));
        } else if (dragonVert == blueDragonBottom) {
            log("To bottom dragon");
            walkMiniMap(dragonBottom.getRandomPosition(0));
        }
    }

    void switchDragonSpotsRng() throws InterruptedException {
        if (dragon != null && dragonVert == blueDragonTop) {
            log("To top dragon");
            walkExact(safeSpotTop, 8);
            log("Froze 22?");
            //sleep(random(900, 1000)); top = 1101
            //client.rotateCameraPitch(random(30, 42)); 1009
            sleep(random(500, 600));
            client.rotateCameraToAngle(random(285, 315));
            sleep(random(400, 500));
        } else if (dragon != null && dragonVert == blueDragonBottom) {
            log("To bottom dragon");
            walkExact(safeSpotBottom, 8);
            log("Froze 28?");
            //sleep(random(700, 900));
            //client.rotateCameraPitch(random(22, 30));
            sleep(random(500, 600));
            client.rotateCameraToAngle(random(242, 250));
            sleep(random(400, 500));
        } else if (dragonVert == -1) {
            if (myPlayer().getPosition().distance(safeSpotTop.getRandomPosition(0))
                    < myPlayer().getPosition().distance(safeSpotBottom.getRandomPosition(0))) {
                log("To bottom dragon");
                walkExact(safeSpotBottom, 8);
                log("Froze 28?");
                //sleep(random(700, 900));
                //client.rotateCameraPitch(random(22, 30));
                sleep(random(500, 600));
                client.rotateCameraToAngle(random(242, 250));
                sleep(random(400, 500));
            } else {
                log("To top dragon");
                walkExact(safeSpotTop, 8);
                log("Froze 22?");
                //sleep(random(900, 1000)); top = 1101
                //client.rotateCameraPitch(random(30, 42)); 1009
                sleep(random(500, 600));
                client.rotateCameraToAngle(random(285, 315));
                sleep(random(400, 500));
            }
        }
    }

    void eat() throws InterruptedException {
        status = "HP is low Eatting";
        openTab(Tab.INVENTORY);
        client.getInventory().interactWithId(foodID, "Eat");
    }

    void loot() throws InterruptedException {
        status = "Looting";
        // checks for items on the floor
        bones = closestGroundItemForName("Dragon bones");
        hides = closestGroundItemForName("Blue dragonhide");
        misc = closestGroundItem(miscItems);
        boltType = closestGroundItemForName(boltToLoot);

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
            sleep(random(800, 1200));

        } else if (bones != null && canReach(bones)) {
            bones.interact("Take");
            sleep(random(800, 1200));
        } else {
            log("Nothing to loot");
            if (!isRanging) {
                switchDragonSpots();
            } else {
                switchDragonSpotsRng();
            }

        }

    }

    void potions() throws InterruptedException {
        status = "Repotting";
        if (!isRanging) {
            if ((Math.abs(potStr - strength) < (strength * .08)) && usingStrPot) {
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
            } else if ((Math.abs(potAtt - attack) < (attack * .08)) && usingAttPot) {
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
            } else if ((Math.abs(potDef - defence) < (defence * .08)) && usingDefPot) {
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

    void goToBank() throws InterruptedException {
        status = "Teleporting to bank";
        dragon = closestNPCForName("Blue dragon");
        if (dragon != null) {
            if (myPlayer().getPosition().distance(safeZone.getRandomPosition(0)) < myPlayer().getPosition().distance(safeSpotBottom.getRandomPosition(0))) {
                walk(safeZone);
            } else {
                walk(safeSpotBottom);
            }

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
        }
    }

    void bank() throws InterruptedException {
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
                    meleePots(false);
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    meleePots(false);
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    meleePots(false);
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
                        bankEat();
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

    void bankFourDose() throws InterruptedException {
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
                    meleePots(true);
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else if (client.getInventory().isEmptyExcept(rings)) {
                    meleePots(true);
                    if (bank.contains(foodID)) {
                        bank.withdrawX(foodID, foodAmount);
                    } else {
                        log("No food left");
                        stop();
                    }
                } else {
                    bank.depositAll();
                    bank.withdraw1(2552);
                    meleePots(true);
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
                        bankEat();
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

    void meleePots(boolean four) throws InterruptedException {
        Bank bank = client.getBank();
        if (four) {
            if (usingAttPot && bank.contains(2436)) {
                bank.withdraw1(2436);
            }
            if (usingStrPot && bank.contains(2440)) {
                bank.withdraw1(2440);
            }
            if (usingDefPot && bank.contains(2442)) {
                bank.withdraw1(2442);
            }
        } else {
            if (usingAttPot && bank.contains(145)) {
                bank.withdraw1(145);
            }
            if (usingStrPot && bank.contains(157)) {
                bank.withdraw1(157);
            }
            if (usingDefPot && bank.contains(163)) {
                bank.withdraw1(163);
            }
        }
    }

    void bankRng() throws InterruptedException {
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
                        bankEat();
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

    void bankFourDoseRng() throws InterruptedException {
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
                        bankEat();
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

    void bankEat() throws InterruptedException {
        Entity bankBox = closestObject(bankChest);
        Bank bank = client.getBank();
        int needToEat = (client.getSkills().getLevel(Skill.HITPOINTS) - client.getSkills().getCurrentLevel(Skill.HITPOINTS)) / 12;
        if (bank.isOpen()) {
            bank.withdrawX(foodID, needToEat);
            sleep(random(500, 800));
            bank.close();
            sleep(random(500, 800));
            for (int i = 0; i < needToEat; i++) {
                client.getInventory().interactWithId(foodID, "Eat");
                sleep(random(1522, 1900));
            }
        } else {
            if (bankBox != null) {
                if (bankBox.isVisible()) {
                    bankBox.interact("Use");
                    sleep(random(700, 900));
                    bankEat();
                } else {
                    client.moveCameraToEntity(bankBox);
                }
            }
        }
    }

    // Anti ban
    void AntiBan() throws InterruptedException {
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
            case 2:
            case 23:
            case 24:
                status = "Anti-Ban Check Buddy List";
                openTab(Tab.FRIENDS);
                log("case 2");
                sleep(random(500, 600));
                openTab(Tab.INVENTORY);
                break;
            case 3:
                status = "Anti-Ban rotate camera";
                client.rotateCameraToAngle(random(43, 68));
                log("case 3");
                break;
            case 5:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(100).hover();
                log("case 5");
                break;
            case 7:
            case 17:
            case 26:
            case 42:
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
            case 16:
                status = "Anti-Ban rotate camera";
                client.rotateCameraToAngle(random(44, 58));
                log("case 8");
                break;
            case 11:
            case 13:
            case 21:
            case 28:
            case 38:
            case 44:
            case 32:
                status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 11");
                break;
            case 14:
            case 35:
                status = "Anti-Ban sleepy";
                sleep(random(4279, 6732));
                log("case 14");
                break;
            case 19:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(84).hover();
                log("case 19");
                break;
            case 22:
            case 34:
            case 43:
                status = "Antiban mouse move rec";
                client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 22");
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
            case 39:
            case 41:
                status = "Anti-Ban move mouse around";
                client.getInterface(548).getChild(44).hover();
                log("case 41");
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
            return (long) ((xpTNL / xpPH) * 3600000.0D);

        }
        return 0;
    }

    private String formatTime(long time) {
        int sec = (int) (time / 1000L), d = sec / 86400, h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (d < 10 ? new StringBuilder().append("0").append(d)
                .toString() : Integer.valueOf(d)) + ":" + (h < 10 ? new StringBuilder().append("0").append(h)
                .toString() : Integer.valueOf(h)) + ":" + (m < 10 ? new StringBuilder().append("0").append(m)
                .toString() : Integer.valueOf(m)) + ":" + (s < 10 ? new StringBuilder().append("0").append(s)
                .toString() : Integer.valueOf(s));
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

    void WalkAlongPath(int[][] path, boolean AscendThroughPath) {
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
            walk(new Area(path[destination][0], path[destination][1], path[destination][0], path[destination][1]));
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
