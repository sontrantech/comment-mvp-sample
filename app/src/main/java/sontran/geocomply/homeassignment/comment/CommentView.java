package sontran.geocomply.homeassignment.comment;

import androidx.annotation.NonNull;

import sontran.geocomply.homeassignment.base.BaseView;

public interface CommentView extends BaseView {
    void onJsonCreated(@NonNull String json);

    void onPatternNotFound();
}
