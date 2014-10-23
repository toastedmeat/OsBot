package chaosdruids;

import chaosdruids.rsItem;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.osbot.script.MethodProvider;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.Client;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.GroundItem;
import org.osbot.script.rs2.model.Item;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.skill.Skill;
import org.osbot.script.rs2.skill.Skills;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.RS2InterfaceChild;
import org.osbot.script.rs2.ui.SettingsTab;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author = "toastedmeat", info = "Kills Druids + Collects Herbs in A.", name = "ChaosDruids", version = 0.39D)
public class ChaosDruids extends Script {

    double myVersion = 0.39D;
    Skill skill = Skill.STRENGTH;

    private final Font regFont = new Font("Serif", 0, 12);
    private final Color white = new Color(255, 255, 255);
    private final Color box = new Color(175, 175, 175);
    private final Color black = new Color(0, 0, 0);
    Item food;
    final String DRUID_NAME = "Chaos druid";
    String foodName;
    String run = "False";
    String status = "STARTING UP";
    String timeTL = "";

    final int DRUID_ID = 128;
    final int BANK_BOOTH_ID = 2196;
    final int DOOR_ID = 543;
    final int LADDER_DOWN = 24306;
    final int LADDER_UP = 24307;
    final int LOGTB = 9328;
    final int LOGTD = 9330;
    int eatLevel;
    int startHP;
    int randomInt;
    int pitch;
    int angle;
    int startExp;
    int exp;
    int runInt;
    int herbsCollected = 0;
    int herbsPH;
    int mBoltCollected = 0;
    int mBoltPH;
    int lawRuneCollected = 0;
    int lawRunePH;
    int natRuneCollected = 0;
    int natRunePH;
    int harranlanderCounter = 0;
    int ranarrCounter = 0;
    int iritCounter = 0;
    int avantoeCounter = 0;
    int kwuarmCounter = 0;
    int cadantineCounter = 0;
    int dwarfCounter = 0;
    int torstolCounter = 0;
    int lantadymeCounter = 0;

    public long startTime = 0L;
    public long millis = 0L;
    public long hours = 0L;
    public long minutes = 0L;
    public long seconds = 0L;
    public long last = 0L;
    long level;
    long leveled;
    int priceH;
    int priceR;
    int priceI;
    int priceA;
    int priceK;
    int priceC;
    int priceD;
    int priceT;
    int priceL;
    int priceLaw;
    int priceMith;
    int priceNat;
    double profit;
    double profitPH;
    double expTNL;
    double expPH;
    boolean running = true;
    boolean isFighting = false;
    boolean stop = false;

    int[] IDS = {205, 207, 209, 211, 213, 215, 217, 219, 2485};
    int[] IDSB = {199, 201, 203, 1917, 1971, 526};

    String[] Names = {"Grimy harralander", "Grimy ranarr", "Grimy irit", "Grimy avantoe", "Grimy kwuarm", "Grimy cadantine", "Grimy dwarf weed", "Grimy torstol", "Grimy lantadyme"};

    public final Area TOWER_AREA = new Area(2560, 3354, 2564, 3358);
    public final Area BANK_AREA = new Area(2613, 3332, 2620, 3334);
    public final Area FRONT_TOWER_AREA = new Area(2566, 3354, 2570, 3357);
    public final Area LOG_BANK_SIDE = new Area(2602, 3336, 2602, 3336);
    public final Area LOG_DRUID_SIDE = new Area(2598, 3336, 2598, 3336);
    public final Area RANDOM_DOWN = new Area(2562, 9757, 2562, 9757);

    private int[][] PATH_BANKSIDE = {{2616, 3335}, {2613, 3339}, {2608, 3336}, {2603, 3336}};

    private int[][] PATH_DRUIDSIDE = {{2595, 3339}, {2587, 3344}, {2584, 3346}, {2580, 3348}, {2577, 3350}, {2574, 3352}, {2570, 3353}, {2567, 3355}};

    private int[][] FALL_TODRUID = {{2593, 3331}, {2588, 3334}, {2587, 3339}, {2586, 3345}, {2583, 3346}, {2579, 3348}, {2577, 3350}, {2574, 3353}, {2570, 3354}, {2567, 3356}};

    private int[][] FALL_TOBANK = {{2608, 3335}, {2614, 3339}, {2617, 3335}};

    static enum State {

        FIGHT, BANK, ANTIBAN, RANDOM, RANDOMUP, WALKTOLOGFD, WALKTOLOGFB, LOOT, IDLE, FALLTB, FALLTD, DONEBANKING, ATLOGTB, ATLOGTD, WALKTODRUIDFL, WALKTOBANKFL, ATDOOR, STARTING;
    }
    public ChaosDruids.State state = ChaosDruids.State.IDLE;
    GroundItem herb;
    GroundItem mBolt;
    GroundItem lawRune;
    GroundItem natRune;
    NPC druid;

