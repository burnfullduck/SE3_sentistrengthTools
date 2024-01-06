package rainbowsix.ss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


/**
 * 对text单行文本进行分析功能的测试.
 * @author 朱甲豪
 */
public class ExplainTest {

    public ByteArrayOutputStream out = null;

    @Before
    public void setUp() throws Throwable{
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() throws Throwable{
        out.close();
        System.setOut(System.out); //将输出重新设置为控制台输出
    }

    @org.junit.Test
    public void explainTest1(){
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "I hate it!", "explain"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "1 -5 I hate[-4] it ![-1 punctuation emphasis] [sentence: 1,-5] [result: max + and - of any sentence][overall result = -1 as pos<-neg]"+System.lineSeparator());
    }

    @Test
    public void explainTest2() {
        SentiStrength ss = new SentiStrength();
        String[] args = new String[]{"text", "YOU ARE A FUCKING RETARD!", "scale", "explain"};
        ss.initialiseAndRun(args);

        String ans = out.toString();
        assertEquals(ans, "1 -5 -4 YOU ARE A FUCKING RETARD[-2] [-2 booster word] ![-1 punctuation emphasis] [sentence: 1,-5] [result: max + and - of any sentence][scale result = sum of pos and neg scores]"+System.lineSeparator());
    }
}