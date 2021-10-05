package com.hust.grp1.repository;

import com.hust.grp1.entity.ItemTag;
import com.hust.grp1.entity.id.ItemTagId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemTagRepository extends JpaRepository<ItemTag, ItemTagId> {

    @Query("SELECT DISTINCT tag from ItemTag")
    List<String> findDistinctTag();

    long countByTag(String tag);

    Page<ItemTag> findByTag(String tag, Pageable pageable);

    @Query("select count(tag) from ItemTag where DATE(createdDate) = DATE(:date) and tag = :tag")
    long countByCreatedDateAndTag(@Param(value = "date") String date, @Param(value = "tag") String tag);
}
