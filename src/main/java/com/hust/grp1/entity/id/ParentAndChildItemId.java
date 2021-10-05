package com.hust.grp1.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class ParentAndChildItemId implements Serializable {

    private int parent;
    private int child;

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentAndChildItemId that = (ParentAndChildItemId) o;
        return parent == that.parent &&
                child == that.child;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
}
