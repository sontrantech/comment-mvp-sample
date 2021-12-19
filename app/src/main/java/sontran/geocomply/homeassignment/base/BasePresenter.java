package sontran.geocomply.homeassignment.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Base presenter
 */
abstract public class BasePresenter<V extends BaseView> {

    private final WeakReference<V> mWeakView;

    public BasePresenter(@NonNull V view) {
        mWeakView = new WeakReference<>(view);
    }

    @Nullable
    public V getView() {
        return mWeakView.get();
    }

}