    public void onStart() {
        checkForUpdate();
        this.client.setMouseSpeed(random(12, 18));
        this.startExp = this.client.getSkills().getExperience(this.skill);
        this.level = this.client.getSkills().getLevel(this.skill);
        this.startTime = System.currentTimeMillis();
        this.status = "Starting script";
        this.startHP = this.client.getSkills().getCurrentLevel(Skill.HITPOINTS);
        this.randomInt = random(1, 10);
        this.runInt = random(15, 27);

        rsItem H = new rsItem("clean+harralander");
        this.priceH = H.getAveragePrice();
        rsItem R = new rsItem("clean+ranarr");
        this.priceR = R.getAveragePrice();
        rsItem I = new rsItem("clean+irit");
        this.priceI = I.getAveragePrice();
        rsItem A = new rsItem("clean+avantoe");
        this.priceA = A.getAveragePrice();
        rsItem K = new rsItem("clean+kwuarm");
        this.priceK = K.getAveragePrice();
        rsItem C = new rsItem("clean+cadantine");
        this.priceC = C.getAveragePrice();
        rsItem D = new rsItem("clean+dwarf+weed");
        this.priceD = D.getAveragePrice();
        rsItem T = new rsItem("clean+torstol");
        this.priceT = T.getAveragePrice();
        rsItem L = new rsItem("clean+lantadyme");
        this.priceL = L.getAveragePrice();
        rsItem Law = new rsItem("Law+rune");
        this.priceLaw = Law.getAveragePrice();
        rsItem Mith = new rsItem("Mithril+bolts");
        this.priceMith = Mith.getAveragePrice();
        rsItem Nat = new rsItem("Nature+rune");
        this.priceNat = Nat.getAveragePrice();

        this.state = ChaosDruids.State.STARTING;

        log(new StringBuilder().append("Toasty Druids v").append(this.myVersion).toString());
        log("120k+/h");
    }

    public int onLoop() throws InterruptedException {
        this.herb = closestGroundItem(this.IDS);
        this.mBolt = closestGroundItemForName(new String[]{"Mithril bolts"});
        this.lawRune = closestGroundItemForName(new String[]{"Law rune"});
        this.natRune = closestGroundItemForName(new String[]{"Nature rune"});

        this.status = "Checking States";

        if ((this.client.getInventory().contains(199)) || (this.client.getInventory().contains(201)) || (this.client.getInventory().contains(203)) || (this.client.getInventory().contains(1917)) || (this.client.getInventory().contains(1971)) || (this.client.getInventory().contains(526))) {
            this.client.getInventory().dropAllForIds(this.IDSB);
            sleep(random(800, 1200));
        }
        checkStates();
        switch (state) {
            case FIGHT:
                this.status = "Fighting";
                fight();
                break;
            case BANK:
                this.status = "Banking";
                banking();
                break;
            case WALKTOLOGFD:
                this.status = "Walking to bank";
                walkToLogFromDruid();
                break;
            case WALKTOLOGFB:
                this.status = "Walking to Tower";
                WalkAlongPath(this.PATH_BANKSIDE, true);
                sleep(random(1000, 2000));
                this.state = ChaosDruids.State.ATLOGTD;
                break;
            case LOOT:
                this.status = "Looting";
                Looting();
                break;
            case RANDOM:
                this.status = "Running from random";
                Entity ladderdown = closestObject(new int[]{24306});

                if (myPlayer().isInArea(this.TOWER_AREA)) {
                    ladderdown.interact("Climb-down");
                    sleep(random(3000, 6000));
                    this.state = ChaosDruids.State.RANDOMUP;
                } else if (myPlayer().isInArea(this.FRONT_TOWER_AREA)) {
                    Entity door = closestObject(new int[]{543});
                    this.client.moveCameraToEntity(door);
                    door.interact("Pick-lock");
                    sleep(random(1000, 2000));
                }
                break;
            case RANDOMUP:
                Entity ladderup = closestObject(new int[]{24307});
                ladderup.interact("Climb-up");
                sleep(random(4000, 5000));
                break;
            case FALLTB:
                fallTB();
                break;
            case FALLTD:
                fallTD();
                break;
            case ATLOGTB:
                Entity logtobank = closestObject(new int[]{9328});
                this.client.rotateCameraPitch(random(55, 67));
                logtobank.interact("Walk-across");
                this.status = "Walking across the log";
                sleep(random(2000, 3000));
                break;
            case ATLOGTD:
                Entity logtoDruid = closestObject(new int[]{9330});
                this.client.rotateCameraPitch(random(55, 67));
                logtoDruid.interact("Walk-across");
                this.status = "Walking across the log";
                sleep(random(2000, 3000));
                break;
            case WALKTODRUIDFL:
                WalkAlongPath(this.PATH_DRUIDSIDE, true);
                sleep(random(1000, 2000));
                if (!myPlayer().isInArea(this.FRONT_TOWER_AREA)) {
                    walk(this.FRONT_TOWER_AREA);
                    sleep(random(1000, 2000));
                }
                this.state = ChaosDruids.State.ATDOOR;
                break;
            case WALKTOBANKFL:
                WalkAlongPath(this.PATH_BANKSIDE, false);
                sleep(random(1000, 2000));
                walk(this.BANK_AREA);
                sleep(random(1000, 2000));
                this.state = ChaosDruids.State.IDLE;
                break;
            case ATDOOR:
                this.status = "Attempting to pick-lock";
                if (myPlayer().isInArea(this.FRONT_TOWER_AREA)) {
                    Entity door = closestObject(new int[]{543});
                    this.client.moveCameraToEntity(door);
                    door.interact("Pick-lock");
                    sleep(random(1000, 2000));
                } else {
                    this.state = ChaosDruids.State.WALKTODRUIDFL;
                }
                break;
        }
        if (this.running) {
            if ((this.run == "False") && (this.client.getRunEnergy() >= this.runInt)) {
                this.status = "Toggling running";
                this.settingsTab.open();
                random(165, 279);
                setRunning(true);
                openTab(Tab.INVENTORY);
                this.run = "True";
            } else if ((this.run == "False") && (this.client.getRunEnergy() == 100)) {
                this.status = "Toggling running";
                this.settingsTab.open();
                random(186, 293);
                setRunning(true);
                openTab(Tab.INVENTORY);
                this.run = "True";
            }

            if (this.client.getRunEnergy() == 0) {
                this.runInt = random(1, 53);
                this.run = "False";
            }
        }

        if (this.client.getRunEnergy() == 0) {
            this.runInt = random(15, 47);
            this.run = "False";
        }

        return random(100, 200);
    }

