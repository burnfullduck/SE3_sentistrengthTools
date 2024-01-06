package rainbowsix.ss;


import org.junit.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ClassificationTest {

    /**
     * @author XuChen
     * 对文本内所有行进行分析功能的测试
     */
    @Test
    public void fileTest1(){
        SentiStrength ss = new SentiStrength();
        String[] arg = new String[]{"input", "src/test/java/rainbowsix/ss/testData/testText.txt"};
        ss.initialiseAndRun(arg);

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/test/java/rainbowsix/ss/testData/testText0_out.txt"));
            StringBuilder sb = new StringBuilder();
            for (String line: lines) {
                sb.append(line+"\n");
            }
            String testString = sb.toString();

            lines = Files.readAllLines(Paths.get("src/test/java/rainbowsix/ss/testData/testTextCorrectClassification.txt"));
            sb = new StringBuilder();
            for (String line: lines) {
                sb.append(line+"\n");
            }
            String correctString = sb.toString();

            File file = new File("src/test/java/rainbowsix/ss/testData/testText0_out.txt");
            file.delete();
            assertEquals(correctString, testString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}