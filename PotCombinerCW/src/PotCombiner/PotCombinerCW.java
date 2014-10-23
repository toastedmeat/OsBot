/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PotCombiner;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;

import org.osbot.script.rs2.model.Entity;

import org.osbot.script.rs2.ui.Bank;

import org.osbot.script.rs2.ui.Inventory;


/**
 *
 * @author Eric
 */
@ScriptManifest(author = "toastedmeat", info = "pots",
        name = "PotCombinerCW", version = 1)
public class PotCombinerCW extends Script {

    int[] pots = { 
    };

    String potOneIdTemp, potTwoIdTemp;
    int potOneId = 165, potTwoId = 167, counter;

    public void onStart() {
        potOneIdTemp = JOptionPane.showInputDialog("Enter first items Id \n"
                + "Super attack 147, 149,\n" +
"Super strength 159, 161,\n" +
"Super defence 165, 167", "");
        potOneId = Integer.parseInt(potOneIdTemp);
        potOneIdTemp = JOptionPane.showInputDialog("Enter second items Id \n "
                + "Super attack147, 149,\n" +
"Super strength 159, 161,\n" +
"Super defence 165, 167", "");
        potTwoId = Integer.parseInt(potOneIdTemp);
 
    }

    public int onLoop() throws InterruptedException {

        Bank bank = client.getBank();
        Inventory bag = client.getInventory();
        if (bag.contains(potOneId)
                && bag.contains(potTwoId)) {
            bag.interactWithId(potOneId, "Use");
            bag.interactWithId(potTwoId, "Use");
            counter++;
            sleep(random(600, 700));
        } else {
            Entity bankBox = closestObject(4483);
            if (bank.isOpen()) {
                bank.depositAll();
                sleep(random(400, 500));
                bank.withdrawX(potOneId, 14);
                sleep(random(400, 500));
                bank.withdrawX(potTwoId, 14);
                sleep(random(400, 500));
                if (bank.isOpen()) {
                    bank.close();
                }
            } else {
                if (bankBox != null) {
                    if (bankBox.isVisible()) {
                        bankBox.interact("Use");
                        sleep(random(700, 900));
                    } else {
                        client.moveCameraToEntity(bankBox);
                    }
                }
            }
        }

        return 3000;
    }

    public void onPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawString("Made " + counter + " Items.", 200, 280);
    }

    public void onExit() {
        log("Combined " + counter + " items.");
    }

}
