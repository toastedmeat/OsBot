/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toastytut;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static org.osbot.script.MethodProvider.random;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.ui.EquipmentSlot;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author = "Toastedmeat", info = "If you don't know what this does (facepalm)",
        name = "Toasty's Tutorial Island", version = 1.6)
public class ToastyTut extends Script {

    //hmmm
    private final double Version = 1.6;
    //Area's
    private final Area firstDoor = new Area(3097, 3107, 3097, 3017);
    private final Area gate1 = new Area(3090, 3091, 3091, 3093);
    private final Area cookingArea = new Area(3073, 3082, 3078, 3091);
    private final Area bankDoor1 = new Area(3125, 3123, 3125, 3125);
    private final Area bankDoor2 = new Area(3130, 3123, 3130, 3125);

    //Paths
    private final int[][] toFisher = new int[][]{{3098, 3107}, {3103, 3104}, {3103, 3100}, {3102, 3095}};
    private final int[][] toGate1 = new int[][]{{3104, 3094}, {3101, 3096}, {3095, 3091}, {3090, 3092}};
    private final int[][] toCookingArea = new int[][]{{3089, 3092}, {3086, 3083}, {3079, 3084}};
    private final int[][] toQuester = new int[][]{{3071, 3090}, {3070, 3100},
    {3070, 3110}, {3073, 3120}, {3079, 3128}, {3086, 3127}
    };
    private final int[][] toMiningSmith = new int[][]{{3088, 9520}, {3079, 9516}, {3081, 9508}, {3081, 9505}};
    private final int[][] toCombat = new int[][]{{3082, 9499}, {3086, 9504}, {3094, 9503}};
    private final int[][] toChurch = new int[][]{{3130, 3124}, {3134, 3122}, {3134, 3116}, {3130, 3109}, {3130, 3107}};
    private final int[][] toWizard = new int[][]{{3122, 3102}, {3126, 3095}, {3130, 3089}, {3140, 3087}};
    private final int[][] toLadderComb = new int[][]{{3103, 9505}, {3106, 9509}, {3110, 9513},
    {3112, 9518}, {3111, 9523}, {3111, 9525}};

    //Boolean's
    private boolean passedGate1;
    private int runInt;

    //Timer
    private long startTime = 0L;

    //ints
    private enum State {

