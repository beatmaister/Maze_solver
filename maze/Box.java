package maze;

public class Box {
    public Box parent;
    public int x;
    public int y;
    public Box(int x,int y, Box p){
        this.x=x;
        this.y=y;
        this.parent = p;
    }
}
