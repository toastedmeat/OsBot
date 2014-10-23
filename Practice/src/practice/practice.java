/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 *
 * @author Eric
 */
public class practice {
    
    String rsItem = "";
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        practice p = new practice();
        NewJPanel a = new NewJPanel();
        a.setVisible(true);
        //p.getItems();
        
    }
    
    public void getItems() throws FileNotFoundException, IOException{
        rsItem = "a.getText()";
        rsItem = "a.jTextArea1.setText(itemFinder(rsItem))";
    }
    public String itemFinder(String item) throws FileNotFoundException, IOException{
        URL url = new URL("http://pastebin.com/raw.php?i=mvVL2pHw");
        Scanner s = new Scanner(url.openStream());
        MatchResult mr = null;
        while (null != s.findWithinHorizon("(?i)\\b"+ item+"\\b", 0)) {
            mr = s.match();
        }
        
        s.close();
        return ("Item found: " + mr.group() + s.nextLine() + "\n");
    }
}
/*
int i, j, dicCheck = 0, inpCheck = 0, outCheck = 0, *poo;
    FILE *new, *output;
    char outputFilename[] = "out.txt";
    for (i = 1; i < argc; i = i + 2) {
        if (dicCheck == 0) {
            if (strncmp(argv[i], "-d", 2) == 0) {
                printf("\nDictionary");
                new = fopen(argv[i + 1], "r");
                output = fopen(outputFilename, "w");
                if (output == NULL) {
                    fprintf(stderr, "Can't open output file %s!\n", outputFilename);
                }
                int ch;
                ch = getc(new);
                putc(ch, output);
                dicCheck = 1;
            } else if (i == argc - 1 && strncmp(argv[i], "-d", 2) != 0) {
                printf("\nUsing default dic");
            }
        }
        if (inpCheck == 0) {
            if (strncmp(argv[i], "-i", 2) == 0) {
                printf("\nInput file");
                inpCheck = 1;
            } else if (i == argc - 1 && strncmp(argv[i], "-i", 2) != 0) {
                printf("\nUsing default stdin");
            }
        }
        if (outCheck == 0) {
            if (strncmp(argv[i], "-o", 2) == 0) {
                printf("\nOutput file");
                outCheck = 1;
            } else if (i == argc - 1 && strncmp(argv[i], "-o", 2) != 0) {
                printf("\nUsing default output");
            }
        }
        printf("\nFirst: %s\n", argv[i]);
    }

    fclose(new);
    fclose(output);
    if (argc == 1) {
        printf("\nNo args using defaults\n");
    }
*/