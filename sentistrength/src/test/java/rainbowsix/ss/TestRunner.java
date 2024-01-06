package rainbowsix.ss;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author XuChen
 */
public class TestRunner {
    public static void main(String[] args){
        Result result = JUnitCore.runClasses(ClassificationTest.class, ExplainTest.class, TextTest.class);
        for(Failure failure: result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println("Test Result: "+result.wasSuccessful());
    }
}
