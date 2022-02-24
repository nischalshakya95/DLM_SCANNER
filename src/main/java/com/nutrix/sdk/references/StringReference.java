package com.nutrix.sdk.references;

import com.sun.jna.ptr.ByReference;

public class StringReference extends ByReference {


    public StringReference() {
        this(0);
    }

    public StringReference(int size) {
        super(Math.max(size, 4));
        getPointer().clear(Math.max(size, 4));
    }

    public StringReference(String str) {
        super(str.length() < 4 ? 4 : str.length() + 1);
        setValue(str);
    }

    public void setValue(String str) {
        getPointer().setString(0, str);
    }

    public String getValue() {
        return getPointer().getString(0);
    }

}
