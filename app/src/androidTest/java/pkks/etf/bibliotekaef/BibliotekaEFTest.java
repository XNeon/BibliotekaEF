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

    public void testUnesenaKnjiga () {
        onView(withText("Dodirni ovdje za novi unos")).perform(click());
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("9780545010221"));
        Espresso.closeSoftKeyboard();
        onView(withText("Pretražiti google Books servis za ostale podatke?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
        onView(withText("Ne")).perform(click());
        onView(withId(R.id.etDate)).perform(typeText("20-05-2015"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etAuthor)).perform(typeText("Enil Pajic, Faruk Ljuca"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etName)).perform(typeText("Osnove android programiranja i testiranja"));
        Espresso.closeSoftKeyboard();
        //BookInputActivity.Ja.currentEntry.coverImage = new File();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btSave)).perform(click());
        onView(withText("Dodirni ovdje za novi unos")).check(matches(isDisplayed()));
    }
}
