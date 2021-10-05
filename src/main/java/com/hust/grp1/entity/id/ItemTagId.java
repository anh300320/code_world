package com.hust.grp1.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class ItemTagId implements Serializable {

    private Integer item;
    private String tag;

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemTagId itemTagId = (ItemTagId) o;
        return item.equals(itemTagId.item) &&
                tag.equals(itemTagId.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, tag);
    }
}
