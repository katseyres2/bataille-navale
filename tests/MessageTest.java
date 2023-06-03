package tests;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.sql.Time;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void LocalDateTime testgetTime(){
        Time time = new Time();
        LocalDateTime  expectedTime = LocalDateTime.now();
        time.setTime(expectedTime);
        LocalDateTime mytime = time.getTime();
        assertEquals(expectedTime,mytime);


    }
    @Test
    public void testNullTime(){
        Time time = new Time();
        assertNull(time.getTime());
    }

    @Test
    public void  String testgetText(){
        Text text = new Text();
        String expected  = "Touché Coulé";
        text.setText(expected);
        String montext = text.getText();
        assertEquals(expected, montext);
    }
}
