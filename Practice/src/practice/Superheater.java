package practice;

import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.Player;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.Spell;
import org.osbot.script.rs2.ui.Tab;

@ScriptManifest(author = "Gilgad", info = "Superheater", name = "Steel Superheater", version = 1)

public class Superheater extends Script {

    Inventory inven;
    Bank bank;
    Player player;

    int bankattempt = 0;
    int IRON_ID = 440;
    int COAL_ID = 453;
    int STEEL_ID = 2353;
    int NATURE_ID = 561;
    int IRON_BAR_ID = 2351;

    public void onStart() {
    }

    public void onExit() {
    }

    public int onLoop() throws InterruptedException {
        inven = client.getInventory();
        bank = client.getBank();
        player = client.getMyPlayer();
        Entity bankbooth = closestObjectForName("Bank booth");

        //Check if bank is open
        if (!bank.isOpen() && inven.contains(COAL_ID) && inven.contains(IRON_ID)) {
            this.magicTab.castSpell(Spell.SUPERHEAT_ITEM);
            sleep(random(350, 400));
            inven.interactWithName("Iron ore", "Cast");
            sleep(random(900, 1000));
        } else if (!bank.isOpen() && bankattempt <= 3) {
            bankbooth.interact("Bank");
            bankattempt++;
            sleep(random(600, 700));
        } else if (!bank.isOpen() && bankattempt >= 4) {
            openTab(Tab.MAGIC);
            sleep(random(250, 450));
            bankattempt = 0;

        } else {
            if (bank.isOpen()) {
                bankattempt = 0;
            } else if (inven.contains(IRON_BAR_ID)) {
                bank.depositAllExcept(NATURE_ID);
                sleep(random(700, 800));
            } else if (!inven.contains(STEEL_ID) && !inven.contains(COAL_ID) && !inven.contains(IRON_ID)) {
                bank.withdrawX(IRON_ID, 9);
                sleep(random(700, 800));
            } else if (!inven.contains(STEEL_ID) && inven.contains(IRON_ID) && !inven.contains(COAL_ID)) {
                bank.withdrawAll(COAL_ID);
                sleep(random(700, 800));
            } else if (inven.contains(STEEL_ID) && !inven.contains(COAL_ID) && !inven.contains(IRON_ID)) {
                bank.withdrawAll(COAL_ID);
                sleep(random(700, 800));
            } else if (inven.contains(STEEL_ID) && inven.contains(COAL_ID)) {
                bank.depositAll(STEEL_ID);
                sleep(random(700, 800));
            } else if (!inven.contains(STEEL_ID) && inven.contains(COAL_ID) && !inven.contains(IRON_ID)) {
                bank.withdrawAll(IRON_ID);
                sleep(random(700, 800));
            } else if (!inven.contains(STEEL_ID) && inven.contains(COAL_ID) && inven.contains(IRON_ID)) {
                bank.close();
                sleep(random(700, 800));
            } else {
                if (bankbooth != null) {
                    if (bankbooth.isVisible()) {
                        if (!player.isAnimating()) {
                            bankbooth.interact("Bank");
                            sleep(random(700, 800));
                        }
                    }
                } else {
                    client.moveCameraToEntity(bankbooth);
                }

            }
            //open bank

            //withdraw/deposit
            //close bank
            //superheat
        }
        return 50;
    }
}
