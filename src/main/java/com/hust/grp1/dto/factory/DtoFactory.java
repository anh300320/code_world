package com.hust.grp1.dto.factory;


import com.hust.grp1.dto.post.PostAndRelatedPostDto;
import com.hust.grp1.dto.post.PostedItemDto;
import com.hust.grp1.dto.post.QuestionDto;
import com.hust.grp1.dto.tag.TagDto;
import com.hust.grp1.dto.user.UserCreateDto;
import com.hust.grp1.dto.user.UserInfoDto;
import com.hust.grp1.entity.ItemTag;
import com.hust.grp1.entity.PostedItem;
import com.hust.grp1.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DtoFactory {

    public static PostedItemDto createDto(PostedItem postedItem){

        if(postedItem == null) return null;

        PostedItemDto dto = new PostedItemDto();
        dto.setId(postedItem.getId());
        dto.setContent(postedItem.getContent());
        dto.setCreatedDate(postedItem.getCreatedDate());
        dto.setOwner(createDto(postedItem.getOwner()));
        dto.setUpvoteCount(postedItem.getUpvoteCount());
        dto.setTitle(postedItem.getTitle());
        dto.setCode(postedItem.getCode());
        dto.setSolution(createDto(postedItem.getSolution()));
        dto.setCurrentUserVote(postedItem.getCurrentUserVote());

        // Set tags
        List<String> tags = getTagsAsListString(postedItem.getTags());
        dto.setTags(tags);
        // Set comments
        List<PostedItemDto> comments = new ArrayList<>();
        if(postedItem.getChildren() != null)
            for(PostedItem child : postedItem.getChildren()){
                comments.add(createDto(child));
            }
        dto.setComments(comments);
        // Set parent
        if(postedItem.getParent() != null){
            PostedItemDto parent = new PostedItemDto();
            PostedItemDto solution = null;
            if(postedItem.getParent().getSolution() != null){
                solution = new PostedItemDto();
                solution.setId(postedItem.getParent().getSolution().getId());
            }
            parent.setSolution(solution);
            parent.setId(postedItem.getParent().getId());
            dto.setParent(parent);
        }

        return dto;
    }

    private static List<String> getTagsAsListString(List<ItemTag> itemTags) {
        if(itemTags == null) return null;
        List<String> tags = new ArrayList<>();
        for(ItemTag i: itemTags){
            tags.add(i.getTag());
        }
        return tags;
    }

    public static UserCreateDto createDto(User user){
        UserCreateDto dto = new UserCreateDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public static PostedItem createOriginalObject(PostedItemDto dto){

        if(dto == null) return null;

        PostedItem postedItem = new PostedItem();
        postedItem.setContent(dto.getContent());
        postedItem.setSolution(createOriginalObject(dto.getSolution())); // TODO remove
        postedItem.setTitle(dto.getTitle());
        postedItem.setCode(dto.getCode());

        // Set tags
        if(dto.getTags() != null){
            List<ItemTag> tags = new ArrayList<>();
            for(String tag : dto.getTags()){
                tag = tag.toLowerCase().trim();
                ItemTag item = new ItemTag();
                item.setItem(postedItem);
                item.setTag(tag);
                tags.add(item);
            }
            postedItem.setTags(tags);
        }

        User owner = new User();
        owner.setId(dto.getOwner().getId());
        postedItem.setOwner(owner);

        return postedItem;
    }

    public static User createOriginalObject(UserCreateDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstname());
        user.setLastName(dto.getLastname());

        return user;
    }

    public static TagDto createDto(ItemTag itemTag){
        TagDto tagDto = new TagDto();
        tagDto.setTag(itemTag.getTag());
        tagDto.setRecentPost(itemTag.getCreatedDate());
        return tagDto;
    }

    public static PostAndRelatedPostDto createPostAndRelatedPostDto(PostedItem mainPost, List<PostedItem> relatedPosts){

        List<PostedItemDto> relatedPostsDto = new ArrayList<>();

        for(PostedItem postedItem : relatedPosts) {
            relatedPostsDto.add(createDto(postedItem));
        }

        PostAndRelatedPostDto dto = new PostAndRelatedPostDto();
        dto.setMainPost(createDto(mainPost));
        dto.setRelatedPosts(relatedPostsDto);

        return dto;
    }

    public static List<PostedItemDto> createListDto(List<PostedItem> postedItems) {
        List<PostedItemDto> dtos = new ArrayList<>();
        for(PostedItem postedItem : postedItems) {
            dtos.add(createDto(postedItem));
        }

        return dtos;
    }

    public static QuestionDto createQuestionDto(PostedItem postedItem, long commentCount) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(postedItem.getId());
        questionDto.setCode(postedItem.getCode());
        questionDto.setOwner(createDto(postedItem.getOwner()));
        questionDto.setContent(postedItem.getContent());
        questionDto.setCreatedDate(postedItem.getCreatedDate());
        questionDto.setTitle(postedItem.getTitle());
        questionDto.setCommentCount(commentCount);
        questionDto.setUpvoteCount(postedItem.getUpvoteCount());
        questionDto.setTags(getTagsAsListString(postedItem.getTags()));

        return questionDto;
    }

    public static UserInfoDto createUserInfoDto(User user, long questionCount, long commentCount, long bookmarkCount) {

        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setFirstName(user.getFirstName());
        userInfoDto.setLastName(user.getLastName());
        userInfoDto.setReputation(user.getReputation());
        userInfoDto.setAbout(user.getAbout());
        userInfoDto.setQuestionCount(questionCount);
        userInfoDto.setCommentCount(commentCount);
        userInfoDto.setBookmarkCount(bookmarkCount);
        userInfoDto.setCreatedDate(user.getCreatedDate());

        return userInfoDto;
    }
}
