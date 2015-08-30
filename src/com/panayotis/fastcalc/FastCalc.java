package com.panayotis.fastcalc;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import java.util.Random;


public final class FastCalc extends MIDlet implements CommandListener {

    /** Soft button for exiting the game. */
    private final Command exitCmd  = new Command("Exit", Command.EXIT, 1);

    private final Command aboutCmd = new Command("About", Command.SCREEN, 1);


	private final Form mainform = new Form("FastCalc 1.0");

	private final Calculator calc = new Calculator();

	
	public FastCalc() {
        mainform.addCommand(exitCmd);
        mainform.addCommand(aboutCmd);
        mainform.setCommandListener(this);

		mainform.append(calc);
	}
		
	
    protected void startApp() {
        Display.getDisplay(this).setCurrent(mainform);
    }

    protected void destroyApp(boolean unconditional) {
	}

    protected void pauseApp() {}

    /**
     * Responds to commands issued on CalculatorForm.
     *
     * @param c command object source of action
     * @param d screen object containing actioned item
     */
    public void commandAction(Command c, Displayable d) {
        if (c == exitCmd) {
            destroyApp(false);
            notifyDestroyed();
            return;
        }
		
		if ( c == aboutCmd ) {
			String infotext = "FastCalc is a calculator aimed to be controlled only with the 12 main phone keys (the 10 numbers, the star and the pound). The pound key works as a SHIFT key, which transfers from the main usage (the 10 numbers and the star as a point) to the extended usage (all other commands). When the pound symbol is pressed, then the keys have their extended usage.\n\nThe meaning of the extended commands are as follows:\n\n1  Clear\n2 Clear all\n3 Memory set\n4 Reverse\n5 Change sign\n6 Memory get\n7 Divide\n8 Substract\n9 Result\n* Multiply\n0 Add\n\nWritten by Panayotis Katsaloulis (panayotis@panayotis.com). This program is under the GNU Licence.";
			Alert alert = new Alert("About reTax 1.0", infotext, null, AlertType.INFO);
			alert.setTimeout(Alert.FOREVER);
			Display.getDisplay(this).setCurrent(alert);
		}
    }

}
