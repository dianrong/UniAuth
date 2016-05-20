package com.dianrong.common.uniauth.common.bean;

import java.io.Serializable;

/**
 * Created by Arc on 18/3/2016.
 */
public class Linkage<E1, E2> implements Serializable {

    private E1 entry1;
    private E2 entry2;

    public E1 getEntry1() {
        return entry1;
    }

    public Linkage setEntry1(E1 entry1) {
        this.entry1 = entry1;
        return this;
    }

    public E2 getEntry2() {
        return entry2;
    }

    public Linkage setEntry2(E2 entry2) {
        this.entry2 = entry2;
        return this;
    }

}
