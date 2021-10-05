package com.hust.grp1.repository;

import com.hust.grp1.config.ApplicationConstants;
import com.hust.grp1.dto.factory.DtoFactory;
import com.hust.grp1.dto.post.PostedItemByPageDto;
import com.hust.grp1.dto.post.TagByPageDto;
import com.hust.grp1.dto.post.QuestionDto;
import com.hust.grp1.dto.post.QuestionsPageDto;
import com.hust.grp1.dto.tag.TagDto;
import com.hust.grp1.dto.user.UserInfoDto;
import com.hust.grp1.dto.user.UserPageDto;
import com.hust.grp1.dto.user.UserTagDto;
import com.hust.grp1.entity.PostedItem;
import com.hust.grp1.entity.Upvote;
import com.hust.grp1.entity.User;
import com.hust.grp1.util.StringUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public enum GetTagsSortType {
        SORT_BY_ITEM_COUNT("ORDER BY COUNT(t.item) DESC "),
        SORT_BY_LATEST_CREATED("ORDER BY MAX(t.item.createdDate) DESC "),
        SORT_BY_NAME("ORDER BY t.tag ");

        private String orderHql;

        private GetTagsSortType(String orderHql) {
            this.orderHql = orderHql;
        }

        public String getOrderHql() {
            return orderHql;
        }
    }

    public enum GetPostedItemSortType {
        SORT_BY_NEWEST("ORDER BY p.createdDate DESC "),
        SORT_BY_UPVOTE("ORDER BY p.upvoteCount DESC "),
        SORT_BY_COMMENT("ORDER BY p.commentCount DESC ");

        private String hql;

        private GetPostedItemSortType(String hql) {
            this.hql = hql;
        }
    }

    public enum GetUsersSortType {
        SORT_BY_REPUTATION("ORDER BY u.reputation DESC "),
        SORT_BY_NEWEST("ORDER BY u.createdDate DESC "),
        SORT_BY_QUESTION("ORDER BY question DESC ");

        private String hql;

        GetUsersSortType(String hql) {
            this.hql = hql;
        }
    }

    /**
     * Get comments of a post
     */
    public List<PostedItem> getChildrenPostedItem(int parentItemId, int userId) {
        // TODO fix this method to take username

        String hql = "SELECT p, u FROM PostedItem p " +
                "LEFT JOIN Upvote u ON p.id = u.item.id AND u.upvoteUser.id = :userId " +
                "WHERE p.parent.id = :parentId order by p.createdDate";

        List<PostedItem> result = new ArrayList<>();

        Query query = entityManager.createQuery(hql);
        query.setParameter("parentId", parentItemId);
        query.setParameter("userId", userId);
        List<Object[]> queryResult = query.getResultList();
        for (Object[] tuple : queryResult) {
            PostedItem postedItem = (PostedItem) tuple[0];
            Upvote upvote = (Upvote) tuple[1];

            if (upvote == null) postedItem.setCurrentUserVote(0);
            else postedItem.setCurrentUserVote(upvote.getVote());
            result.add(postedItem);
        }

        return result;
    }

    public TagByPageDto getAllTags(int page, GetTagsSortType sortType, String searchKey) {

        List<TagDto> results = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDateTime.now().format(formatter);

        String hql = "SELECT " +
                "t.tag, " +
                "COUNT (t.item), " +
                "MAX(t.item.createdDate), " +
                "SUM(CASE WHEN DATE(t.item.createdDate) = DATE(:today) THEN 1 ELSE 0 END)" +
                " FROM ItemTag t where t.tag like '%" + searchKey + "%' GROUP BY t.tag " +
                sortType.getOrderHql();

        Query query = entityManager.createQuery(hql);
        query
                .setMaxResults(ApplicationConstants.GET_ALL_TAGS_PAGE_SIZE)
                .setFirstResult(page * ApplicationConstants.GET_ALL_TAGS_PAGE_SIZE)
                .setParameter("today", today);

        List<Object[]> queryResult = query.getResultList();
        for (Object[] tuple : queryResult) {
            String tagName = (String) tuple[0];
            long postCount = (long) tuple[1];
            LocalDateTime latestPost = (LocalDateTime) tuple[2];
            long postTodayCount = (long) tuple[3];

            results.add(new TagDto(tagName, postCount, latestPost, postTodayCount));
        }

        String hql1 = "SELECT COUNT(DISTINCT t.tag) FROM ItemTag t where t.tag like '%" + searchKey + "%'";

        Query query1 = entityManager.createQuery(hql1);
        List<Object> result = query1.getResultList();
        long total = (long) result.get(0);
        int totalPage = (int) Math.ceil(total/(float)ApplicationConstants.GET_ALL_TAGS_PAGE_SIZE);
        return new TagByPageDto(page, total, totalPage, results);
    }

    public QuestionsPageDto getPostedItems(int page, int size, GetPostedItemSortType sortType, String searchKey) {

        String searchSql = "";
        if(searchKey != null) {
            searchKey = StringUtil.getSqlSearchFormat(searchKey);
            searchSql = "AND p.content LIKE \'" + searchKey + "\' ";
        }

        String hql = "SELECT p, COUNT(cmt) " +
                "FROM PostedItem p " +
                "LEFT JOIN PostedItem cmt " +
                "ON p.id = cmt.parent.id " +
                "WHERE p.parent IS NULL " +
                searchSql +
                "GROUP BY p " +
                sortType.hql;

        Query query = entityManager.createQuery(hql);
        query
                .setMaxResults(size)
                .setFirstResult(page * size);

        List<Object[]> result = query.getResultList();

        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Object[] tuple : result) {
            PostedItem postedItem = (PostedItem) tuple[0];
            long commentCount = (long) tuple[1];

            questionDtos.add(DtoFactory.createQuestionDto(postedItem, commentCount));
        }

        // Count number of posts
        String countResultHql = "SELECT COUNT(p) FROM PostedItem p WHERE p.parent IS NULL " + searchSql;
        query = entityManager.createQuery(countResultHql);
        long totalCount = (long) query.getSingleResult();

        QuestionsPageDto questionsPageDto = new QuestionsPageDto();
        questionsPageDto.setQuestions(questionDtos);
        questionsPageDto.setTotalItems(totalCount);
        questionsPageDto.setTotalPages(totalCount / size + 1);
        questionsPageDto.setPageSize(size);
        questionsPageDto.setCurrentPage(page);
        return questionsPageDto;
    }

    public UserInfoDto getUserInfo(int userId) {

        String hql = "SELECT u, " +
                "SUM(CASE WHEN p.parent IS NULL AND p IS NOT NULL THEN 1 ELSE 0 END), " +
                "SUM(CASE WHEN p.parent IS NOT NULL THEN 1 ELSE 0 END) " +
                "FROM User u " +
                "LEFT JOIN PostedItem p " +
                "ON p.owner.id = u.id " +
                "WHERE u.id = :userId " +
                "GROUP BY u ";

        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        Object[] result;
        try{
            result = (Object[]) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }


        User user = (User) result[0];
        long questionCount = (long) result[1];
        long commentCount = (long) result[2];

        if(user == null) return null;

        return DtoFactory.createUserInfoDto(user, questionCount, commentCount, 0);
    }

    public UserPageDto getUsersInfo(String searchKey, int page, GetUsersSortType sortType) {

        String filterHql = "";
        if(searchKey != null){
            String searchKeySql = StringUtil.getSqlSearchFormat(searchKey);
            filterHql += "WHERE u.username LIKE \'" + searchKeySql + "\' " +
                    "OR u.firstName LIKE \'" + searchKeySql + "\' " +
                    "OR u.lastName LIKE \'" + searchKeySql + "\' ";
        }

        String hql = "SELECT u, " +
                "SUM (CASE WHEN p.parent IS NULL AND p IS NOT NULL THEN 1 ELSE 0 END) AS question, " +
                "SUM (CASE WHEN p.parent IS NOT NULL THEN 1 ELSE 0 END) AS comment " +
                "FROM User u " +
                "LEFT JOIN PostedItem p " +
                "ON p.owner.id = u.id " +
                filterHql +
                "GROUP BY u "
                + sortType.hql;

        Query query = entityManager.createQuery(hql);
        query
                .setMaxResults(ApplicationConstants.GET_USERS_PAGE_SIZE)
                .setFirstResult(page * ApplicationConstants.GET_USERS_PAGE_SIZE);

        List<Object[]> result = query.getResultList();
        List<UserInfoDto> userInfoDtos = new ArrayList<>();
        for(Object[] tuple : result) {
            User user = (User) tuple[0];
            long questionCount = (long) tuple[1];
            long commentCount = (long) tuple[2];
            userInfoDtos.add(DtoFactory.createUserInfoDto(user, questionCount, commentCount, 0));
        }

        String countResultHql = "SELECT COUNT(u) FROM User u " + filterHql;
        query = entityManager.createQuery(countResultHql);
        long totalCount = (long) query.getSingleResult();
        int totalPage = (int) Math.ceil(totalCount/(float)ApplicationConstants.GET_USERS_PAGE_SIZE);

        return new UserPageDto(page, totalCount, totalPage, userInfoDtos);
    }

    public List<UserTagDto> getUserTags(String username) {
        String hql = "SELECT t.tag, COUNT (p) " +
                "FROM ItemTag t " +
                "JOIN PostedItem p " +
                "ON t.item = p " +
                "WHERE p.owner.username = :username " +
                "GROUP BY t.tag";
        Query query = entityManager.createQuery(hql);
        query.setParameter("username", username);
        List<Object[]> result = query.getResultList();
        List<UserTagDto> userTagDtos = new ArrayList<>();
        for(Object[] tuple : result) {
            String tag = (String) tuple[0];
            long postCount = (long) tuple[1];
            userTagDtos.add(new UserTagDto(tag, postCount));
        }

        return userTagDtos;
    }

    public List<QuestionDto> getHotQuestions() {
        String sql = "SELECT p.id, p.title, p.content, p.upvote_count, p.comment_count " +
                "FROM posted_item p " +
                "WHERE p.parent_id IS NULL OR p.parent_id = 0 " +
                "ORDER BY (p.upvote_count + p.comment_count) DESC " +
                "LIMIT 10 ";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        List<QuestionDto> hotQuestions = new ArrayList<>();
        for(Object[] tuple : result) {
            QuestionDto dto = new QuestionDto();
            dto.setId((Integer) tuple[0]);
            dto.setTitle((String) tuple[1]);
            dto.setContent((String) tuple[2]);
            dto.setUpvoteCount((int) tuple[3]);
            dto.setCommentCount((int) tuple[4]);
            hotQuestions.add(dto);
        }
        return hotQuestions;
    }

    public Map<Integer, Integer> getCurrentUserUpvote(List<Integer> itemIds, String username) {
        String hql = "SELECT p.id, u.vote FROM PostedItem p " +
                "LEFT JOIN Upvote u " +
                "ON p.owner = u.upvoteUser AND u.upvoteUser.username = :username " +
                "WHERE p.id IN (:itemIds) ";
        Query query = entityManager.createQuery(hql);
        query
                .setParameter("username", username)
                .setParameter("itemIds", itemIds);
        List<Object[]> result = query.getResultList();
        Map<Integer, Integer> currentUpvote = new HashMap<>();
        for(Object[] tuple : result) {
            int itemId = (int) tuple[0];
            int vote = (int) tuple[1];
            currentUpvote.put(itemId, vote);
        }

        return currentUpvote;
    }

    public PostedItemByPageDto getPostsByTag(String tag, GetPostedItemSortType sortType, int page, int pageSize) {
        String hql = "SELECT DISTINCT (p) FROM PostedItem p JOIN ItemTag t ON p = t.item " +
                "WHERE t.tag = :tag " + sortType.hql;
        Query query = entityManager.createQuery(hql);
        query
                .setParameter("tag", tag)
                .setMaxResults(pageSize)
                .setFirstResult(page * pageSize);
        List<PostedItem> result = query.getResultList();

        String hqlCount = "SELECT COUNT(DISTINCT t.item.id) FROM ItemTag t " +
                "WHERE t.tag = :tag " +
                "GROUP BY t.tag ";
        Query countQuery = entityManager.createQuery(hqlCount);
        countQuery.setParameter("tag", tag);
//        Object countResult = ;
        long totalCount = (long) countQuery.getSingleResult();
        PostedItemByPageDto postedItemByPageDto = new PostedItemByPageDto(
                page,
                totalCount,
                totalCount / pageSize,
                DtoFactory.createListDto(result)
        );
        return postedItemByPageDto;
    }
}
