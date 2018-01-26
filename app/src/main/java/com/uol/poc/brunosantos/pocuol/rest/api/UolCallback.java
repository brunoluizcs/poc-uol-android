package com.uol.poc.brunosantos.pocuol.rest.api;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by brunosantos on 26/01/2018.
 */

public interface UolCallback<T> {
    void onSuccess(@NonNull T object);
    void onError(@NonNull Throwable throwable);
}
