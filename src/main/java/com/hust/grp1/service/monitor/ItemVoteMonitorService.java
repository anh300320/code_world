package com.hust.grp1.service.monitor;

import com.hust.grp1.entity.PostedItem;
import org.springframework.stereotype.Service;

@Service
public interface ItemVoteMonitorService {

    public void onVote(PostedItem postedItem, int vote);
}
