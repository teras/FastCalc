package com.panayotis.fastcalc;

import javax.microedition.lcdui.*;

public final class Calculator extends CustomItem {

	int mode = 0;

	int minsize = 20;
	
	String [][][] keys = { 
		{{"1","2","3"},{"4","5","6"},{"7","8","9"},{".","0","->"}},
		{{"C","AC",">M"},{"1/x","+/-","M>"},{"/","-","="},{"*","+","->"}}
	};
	

	String memory;
	String display;
	String prev;
	char action;
	boolean replace;


	public Calculator () {
		super("");
		resetAll();
		updateDisplay();
	}


	private void updateDisplay() {
		setLabel(display+"\n ");
	}
	
	public int getMinContentHeight() {
		return minsize*4;
	}
	
	public int getMinContentWidth () {
		return minsize*4;
	}
	
	public int getPrefContentHeight(int height) {
		return minsize*4;
	}
	
	public int getPrefContentWidth (int height) {
		return minsize*4;
	}
	
	public void paint (Graphics g, int w, int h) {
		int col1=0;
		int col2=0;
		
		int i,j;
		
		int dx,dy;
		dx=w/3;
		dy=h/4;
		
		if ( mode==0 ) {
			col1 = 0xffffff;
			col2 = 0x000000;
		}
		else {
			col1 = 0x000000;
			col2 = 0xffffff;
		}
		
		g.setColor (col1);
		g.fillRect(0,0,w-1,h-1);

		g.setColor(col2);
		g.drawRect(0,0,w-1,h-1);

		/* Draw lines */
		for (i=1; i < 3; i++) {
			g.drawLine(dx*i,0,dx*i,h-1);
		}
		for (i=1; i < 4; i++) {
			g.drawLine(0,dy*i,w-1,dy*i);
		}
		
		for (i=0;i<3;i++) {
			for (j=0;j<4;j++) {
				g.drawString(keys[mode][j][i], dx*i + dx/2, dy*j+2, Graphics.HCENTER|Graphics.TOP);
			}
		}
				
	}

	public void keyPressed (int key) {
		if ( key == Canvas.KEY_POUND ) {
			toggleMode();
			return;
		}
		if ( mode == 0 ) {
			sendSet1 (key);
		}
		else if ( mode == 1 ) {
			sendSet2 (key);
			toggleMode();
		}
		updateDisplay();
	}


	public void toggleMode() {
		mode = (mode==0)?1:0;
		repaint();
	}

	public void sendSet1 (int key) {
		switch (key) {
		case Canvas.KEY_NUM0:
			sendNumber(0);
			break;
		case Canvas.KEY_NUM1:
			sendNumber(1);
			break;
		case Canvas.KEY_NUM2:
			sendNumber(2);
			break;
		case Canvas.KEY_NUM3:
			sendNumber(3);
			break;
		case Canvas.KEY_NUM4:
			sendNumber(4);
			break;
		case Canvas.KEY_NUM5:
			sendNumber(5);
			break;
		case Canvas.KEY_NUM6:
			sendNumber(6);
			break;
		case Canvas.KEY_NUM7:
			sendNumber(7);
			break;
		case Canvas.KEY_NUM8:
			sendNumber(8);
			break;
		case Canvas.KEY_NUM9:
			sendNumber(9);
			break;
		case Canvas.KEY_STAR:
			sendPoint();
			break;
		}
	}

	public void sendSet2 (int key) {
		switch (key) {
		case Canvas.KEY_NUM0:
			sendActionSign('+');
			break;
		case Canvas.KEY_NUM1:
			sendActionClear();
			break;
		case Canvas.KEY_NUM2:
			sendActionReset();
			break;
		case Canvas.KEY_NUM3:
			sendMemorySet();
			break;
		case Canvas.KEY_NUM4:
			sendActionReverse();
			break;
		case Canvas.KEY_NUM5:
			sendActionNeg();
			break;
		case Canvas.KEY_NUM6:
			sendMemoryGet();
			break;
		case Canvas.KEY_NUM7:
			sendActionSign('/');
			break;
		case Canvas.KEY_NUM8:
			sendActionSign('-');
			break;
		case Canvas.KEY_NUM9:
			sendActionResult();
			break;
		case Canvas.KEY_STAR:
			sendActionSign('*');
			break;
		}
	}



	private void sendNumber(int num) {
		if ( replace ) {
			replace = false;
			display = "";
		}
		if ( display.length() < 9 ) 
			display = display+num;
	}

	private void sendPoint() { 
		if ( display.indexOf('.') >= 0 ) return;	// Already a point exists
		if ( replace ) {
			replace = false;
			display = "0.";
			return;
		}
		display += '.';
	}

	private void sendActionSign(char sign) {
		/* Note: if there is no action pending, then the 
		 * following method will return immediately */
		sendActionResult();
		prev = display;
		action = sign;
	}

	private void sendActionResult() {
		float num1, num2, res;
		
		/* Make sure that next numbers will override this one */
		replace = true;

		/* If there is no action pending, exit */
		if ( action == ' ') return;

		res = 0;
		num1 = getFloat(prev);
		num2 = getFloat(display);
		switch (action) {
		case '+':
			res = num1+num2;
			break;
		case '-':
			res = num1-num2;
			break;
		case '*':
			res = num1*num2;
			break;
		case '/':
			if ( num2 == 0 ) return;
			res = num1/num2;
		}
		action = ' ';
		prev = "";
		display = getCleanValue(res);
	}
	

	private void sendMemorySet() {
		memory = display;
	}
	
	private void sendActionReset() {
		resetAll();
	}
	
	private void sendMemoryGet() {
		display = memory;
		replace = true;
	}
	
	private void sendActionClear() {
		display = "0";
		replace = true;
	}
	
	private void sendActionNeg() {
		if ( display.length() < 1 ) return;
		if ( display.charAt(0) == '-' ) {
			display = display.substring(1);
		}
		else {
			display = '-' + display;
		}
	}
	

	private void sendActionReverse() {
		float num = getFloat(display);
		num = 1/num;
		display = getCleanValue(num);
		replace = true;
	}


	private float getFloat(String num) {
		float res = 0;
		try {
			res = Float.parseFloat(num);
		}
		catch ( NumberFormatException e ) {}
		return res;
	}
	
	private String getCleanValue(float data) {
		String res = String.valueOf(data);
		if (res.endsWith(".0") ) {
			res = res.substring(0, res.length()-2 );
		}
		return res;
	}


	/* Reset calculator values */
	private void resetAll () {
		memory = "";
		display = "0";
		prev = "";
		action = ' ';
		replace = true;
	}
}
