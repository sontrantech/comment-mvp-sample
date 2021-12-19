package sontran.geocomply.homeassignment.comment;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import sontran.geocomply.homeassignment.R;
import sontran.geocomply.homeassignment.base.BaseActivity;

public class CommentActivity extends BaseActivity<CommentPresenter> implements CommentView {

    private AppCompatTextView tvJson;
    private AppCompatEditText edtComment;

    @Override
    public CommentPresenter createPresenter() {
        return new CommentPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void bindingView() {
        tvJson = findViewById(R.id.tv_json);
        edtComment = findViewById(R.id.edt_comment);
    }

    @Override
    protected void setupView() {
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    mPresenter.analyzeInput(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtComment.requestFocus();
    }

    @Override
    public void onJsonCreated(@NonNull String json) {
        tvJson.setText(beautifyJson(json));
    }

    @Override
    public void onPatternNotFound() {
        tvJson.setText(getString(R.string.pattern_not_found));
    }

    @VisibleForTesting
    public static String beautifyJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = JsonParser.parseString(json);
        return gson.toJson(el);
    }
}