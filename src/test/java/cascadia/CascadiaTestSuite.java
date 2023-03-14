package cascadia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ScoringTestSuite.class,
        CurrentDeckTest.class
})

public class CascadiaTestSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
}