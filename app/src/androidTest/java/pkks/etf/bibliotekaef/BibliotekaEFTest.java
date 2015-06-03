package pkks.etf.bibliotekaef;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.not;

import android.media.Image;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;

import java.io.File;

public class BibliotekaEFTest extends ActivityInstrumentationTestCase2<BibliotekaEF> {

    public BibliotekaEFTest () {
        super(BibliotekaEF.class);
    }

    @Override
    protected void setUp () throws Exception {
        super.setUp();
        getActivity();
    }

    public void testPostojanjeSvihElemenata () {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void testButtonUnosKnjige () {
        onView(withText("Dodirni ovdje za novi unos")).perform(click());

        onView(withId(R.id.btSave)).check(matches(isDisplayed()));
    }

    public void testPovratakNazadDa () {
        onView(withText("Dodirni ovdje za novi unos")).perform(click());
        onView(withId(R.id.action_back)).perform(click());
        onView(withText("Da")).perform(click());
        onView(withText("Dodirni ovdje za novi unos")).check(matches(isDisplayed()));
    }

    public void testOtkazSkenera () {
        onView(withText("Dodirni ovdje za novi unos")).perform(click());
        onView(withId(R.id.btISBNScan)).perform(click());
        onView(withId(R.id.btCancel)).perform(click());
        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));
    }
}