    void fight() throws InterruptedException {
        Player player = this.client.getMyPlayer();

        if (!this.isFighting) {
            this.druid = closestNPCForName(new String[]{"Chaos druid"});
            if ((this.druid != null) && (!this.druid.isUnderAttack())) {
                if (this.druid.isVisible()) {
                    if (this.state != ChaosDruids.State.LOOT) {
                        this.druid.interact("Attack");
                        this.isFighting = true;
                        sleep(random(500, 800));
                    } else if (myPlayer().isAnimating()) {
                        sleep(random(500, 800));
                    }
                } else {
                    this.client.moveCameraToEntity(this.druid);
                }
            } else {
                this.druid = closestNPCForName(new String[]{"Chaos druid"});
            }
        } else {
            if ((this.isFighting == true) && (!player.isAnimating()) && (!this.druid.isFacing(player)) && (this.druid != null) && (!this.druid.isAnimating())) {
                this.druid.interact("Attack");
                sleep(random(600, 700));
            }
            if ((this.druid.isAnimating()) && (!player.isAnimating()) && (!this.druid.isFacing(player))) {
                this.isFighting = false;
            }
            if ((!player.isAnimating()) && (!player.isFacing(this.druid)) && (player.getCombatTime() > 2000)) {
                this.isFighting = false;
            }
            if ((!player.isInArea(this.TOWER_AREA)) && (this.state != ChaosDruids.State.WALKTOLOGFD)) {
                this.state = ChaosDruids.State.ATDOOR;
            }
        }
        AntiBan();
    }

    void banking() throws InterruptedException {
        Player player = this.client.getMyPlayer();
        Bank bank = this.client.getBank();
        if (this.BANK_AREA.contains(player)) {
            Entity bankbooth = closestObject(new int[]{2196});

            if (bank.isOpen()) {
                bank.depositAll();
                sleep(random(1000, 1500));
                if (this.client.getInventory().isEmpty()) {
                    this.state = ChaosDruids.State.DONEBANKING;
                }
            } else if (bankbooth != null) {
                if (bankbooth.isVisible()) {
                    bankbooth.interact("Bank");
                    sleep(random(50, 100));
                } else {
                    this.client.moveCameraToEntity(bankbooth);
                }
            }
        } else {
            walk(this.BANK_AREA);
        }
    }

    void walkToLogFromDruid() throws InterruptedException {
        if (this.TOWER_AREA.contains(this.client.getMyPlayer())) {
            Entity door = closestObject(new int[]{543});
            door.interact("Open");
            sleep(random(1000, 2012));
        }
        WalkAlongPath(this.PATH_DRUIDSIDE, false);
        sleep(random(1000, 2000));
        this.state = ChaosDruids.State.ATLOGTB;
    }

