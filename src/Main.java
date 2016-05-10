public class Main {

    public static void main(String[] args) {
        System.out.println("Sample LCD tutorial");
        //with initialization
        LCDUtils lcdUtils = new LCDUtils();

        //Writes a string to the provided line (1-4)
        lcdUtils.lcd_WriteString("String on line1",1);
        lcdUtils.lcd_WriteString("String on line2",2);
        lcdUtils.lcd_WriteString("String on line3",3);
        lcdUtils.lcd_WriteString("String on line4",4);

        //clear display
        lcdUtils.lcd_ClearDisplay();

        //it should be noted that there are multiple commands that can be used with
        //lcdUtils.lcd_WriteCMD();
        // check the LCDUtils constants


        //static, no initialization
        //the Init must be called otherwise the LCD won't display properly (unless you customly set it up)
        LCDUtils.initLCD();
        LCDUtils.lcd_WriteString("String on line1",1);
    }
}
