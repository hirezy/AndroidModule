package me.shouheng.commons.view.fragment;

import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author shouh
 * @version $Id: CommonDaggerFragment, v 0.1 2018/6/9 13:40 shouh Exp$
 */
public abstract class CommonDaggerFragment<T extends ViewDataBinding> extends CommonFragment<T> implements LifecycleRegistryOwner {

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Override
    @NonNull
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