    void fallTD() throws InterruptedException {
        WalkAlongPath(this.FALL_TODRUID, true);
        sleep(random(1000, 2000));
        if (!myPlayer().isInArea(this.FRONT_TOWER_AREA)) {
            walk(this.FRONT_TOWER_AREA);
            sleep(random(1000, 2000));
        }
        if (myPlayer().isInArea(this.FRONT_TOWER_AREA)) {
            this.state = ChaosDruids.State.ATDOOR;
        }
    }

    void fallTB() throws InterruptedException {
        WalkAlongPath(this.FALL_TOBANK, true);
        sleep(random(1000, 2000));
        walk(this.BANK_AREA);
        sleep(random(1000, 2000));
        this.state = ChaosDruids.State.IDLE;
    }

    void checkStates() throws InterruptedException {
        NPC random = closestNPCForName(new String[]{"Evil Chicken", "Swarm"});

        if ((random != null) && (random.isFacing(myPlayer()))) {
            this.state = ChaosDruids.State.RANDOM;
        } else if (myPlayer().isInArea(this.RANDOM_DOWN)) {
            this.state = ChaosDruids.State.RANDOMUP;
        } else if (((!this.client.getInventory().isFull()) && (myPlayer().isInArea(this.TOWER_AREA)) && (this.herb != null) && (canReach(this.herb))) || ((this.mBolt != null) && (canReach(this.mBolt))) || ((this.lawRune != null) && (canReach(this.lawRune))) || ((this.natRune != null) && (canReach(this.natRune)))) {
            this.state = ChaosDruids.State.LOOT;
        } else if ((myPlayer().isInArea(this.TOWER_AREA)) && (this.client.getSkills().getCurrentLevel(Skill.HITPOINTS) >= this.client.getSkills().getLevel(Skill.HITPOINTS) / 2 - 10) && (!this.client.getInventory().isFull())) {
            this.state = ChaosDruids.State.FIGHT;
        } else if (((myPlayer().isInArea(this.BANK_AREA)) && (!this.client.getInventory().isFull()) && (this.state == ChaosDruids.State.DONEBANKING)) || ((myPlayer().isInArea(this.BANK_AREA)) && (this.state == ChaosDruids.State.STARTING))) {
            this.state = ChaosDruids.State.WALKTOLOGFB;
        } else if ((this.client.getInventory().isFull()) && (!myPlayer().isInArea(this.TOWER_AREA)) && (this.state == ChaosDruids.State.IDLE)) {
            this.state = ChaosDruids.State.BANK;
        } else if ((this.client.getInventory().isFull()) && (myPlayer().isInArea(this.TOWER_AREA))) {
            this.state = ChaosDruids.State.WALKTOLOGFD;
        } else if ((myPlayer().isInArea(this.FRONT_TOWER_AREA)) && (this.state != ChaosDruids.State.WALKTOLOGFD)) {
            this.state = ChaosDruids.State.ATDOOR;
        }
    }

    void Looting() throws InterruptedException {
        int currentherb = 0;
        if (!this.client.getInventory().isFull()) {
            if ((this.herb != null) && (canReach(this.herb))) {
                currentherb = this.herb.getId();
                this.herb.interact("Take");
                sleep(random(1000, 1200));
                if ((!this.client.getInventory().contains(199)) || (!this.client.getInventory().contains(201)) || (!this.client.getInventory().contains(203)) || (!this.client.getInventory().contains(1917)) || (!this.client.getInventory().contains(1971)) || (this.client.getInventory().contains(1971))) {
                    this.herbsCollected += 1;
                    switch (currentherb) {
                        case 205:
                            this.harranlanderCounter += 1;
                            break;
                        case 207:
                            this.ranarrCounter += 1;
                            break;
                        case 209:
                            this.iritCounter += 1;
                            break;
                        case 211:
                            this.avantoeCounter += 1;
                            break;
                        case 213:
                            this.kwuarmCounter += 1;
                            break;
                        case 215:
                            this.cadantineCounter += 1;
                            break;
                        case 217:
                            this.dwarfCounter += 1;
                            break;
                        case 219:
                            this.torstolCounter += 1;
                            break;
                        case 2485:
                            this.lantadymeCounter += 1;
                    }

                }

                sleep(random(400, 600));
            } else if (this.mBolt != null) {
                this.mBolt.interact("Take");
                sleep(random(400, 600));
                this.mBoltCollected += this.mBolt.getAmount();
            } else if (this.lawRune != null) {
                this.lawRune.interact("Take");
                sleep(random(400, 600));
                this.lawRuneCollected += 2;
            } else if (this.natRune != null) {
                this.natRune.interact("Take");
                sleep(random(900, 1200));
                this.natRuneCollected += 3;
            } else {
                sleep(random(500, 600));
            }
        } else {
            sleep(random(500, 600));
        }
    }

