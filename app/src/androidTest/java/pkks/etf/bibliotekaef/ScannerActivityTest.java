package pkks.etf.bibliotekaef;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.not;

import android.test.ActivityInstrumentationTestCase2;

public class ScannerActivityTest extends ActivityInstrumentationTestCase2<ScannerActivity> {

    public ScannerActivityTest() {
        super(ScannerActivity.class);
    }

    @Override
    protected void setUp () throws Exception {
        super.setUp();
        getActivity();
    }

    /*
    public void testIzlazNaButtonPrekid () {
        onView(withId(R.id.btCancel)).perform(click());
        onView(withId(R.id.scannerCarry)).check(matches(not(isDisplayed())));
    }
    */
}
