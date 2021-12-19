package sontran.geocomply.homeassignment.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Base activity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        setContentView(getLayoutId());
        bindingView();
        setupView();
    }

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void bindingView();

    protected abstract void setupView();
}
