package com.hycinth.loadsir.core;

import com.hycinth.loadsir.callback.Callback;

public interface Convertor<T> {
    Class<?extends Callback> map(T t);
}
