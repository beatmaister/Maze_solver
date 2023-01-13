package maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class DFS {
    public static Queue<Box> q = new LinkedList<Box>();
    public static HashMap<String,Boolean> map_seen = new HashMap<String,Boolean>();
    public static void searchPath(int[][] maze, int x, int y, ArrayList<Integer> path) {
        q.add(new Box(x,y,null));

        while(!q.isEmpty()) {
            Box p = q.poll();
            String ps = Integer.toString(p.x) + "," + Integer.toString(p.y);
            map_seen.put(ps, true);

            if (maze[p.y][p.x] == 9) {
                System.out.println("target found! ");
                getPath(p, maze, path);
                q.clear();
                map_seen.clear();
                return;
            }

            if(isFree(maze, p.x+1,p.y)) {                            
                String nextS= Integer.toString(p.x+1) + "," + Integer.toString(p.y);
                if(!map_seen.containsKey(nextS)) {
                    Box nextP= new Box(p.x+1,p.y,p);
                    q.add(nextP);
                }
            }

            if(isFree(maze, p.x-1,p.y)) {                              
                String nextS= Integer.toString(p.x-1) + "," + Integer.toString(p.y);
                if(!map_seen.containsKey(nextS)) {
                    Box nextP= new Box(p.x-1,p.y,p);
                    q.add(nextP);
                }
            }

            if(isFree(maze, p.x,p.y+1)) {                               
                String nextS= Integer.toString(p.x) + "," + Integer.toString(p.y+1);
                if(!map_seen.containsKey(nextS)) {
                    Box nextP= new Box(p.x,p.y+1,p);
                    q.add(nextP);
                }
            }

            if(isFree(maze, p.x,p.y-1)) {                
                String nextS= Integer.toString(p.x) + "," + Integer.toString(p.y-1);
                if(!map_seen.containsKey(nextS)) {
                    Box nextP= new Box(p.x,p.y-1,p);
                    q.add(nextP);
                }
            }
        }
        
        System.out.println("exited reached");
    }

    public static boolean isFree(int[][] maze, int x, int y) {
        if((x >= 0 && x < maze[0].length) && (y >= 0 && y < maze.length) && (maze[y][x] == 0 || maze[y][x] == 9)) {
            return true;
        }
        return false;        
    }

    public static int[][] getPath(Box node, int[][] maze, ArrayList<Integer> path){
        while(node!=null){
            path.add(node.x);
            path.add(node.y);
            node = node.parent;
        }
        map_seen = new HashMap<String,Boolean>();
        return maze;
    }    
}
