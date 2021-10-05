package com.hust.grp1.service;

import com.hust.grp1.dto.EntityFactory;
import com.hust.grp1.dto.factory.DtoFactory;
import com.hust.grp1.dto.post.*;
import com.hust.grp1.dto.tag.TagDto;
import com.hust.grp1.dto.user.UserCreateDto;
import com.hust.grp1.entity.*;
import com.hust.grp1.entity.id.BookmarkId;
import com.hust.grp1.entity.id.UpvoteId;
import com.hust.grp1.exception.*;
import com.hust.grp1.repository.*;
import com.hust.grp1.service.monitor.ItemVoteMonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;

import static com.hust.grp1.config.ApplicationConstants.BOOKMARKED_ITEMS_PAGE_SIZE;

@Service
public class PostedItemService {

    private static final int RELATED_POST_LIMIT = 10;

    @Autowired
    private PostedItemRepository postedItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UpvoteRepository upvoteRepository;

    @Autowired
    private ItemTagRepository itemTagRepository;

    @Autowired
    private ItemVoteMonitorService itemVoteMonitorService;

    @Autowired
    private BookmarkRepository bookMarkRepository;

    @Autowired
    private CustomRepository customRepository;

    public PostAndRelatedPostDto getPostedItem(int id, String username) throws ItemDoesNotExistException {
        Optional<PostedItem> postedItem = postedItemRepository.findById(id);
        if (!postedItem.isPresent()) throw new ItemDoesNotExistException();
        /* Get current user vote*/
        int currentUserVote = 0;
        if (username != null) {
            Upvote userUpvote = upvoteRepository.findByItem_IdAndUpvoteUser_Username(id, username);
            if (userUpvote != null) currentUserVote = userUpvote.getVote();
        }

        /* Get comments */
        int userId = 0;
        if (username != null) {
            User user = userRepository.findOneByUsername(username);
            userId = user.getId();
        }

        List<PostedItem> childrenItems = customRepository.getChildrenPostedItem(id, userId);
        postedItem.get().setChildren(childrenItems);
        Integer upvoteCount = upvoteRepository.countItemUpvote(id);
        if (upvoteCount == null) upvoteCount = 0;
        List<String> tags = new ArrayList<>();
        for (ItemTag tag : postedItem.get().getTags())
            tags.add(tag.getTag());

        /* Get related posts */
        Pageable pageable = PageRequest.of(0, RELATED_POST_LIMIT);
        List<PostedItem> relatedPosts = postedItemRepository.findRelatedPost(id, tags, pageable);


        PostAndRelatedPostDto postAndRelatedPostDto;
        postAndRelatedPostDto = DtoFactory.createPostAndRelatedPostDto(postedItem.get(), relatedPosts);
        postAndRelatedPostDto.getMainPost().setCurrentUserVote(currentUserVote);
        postAndRelatedPostDto.getMainPost().setUpvoteCount(upvoteCount);
        BookmarkId bookmarkId = new BookmarkId();
        bookmarkId.setUser(userId);
        bookmarkId.setPostedItem(id);
        Optional<Bookmark> mark = bookMarkRepository.findById(bookmarkId);
        if(mark.isPresent()){
            postAndRelatedPostDto.setBookmark(true);
        }
        return postAndRelatedPostDto;
    }


    public PostedItemByPageDto getPostedItem(Pageable pageable){
        Page<PostedItem> posts = postedItemRepository.findByParentIsNull(pageable);

        List<PostedItemDto> out = new ArrayList<>();
        for (PostedItem p : posts.getContent()) {
            out.add(DtoFactory.createDto(p));
        }
        return new PostedItemByPageDto(posts.getNumber(), posts.getTotalElements(), posts.getTotalPages(), out);
    }

    public PostedItemByPageDto getPostedItem(Pageable pageable, String searchKey) {
        String a = "%x%";
        a = a.replaceAll("x", searchKey.replaceAll(" ", "%"));
        Page<PostedItem> pages = postedItemRepository.findByContentLikeIgnoreCaseOrTitleLikeIgnoreCaseAndParentIsNull(a, a, pageable);
        List<PostedItem> postedItems = pages.getContent();
        List<PostedItemDto> out = new ArrayList<>();
        for (PostedItem p : postedItems) {
            out.add(DtoFactory.createDto(p));
        }
        return new PostedItemByPageDto(pages.getNumber(), pages.getTotalElements(), pages.getTotalPages(), out);
    }

