/**
 * Created by Max on 5/9/2016.
 */
import edu.wpi.first.wpilibj.*;


public class LCDUtils {

    private static final int LCD_CLEARDISPLAY = 0x01;
    private static final int LCD_RETURNHOME = 0x02;
    private static final int LCD_ENTRYMODESET = 0x04;
    private static final int LCD_DISPLAYCONTROL = 0x08;
    private static final int LCD_CURSORSHIFT = 0x10;
    private static final int LCD_FUNCTIONSET = 0x20;
    private static final int LCD_SETCGRAMADDR = 0x40;
    private static final int LCD_SETDDRAMADDR = 0x80;

    // Flags for display on/off control
    private static final int LCD_DISPLAYON = 0x04;
    private static final int LCD_DISPLAYOFF = 0x00;
    private static final int LCD_CURSORON = 0x02;
    private static final int LCD_CURSOROFF = 0x00;
    private static final int LCD_BLINKON = 0x01;
    private static final int LCD_BLINKOFF = 0x00;

    // Flags for display entry mode
    // private static final int LCD_ENTRYRIGHT = 0x00;
    private static final int LCD_ENTRYLEFT = 0x02;
    private static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
    private static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

    // Flags for display/cursor shift
    private static final int LCD_DISPLAYMOVE = 0x08;
    private static final int LCD_CURSORMOVE = 0x00;
    private static final int LCD_MOVERIGHT = 0x04;
    private static final int LCD_MOVELEFT = 0x00;

    // flags for function set
    private static final int LCD_8BITMODE = 0x10;
    private static final int LCD_4BITMODE = 0x00;
    private static final int LCD_2LINE = 0x08;	//for 2 or 4 lines actualy
    private static final int LCD_1LINE = 0x00;
    private static final int LCD_5x10DOTS = 0x04;	//seldom used!!
    private static final int LCD_5x8DOTS = 0x00;

    // flags for backlight control
    private static final int LCD_BACKLIGHT = 0x08;
    private static final int LCD_NOBACKLIGHT = 0x00;

    //bitmasks for register control
    private static final int En = 0b00000100; // Enable bit
    private static final int Rw = 0b00000010; // Read/Write bit
    private static final int Rs = 0b00000001; // Register select bit

    private static I2C lcdDisplay;

    public LCDUtils() {
        initLCD();
    }

    public static void initLCD() {
        lcdDisplay = new I2C(I2C.Port.kOnboard, 0x27);

        lcd_WriteCMD(0x03);
        lcd_WriteCMD(0x03);
        lcd_WriteCMD(0x03);
        lcd_WriteCMD(0x02);
        //4 bit mode??? -- yes. Always. It's the default way of doing this for LCD displays
        lcd_WriteCMD( LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE );
        lcd_WriteCMD( LCD_DISPLAYCONTROL | LCD_DISPLAYON );
        lcd_WriteCMD( LCD_CLEARDISPLAY );
        lcd_WriteCMD( LCD_ENTRYMODESET | LCD_ENTRYLEFT );
        zsleep(10);
    }

    //write a sleep method to get rid of the try-catch stuff
    public static void zsleep(int t) {
        try { Thread.sleep(t);
        } catch(InterruptedException e) {}
    }

    //This is for writing commands, 4 bits at a time
    public static void lcd_WriteCMD(int data) {
        lcd_RawWrite(data & 0xF0);
        lcd_RawWrite((data <<4 ) & 0xF0);
    }

    //This is for writing a character, 4 bits at a time
    public static void lcd_WriteChar ( int data) {
        lcd_RawWrite( Rs |  (data & 0xF0));
        lcd_RawWrite( Rs | ((data <<4 ) & 0xF0));
    }

    public static void lcd_RawWrite( int data) {
        lcdDisplay.write(0, data | LCD_BACKLIGHT );
        strobe(data);
    }

    public static void strobe(int data){
        //    	Syntax: lcdDisplay.write(reg,data);
        lcdDisplay.write(0, data | En | LCD_BACKLIGHT );
        zsleep(1);
        lcdDisplay.write(0, (data & ~En) | LCD_BACKLIGHT );
        zsleep(1);
    }

    //This is the "public" method. The one that is actually used by other code to write to the display.
    public static void lcd_WriteString(String s, int line) {
        switch (line) {
            case 1: lcd_WriteCMD(0x80); break;
            case 2: lcd_WriteCMD(0xC0); break;
            case 3: lcd_WriteCMD(0x94); break;
            case 4: lcd_WriteCMD(0xD4); break;
            default: return;	//invalid line number does nothing.
        }

        //limit to 20 chars/line so we don't have to worry about overflow messing up the display
        if (s.length() > 20) s = s.substring(0, 20);

        for (int i=0; i<s.length(); i++){
            lcd_WriteChar(s.charAt(i));
        }
    }

    public void lcd_ClearDisplay(){
        lcd_WriteCMD(LCD_CLEARDISPLAY);
    }

}
