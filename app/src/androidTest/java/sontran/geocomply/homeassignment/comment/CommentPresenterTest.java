package sontran.geocomply.homeassignment.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CommentPresenterTest {
    private final Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
    private final JsonFileUtils jsonFileUtils = new JsonFileUtils(appContext);

    CommentPresenter presenter;

    @Before
    public void setUp() {
        openMocks(this);
        CommentView view = mock(CommentView.class);
        presenter = Mockito.spy(new CommentPresenter(view));
    }

    @Test
    public void check_remove_special_chars_func() {
        assertEquals(presenter.stringWithoutSpecialChars("abcd0987"), "abcd0987");
        assertEquals(presenter.stringWithoutSpecialChars("%ad#asd123()"), "adasd123");
    }

    @Test
    public void verify_mention_pattern() {
        assertTrue(presenter.isMention("@son"));
        assertTrue(presenter.isMention("@mention @hello"));

        assertFalse(presenter.isMention("email address myname@google.com"));
        assertFalse(presenter.isMention("1234"));
    }

    @Test
    public void verify_link_pattern() {
        assertTrue(presenter.isLink("https://www.olympics.com/tokyo-2020/en/"));
        assertTrue(presenter.isLink("https://olympics.com/tokyo-2020/en/"));
        assertTrue(presenter.isLink("http://olympics.com/tokyo-2020/en/"));
        assertTrue(presenter.isLink("www.olympics.com/tokyo-2020/en"));
        assertTrue(presenter.isLink("olympics.com/tokyo-2020/en"));

        assertFalse(presenter.isLink("Olympic"));
        assertFalse(presenter.isLink("Olympic 2020"));
    }

    @Test
    public void should_return_single_mention_json() {
        // prepare mock data
        String expectedJson = jsonFileUtils.dataFromJsonFile("comment_single_mention_output.json");

        presenter.analyzeInput("hi @billgates");

        // check if the words have been verified
        verify(presenter).isMention("hi");
        verify(presenter).isMention("@billgates");
        verify(presenter).isLink("hi");
        verify(presenter).isLink("@billgates");

        verify(presenter).onJsonCreated(any());
        verify(presenter.getView()).onJsonCreated(expectedJson);
    }

    @Test
    public void should_return_multiple_mentions_json() {
        // prepare mock data
        String expectedJson = jsonFileUtils.dataFromJsonFile("comment_multiple_mentions_output.json");

        presenter.analyzeInput("@billgates do you where is @elonmusk?");

        // check if the words have been verified
        verify(presenter).isMention("@billgates");
        verify(presenter).isMention("where");
        verify(presenter).isLink("is");
        verify(presenter).isLink("@elonmusk?");

        verify(presenter).onJsonCreated(any());
        verify(presenter.getView()).onJsonCreated(expectedJson);
    }

    @Test
    public void should_return_multiple_links_json() {
        // prepare mock data
        String expectedJson = jsonFileUtils.dataFromJsonFile("comment_multiple_links_output.json");

        presenter.analyzeInput(
                "Olympics 2020 is happening; https://olympics.com/tokyo-2020/en/"
                        + " Let's Google!; www.google.com/"
        );

        // check if the words have been verified
        verify(presenter, atLeastOnce()).isMention("Olympics");
        verify(presenter, atLeastOnce()).isMention("2020");
        verify(presenter, atLeastOnce()).isLink("happening;");
        verify(presenter, atLeastOnce()).isLink("https://olympics.com/tokyo-2020/en/");

        verify(presenter).onJsonCreated(any());
        verify(presenter.getView()).onJsonCreated(expectedJson);
    }

    @Test
    public void should_return_both_mention_and_link_json() {
        // prepare mock data
        String expectedJson = jsonFileUtils.dataFromJsonFile("comment_both_mention_and_link_output.json");

        presenter.analyzeInput("hey @Google How big is the universe?; www.google.com");

        // check if the words have been verified
        verify(presenter).isMention("@Google");
        verify(presenter).isMention("big");
        verify(presenter).isLink("universe?;");
        verify(presenter).isLink("www.google.com");

        verify(presenter).onJsonCreated(any());
        verify(presenter.getView()).onJsonCreated(expectedJson);
    }


    @Test
    public void should_return_pattern_not_found() {
        // prepare mock data
        presenter.analyzeInput("Random text");

        // check if the words have been verified
        verify(presenter).isMention("Random");
        verify(presenter).isLink("text");

        verify(presenter).onPatternNotFound();
        verify(presenter.getView()).onPatternNotFound();
    }
}