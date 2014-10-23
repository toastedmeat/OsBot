/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herbviewer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.osbot.script.Script;
import org.osbot.script.ScriptManifest;
import org.osbot.script.rs2.model.Item;
import org.osbot.script.rs2.ui.Tab;

/**
 *
 * @author Eric
 */
@ScriptManifest(author = "toastedmeat", info = "Herb/ID viewer",
        name = "ToastyHerbViewer", version = 1)
public class HerbViewer extends Script{

    public void onStart(){
    }
    public int onLoop(){
        return 50;
    }
    public void onPaint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        showItemIDs(g2);
    }
    public void onExit(){
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
 
                    String slot = "";
                    switch (items[i].getId()) {
                        case 199:case 200:
                            slot = "Guam";
                            break;
                            case 201:case 202:
                            slot = "Marren";
                            break;
                                case 203:case 204:
                            slot = "Tarro";
                            break;
                        case 205:case 206:
                            slot = "Harra";
                            break;
                        case 207:case 208:
                            slot = "Ranarr";
                            break;
                        case 209:case 210:
                            slot = "Irit";
                            break;
                        case 211:case 212:
                            slot = "Avantoe";
                            break;
                        case 213:case 214:
                            slot = "Kwuarm";
                            break;
                        case 215:case 216:
                            slot = "Cada";
                            break;
                        case 217:case 218:
                            slot = "Dwarf";
                            break;
                        case 219:case 220:
                            slot = "Tors";
                            break;
                        case 2485:case 2486:
                            slot = "Lanta";
                            break;
                        default:
                            slot = new StringBuilder().append("").append(items[i].getId()).toString();
                    }
                    g2.setColor(Color.CYAN);
                    g2.fillRect(nx + (int) ((r.getWidth() - g2.getFontMetrics().stringWidth(slot)) / 2.0D) - 2, ny + h / 4, g2.getFontMetrics().stringWidth(slot) + 4, h);
 
                    g2.setComposite(AlphaComposite.getInstance(3, 1.0F));
 
                    g2.setColor(Color.BLACK);
                    g2.drawString(slot, nx + (int) ((r.getWidth() - g2.getFontMetrics().stringWidth(slot)) / 2.0D), ny + h);
                }
            }
 
        }
 
        g2.setComposite(old);
    }
 
    
}
