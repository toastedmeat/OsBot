package silkstealers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.Client;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.Item;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.skill.Skill;
import org.osbot.script.rs2.skill.Skills;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2Interface;
import org.osbot.script.rs2.ui.RS2InterfaceChild;
import org.osbot.script.rs2.ui.Tab;
import org.osbot.script.rs2.utility.Area;

@ScriptManifest(author="toastedmeat", info="Steals silk from stall. Make sure you trap all the guards in a house.", name="SilkStealers", version=1.1D)
public class SilkStealers extends Script
{
  private final Font regFont = new Font("Arial", 0, 14);
  private final Color white = new Color(255, 255, 255);

  String run = "False";
  Item food;
  String foodName;
  int eatLevel;
  int startHP;
  int randomInt;
  int pitch;
  int angle;
  public long startTime = 0L;
  public long millis = 0L;
  public long hours = 0L;
  public long minutes = 0L;
  public long seconds = 0L;
  public long last = 0L;

  String status = "WAITING";
  String timeTL = "";
  long level;
  long leveled;
  long silkPH;
  double expPH;
  int startExp;
  int exp;
  double expTNL;
  int runInt;
  boolean running = true;

  public final Area SILK_AREA = new Area(2663, 3316, 2262, 3316);
  public final Area BANK_AREA = new Area(2649, 3280, 2654, 3287);

  int stolenSilk = 0;

  public void onStart()
  {
    this.client.setMouseSpeed(random(5, 8));
    this.startExp = this.client.getSkills().getExperience(Skill.THIEVING);
    this.level = this.client.getSkills().getLevel(Skill.THIEVING);
    this.startTime = System.currentTimeMillis();
    this.status = "Starting script";
    this.startHP = this.client.getSkills().getCurrentLevel(Skill.HITPOINTS);
    this.randomInt = random(1, 10);

    log("SilkStealer v1.1 ");
    log("Trap all guards inside building");
    log("Johnny is a noob");
  }

  public int onLoop() throws InterruptedException
  {
    Player player = this.client.getMyPlayer();
    Inventory bag = this.client.getInventory();
    Entity SILK_STALL = closestObject(new int[] { 549 });
    openTab(Tab.INVENTORY);

    if (this.SILK_AREA.contains(player)) {
      if (!bag.isFull()) {
        if (SILK_STALL != null) {
          if (SILK_STALL.isVisible()) {
            if ((!player.isAnimating()) && 
              (!player.isMoving())) {
              this.status = "Stealing";
              SILK_STALL.interact("Steal-from");
              sleep(random(1000, 1350));
              this.status = "Dropping Silk";
              bag.dropAll();

              sleep(random(700, 900));
            }
          }
          else
            sleep(random(400, 600));
        }
      }
      else
      {
        this.status = "Dropping all Silk";
        bag.dropAll();
      }
    } else {
      this.status = "Walking back";
      walk(this.SILK_AREA);
    }

    AntiBan();
    return random(50, 500);
  }

  public void onMessage(String message) {
    if (message.contains("You steal a piece of silk."))
      this.stolenSilk += 1;
  }

  public void onPaint(Graphics g)
  {
    this.pitch = this.client.getCameraPitch();
    this.angle = this.client.getCameraPitchAngle();
    this.exp = (this.client.getSkills().getExperience(Skill.THIEVING) - this.startExp);
    this.leveled = (this.client.getSkills().getLevel(Skill.THIEVING) - this.level);
    this.expTNL = experienceToNextLevel(Skill.THIEVING);
    this.millis = (System.currentTimeMillis() - this.startTime);
    this.hours = (this.millis / 3600000L);
    this.millis -= this.hours * 3600000L;
    this.minutes = (this.millis / 60000L);
    this.millis -= this.minutes * 60000L;
    this.seconds = (this.millis / 1000L);

    this.expPH = ((int)(this.exp * 3600000.0D / (System.currentTimeMillis() - this.startTime)));
    this.silkPH = ((int)(this.stolenSilk * 3600000.0D / (System.currentTimeMillis() - this.startTime)));

    this.timeTL = formatTime(timeTnl(this.expTNL, this.expPH));

    Graphics2D g2 = (Graphics2D)g;

    g2.setFont(this.regFont);
    g2.setColor(this.white);

    g2.drawString("Time Elapsed: " + this.hours + " hours " + this.minutes + 
      " minutes " + this.seconds + " seconds", 10, 80);
    g2.drawString(
      "HP: " + this.client.getSkills().getCurrentLevel(Skill.HITPOINTS), 
      10, 100);
    g2.drawString(
      "Level: " + this.client.getSkills().getCurrentLevel(Skill.THIEVING) + 
      "(" + this.level + "+" + this.leveled + ")", 10, 120);
    g2.drawString("Exp Gained: " + this.exp, 10, 140);
    g2.drawString("Exp Per Hour: " + this.expPH, 10, 160);
    g2.drawString("Exp till next Level: " + this.expTNL, 10, 180);
    g2.drawString("Time till Level: " + this.timeTL, 10, 200);
    g2.drawString("Silk Stolen: " + this.stolenSilk, 10, 220);
    g2.drawString("Silk per hour: " + this.silkPH, 10, 240);
    g2.drawString("Running at: " + this.runInt, 10, 260);
    g2.drawString("Status: " + this.status, 10, 280);
    g2.drawString("Pitch: " + this.pitch, 10, 300);
    g2.drawString("Angle: " + this.angle, 10, 320);
  }

