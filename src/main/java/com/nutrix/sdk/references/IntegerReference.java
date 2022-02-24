package com.nutrix.sdk.references;

import com.sun.jna.ptr.ByReference;

public class IntegerReference extends ByReference {

    public IntegerReference() {
        this(0);
    }

    /**
     * Allocates memory at this pointer, to contain the pointed-to value.
     *
     * @param size The number of bytes to allocate. Must match the byte size of
     *             <code>T</code> in the derived class
     *             <code>setValue(&lt;T&gt;)</code> and
     *             <code>&lt;T&gt; getValue()</code> methods.
     */
    public IntegerReference(int size) {
        super(Math.max(size, 4));
        getPointer().clear(Math.max(size, 4));
    }

    public void setValue(int str) {
        getPointer().setInt(0, str);
    }

    public int getValue() {
        return getPointer().getInt(0);
    }

}
