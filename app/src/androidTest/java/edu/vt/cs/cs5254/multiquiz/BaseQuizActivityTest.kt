package edu.vt.cs.cs5254.multiquiz

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.vt.cs.cs5254.multiquiz.OrientationChangeAction.Companion.orientationLandscape
import edu.vt.cs.cs5254.multiquiz.OrientationChangeAction.Companion.orientationPortrait
import org.hamcrest.core.IsNot.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseQuizActivityTest {

    @get:Rule
    var myActivityRule = ActivityTestRule(QuizActivity::class.java)

    // ==========================================================
    // Please ensure your application passes these tests
    // before submitting your project
    // ==========================================================

    @Test
    fun appContextGivesCorrectPackageName() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        // val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("edu.vt.cs.cs5254.multiquiz", appContext.packageName)
    }

    @Test
    fun clickingWrongAnswerEnableSubmitButton() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun clickingHint3TimesDisablesAllButCorrect() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun clickingHint3TimesDisablesHint() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun clickingAnswer0ThenAnswer1DeselectsAnswer0AndSelectsAnswer1() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_1_button))
            .check(matches(isSelected()))
    }

    @Test
    fun clickingAnswer1TwiceDeselectsAnswer1() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
    }

    // ==========================================================
    // Instructor Tests that employ orientation changes
    // ==========================================================

    @Test
    fun clickingAnswer1ThenRotatingSelectsAnswer1() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(isRoot()).perform(orientationLandscape())
        onView(withId(R.id.answer_1_button))
            .check(matches(isSelected()))
    }

    @Test
    fun clickingHint3TimesThenRotatingDisablesHint() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(isRoot()).perform(orientationLandscape())
        onView(withId(R.id.hint_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun clickingHint3TimesThenRotatingTwiceDisablesAllButCorrect() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(isRoot()).perform(orientationLandscape())
        onView(isRoot()).perform(orientationPortrait())
        onView(withId(R.id.answer_0_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isEnabled())))
    }

    // ==========================================================
    // Instructor Tests that advance to the report activity
    // ==========================================================

    @Test
    fun all4QuestionsCorrectNoHints() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_2_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_3_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        // you should now be in the report activity
        onView(withId(R.id.total_questions_value))
            .check(matches(withText("4")))
        onView(withId(R.id.total_correct_value))
            .check(matches(withText("4")))
        onView(withId(R.id.hints_used_value))
            .check(matches(withText("0")))
    }
}