  public void onExit() throws InterruptedException {
    this.exp = (this.client.getSkills().getExperience(Skill.THIEVING) - this.startExp);
    this.leveled = (this.client.getSkills().getLevel(Skill.THIEVING) - this.level);
    log("You gained " + this.exp + " Thieving Exp and " + this.leveled + 
      " Level(s)");
    log("DID YOU GAIN ALOT?");
    log("Johnnys still a noob");
  }

  public void AntiBan()
    throws InterruptedException
  {
    Rectangle k = new Rectangle(1 + random(489), 1 + random(328), 10, 10);
    switch (random(1, 200)) {
    case 1:
      this.status = "Anti-Ban rotate camera";
      this.client.rotateCameraPitch(random(35, 60));
      log("case 1");
      break;
    case 2:
      this.status = "Anti-Ban Check Buddy List";
      openTab(Tab.FRIENDS);
      log("case 2");
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

      if (this.client.getInterface(320).getChild(133).isVisible()) {
        this.client.getInterface(320).getChild(133).hover();
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

      if (this.client.getInterface(320).getChild(133).isVisible()) {
        this.client.getInterface(320).getChild(133).hover();
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
      this.client.rotateCameraPitch(random(5, 19));
      log("case 23");
      break;
    case 24:
      this.status = "Anti-Ban Check Buddy List";
      openTab(Tab.FRIENDS);
      log("case 24");
      break;
    case 26:
      this.status = "Anti-Ban check skill";
      openTab(Tab.SKILLS);

      if (this.client.getInterface(320).getChild(133).isVisible()) {
        this.client.getInterface(320).getChild(133).hover();
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
      this.client.rotateCameraPitch(random(15, 50));
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

      if (this.client.getInterface(320).getChild(133).isVisible()) {
        this.client.getInterface(320).getChild(133).hover();
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
      break;
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
    default:
      this.status = "Waiting";
      sleep(random(1000, 1500));
    }
  }

  private int experienceToNextLevel(Skill skill)
  {
    int[] xpForLevels = { 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 
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
      7944614, 8771558, 9684577, 10692629, 11805606, 13034431 };
    int xp = this.client.getSkills().getExperience(skill);
    for (int i = 0; i < 99; i++) {
      if (xp < xpForLevels[i]) {
        return xpForLevels[i] - xp;
      }
    }
    return 200000000 - xp;
  }

  long timeTnl(double xpTNL, double xpPH)
  {
    if (xpPH > 0.0D) {
      long timeTNL = (long)(xpTNL / xpPH * 3600000.0D);
      return timeTNL;
    }
    return 0L;
  }

  private String formatTime(long time)
  {
    int sec = (int)(time / 1000L); int d = sec / 86400; int h = sec / 3600; int m = sec / 60 % 60; int s = sec % 60;
    return (d < 10 ? "0" + d : Integer.valueOf(d)) + ":" + (h < 10 ? "0" + h : Integer.valueOf(h)) + ":" + (m < 10 ? "0" + m : Integer.valueOf(m)) + ":" + (s < 10 ? "0" + s : Integer.valueOf(s));
  }
}