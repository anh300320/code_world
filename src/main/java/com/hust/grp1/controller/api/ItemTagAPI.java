package com.hust.grp1.controller.api;

import com.hust.grp1.dto.ApiResponseDto;
import com.hust.grp1.dto.GetPostsByTagDto;
import com.hust.grp1.dto.post.PostedItemDto;
import com.hust.grp1.dto.post.TagByPageDto;
import com.hust.grp1.repository.CustomRepository;
import com.hust.grp1.service.ItemTagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class ItemTagAPI {
    @Autowired
    ItemTagService itemTagService;

    private Logger LOG = LogManager.getLogger(ItemTagAPI.class);

    @GetMapping("/all")
    public ApiResponseDto getAllTags(@RequestParam int page,
                                     @RequestParam(required = false, defaultValue = "POST_COUNT") String sortType,
                                     @RequestParam(required = false, defaultValue = "") String searchKey){

        CustomRepository.GetTagsSortType sortTypeEnum;

        switch (sortType) {
            case "TAG_NAME":
                sortTypeEnum = CustomRepository.GetTagsSortType.SORT_BY_NAME;
                break;
            case "LATEST_CREATED":
                sortTypeEnum = CustomRepository.GetTagsSortType.SORT_BY_LATEST_CREATED;
                break;
            default:
                sortTypeEnum = CustomRepository.GetTagsSortType.SORT_BY_ITEM_COUNT;
        }

        try {
            TagByPageDto tags = itemTagService.getAllTags(page, sortTypeEnum, searchKey);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, tags);
        } catch (Exception e) {
            LOG.debug("Unknown error: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/related")
    public ApiResponseDto getRelatedPosts(@RequestParam String tag,
                                          @RequestParam int page) {

        try {
            List<PostedItemDto> postedItemDtos = itemTagService.getRelatedPosts(tag, page);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, postedItemDtos);
        } catch (Exception e) {
            LOG.debug("Unknown error: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/get")
    public ApiResponseDto getPostByTag(@RequestBody GetPostsByTagDto requestDto){

        CustomRepository.GetPostedItemSortType sortType;
        switch (requestDto.getSortTypeStr()) {
            case "COMMENT":
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_COMMENT;
                break;
            case "UPVOTE":
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_UPVOTE;
                break;
            default:
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_NEWEST;
        }

        try {
            return new ApiResponseDto(
                    ApiResponseDto.StatusCode.SUCCESS,
                    itemTagService.findPostByTag(
                            requestDto.getTag(),
                            sortType,
                            requestDto.getPage(),
                            requestDto.getSize()
                    )
            );
        } catch (Exception e) {
            LOG.debug("Unknown exception ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.UNKNOWN_ERROR);
        }
    }
}
