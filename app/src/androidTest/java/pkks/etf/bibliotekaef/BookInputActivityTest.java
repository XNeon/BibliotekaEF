package pkks.etf.bibliotekaef;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.Espresso;
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

    public void testDodavanjePrazneKnjige () {
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.br_stranica))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
    // Testiranje broja stranica

    public void testValidanBrojStranica () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.invalid_isbn))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testNegativanBrojStranica () {
        onView(withId(R.id.etPageCount)).perform(typeText("-200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.br_stranica))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testBrojStranicaNula () {
        onView(withId(R.id.etPageCount)).perform(typeText("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.br_stranica))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


    // Testiranje ISBN

    public void testValidanISBN_13cifara () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("9780545010221"));
        Espresso.closeSoftKeyboard();
        onView(withText("Pretražiti google Books servis za ostale podatke?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
        onView(withText("Ne")).perform(click());
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.datum_neispravan))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testValidanISBN_10cifara () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("0545010225..."));
        Espresso.closeSoftKeyboard();
        onView(withText("Pretražiti google Books servis za ostale podatke?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
        onView(withText("Ne")).perform(click());
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.datum_neispravan))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testKratakISBN () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("97805"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.invalid_isbn))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testNevalidanISBN () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("aaaaaaaaaaaaa"));
        onView(withText(R.string.invalid_isbn))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


    // Testiranje Datuma

    public void testValidanDatum () {
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
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.slika_nije_ucitana))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testNevalidanDatum () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("9780545010221"));
        Espresso.closeSoftKeyboard();
        onView(withText("Pretražiti google Books servis za ostale podatke?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
        onView(withText("Ne")).perform(click());
        onView(withId(R.id.etDate)).perform(typeText("aaaa"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.datum_neispravan))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testNevalidanDatum_restrikcijeNaFormat () {
        onView(withId(R.id.etPageCount)).perform(typeText("200"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etISBN)).perform(typeText("9780545010221"));
        Espresso.closeSoftKeyboard();
        onView(withText("Pretražiti google Books servis za ostale podatke?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
        onView(withText("Ne")).perform(click());
        onView(withId(R.id.etDate)).perform(typeText("20-50-2015"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btSave)).perform(click());
        onView(withText(R.string.datum_neispravan))
                .inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    public void testDodavanjeSlike () {
        onView(withId(R.id.ivAddPic)).perform(click());

        onView(withText("Uslikaj")).check(matches(isDisplayed()));
        onView(withText("Sa uređaja")).check(matches(isDisplayed()));
    }

    public void testPovratakNazad () {
        onView(withId(R.id.action_back)).perform(click());

        onView(withText("Prekinuti unos?")).check(matches(isDisplayed()));
        onView(withText("Da")).check(matches(isDisplayed()));
        onView(withText("Ne")).check(matches(isDisplayed()));
    }

    /*
    public void testPovratakNazadDa () {
        onView(withId(R.id.action_back)).perform(click());
        onView(withText("Da")).perform(click());

        onView(withId(R.id.textView)).check(matches(not(isDisplayed())));
    }
    */

    /*
    public void testPovratakNazadNe () {
        onView(withId(R.id.action_back)).perform(click());
        onView(withText("Ne")).perform(click());

        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
    }
    */


}