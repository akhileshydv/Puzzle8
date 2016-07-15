package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    public int steps;
    public PuzzleBoard previousBoard;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles=new ArrayList<>();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, true);
        int chunkSize=parentWidth/NUM_TILES;
        int cnt=0;
        for(int i=0;i<NUM_TILES;i++){
            for(int j=0;j<NUM_TILES;j++){
                 if(cnt==NUM_TILES*NUM_TILES-1){
                     tiles.add(null);
                     break;
                 }
                 Bitmap tile= Bitmap.createBitmap(scaledBitmap,j*chunkSize,i*chunkSize,chunkSize,chunkSize);
                 PuzzleTile t=new PuzzleTile(tile,cnt);
                 tiles.add(t);
                 cnt++;
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        steps=otherBoard.steps+1;
        previousBoard=otherBoard;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }
    public PuzzleBoard getParent(){
         return previousBoard;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> store=new ArrayList<>();
        int xCur=0,yCur=0;
        for (int x=0;x<NUM_TILES;x++){
            for(int y=0;y<NUM_TILES;y++){
                  if(tiles.get(XYtoIndex(x,y))==null){
                      xCur=x;yCur=y;
                  }
            }
        }
        for(int x=0;x<4;x++){
               int newX=NEIGHBOUR_COORDS[x][0];
               int newY=NEIGHBOUR_COORDS[x][1];
               if(newX>=0&&newX<NUM_TILES&&newY>=0&&newY<NUM_TILES){
                     PuzzleBoard pBoard=new PuzzleBoard(this);
                     pBoard.swapTiles(XYtoIndex(xCur,yCur),XYtoIndex(newX,newY));
                     store.add(pBoard);
               }
        }
        return store;
    }

    public int priority() {
        int manhattanDistance=0;
        for(int x=0;x<NUM_TILES;x++){
            for(int y=0;y<NUM_TILES;y++){
                  if(tiles.get(XYtoIndex(x,y))!=null){
                        int numTile=tiles.get(XYtoIndex(x,y)).getNumber();
                        numTile-=1;
                        int xNumTile=numTile/NUM_TILES;
                        int yNumTile=numTile%NUM_TILES;
                        manhattanDistance+=Math.abs(x-xNumTile)+Math.abs(y-yNumTile);
                  }
            }
        }
        return manhattanDistance+steps;
    }
    public boolean check(PuzzleBoard board){
    	 if(board==null)
    	 	return false;
         int len=this.tiles.size();
         boolean flag=true;
         for(int i=0;i<len;i++){
               if(this.tiles.get(i)==null&&board.tiles.get(i)==null)
                   continue;
               if(this.tiles.get(i)!=null&&board.tiles.get(i)==null){
                   flag=false;
                   break;
               }
               if(this.tiles.get(i)==null&&board.tiles.get(i)!=null){
                   flag=false;
                   break;
               }
               if(this.tiles.get(i)!=null&&board.tiles.get(i)!=null){
                   //Log.d("VALUES",""+this.tiles.get(i).getNumber()+" "+board.tiles.get(i).getNumber());
                   if(this.tiles.get(i).getNumber()!=board.tiles.get(i).getNumber()){
                       flag=false;
                       break;
                   }
               }

         }
         return flag;
    }

}
