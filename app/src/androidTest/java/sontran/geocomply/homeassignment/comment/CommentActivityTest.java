package sontran.geocomply.homeassignment.comment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intercepting.SingleActivityFactory;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sontran.geocomply.homeassignment.R;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CommentActivityTest {
    private Context appContext;
    private CommentActivity commentActivity;
    private CommentPresenter presenter;

    SingleActivityFactory<CommentActivity> activityFactory = new SingleActivityFactory<CommentActivity>(CommentActivity.class) {
        @Override
        protected CommentActivity create(Intent intent) {
            commentActivity = spy(getActivityClassToIntercept());
            presenter = spy(new CommentPresenter(commentActivity));
            when(commentActivity.createPresenter()).thenReturn(presenter);
            return commentActivity;
        }
    };

    @Rule
    public ActivityTestRule<CommentActivity> testRule = new ActivityTestRule<>(activityFactory, true, true);

    @Test
    public void activity_isBeingSpied() {
        verify(commentActivity).setContentView(R.layout.activity_comment);
    }

    @Before
    public void setUp() {
        appContext = getInstrumentation().getTargetContext();
        openMocks(this);
    }

    @Test
    public void verify_ui_components_visible() {
        Matcher<View> edtComment = withId(R.id.edt_comment);
        onView(edtComment)
                .check(matches(withHint(appContext.getString(R.string.txt_hint_comment))))
                .check(matches(isDisplayed()));

        Matcher<View> tvJson = withId(R.id.tv_json);
        onView(tvJson).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void should_show_json_text() {
        Matcher<View> edtComment = withId(R.id.edt_comment);
        Matcher<View> tvJson = withId(R.id.tv_json);

        String inputText = "@Gandhi";
        onView(edtComment).perform(ViewActions.typeText(inputText));

        verify(presenter, atLeastOnce()).analyzeInput(inputText);
        String expectedJsonRaw = "{\"mentions\":[\"Gandhi\"]}";
        verify(commentActivity, atLeastOnce()).onJsonCreated(expectedJsonRaw);

        // UI should change properly
        String jsonToShow = CommentActivity.beautifyJson(expectedJsonRaw);
        onView(tvJson).check(matches(withText(jsonToShow)));
    }

    @Test
    public void should_show_no_pattern_found() {
        Matcher<View> edtComment = withId(R.id.edt_comment);
        Matcher<View> tvJson = withId(R.id.tv_json);

        String inputText = "Random text";
        onView(edtComment).perform(ViewActions.typeText(inputText));

        verify(presenter, atLeastOnce()).analyzeInput(inputText);
        verify(commentActivity, atLeastOnce()).onPatternNotFound();
        verify(commentActivity, never()).onJsonCreated(any());

        // UI should change properly
        onView(tvJson).check(matches(withText(appContext.getString(R.string.pattern_not_found))));
    }
}