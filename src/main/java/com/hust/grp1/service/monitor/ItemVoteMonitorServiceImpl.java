package com.hust.grp1.service.monitor;

import com.hust.grp1.entity.PostedItem;
import com.hust.grp1.entity.User;
import com.hust.grp1.repository.PostedItemRepository;
import com.hust.grp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemVoteMonitorServiceImpl implements ItemVoteMonitorService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onVote(PostedItem postedItem, int vote) {

        User postOwner = postedItem.getOwner();
        long reputation = postOwner.getReputation();
        reputation += vote;

        postOwner.setReputation(reputation);
        userRepository.save(postOwner);
    }
}
