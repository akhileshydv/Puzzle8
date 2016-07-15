package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            for(int x=0;x<NUM_SHUFFLE_STEPS;x++){
                   ArrayList<PuzzleBoard> neigh=puzzleBoard.neighbours();
                   Random rand=new Random();
                   int rnum=rand.nextInt(neigh.size());
                   puzzleBoard=neigh.get(rnum);
            }
            //puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    /*public void solve() {
        Log.d("DEMO1","LOOP");
        PuzzleBoardComparator comparator=new PuzzleBoardComparator();
        Log.d("DEMO2","LOOP");
        PriorityQueue<PuzzleBoard> queue=new PriorityQueue<>(10,comparator);
        Log.d("DEMO3","LOOP");
       puzzleBoard.steps=0;
        puzzleBoard.previousBoard=null;
        queue.add(puzzleBoard);
        while(!queue.isEmpty()){
              Log.d("DEMO4",""+queue.size());
              PuzzleBoard cBoard=queue.remove();
              if(cBoard.resolved()){
                   ArrayList<PuzzleBoard> solution=new ArrayList<>();
                   //solution.add(cBoard);
                   while(cBoard.getParent()!=null){
                         solution.add(cBoard);
                         cBoard=cBoard.getParent();
                   }
                   solution.add(cBoard);
                   Collections.reverse(solution);
                   animation=solution;
                   break;
              }
              ArrayList<PuzzleBoard> neigh;
              neigh=cBoard.neighbours();
              Iterator<PuzzleBoard> it=neigh.iterator();
              while(it.hasNext()){
                   queue.add(it.next());
              }
        }
    }*/
    public void solve() {
        PuzzleBoardComparator comparator=new PuzzleBoardComparator();
        PriorityQueue<PuzzleBoard> queue=new PriorityQueue<>(10,comparator);
        HashSet<PuzzleBoard> visited=new HashSet<>();
        puzzleBoard.steps=0;
        puzzleBoard.previousBoard=null;
        queue.add(puzzleBoard);
        while(!queue.isEmpty()){
              Log.d("CHECKPOINT"," "+queue.size());
              PuzzleBoard cBoard=queue.remove();
              if(cBoard.resolved()){
                   ArrayList<PuzzleBoard> solution=new ArrayList<>();
                   while(cBoard.getParent()!=null){
                         solution.add(cBoard);
                         cBoard=cBoard.getParent();
                   }
                   solution.add(cBoard);
                   Collections.reverse(solution);
                   animation=solution;
                   break;
              }
              ArrayList<PuzzleBoard> neigh;
              neigh=cBoard.neighbours();
              Iterator<PuzzleBoard> it=neigh.iterator();
              //Log.d("BOOLEANV",""+cBoard.check(cBoard));
              while(it.hasNext()){
                     PuzzleBoard board=it.next();
                     if(!board.check(cBoard.previousBoard))
                         queue.add(board);

              }
        }

    }
}
