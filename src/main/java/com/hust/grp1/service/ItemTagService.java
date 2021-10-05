package com.hust.grp1.service;

import com.hust.grp1.config.ApplicationConstants;
import com.hust.grp1.dto.factory.DtoFactory;
import com.hust.grp1.dto.post.PostedItemByPageDto;
import com.hust.grp1.dto.post.PostedItemDto;
import com.hust.grp1.dto.post.TagByPageDto;
import com.hust.grp1.dto.tag.TagDto;
import com.hust.grp1.entity.ItemTag;
import com.hust.grp1.entity.PostedItem;
import com.hust.grp1.repository.CustomRepository;
import com.hust.grp1.repository.ItemTagRepository;
import com.hust.grp1.repository.PostedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItemTagService {

    @Autowired
    private ItemTagRepository itemTagRepository;

    @Autowired
    private PostedItemRepository postedItemRepository;

    @Autowired
    private CustomRepository customRepository;

    @Transactional
    public List<TagDto> getTags(){
        List<String> tags = itemTagRepository.findDistinctTag();
        List<TagDto> out = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = LocalDateTime.now().format(formatter);
        for(String t: tags){
            TagDto tag = new TagDto();
            tag.setTag(t);
            tag.setNumPost(itemTagRepository.countByTag(t));
            tag.setPostToday(itemTagRepository.countByCreatedDateAndTag(now, t));
            out.add(tag);
        }
        return out;
    }

    public PostedItemByPageDto findPostByTag(String tag,
                                             CustomRepository.GetPostedItemSortType sortType,
                                             int page,
                                             int pageSize){

        return customRepository.getPostsByTag(tag, sortType, page, pageSize);
    }

    public TagByPageDto getAllTags(int page, CustomRepository.GetTagsSortType sortType, String searchKey) {

        return customRepository.getAllTags(page, sortType, searchKey);
    }

    public List<PostedItemDto> getRelatedPosts(String tag, int page) {

        Pageable pageable = PageRequest.of(page, ApplicationConstants.RELATED_POSTS_BY_TAG_PAGE_SIZE);
        List<PostedItem> relatedPosts = postedItemRepository.findRelatedPost(0, Collections.singletonList(tag), pageable);

        return DtoFactory.createListDto(relatedPosts);
    }
}
