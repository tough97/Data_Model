package com.ynjs.data.listener;

import com.ynjs.data.TreeElement;

public interface PropertyListener {

    //parameters
    //element, current TreeElement object
    //
    void onPropertySet(final TreeElement element, final String key, final Object originalValue, final Object newValue);
    void onPropertyRemoved(final TreeElement element, final String key, final Object value);

}