    public void onMessage(String message) {
        if (message.contains("You fail to pick the lock")) {
            Entity door = closestObject(new int[]{543});
            try {
                door.interact("Pick-lock");
                sleep(random(800, 1300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (message.contains("you have activated a trap")) {
            Entity door = closestObject(new int[]{543});
            try {
                door.interact("Pick-lock");
                sleep(random(700, 1200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (message.contains("You manage to pick the lock")) {
            this.status = "Inside Tower";
            log("YOU MADE IT IN");
            try {
                sleep(random(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.state = ChaosDruids.State.IDLE;
        }

        if (message.contains("You lose your footing and fall")) {
            try {
                sleep(random(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (message.contains("You make it across the log without")) {
            if (this.state == ChaosDruids.State.ATLOGTB) {
                this.state = ChaosDruids.State.WALKTOBANKFL;
            } else {
                this.state = ChaosDruids.State.WALKTODRUIDFL;
            }
        }
        if (message.contains("You attempt to walk across the slippery")) {
            try {
                sleep(random(1000, 1500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (message.contains("You finally come to shore")) {
            if (this.state == ChaosDruids.State.ATLOGTB) {
                this.state = ChaosDruids.State.FALLTB;
            } else {
                this.state = ChaosDruids.State.FALLTD;
            }
        }
    }

    public void onPaint(Graphics g) {
        this.pitch = this.client.getCameraPitch();
        this.angle = this.client.getCameraPitchAngle();
        this.exp = (this.client.getSkills().getExperience(this.skill) - this.startExp);
        this.leveled = (this.client.getSkills().getLevel(this.skill) - this.level);
        this.expTNL = experienceToNextLevel(this.skill);
        this.millis = (System.currentTimeMillis() - this.startTime);
        this.hours = (this.millis / 3600000L);
        this.millis -= this.hours * 3600000L;
        this.minutes = (this.millis / 60000L);
        this.millis -= this.minutes * 60000L;
        this.seconds = (this.millis / 1000L);
        this.profit = ((this.priceH * this.harranlanderCounter + this.priceR * this.ranarrCounter + this.priceI * this.iritCounter + this.priceA * this.avantoeCounter + this.priceK * this.kwuarmCounter + this.priceC * this.cadantineCounter + this.priceD * this.dwarfCounter + this.priceT * this.torstolCounter + this.priceL * this.lantadymeCounter + this.priceLaw * this.lawRuneCollected + this.priceMith * this.mBoltCollected + (this.priceNat + this.natRuneCollected)) / 1000);

        this.expPH = ((int) (this.exp * 3600000.0D / (System.currentTimeMillis() - this.startTime)));
        this.herbsPH = ((int) (this.herbsCollected * 3600000.0D / (System.currentTimeMillis() - this.startTime)));

        this.mBoltPH = ((int) (this.mBoltCollected * 3600000.0D / (System.currentTimeMillis() - this.startTime)));

        this.lawRunePH = ((int) (this.lawRuneCollected * 3600000.0D / (System.currentTimeMillis() - this.startTime)));

        this.natRunePH = ((int) (this.natRuneCollected * 3600000.0D / (System.currentTimeMillis() - this.startTime)));

        this.profitPH = (this.profit * 3600000.0D / (System.currentTimeMillis() - this.startTime));

        this.timeTL = formatTime(timeTnl(this.expTNL, this.expPH));

        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(this.regFont);
        g2.setColor(this.white);

        g2.drawString(new StringBuilder().append("Time Elapsed: ").append(this.hours).append(" hours ").append(this.minutes).append(" minutes ").append(this.seconds).append(" seconds").toString(), 10, 50);

        g2.drawString(new StringBuilder().append("Run at: ").append(this.runInt).toString(), 10, 70);
        g2.drawString(new StringBuilder().append("HP: ").append(this.client.getSkills().getCurrentLevel(Skill.HITPOINTS)).toString(), 10, 90);

        g2.drawString(new StringBuilder().append("Level: ").append(this.client.getSkills().getCurrentLevel(this.skill)).append("(").append(this.level).append("+").append(this.leveled).append(")").toString(), 10, 100);

        g2.drawString(new StringBuilder().append("Exp Gained: ").append(this.exp).toString(), 10, 110);
        g2.drawString(new StringBuilder().append("Exp Per Hour: ").append(this.expPH).toString(), 10, 120);
        g2.drawString(new StringBuilder().append("Exp till next Level: ").append(this.expTNL).toString(), 10, 130);
        g2.drawString(new StringBuilder().append("Time till Level: ").append(this.timeTL).toString(), 10, 140);
        g2.drawString(new StringBuilder().append("Herbs collected: ").append(this.herbsCollected).toString(), 10, 160);
        g2.drawString(new StringBuilder().append("Herbs per hour: ").append(this.herbsPH).toString(), 10, 170);
        g2.drawString(new StringBuilder().append("mBolt collected: ").append(this.mBoltCollected).toString(), 10, 180);
        g2.drawString(new StringBuilder().append("mBolt per hour: ").append(this.mBoltPH).toString(), 10, 190);
        g2.drawString(new StringBuilder().append("Law Rune collected: ").append(this.lawRuneCollected).toString(), 10, 200);
        g2.drawString(new StringBuilder().append("Law Rune per hour: ").append(this.lawRunePH).toString(), 10, 210);
        g2.drawString(new StringBuilder().append("Nature Rune collected: ").append(this.natRuneCollected).toString(), 10, 220);
        g2.drawString(new StringBuilder().append("Nature Rune per hour: ").append(this.natRunePH).toString(), 10, 230);
        g2.drawString(new StringBuilder().append("Status: ").append(this.status).toString(), 10, 250);
        g2.setColor(this.black);
        g2.drawString(new StringBuilder().append("State: ").append(this.state).toString(), 360, 458);

        g2.setComposite(AlphaComposite.getInstance(3, 0.6F));

        g2.setColor(this.box);
        g2.fillRect(10, 260, 500, 80);

        g2.setComposite(AlphaComposite.getInstance(3, 1.0F));

        g2.setColor(this.black);
        g2.drawString(new StringBuilder().append("Toasty Druids v").append(this.myVersion).toString(), 10, 270);
        g2.drawString("Made by Toasty", 270, 270);

        g2.drawString(new StringBuilder().append("Harralander collected: ").append(this.harranlanderCounter).toString(), 10, 290);
        g2.drawString(new StringBuilder().append("Ranarr collected: ").append(this.ranarrCounter).toString(), 10, 300);
        g2.drawString(new StringBuilder().append("Irit collected: ").append(this.iritCounter).toString(), 10, 310);
        g2.drawString(new StringBuilder().append("Avantoe collected: ").append(this.avantoeCounter).toString(), 10, 320);
        g2.drawString(new StringBuilder().append("Kwuarm collected: ").append(this.kwuarmCounter).toString(), 10, 330);

        g2.drawString(new StringBuilder().append("Cadantine collected: ").append(this.cadantineCounter).toString(), 226, 290);
        g2.drawString(new StringBuilder().append("Dwarf Weed collected: ").append(this.dwarfCounter).toString(), 226, 300);
        g2.drawString(new StringBuilder().append("torstol collected: ").append(this.torstolCounter).toString(), 226, 310);
        g2.drawString(new StringBuilder().append("Lantadyme collected: ").append(this.lantadymeCounter).toString(), 226, 320);
        g2.drawString(new StringBuilder().append("Profit: ").append(this.profit).append(" k").toString(), 226, 330);
        g2.drawString(new StringBuilder().append("Profit/h: ").append(this.profitPH).append(" k/h").toString(), 226, 340);

        showItemIDs(g2);
    }

    public void onExit() {
    }

    public void AntiBan()
            throws InterruptedException {
        Rectangle k = new Rectangle(1 + random(489), 1 + random(328), 10, 10);
        switch (random(1, 4500)) {
            case 1:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraPitch(random(55, 60));
                log("case 1");
                break;
            case 2:
                this.status = "Anti-Ban Check Buddy List";
                openTab(Tab.FRIENDS);
                log("case 2");
                sleep(random(500, 600));
                openTab(Tab.INVENTORY);
                break;
            case 3:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraPitch(random(67, 90));
                this.client.rotateCameraToAngle(random(43, 68));
                log("case 3");
                break;
            case 5:
                this.status = "Anti-Ban move mouse around";
                this.client.getInterface(548).getChild(100).hover();
                log("case 5");
                break;
            case 7:
                this.status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);

                if (this.client.getInterface(320).getChild(126).isVisible()) {
                    this.client.getInterface(320).getChild(126).hover();
                    sleep(random(1394, 1867));
                    openTab(Tab.INVENTORY);
                }
                log("case 7");
                break;
            case 8:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraToAngle(random(44, 58));
                log("case 8");
                break;
            case 11:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 11");
                break;
            case 13:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 13");
                break;
            case 14:
                this.status = "Anti-Ban sleepy";
                sleep(random(4279, 6732));
                log("case 14");
                break;
            case 16:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraPitch(random(84, 100));
                log("case 16");
                break;
            case 17:
                this.status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);

                if (this.client.getInterface(320).getChild(126).isVisible()) {
                    this.client.getInterface(320).getChild(126).hover();
                    sleep(random(1200, 1500));
                    openTab(Tab.INVENTORY);
                }
                log("case 17");
                break;
            case 19:
                this.status = "Anti-Ban move mouse around";
                this.client.getInterface(548).getChild(84).hover();
                log("case 19");
                break;
            case 21:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 21");
                break;
            case 22:
                this.status = "Antiban mouse move rec";
                this.client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 22");
                break;
            case 23:
                this.status = "Anti-Ban rotate Camera";
                this.client.rotateCameraPitch(random(45, 65));
                log("case 23");
                break;
            case 24:
                this.status = "Anti-Ban Check Buddy List";
                openTab(Tab.FRIENDS);
                log("case 24");
                sleep(random(400, 500));
                openTab(Tab.INVENTORY);
                break;
            case 26:
                this.status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);

                if (this.client.getInterface(320).getChild(126).isVisible()) {
                    this.client.getInterface(320).getChild(126).hover();
                    sleep(random(1442, 1867));
                    openTab(Tab.INVENTORY);
                }
                log("case 26");
                break;
            case 28:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 28");
                break;
            case 30:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraToAngle(random(41, 66));
                log("case 30");
                break;
            case 31:
                this.status = "Anti-Ban move mouse around";
                this.client.getInterface(548).getChild(75).hover();
                log("case 31");
                break;
            case 32:
                this.status = "Anti-Ban move mouse out 3";
                moveMouseOutsideScreen();
                log("case 32");
                break;
            case 34:
                this.status = "Antiban mouse move rec";
                this.client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 34");
                break;
            case 35:
                this.status = "Anti-Ban sleepy";
                sleep(random(4000, 5000));
                log("case 35");
                break;
            case 38:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 38");
                break;
            case 39:
                this.status = "Anti-Ban rotate camera";
                this.client.rotateCameraPitch(random(45, 60));
                log("case 39");
                break;
            case 41:
                this.status = "Anti-Ban move mouse around";
                this.client.getInterface(548).getChild(44).hover();
                log("case 41");
                break;
            case 42:
                this.status = "Anti-Ban check skill";
                openTab(Tab.SKILLS);

                if (this.client.getInterface(320).getChild(126).isVisible()) {
                    this.client.getInterface(320).getChild(126).hover();
                    sleep(random(1481, 1957));
                    openTab(Tab.INVENTORY);
                }
                log("case 42");
                break;
            case 43:
                this.status = "Antiban mouse move rec";
                this.client.moveMouseTo(new RectangleDestination(k), false, false, false);
                log("case 43");
                break;
            case 44:
                this.status = "Anti-Ban move mouse out";
                moveMouseOutsideScreen();
                log("case 44");
                break;
            case 45:
                this.status = "Anti-Ban move mouse around";
                this.client.getInterface(548).getChild(109).hover();
                log("case 45");
            case 4:
            case 6:
            case 9:
            case 10:
            case 12:
            case 15:
            case 18:
            case 20:
            case 25:
            case 27:
            case 29:
            case 33:
            case 36:
            case 37:
            case 40:
        }
    }

    private int experienceToNextLevel(Skill skill) {
        int[] xpForLevels = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431};

        int xp = this.client.getSkills().getExperience(skill);
        for (int i = 0; i < 99; i++) {
            if (xp < xpForLevels[i]) {
                return xpForLevels[i] - xp;
            }
        }
        return 200000000 - xp;
    }

    long timeTnl(double xpTNL, double xpPH) {
        if (xpPH > 0.0D) {
            long timeTNL = (long) (xpTNL / xpPH * 3600000.0D);
            return timeTNL;
        }
        return 0L;
    }

    private String formatTime(long time) {
        int sec = (int) (time / 1000L);
        int d = sec / 86400;
        int h = sec / 3600;
        int m = sec / 60 % 60;
        int s = sec % 60;
        return new StringBuilder().append(d < 10 ? new StringBuilder().append("0").append(d).toString() : Integer.valueOf(d)).append(":").append(h < 10 ? new StringBuilder().append("0").append(h).toString() : Integer.valueOf(h)).append(":").append(m < 10 ? new StringBuilder().append("0").append(m).toString() : Integer.valueOf(m)).append(":").append(s < 10 ? new StringBuilder().append("0").append(s).toString() : Integer.valueOf(s)).toString();
    }

    public boolean WalkAlongPath(int[][] path, boolean AscendThroughPath, int distanceFromEnd) {
        if (distanceToPoint(AscendThroughPath ? path[(path.length - 1)][0] : path[0][0], AscendThroughPath ? path[(path.length - 1)][1] : path[0][1]) <= distanceFromEnd) {
            return true;
        }
        WalkAlongPath(path, AscendThroughPath);
        return false;
    }

    public void WalkAlongPath(int[][] path, boolean AscendThroughPath) {
        int destination = 0;
        for (int i = 0; i < path.length; i++) {
            if (distanceToPoint(path[i][0], path[i][1]) < distanceToPoint(path[destination][0], path[destination][1])) {
                destination = i;
            }
        }
        if (this.client.getMyPlayer().isMoving()) {
            if (distanceToPoint(path[destination][0], path[destination][1]) > (isRunning() ? 3 : 2)) {
                return;
            }
        }
        if (((AscendThroughPath) && (destination != path.length - 1)) || ((!AscendThroughPath) && (destination != 0))) {
            destination += (AscendThroughPath ? 1 : -1);
        }
        try {
            log(new StringBuilder().append("Walking to node:").append(destination).toString());
            walk(new Position(path[destination][0], path[destination][1], 0));
            Thread.sleep(700 + MethodProvider.random(600));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math.pow(this.client.getMyPlayer().getX() - pointX, 2.0D) + Math.pow(this.client.getMyPlayer().getY() - pointY, 2.0D));
    }

    private void checkForUpdate() {
        try {
            URL url = new URL("http://pastebin.com/raw.php?i=wDSqCLwe");
            Scanner s = new Scanner(url.openStream());
            String latestVersion = s.next();
            log(new StringBuilder().append("[Toasty Druids] My version: ").append(this.myVersion).toString());
            log(new StringBuilder().append("[Toasty Druids] Latest version: ").append(latestVersion).toString());
            double latest = Double.parseDouble(latestVersion);
            if (latest == this.myVersion) {
                log("[Toasty Druids] We are up to date!");
            } else {
                updateScript();
                this.stop = true;
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateScript() {
        try {
            URL urlDL = new URL("https://dl.dropboxusercontent.com/s/ayrt8s9ggsj7fje/ChaosDruids.jar.Desktop");

            InputStream in = urlDL.openStream();

            byte[] buf = new byte[4096];
            String fs = System.getProperty("file.separator");
            String filename = new StringBuilder().append(fs).append("OSBot").append(fs).append("scripts").append(fs).append("ChaosDruids.jar").toString();

            OutputStream os = new FileOutputStream(new StringBuilder().append(System.getProperty("user.home")).append(filename).toString());
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                os.write(buf, 0, bytesRead);
            }
            os.flush();
            os.close();
            in.close();
            JOptionPane.showMessageDialog(null, "Script has been automatically updated! Please refresh your scripts list and run the script again.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showItemIDs(Graphics2D g2) {
        Composite old = g2.getComposite();

        if (currentTab() == Tab.INVENTORY) {
            Item[] items = this.client.getInventory().getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    g2.setComposite(AlphaComposite.getInstance(3, 0.6F));

                    Rectangle r = this.client.getInventory().getDestinationForSlot(i);

                    int nx = (int) r.getX();
                    int ny = (int) r.getY();
                    int h = g2.getFontMetrics().getHeight();

                    String s = "";
                    switch (items[i].getId()) {
                        case 205:
                        case 206:
                            s = "Harralander";
                            break;
                        case 207:
                        case 208:
                            s = "Ranarr";
                            break;
                        case 209:
                        case 210:
                            s = "Irit";
                            break;
                        case 211:
                        case 212:
                            s = "Avantoe";
                            break;
                        case 213:
                        case 214:
                            s = "Kwuarm";
                            break;
                        case 215:
                        case 216:
                            s = "Cadantine";
                            break;
                        case 217:
                        case 218:
                            s = "Dwarf weed";
                            break;
                        case 219:
                        case 220:
                            s = "Torstol";
                            break;
                        case 2485:
                        case 2486:
                            s = "Lantadyme";
                            break;
                        default:
                            s = new StringBuilder().append("").append(items[i].getId()).toString();
                    }
                    g2.setColor(Color.WHITE);
                    g2.fillRect(nx + (int) ((r.getWidth() - g2.getFontMetrics().stringWidth(s)) / 2.0D) - 2, ny + h / 4, g2.getFontMetrics().stringWidth(s) + 4, h);

                    g2.setComposite(AlphaComposite.getInstance(3, 1.0F));

                    g2.setColor(Color.BLACK);
                    g2.drawString(s, nx + (int) ((r.getWidth() - g2.getFontMetrics().stringWidth(s)) / 2.0D), ny + h);
                }
            }

        }

        g2.setComposite(old);
    }

}
