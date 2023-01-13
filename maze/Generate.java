package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class Generate {
    public int[][] maze;
    private ArrayList<int[]> all = new ArrayList<int[]>();
    private HashMap<String,Boolean> table = new HashMap<String,Boolean>();
    int cols;
    int rows;
    
    public Generate(int  rows, int cols){
        int[][] map = new int[rows][cols];
        this.cols=cols;
        this.rows=rows;
        this.all = new ArrayList<int[]>(rows*cols);
        
        for(int y=0; y<rows; y++){
            for(int x=0; x<cols; x++){
                map[y][x] = 1;
                int[] crd = {y,x};
                String crds = Arrays.toString(crd);
                table.put(crds, false);
            }
        }

        Random random = new Random();
        int stx = random.nextInt(rows);
        int sty = random.nextInt(rows);
        int[] start = {stx,sty};
        
        Stack<int[]> S = new Stack<int[]>();
        S.push(start);
        all.add(start);
        map[start[1]][start[0]] = 0;

        // O(n^2)?? 
        while(!S.isEmpty()){
            int[] curr = S.pop();
            // get possible neighbors(separate method)
            ArrayList<int[]> spots = getNeigh(curr); 
            
            // randomize child list
            Collections.shuffle(spots);
            // O(1) spots size is always < 4
            for(int i=0; i<spots.size() ; i++){
                // get adjacent neighbors of spot(parent removel later).
                int uy = spots.get(i)[0]-1; int ux = spots.get(i)[1];
                int[] up = {uy, ux};
                int dy = spots.get(i)[0]+1; int dx = spots.get(i)[1];
                int[] down = {dy,dx}; 
                int ly = spots.get(i)[0]; int lx = spots.get(i)[1]-1; 
                int[] left = {ly,lx};
                int ry = spots.get(i)[0]; int rx = spots.get(i)[1]+1;
                int[] right =  {ry,rx};

                ArrayList<int[]> directions = new ArrayList<int[]>();
                // storing only in-bound and non-parent neighbors
                if(!Arrays.equals(up,curr) && up[0]>=0 && up[0]<rows && up[1]>=0 && up[1]<cols) directions.add(up);
                if(!Arrays.equals(down,curr) && down[0]>=0 && down[0]<rows && down[1]>=0 && down[1]<cols) directions.add(down);
                if(!Arrays.equals(left,curr) && left[0]>=0 && left[0]<rows && left[1]>=0 && left[1]<cols)directions.add(left);
                if(!Arrays.equals(right,curr) && right[0]>=0 && right[0]<rows && right[1]>=0 && right[1]<cols) directions.add(right);
                boolean pass = true;
                // list of each neighbor's pass status
                ArrayList<Boolean> passes = new ArrayList<Boolean>();
                int ps = 0;
                // O(1): directions is always < 4 in size;
                for(int[] elem : directions){
                    int[] dir = {elem[0],elem[1]};
                    // table.get() is O(1) lookup
                    if(table.get(Arrays.toString(dir))==true){
                        passes.add(ps,false);
                    }else{
                        passes.add(ps,true);
                    }  
                    ps++;                                       
                }
                // O(1)
                for(boolean el : passes){
                    if(el==false)pass=false;
                }
                int[] to_add = {spots.get(i)[0] , spots.get(i)[1]};
                if(pass){
                    S.push(spots.get(i));
                    table.put(Arrays.toString(to_add), true);
                    map[spots.get(i)[0]][spots.get(i)[1]] = 0;
                }
            }
        }
        this.maze = map;
              
    }

    public ArrayList<int[]> getNeigh(int[] curr){
        // get all non-visited adjacent in-bound neighbors
        ArrayList<int[]> pos = new ArrayList<int[]>(4);
        int uy = curr[0]-1;
        int ux = curr[1];
        int[] up = {uy, ux};
        if(uy>=0 ) if(!table.get(Arrays.toString(up))) pos.add(up);
        
        int dy = curr[0]+1;
        int dx = curr[1];
        int[] down = {dy,dx};
        if(dy<rows) if(!table.get(Arrays.toString(down))) pos.add(down);

        int ly = curr[0];
        int lx = curr[1]-1;
        int[] left = {ly,lx};
        if(lx>=0 ) if(!table.get(Arrays.toString(left))) pos.add(left);
            
        int ry = curr[0];
        int rx = curr[1]+1;
        int[] right =  {ry,rx};
        if(rx<cols) if(!table.get(Arrays.toString(right))) pos.add(right);
        
        return pos;
    }
}
