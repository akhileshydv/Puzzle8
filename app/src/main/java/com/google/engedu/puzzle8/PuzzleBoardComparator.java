package com.google.engedu.puzzle8;

import java.util.Comparator;

/**
 * Created by Akhilesh Yadav on 14-07-2016.
 */
public class PuzzleBoardComparator implements Comparator<PuzzleBoard> {
    @Override
    public int compare(PuzzleBoard A, PuzzleBoard B) {
        Integer priorityA=A.priority();
        Integer priorityB=B.priority();
        if(priorityA.compareTo(priorityB)<0)
            return -1;
        if(priorityA.compareTo(priorityB)>0)
            return 1;
        else
            return 0;
    }
}
