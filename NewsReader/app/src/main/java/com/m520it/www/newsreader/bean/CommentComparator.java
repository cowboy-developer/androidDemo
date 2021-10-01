package com.m520it.www.newsreader.bean;

import java.util.Comparator;

/**
 * Created by xmg on 2017/1/7.
 */

public class CommentComparator implements Comparator<CommentBean>{

    @Override
    public int compare(CommentBean lhs, CommentBean rhs) {
        int lhsVote = lhs.getVote();
        int rhsVote = rhs.getVote();
//        -1 0 1
        if(lhsVote==rhsVote){
            return 0;
        }
        //降序 100    50  40  10
        return lhsVote>rhsVote?-1:1;
    }
}
