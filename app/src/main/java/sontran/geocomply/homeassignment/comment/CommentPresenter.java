package sontran.geocomply.homeassignment.comment;

import static sontran.geocomply.homeassignment.comment.Constants.SPECIAL_CHAR_PATTERN;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.PatternsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Pattern;

import sontran.geocomply.homeassignment.base.BasePresenter;

public class CommentPresenter extends BasePresenter<CommentView> implements ICommentPresenter {

    private final Gson mGson = new Gson();

    public CommentPresenter(@NonNull CommentView view) {
        super(view);
    }

    @Override
    public void analyzeInput(@NonNull String input) {
        String[] words = input.split(" ");
        Comment comment = null;
        int lastIndexOfThePreviousLink = 0; // this field is to get the right title in case there are many links in one comment

        for (String word : words) {
            boolean isMention = isMention(word);
            boolean isLink = isLink(word);

            if (isMention || isLink) {
                if (comment == null) {
                    comment = new Comment();
                }

                if (isMention) {
                    // mention
                    if (comment.getMentions() == null) {
                        comment.setMentions(new ArrayList<>());
                    }
                    comment.getMentions().add(stringWithoutSpecialChars(word)); // remove special characters
                } else {
                    // link
                    if (comment.getLinks() == null) {
                        comment.setLinks(new ArrayList<>());
                    }
                    comment.getLinks().add(getLinkObject(input, word, lastIndexOfThePreviousLink));
                    lastIndexOfThePreviousLink = input.indexOf(word, lastIndexOfThePreviousLink) + word.length();
                }
            }
        }

        if (comment != null) {
            onJsonCreated(comment);
        } else {
            onPatternNotFound();
        }
    }

    private Comment.Link getLinkObject(@NonNull String input, @NonNull String word, int lastIndexOfThePreviousLink) {
        Comment.Link link = new Comment.Link();
        // url
        link.setUrl(word);

        // title
        String title = input.substring(lastIndexOfThePreviousLink, input.indexOf(word)).trim();

        // remove ";" character if needed
        char lastCharTitle = title.charAt(title.length() - 1);
        if (";".charAt(0) == lastCharTitle) {
            title = title.substring(0, title.length() - 1);
        }
        link.setTitle(title);
        return link;
    }

    @VisibleForTesting
    String stringWithoutSpecialChars(@NonNull String word) {
        return word.replaceAll(SPECIAL_CHAR_PATTERN, "");
    }

    @VisibleForTesting
    void onJsonCreated(@NonNull Comment comment) {
        if (getView() != null) {
            getView().onJsonCreated(mGson.toJson(comment));
        }
    }

    @VisibleForTesting
    void onPatternNotFound() {
        if (getView() != null){
            getView().onPatternNotFound();
        }
    }

    @VisibleForTesting
    boolean isLink(@NonNull String word) {
        return PatternsCompat.WEB_URL.matcher(word).matches();
    }

    @VisibleForTesting
    boolean isMention(@NonNull String word) {
        return Pattern.matches(Constants.MENTION_PATTERN, word);
    }

}
