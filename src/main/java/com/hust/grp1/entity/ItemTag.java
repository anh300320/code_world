package com.hust.grp1.entity;

import com.hust.grp1.entity.id.ItemTagId;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "item_tag")
@IdClass(ItemTagId.class)
public class ItemTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private PostedItem item;
    @Id
    private String tag;
    @CreationTimestamp
    private LocalDateTime createdDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public PostedItem getItem() {
        return item;
    }

    public void setItem(PostedItem item) {
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
        ItemTag itemTag = (ItemTag) o;
        return item.equals(itemTag.item) &&
                tag.equals(itemTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, tag);
    }
}