    public PostedItemDto createPostedItem(PostedItemDto dto, String ownerUsername) throws UserNotFoundException, ItemDoesNotExistException, CommentMustNotHasTagsException {

        /* Set owner to posted item */
        User owner = userRepository.findOneByUsername(ownerUsername);
        if (owner == null) throw new UserNotFoundException();
        UserCreateDto ownerDto = new UserCreateDto();
        ownerDto.setId(owner.getId());
        dto.setOwner(ownerDto);
        PostedItem postedItem = DtoFactory.createOriginalObject(dto);
        postedItem.setOwner(owner);

        /* Set parent */
        if (dto.getParent() != null) {
            // Comment must not has tags
            if(dto.getTags() != null && dto.getTags().size() > 0) throw new CommentMustNotHasTagsException();

            Optional<PostedItem> optionalPostedItem = postedItemRepository.findById(dto.getParent().getId());
            if (!optionalPostedItem.isPresent())
                throw new ItemDoesNotExistException();
            PostedItem parent = optionalPostedItem.get();
            parent.increaseCommentCount();

            postedItem.setParent(parent);
        }

        postedItem = postedItemRepository.save(postedItem);
        return DtoFactory.createDto(postedItem);
    }

    @Transactional
    public UpvoteResponeDto upvote(String username, UpvoteDto dto) throws ItemDoesNotExistException, UserNotFoundException {

        User user = userRepository.findOneByUsername(username);
        if (user == null) throw new UserNotFoundException();
        int itemId = dto.getItemId();
        Optional<PostedItem> optionalPostedItem = postedItemRepository.findById(itemId);
        if (!optionalPostedItem.isPresent()) throw new ItemDoesNotExistException();
        PostedItem postedItem = optionalPostedItem.get();
        Upvote upvote = upvoteRepository.findByItem_IdAndUpvoteUser_Username(itemId, username);

        int vote = dto.getVote();
        if (vote != 1 && vote != -1) throw new IllegalArgumentException();
        /**
         * If user have not upvoted this item then create a new upvote
         */
        UpvoteResponeDto res = new UpvoteResponeDto();
        if (upvote == null) {
            // Create new upvote
            upvote = new Upvote();
            upvote.setUpvoteUser(user);
            upvote.setItem(postedItem);
            upvote.setVote(vote);

            upvoteRepository.save(upvote);
            res.setVote(vote);
        } else {

            if (vote != upvote.getVote()) {
                res.setVote(vote - upvote.getVote());
                upvote.setVote(vote);
                upvoteRepository.save(upvote);
            } else {
                upvoteRepository.delete(upvote);
                res.setVote(-vote);
            }
        }
        itemVoteMonitorService.onVote(postedItem, res.getVote());
        postedItem.onVote(res.getVote());
        postedItemRepository.save(postedItem);
        return res;
    }

    public void deletePostedItem(int itemId, String username) {

        postedItemRepository.deleteById(itemId);
    }

    public boolean isItemOwner(int itemId, String username) throws ItemDoesNotExistException {
        Optional<PostedItem> optionalPostedItem = postedItemRepository.findById(itemId);
        if (!optionalPostedItem.isPresent()) throw new ItemDoesNotExistException();

        PostedItem postedItem = optionalPostedItem.get();
        String ownerUsername = postedItem.getOwner().getUsername();
        return ownerUsername.equals(username);
    }

    public void bookmarkItem(String username, int itemId) throws UserNotFoundException {

        Optional<Bookmark> optionalBookMark = bookMarkRepository.findByPostedItem_IdAndUser_Username(itemId, username);
        User user = userRepository.findOneByUsername(username);
        if (user == null) throw new UserNotFoundException();

        if (optionalBookMark.isPresent()) {
            bookMarkRepository.delete(optionalBookMark.get());
        } else {
            bookMarkRepository.save(new Bookmark(itemId, user.getId()));
        }
    }

