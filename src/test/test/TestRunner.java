package test.test;

import org.junit.Test;
import federate.SimulationEntityExecution;

import java.util.Queue;

/**
 * Created by Andrew on 1/12/2017.
 */
public abstract class TestRunner {

    public static abstract class TestCase<T extends SimulationEntityExecution> {
        public TestCase() {}
        public abstract boolean doAction(T entity);
        public abstract boolean isComplete(T entity);
    }

    public abstract Queue<TestCase> getActionQueue();
    public abstract SimulationEntityExecution getEntity();

    @Test
    public void runTests() {
        Queue<TestCase> testQueue = this.getActionQueue();
        SimulationEntityExecution entity = this.getEntity();

        assert testQueue != null && !testQueue.isEmpty();
        assert entity != null;

        while(!testQueue.isEmpty()) {
            TestCase testCase = testQueue.peek();

            testCase.doAction(entity);
            entity.doAction();

            if(testCase.isComplete(entity)) {
                testQueue.poll();
            }
        }
    }
}
