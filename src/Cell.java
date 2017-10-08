/** Cell Class | Marko Sterbentz 4/16/2015
 *  This class/pseudo-struct contains the variables and methods 
 *  relating to the creation of a cell object in a maze.
 */
public class Cell 
{
    boolean[] walls;
    Cell[] adjacentCells; // note will only contain 4 cells: the top, right, bottom, and left Cells from this cell
    boolean isVisited;
    
    public Cell()
    {
        walls = new boolean[4];
        for(int i = 0; i < walls.length; i++)
            walls[i] = true;
        
        adjacentCells = new Cell[4];
        
        isVisited = false;
    }    
}
