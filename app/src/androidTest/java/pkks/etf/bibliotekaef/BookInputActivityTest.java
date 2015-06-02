package pkks.etf.bibliotekaef;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;

import android.test.ActivityInstrumentationTestCase2;

public class BookInputActivityTest extends ActivityInstrumentationTestCase2<BookInputActivity> {

    public BookInputActivityTest () {
        super(BookInputActivity.class);
    }

    @Override
    protected void setUp () throws Exception {
        super.setUp();
        getActivity();
    }

    public void testPostojanjeSvihElemenata () {
        onView(withId(R.id.etAuthor)).check(matches(isDisplayed()));
        onView(withId(R.id.etDate)).check(matches(isDisplayed()));
        onView(withId(R.id.etDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.etISBN)).check(matches(isDisplayed()));
        onView(withId(R.id.etName)).check(matches(isDisplayed()));
        onView(withId(R.id.etPageCount)).check(matches(isDisplayed()));

        onView(withId(R.id.btISBNScan)).check(matches(isDisplayed()));
        onView(withId(R.id.btSave)).check(matches(isDisplayed()));
        onView(withId(R.id.btCal)).check(matches(isDisplayed()));
        onView(withId(R.id.ivAddPic)).check(matches(isDisplayed()));

        onView(withId(R.id.tvPicPrompt)).check(matches(isDisplayed()));
    }

    /*
    public void testDodavanjeNepotpuneKnjige () {
        onView(withId(R.id.btSave)).perform(click());
        onView(withText("Nije izabrana slika!")).check(matches(isDisplayed()));
    }
    */

    public void dodavanjeSlike () {
        onView(withId(R.id.ivAddPic)).perform(click());
        onView(withText("Uslikaj")).check(matches(isDisplayed()));
        // onView(withText("Sa ureðaja")).check(matches(isDisplayed()));
    }
}