        IDLE, FIRST, SURVIVAL, COOKING, QUEST, MINESMITH, COMB, BANK,
        FINANCE, PRAYER, MAGIC, DONE, CONTINUECONVO
    }
    private State state = State.IDLE;

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        passedGate1 = false;
    }

    @Override
    public int onLoop() throws InterruptedException {
        state = checkStates();
        switch (state) {
            case FIRST:
                init();
                break;
            case SURVIVAL:
                survival();
                break;
            case COOKING:
                cook();
                break;
            case QUEST:
                questing();
                break;
            case MINESMITH:
                mineAndSmith();
                break;
            case COMB:
                combat();
                break;
            case BANK:
                banking();
                break;
            case FINANCE:
                finance();
                break;
            case PRAYER:
                prayer();
                break;
            case MAGIC:
                magic();
                break;
            case DONE:
                log("All done.");
                stop();
                break;
            case CONTINUECONVO:
                while (continueConvo()) {
            }
                break;
        }
        if (state != State.FIRST) {
            runningCheck();
        }
        return 600;
    }

    @Override
    public void onPaint(Graphics g) {

        long millis = (System.currentTimeMillis() - startTime);
        long hours = (millis / 3600000L);
        millis -= hours * 3600000L;
        long minutes = (millis / 60000L);
        millis -= minutes * 60000L;
        long seconds = (millis / 1000L);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawString("Version: " + Version, 150, 273);

        g2.drawString("Time Elapsed: " + hours + " hours " + minutes
                + " minutes " + seconds + " seconds", 150, 293);

        g2.setColor(Color.red);
        g2.drawString("State: " + state, 325, 336);
    }

    @Override
    public void onExit() {
    }

    State checkStates() throws InterruptedException {
        if (continueConvo()) {
            return State.CONTINUECONVO;
        } else if (myPlayer().isInArea(new Area(3228, 3226, 3235, 3235))) {
            return State.DONE;
        } else if ((canSeeText("Your final instructor!") && new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Your final instructor!"))
                || canSeeText("Open up your final menu.")
                || canSeeText("Cast Wind Strike at a chicken.")
                || canSeeText("You have almost completed the tutorial!")
                || (canSeeText("Yes") && new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("You have almost completed the tutorial!"))) {
            return State.MAGIC;
        } else if (canSeeText("Prayer.")
                || canSeeText("Your Prayer menu.")
                || canSeeText("Talk with Brother Brace and he'll tell you about prayers.")
                || canSeeText("Friends list.")
                || canSeeText("This is your friends list.")
                || canSeeText("This is your ignore list.")
                || canSeeText("Your final instructor!")) {
            return State.PRAYER;
        } else if (canSeeText("Financial advice.")
                || canSeeText("Continue through the next door.")) {
            return State.FINANCE;
        } else if (canSeeText("Banking.")
                || canSeeText("This is your bank box.")
                || (canSeeText("Yes") && new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Banking."))) {
            return State.BANK;
        } else if (canSeeText("You've finished in this area.")
                || canSeeText("Combat.")
                || canSeeText("Wielding weapons.")
                || canSeeText("This is your worn inventory.")
                || canSeeText("Worn interface")
                || canSeeText("You're now holding your dagger.")
                || canSeeText("Unequipping items.")
                || canSeeText("Combat interface.")
                || canSeeText("This is your combat interface.")
                || canSeeText("Attacking.")
                || canSeeText("Sit back and watch.")
                || canSeeText("Well done, you've made your first kill!")
                || canSeeText("Rat ranging.")
                || (canSeeText("Moving on.") && new RS2Interface(client.getBot(), 372) != null
                && new RS2Interface(client.getBot(), 372).getChild(0).getMessage().equalsIgnoreCase("Moving on."))) {
            return State.COMB;
        } else if (canSeeText("Mining and Smithing.")
                || canSeeText("Prospecting.")
                || canSeeText("It's tin.")
                || canSeeText("It's copper.")
                || canSeeText("Mining.")
                || canSeeText("Smelting.")
                || canSeeText("You've made a bronze bar!")
                || canSeeText("Smithing a dagger.")) {
            return State.MINESMITH;
        } else if (canSeeText("Run to the next guide.")
                || canSeeText("Talk with the Quest Guide.")
                || canSeeText("Open the Quest Journal.")
                || canSeeText("Your Quest Journal.")
                || canSeeText("Moving on.")) {
            return State.QUEST;
        } else if (canSeeText("Find your next instructor.")
                || canSeeText("Making dough.")
                || canSeeText("Cooking dough.")
                || canSeeText("Cooking dough")
                || canSeeText("The music player.")
                || canSeeText("Emotes.")
                || canSeeText("Emotes.")
                || canSeeText("Running.")) {
            return State.COOKING;
        } else if (canSeeText("Moving around")
                || canSeeText("Viewing the items that you were given.")
                || canSeeText("Cut down a tree")
                || canSeeText("Making a fire")
                || canSeeText("to see your skill stats.")
                || canSeeText("Your skill stats.")
                || canSeeText("Catch some Shrimp.")
                || canSeeText("Cooking your shrimp.")
                || canSeeText("Burning your shrimp.")
                || canSeeText("Well done, you've just cooked your first RuneScape meal.")) {
            return State.SURVIVAL;
        } else if (canSeeText("Moving around")
                || canSeeText("Getting started")
                || canSeeText("Please click on the flashing spanner icon found at the bottom")
                || canSeeText("Player controls")
                || canSeeText("Interacting with scenery")) {
            return State.FIRST;
        }
        return State.IDLE;
    }

    void init() throws InterruptedException {
        NPC first = closestNPCForName("RuneScape Guide");
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Getting started")
                && client.getInterface(372).getChild(0).isVisible()) {
            first.interact("Talk-to");
            sleep(random(800, 900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(2).getMessage().equalsIgnoreCase("Please click on the flashing spanner icon found at the bottom")) {
            openTab(Tab.SETTINGS);
            sleep(random(600, 700));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Player controls")
                && client.getInterface(421).getChild(1).isVisible()) {
            first.interact("Talk-to");
            sleep(random(600, 700));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Interacting with scenery")) {
            walk(firstDoor);
            sleep(random(800, 900));
            Entity door1 = closestObjectForName("Door");
            door1.interact("Open");
            sleep(random(1800, 1900));
        }
    }

    void survival() throws InterruptedException {
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Moving around"))
                && client.getInterface(372).getChild(0).isVisible()
                && (myPlayer().getX() == 3097 && myPlayer().getY() == 3107 && myPlayer().getZ() == 0)) {
            walk(firstDoor);
            sleep(random(800, 900));
            Entity door1 = closestObjectForName("Door");
            door1.interact("Open");
            sleep(random(1800, 1900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Moving around"))
                && client.getInterface(372).getChild(0).isVisible()) {
            WalkAlongPath(toFisher, true);
            NPC survive = closestNPCForName("Survival Expert");
            survive.interact("Talk-to");
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Viewing the items that you were given.")) {
            log("Opening inventory.");
            openTab(Tab.INVENTORY);
            sleep(random(600, 700));
        }

        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Cut down a tree")) {
            log("Chopping down a tree.");
            Entity tree = closestObjectForName("Tree");
            if (tree != null && !myPlayer().isAnimating()) {
                tree.interact("Chop down");
                sleep(random(1500, 2100));
            }
        }

        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Making a fire")) {
            log("Making fire.");
            client.getInventory().interactWithName("Tinderbox", "Use");
            sleep(random(300, 600));
            client.getInventory().interactWithName("Logs", "Use");
            sleep(random(1200, 1600));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(3).getMessage().equalsIgnoreCase("to see your skill stats.")) {
            openTab(Tab.SKILLS);
            sleep(random(700, 900));
            openTab(Tab.INVENTORY);
            sleep(random(700, 900));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Your skill stats.")) {
            NPC survive = closestNPCForName("Survival Expert");
            survive.interact("Talk-to");
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Catch some Shrimp.")) {
            NPC spot = closestNPCForName("Fishing spot");
            if (!myPlayer().isAnimating()) {
                spot.interact("Net");
                sleep(random(8500));
                spot.interact("Net");
                sleep(random(6500));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Cooking your shrimp.")) {
            Entity fire = closestObjectForName("Fire");
            if (fire != null) {
                if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                    client.getInventory().interactWithName("Raw shrimps", "Use");
                    sleep(random(800));
                    selectOption(null, fire.getMouseDestination(), "Use", "Raw shrimps -> Fire");
                    sleep(random(2000, 2800));
                }
            } else {
                log("Making fire.");
                client.getInventory().interactWithName("Tinderbox", "Use");
                sleep(random(300, 600));
                if (client.getInventory().contains("Logs")) {
                    client.getInventory().interactWithName("Logs", "Use");
                    sleep(random(1200, 1600));
                } else {
                    log("Chopping down a tree.");
                    Entity tree = closestObjectForName("Tree");
                    tree.interact("Chop down");
                    sleep(random(1500, 2100));
                }
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Burning your shrimp.")) {
            Entity fire = closestObjectForName("Fire");
            if (client.getInventory().contains("Raw shrimps")) {
                if (fire != null) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        client.getInventory().interactWithName("Raw shrimps", "Use");
                        sleep(random(800));
                        selectOption(null, fire.getMouseDestination(), "Use", "Raw shrimps -> Fire");
                        sleep(random(800));
                    }
                } else {
                    log("Making fire.");
                    client.getInventory().interactWithName("Tinderbox", "Use");
                    sleep(random(300, 600));
                    if (client.getInventory().contains("Logs")) {
                        client.getInventory().interactWithName("Logs", "Use");
                        sleep(random(1200, 1600));
                    } else {
                        log("Chopping down a tree.");
                        Entity tree = closestObjectForName("Tree");
                        tree.interact("Chop down");
                        sleep(random(1500, 2100));
                    }
                }
            } else {
                NPC spot = closestNPCForName("Fishing spot");
                if (!myPlayer().isAnimating()) {
                    spot.interact("Net");
                    sleep(random(6500));
                }
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Well done, you've just cooked your first RuneScape meal.")) {
            if (!myPlayer().isInArea(gate1) && !passedGate1) {
                WalkAlongPath(toGate1, true);
            }
            if (myPlayer().isInArea(gate1)) {
                Entity gate = closestObjectForName("Gate");
                gate.interact("Open");
                sleep(random(1500));
            }
        }
    }

    void cook() throws InterruptedException {
        NPC chef = closestNPCForName("Master Chef");
        if ((!myPlayer().isInArea(cookingArea) || (chef != null && !canReach(chef)))
                && new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Find your next instructor.")) {
            WalkAlongPath(toCookingArea, true);
            sleep(random(600));
            Entity door2 = closestObjectForName("Door");
            if (door2 != null) {
                door2.interact("Open");
                sleep(random(800));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Find your next instructor.")
                && chef != null && canReach(chef)) {
            if (chef != null && chef.getPosition().distance(myPlayer().getPosition()) < 10) {
                chef.interact("Talk-to");
                sleep(random(800));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Making dough.")) {
            client.getInventory().interactWithName("Pot of flour", "Use");
            sleep(random(400));
            client.getInventory().interactWithName("Bucket of water", "Use");
            sleep(random(600));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Cooking dough.")) {
            Entity range = closestObjectForName("Range");
            if (range.getPosition().distance(myPlayer().getPosition()) < 3) {
                if (range.isVisible()) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        client.getInventory().interactWithName("Bread dough", "Use");
                        sleep(random(500));
                        selectOption(null, range.getMouseDestination(), "Use", "Bread dough -> Range");
                        sleep(random(800));
                    }
                } else {
                    client.moveCameraToEntity(range);
                }
            } else {
                walk(new Position(3076, 3082, 0));
                sleep(random(900, 1000));
            }
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Cooking dough")) {
            openTab(Tab.MUSIC);
            sleep(random(800));
            openTab(Tab.INVENTORY);
            sleep(random(500));
            walk(new Position(3073, 3090, 0));
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("The music player.")) {

            if (myPlayer().getX() == 3073 && myPlayer().getY() == 3090 && myPlayer().getZ() == 0) {
                Entity door = closestObjectForName("Door");
                door.interact("Open");
                sleep(random(800));
            } else {
                walk(new Position(3073, 3090, 0));
                sleep(random(800));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Emotes.")) {
            openTab(Tab.EMOTES);
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Emotes.")) {
            client.getInterface(464).getChild(47).interact("Laugh");
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Running.")) {
            openTab(Tab.SETTINGS);
            setRunning(true);
            sleep(random(600));
        }
    }

    void questing() throws InterruptedException {
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Run to the next guide.")) {
            WalkAlongPath(toQuester, true);
            sleep(random(500));

            if (myPlayer().getX() == 3086 && myPlayer().getY() == 3127 && myPlayer().getZ() == 0) {
                Entity door = closestObjectForName("Door");
                door.interact("Open");
                sleep(random(900));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Talk with the Quest Guide.")) {
            NPC questGuide = closestNPCForName("Quest Guide");
            questGuide.interact("Talk-to");
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Open the Quest Journal.")) {
            openTab(Tab.QUEST);
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Your Quest Journal.")) {
            NPC questGuide = closestNPCForName("Quest Guide");
            questGuide.interact("Talk-to");
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Moving on.")) {
            Entity ladder = closestObjectForName("Ladder");
            if (ladder.getPosition().distance(myPlayer().getPosition()) < 3) {
                if (ladder.isVisible()) {
                    ladder.interact("Climb-down");
                    sleep(random(1500, 1800));
                } else {
                    client.moveCameraToEntity(ladder);
                    sleep(random(1500));
                }
            } else {
                walk(new Position(3088, 3120, 0));
                sleep(random(800, 1000));
            }
        }
    }

    void mineAndSmith() throws InterruptedException {
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Mining and Smithing.")) {
            WalkAlongPath(toMiningSmith, true);
            sleep(random(900));
            NPC mining = closestNPCForName("Mining Instructor");
            if (mining != null && mining.isVisible()) {
                mining.interact("Talk-to");
            } else if (!mining.isVisible()) {
                client.moveCameraToEntity(mining);
            } else if (mining == null) {
                walk(new Position(3080, 9504, 0));
            }
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Prospecting.")) {
            walk(new Position(3078, 9504, 0));
            sleep(random(1000));
            Entity tinRock = closestObjectForName("Rocks");
            tinRock.interact("Prospect");
            sleep(random(1800));
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("It's tin.")) {
            walk(new Position(3082, 9501, 0));
            sleep(random(1000));
            Entity copperRock = closestObjectForName("Rocks");
            copperRock.interact("Prospect");
            sleep(random(1800));
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("It's copper.")) {
            NPC mining = closestNPCForName("Mining Instructor");
            if (mining.isVisible()) {
                mining.interact("Talk-to");
                sleep(random(800, 1200));
            } else {
                client.moveCameraToEntity(mining);
                sleep(random(800, 1200));
            }
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Mining.")
                && !client.getInterface(372).getChild(1)
                .getMessage().equalsIgnoreCase("Now you have some tin ore you just need some copper ore,")) { // need to fix
            walk(new Position(3078, 9504, 0));
            sleep(random(1000));
            Entity tinRock = closestObjectForName("Rocks");
            tinRock.interact("Mine");
            sleep(random(3800));
        }
        if (client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Now you have some tin ore you just need some copper ore,")) {
            walk(new Position(3082, 9501, 0));
            sleep(random(1000));
            Entity copperRock = closestObjectForName("Rocks");
            copperRock.interact("Mine");
            sleep(random(3800));
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Smelting.")) {
            Entity furn = closestObjectForName("Furnace");
            if (furn.getPosition().distance(myPlayer().getPosition()) < 5) {
                if (furn.isVisible()) {
                    openTab(Tab.INVENTORY);
                    sleep(random(1000));
                    client.getInventory().interactWithName("Copper ore", "Use");
                    sleep(random(1000));
                    selectOption(null, furn.getMouseDestination(), "Use", "Copper ore -> Furnace");
                    sleep(random(1000));
                } else {
                    client.rotateCameraToAngle(random(160, 200));
                    sleep(random(600, 700));
                }
            } else {
                walk(new Position(3079, 9498, 0));
                sleep(random(700, 800));
            }
        }

        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("You've made a bronze bar!")) {
            NPC mining = closestNPCForName("Mining Instructor");
            if (mining.getPosition().distance(myPlayer().getPosition()) < 3) {
                if (mining != null && mining.isVisible()) {
                    mining.interact("Talk-to");
                } else if (!mining.isVisible()) {
                    client.moveCameraToEntity(mining);
                }
            } else {
                walk(mining);
                sleep(random(900));
            }
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Smithing a dagger.")) {
            if (client.getInterface(312) != null && client.getInterface(312).isValid()) {
                client.moveMouseTo(new RectangleDestination(20, 50, 20, 20), false, true, false);
                sleep(random(4000, 5500));
            } else {
                walk(new Position(3082, 9499, 0));
                sleep(random(1000));
                Entity anvil = closestObjectForName("Anvil");
                client.getInventory().interactWithName("Bronze bar", "Use");
                sleep(random(600));
                selectOption(null, anvil.getMouseDestination(), "Use", "Bronze bar -> Anvil");
            }
        }
    }

    void combat() throws InterruptedException {
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("You've finished in this area.")) {
            WalkAlongPath(toCombat, true);
            sleep(random(500));
            Entity gate = closestObjectForName("Gate");
            if (!myPlayer().isMoving() && gate != null && myPlayer().getX() < 3095) {
                if (gate.isVisible()) {
                    gate.interact("Open");
                    sleep(random(800));
                } else {
                    client.moveCameraToEntity(gate);
                }
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Combat.")) {
            walk(new Position(3106, 9509, 0));
            NPC person = closestNPCForName("Combat Instructor");
            if (person.isVisible()) {
                person.interact("Talk-to");
                sleep(random(1000));
            } else {
                client.moveCameraToEntity(person);
                sleep(random(1000));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Wielding weapons.")) {
            openTab(Tab.EQUIPMENT);
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("This is your worn inventory.")) {
            client.getInterface(387).getChild(51).interact("Show Equipment Stats");
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Worn interface")) {
            client.getInventory().interactWithName("Bronze dagger", "Wear");
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("You're now holding your dagger.")) {
            if (client.getInterface(465) != null && client.getInterface(465).isValid()) {
                client.getInterface(465).getChild(77).interact("Close");
                sleep(random(900));
                openTab(Tab.INVENTORY);
                sleep(random(800));
            }
            NPC guy = closestNPCForName("Combat Instructor");
            if (guy.isVisible()) {
                guy.interact("Talk-to");
                sleep(random(1000));
            } else {
                client.moveCameraToEntity(guy);
                sleep(random(1000));
            }
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Unequipping items.")) {
            openTab(Tab.INVENTORY);
            sleep(random(800));
            client.getInventory().interactWithName("Bronze sword", "Wield");
            sleep(random(600));
            client.getInventory().interactWithName("Wooden shield", "Wield");
            sleep(random(600));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Combat interface.")) {
            openTab(Tab.ATTACK);
            sleep(random(600));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("This is your combat interface.")) {
            walk(new Position(3111, 9518, 0));
            sleep(random(1000));
            Entity gate = closestObjectForName("Gate");
            if (gate.isVisible()) {
                gate.interact("Open");
                sleep(random(600));
            } else {
                client.moveCameraToEntity(gate);
                sleep(random(600));
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Attacking.")) {
            client.getInterface(593).getChild(7).interact("Lunge");
            sleep(random(800));
            if (!myPlayer().isAnimating()) {
                if (!myPlayer().isMoving()) {
                    NPC rat = closestAttackableNPCForName("Giant rat");
                    if (!myPlayer().isFacing(rat) && !rat.isFacing(myPlayer())) {
                        if (rat.isVisible()) {
                            rat.interact("Attack");
                            sleep(random(900));
                        } else {
                            client.moveCameraToEntity(rat);
                            sleep(random(900));
                        }
                    }
                }
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Sit back and watch.")) {
            client.getInterface(593).getChild(3).interact("Stab");
            sleep(random(800));
            if (!myPlayer().isAnimating()) {
                if (!myPlayer().isMoving()) {
                    NPC rat = closestAttackableNPCForName("Giant rat");
                    if (!myPlayer().isFacing(rat) && !rat.isFacing(myPlayer())) {
                        if (rat.isVisible()) {
                            rat.interact("Attack");
                            sleep(random(900));
                        } else {
                            client.moveCameraToEntity(rat);
                            sleep(random(900));
                        }
                    }
                }
            }
        }

        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Well done, you've made your first kill!")) {
            NPC guy = closestNPCForName("Combat Instructor");
            Entity gate = closestObjectForName("Gate");
            if (canReach(guy)) {
                if (guy.getPosition().distance(myPlayer().getPosition()) < 4) {
                    if (guy.isVisible()) {
                        guy.interact("Talk-to");
                        sleep(random(1000));
                    } else {
                        client.moveCameraToEntity(guy);
                        sleep(random(1000));
                    }
                } else {
                    walk(new Position(3106, 9509, 0));
                    sleep(random(900));
                }
            } else {
                gate.interact("Open");
                sleep(random(900));
            }
        }

        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Rat ranging.")) {
            if (!equipmentTab.isWearingItem(EquipmentSlot.WEAPON, "Shortbow")) {
                openTab(Tab.INVENTORY);
                sleep(random(900));
                client.getInventory().interactWithName("Shortbow", "Wield");
                sleep(random(900));
                client.getInventory().interactWithName("Bronze arrow", "Wield");
                sleep(random(900));
            } else {
                NPC rat = closestRat();
                if (rat != null) {
                    if (rat.isVisible()) {
                        if (!rat.isUnderAttack()) {
                            rat.interact("Attack");
                            sleep(random(900));
                        }
                    } else {
                        client.moveCameraToEntity(rat);
                        sleep(random(900));
                    }
                } else {
                    client.rotateCameraToAngle(random(5, 35));
                    sleep(random(400, 500));
                }
            }
        }

        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Moving on.")) {
            Entity ladder = closestObjectForName("Ladder");
            if (ladder != null) {
                if (ladder.getPosition().distance(myPlayer().getPosition()) < 3) {
                    if (ladder.isVisible()) {
                        ladder.interact("Climb-up");
                        sleep(random(2000, 2500));
                    } else {
                        client.moveCameraToEntity(ladder);
                        sleep(random(400, 500));
                    }
                } else {
                    WalkAlongPath(toLadderComb, true);
                    sleep(random(400, 700));
                }
            } else {
                WalkAlongPath(toLadderComb, true);
                sleep(random(400, 700));
            }
        }
    }

    void banking() throws InterruptedException {
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Banking.") && client.getInterface(372).getChild(0).isVisible()) {
            Entity booth = closestObjectForName("Bank booth");
            if (booth.getPosition().distance(myPlayer().getPosition()) < 6) {
                if (booth.isVisible()) {
                    booth.interact("Use");
                    sleep(random(900, 1200));
                } else {
                    client.moveCameraToEntity(booth);
                    sleep(random(900, 1200));
                }
            } else {
                walk(new Position(3120, 3123, 0));
                sleep(random(2000));
            }
        }
        if (client.getInterface(228) != null && client.getInterface(228).isValid() && client.getInterface(228).getChild(1).isVisible()) {
            client.getInterface(228).getChild(1).interact("Continue");
            sleep(random(900));
        }
        if (client.getBank().isOpen()) {
            client.getBank().close();
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("This is your bank box.")) {
            Entity door = closestObjectForName(bankDoor1, "Door");
            if (door != null) {
                door.interact("Open");
                sleep(random(500, 900));
            } else {
                walk(new Position(3124, 3124, 0));
                sleep(random(700, 800));
            }
        }
    }

    void finance() throws InterruptedException {
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Financial advice.")) {
            NPC advisor = closestNPCForName("Financial Advisor");
            if (advisor != null) {
                advisor.interact("Talk-to");
                sleep(random(900));
            }
        }
        if (client.getInterface(372).getChild(2).getMessage().equalsIgnoreCase("Continue through the next door.")) {
            Entity door = closestObjectForName(bankDoor2, "Door");
            if (door != null) {
                door.interact("Open");
                sleep(random(900));
            } else {
                walk(new Position(3129, 3124, 0));
                sleep(random(700, 800));
            }
        }
    }

    void prayer() throws InterruptedException {
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Prayer.") && client.getInterface(372).getChild(0).isVisible()) {
            Entity door = closestObjectForName("Large door");
            NPC monk = closestNPCForName("Brother Brace");
            if ((door != null && door.isVisible()) || (monk != null && canReach(monk) && monk.isVisible())) {
                if (myPlayer().getPosition().distance(new Position(3130, 3107, 0)) < 4
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
                if (myPlayer().getPosition().distance(new Position(3130, 3107, 0)) < 3) {
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
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Your Prayer menu.")) {
            openTab(Tab.PRAYER);
            sleep(random(900));
        }
        if (client.getInterface(372).getChild(3).getMessage().equalsIgnoreCase("Talk with Brother Brace and he'll tell you about prayers.")) {
            NPC monk = closestNPCForName("Brother Brace");
            if (monk.isVisible()) {
                monk.interact("Talk-to");
                sleep(random(900));
            } else {
                client.moveCameraToEntity(monk);
                sleep(random(900));
            }
        }
        if (client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Friends list.")) {
            openTab(Tab.FRIENDS);
            sleep(random(900));
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("This is your friends list.")) {
            openTab(Tab.IGNORES);
            sleep(random(900));
        }
        if (client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("This is your ignore list.")) {
            NPC monk = closestNPCForName("Brother Brace");
            if (monk.isVisible()) {
                monk.interact("Talk-to");
                sleep(random(900));
            } else {
                client.moveCameraToEntity(monk);
                sleep(random(900));
            }
        }
        if (client.getInterface(372).getChild(1).getMessage().equalsIgnoreCase("Your final instructor!")) {
            walk(new Position(3122, 3103, 0));
            sleep(random(1000));
            Entity door = closestObjectForName("Door");
            door.interact("Open");
            sleep(random(900));
        }
    }

    void magic() throws InterruptedException {
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Your final instructor!")) {
            WalkAlongPath(toWizard, true);
            sleep(random(500));
            NPC wiz = closestNPCForName("Magic Instructor");
            if (wiz != null && wiz.getPosition().distance(myPlayer().getPosition()) < 10) {
                wiz.interact("Talk-to");
                sleep(random(500));
            } else if (wiz != null && wiz.getPosition().distance(myPlayer().getPosition()) >= 10) {
                walk(wiz);
            }
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("Open up your final menu.")) {
            openTab(Tab.MAGIC);
            sleep(random(900));
        }
        if (new RS2Interface(client.getBot(), 421) != null && new RS2Interface(client.getBot(), 421).isValid()
                && client.getInterface(421).getChild(1).getMessage().equalsIgnoreCase("Cast Wind Strike at a chicken.")) {
            walk(new Position(3140, 3091, 0));
            sleep(random(900));
            client.getInterface(192).getChild(1).interact("Cast");
            sleep(random(900));
            NPC chick = closestNPCForName("Chicken");
            selectOption(null, chick.getMouseDestination(), "Cast", "Wind Strike -> Chicken");
            sleep(random(800));
        }
        if (new RS2Interface(client.getBot(), 372) != null && new RS2Interface(client.getBot(), 372).isValid()
                && client.getInterface(372).getChild(0).getMessage().equalsIgnoreCase("You have almost completed the tutorial!")
                && client.getInterface(372).getChild(0).isVisible()) {
            NPC wiz = closestNPCForName("Magic Instructor");
            wiz.interact("Talk-to");
            sleep(random(500));
        }
        if (client.getInterface(228) != null && client.getInterface(228).isValid()) {
            client.getInterface(228).getChild(1).interact("Continue");
            sleep(random(900));
        }
    }

    //~~~~~~~~~~~~~~~~~~~~ EXTRAS ~~~~~~~~~~~~~~~~~~~
    void runningCheck() throws InterruptedException {
        boolean run = isRunning();
        //log("isRunning: " + Boolean.toString(run));
        if ((!run) && (client.getRunEnergy() >= runInt)) {
            settingsTab.open();
            sleep(random(900, 1279));
            //client.moveMouseTo(runBtn, false, true, false);
            setRunning(true);
            sleep(random(400, 500));
            openTab(Tab.INVENTORY);
            run = true;
        } else if ((!run) && (client.getRunEnergy() == 100)) {
            settingsTab.open();
            sleep(random(986, 1393));
            //client.moveMouseTo(runBtn, false, true, false);
            setRunning(true);
            sleep(random(400, 500));
            openTab(Tab.INVENTORY);
            run = true;
        }
        if (client.getRunEnergy() < 10) {
            runInt = random(15, 47);
        }
    }

    boolean continueConvo() throws InterruptedException {
        int[] parent = {64, 65, 131, 163, 177, 210, 211, 212, 213, 214, 228, 230, 232,
            234, 241, 242, 243, 244, 519, 548};
        int[] child = {1, 2, 3, 4, 5, 6, 95};
        for (int p : parent) {
            for (int c : child) {
                if (client.getInterface(p) != null) {
                    if (client.getInterface(p).getChild(c) != null) {
                        if (client.getInterface(p).getChild(c).getMessage().equalsIgnoreCase("Click here to continue")) {
                            if (client.getInterface(p).getChild(c).interact("Continue")) {
                                log("Continue Dialog");
                                sleep(random(1000, 1600));
                                return true;
                            }
                        } else if (client.getInterface(548).getChild(121).getMessage().equalsIgnoreCase("Click to continue")
                                && client.getInterface(548).getChild(121).isVisible()) {
                            log("Continue");
                            client.getInterface(548).getChild(121).hover();
                            sleep(random(700));
                            client.clickMouse(false);
                            sleep(random(700));
                            return true;
                        } else if (client.getInterface(p).getChild(c).getMessage().equalsIgnoreCase("Nope, I'm ready to move on!")) {
                            if (client.getInterface(p).getChild(c).interact("Continue")) {
                                log("Continue Dialog");
                                sleep(random(1000, 1600));
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;

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

    private boolean canSeeText(String text) {
        return (searchInterfacesForText(text) != null) && (!searchInterfacesForText(text).isEmpty());
    }

    private NPC closestRat() {
        for (int i = 0; i < closestNPCListForName("Giant rat").size(); i++) {
            if (closestNPCListForName("Giant rat").get(i).getPosition().distance(myPlayer().getPosition()) <= 7) {
                return closestNPCListForName("Giant rat").get(i);
            }
        }
        return null;
    }

}