    public void solve(String username, int itemId)
            throws ItemDoesNotExistException, NotPostOwnerException, SolvingParentException {

        Optional<PostedItem> optionalPostedItem = postedItemRepository.findById(itemId);
        if (!optionalPostedItem.isPresent()) throw new ItemDoesNotExistException();
        PostedItem comment = optionalPostedItem.get();
        PostedItem parentItem = comment.getParent();
        if (parentItem == null) throw new SolvingParentException();

        if (!parentItem.getOwner().getUsername().equals(username)) throw new NotPostOwnerException();

        if (parentItem.getSolution() != null && parentItem.getSolution().getId() == comment.getId()) parentItem.setSolution(null);
        else parentItem.setSolution(comment);

        // TODO test if need to save both or dont even have to save?
        postedItemRepository.save(parentItem);
    }

    public PostedItemByPageDto getBookmarks(String username, int page) {

        Pageable pageable = PageRequest.of(page, BOOKMARKED_ITEMS_PAGE_SIZE);
        Page<Bookmark> bookmarks = bookMarkRepository.findAllByUser_Username(username, pageable);

        if (bookmarks == null) return null;

        List<PostedItemDto> bookmarkedItems = new ArrayList<>();
        for (Bookmark bookmark : bookmarks.getContent()) {
            bookmarkedItems.add(DtoFactory.createDto(bookmark.getPostedItem()));
        }

        return new PostedItemByPageDto(page, bookmarks.getTotalElements(), bookmarks.getTotalPages(), bookmarkedItems);
    }

    public PostedItemByPageDto getQuestions(String username, int page){
        Pageable pageable = PageRequest.of(page, BOOKMARKED_ITEMS_PAGE_SIZE);
        Page<PostedItem> questions = postedItemRepository.findByParentIsNullAndOwner_Username(username, pageable);
        if(questions == null) return null;
        List<PostedItemDto> posts = new ArrayList<>();
        for(PostedItem p: questions.getContent()){
            posts.add(DtoFactory.createDto(p));
        }
        return new PostedItemByPageDto(questions.getNumber(), questions.getTotalElements(), questions.getTotalPages(), posts);
    }

    public PostedItemByPageDto getComments(String username, int page){
        Pageable pageable = PageRequest.of(page, BOOKMARKED_ITEMS_PAGE_SIZE);
        Page<PostedItem> comments = postedItemRepository.findByParentNotNullAndOwner_Username(username, pageable);
        if(comments == null) return null;
        List<PostedItemDto> posts = new ArrayList<>();
        for(PostedItem p: comments.getContent()){
            posts.add(DtoFactory.createDto(p));
        }
        return new PostedItemByPageDto(comments.getNumber(), comments.getTotalElements(), comments.getTotalPages(), posts);
    }

    public QuestionsPageDto getPostedItems(int page, int size, CustomRepository.GetPostedItemSortType sortType, String searchKey) {
        return customRepository.getPostedItems(page, size, sortType, searchKey);
    }

    public void updatePostedItem(String username, UpdatePostedItemDto updateDto)
            throws ItemDoesNotExistException, IllegalAccessException {

        Optional<PostedItem> optionalPostedItem = postedItemRepository.findById(updateDto.getId());
        if(!optionalPostedItem.isPresent()) throw new ItemDoesNotExistException();
        PostedItem postedItem = optionalPostedItem.get();
        if(!postedItem.getOwner().getUsername().equals(username)) throw new IllegalAccessException();

        // Update PostedItem
        postedItem.setTitle(updateDto.getTitle());
        postedItem.setContent(updateDto.getContent());
        postedItem.setCode(updateDto.getCode());
        // TODO just delete which tags is no more mapped with this posted item
        List<ItemTag> currentTags = postedItem.getTags();
        List<ItemTag> newTags = new ArrayList<>();
        if(currentTags == null) currentTags = new ArrayList<>();
        for (String tag : updateDto.getTags()) {
            ItemTag itemTag = new ItemTag();
            itemTag.setItem(postedItem);
            itemTag.setTag(tag);
            newTags.add(itemTag);
            if(!currentTags.contains(itemTag)) currentTags.add(itemTag);
        }
        List<ItemTag> removedTags = new ArrayList<>();
        for(ItemTag currentTag : currentTags) {
            boolean flag = false;
            for (ItemTag newTag : newTags) {
                if (currentTag.equals(newTag)) {
                    flag = true;
                    break;
                }
            }
            if(!flag) removedTags.add(currentTag);
        }
        currentTags.removeAll(removedTags);
        postedItemRepository.save(postedItem);
    }

    public List<QuestionDto> getHotQuestions() {

        return customRepository.getHotQuestions();
    }
}
