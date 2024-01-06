package rainbowsix.ss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;


/**
 * 对text单行文本进行分析功能的测试.
 * @author 朱甲豪
 */
public class TextTest {

    public ByteArrayOutputStream out = null;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() throws IOException {
        out.close();
        System.setOut(System.out); //将输出重新设置为控制台输出
    }

    @Test
    public void textTest1() {
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "I hate it!"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "1 -5"+System.lineSeparator());
    }

    @Test
    public void textTest2() {
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "I hate it!", "trinary"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "1 -5 -1"+System.lineSeparator());
    }

    @Test
    public void textTest3() {
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "Oh god! I love it so much!", "binary"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "4 -1 1"+System.lineSeparator());
    }

    @Test
    public void textTest4() {
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "YOU ARE A FUCKING RETARD!", "scale"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "1 -5 -4"+System.lineSeparator());
    }
}