/** Maze Class | Marko Sterbentz 4/16/2015
 *  This class contains the methods and variables relating to the creation
 *  of a Maze object.
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
public class Maze 
{
    private Cell[][] cells;
    private final int TOP = 0;
    private final int RIGHT = 1;
    private final int BOTTOM = 2;
    private final int LEFT = 3;
    
    public Maze(int mazeHeight, int mazeWidth)
    {
        cells = new Cell[mazeHeight][mazeWidth];
    }
    
    /** Generate Connected Maze | Marko Sterbentz 4/17/2015
     *  This method will alter the 'cells' variable to create a traversable maze.
     *  Note: The start cell is the top left cell, and the exit cell is the bottom left cell.
     *  Note: Every cell must be reachable.
     */
    public void generateConnectedMaze()
    {
        // 1. initialize cells, and special case start cell and exit cells
        initializeAllCells();
        initializeStartCell();
        initializeExitCell(); 
        
        // 2. create and initialize unvisited cell list
        ArrayList<Cell> unvisitedCells = new ArrayList();
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
                unvisitedCells.add(cells[i][j]);
        
        // 3. create visited cell list
        ArrayList<Cell> visitedCells = new ArrayList();
        
        Random rand = new Random();
        Cell currentCell = cells[0][0]; // start generating from the top left corner
        while(!unvisitedCells.isEmpty())
        {
            unvisitedCells.remove(currentCell);
            visitedCells.add(currentCell);           

            int wallOrientationIndex = rand.nextInt(4);
            boolean breakOccurred = false;
            // 4. Break down wall
            for(int i = 0 ; i < currentCell.walls.length && !breakOccurred; i++)
            {
                if(!isCellViable(currentCell, wallOrientationIndex, unvisitedCells))
                    wallOrientationIndex = (wallOrientationIndex + 1) % 4;
                else
                {
                    breakWall(currentCell, wallOrientationIndex);
                    currentCell = currentCell.adjacentCells[wallOrientationIndex]; 
                    breakOccurred = true;
                }
            }          
            // 5. If no viable wall to break, go to new cell
            if(!breakOccurred)
                currentCell = visitedCells.get(rand.nextInt(visitedCells.size()));                           
        }
    }
    
    /** Is Cell Viable | Marko Sterbentz 4/18/2015
     *  This method will return a boolean that is dependent upon whether the given
     *  Cell is viable to be broken out of to the given direction.
     *  Note: A cell is viable if it exists within the maze and is unvisited.
     */
    private boolean isCellViable(Cell cell, int direction, ArrayList<Cell> unvisitedCells)
    {
        if(cell.adjacentCells[direction] == null || !unvisitedCells.contains(cell.adjacentCells[direction]))
            return false;
        return true;           
    }
    
    /** Break Wall | Marko Sterbentz 4/18/2015
     *  This method will break down the wall in the given direction 
     *  of the given Cell.
     */
    private void breakWall(Cell cell, int direction)
    {
        cell.walls[direction] = false;
        cell.adjacentCells[direction].walls[(direction + 2) % 4] = false;
    }
    
    /** Initialize All Cells | Marko Sterbentz 4/18/2015
     *  This method will initialize all Cells in the cells matrix.
     */
    private void initializeAllCells()
    {
        // initialize cells
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
                cells[i][j] = new Cell();
        
        // initialize adjacentCell lists
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
            {
                if(i == 0)
                    cells[i][j].adjacentCells[TOP] = null;
                else
                    cells[i][j].adjacentCells[TOP] = cells[i - 1][j];
                
                if(j == 0)
                    cells[i][j].adjacentCells[LEFT] = null;
                else
                    cells[i][j].adjacentCells[LEFT] = cells[i][j - 1];
                
                if(i == cells.length - 1)
                    cells[i][j].adjacentCells[BOTTOM] = null;
                else 
                    cells[i][j].adjacentCells[BOTTOM] = cells[i + 1][j];
                
                if(j == cells[i].length - 1)
                    cells[i][j].adjacentCells[RIGHT] = null;
                else
                    cells[i][j].adjacentCells[RIGHT] = cells[i][j + 1];
            }
    }
    
    /** Initialize As Start Cell | Marko Sterbentz 4/17/2015
     *  This method will return a Cell that has the properties of a startCell.
     */
    private void initializeStartCell()
    {
        cells[0][0].walls[TOP] = false;
        cells[0][0].walls[LEFT] = false;
    }
    
    /** Initialize As Exit Cell | Marko Sterbentz 4/17/2015
     *  This method will return a Cell that has the properties of a exitCell.
     */
    private void initializeExitCell()
    {
        cells[cells.length - 1][cells[cells.length - 1].length - 1].walls[RIGHT] = false;
        cells[cells.length - 1][cells[cells.length - 1].length - 1].walls[BOTTOM] = false;        
    }
    
    /** Generate Minimal Maze | Marko Sterbentz 4/20/2015
     *  This method will generate a maze with a minimal amount of walls broken
     *  between the start and end cell.
     *  Note: Not all cells will be reachable.
     */
    public void generateMinimalMaze()
    {
        // 1. initialize cells, and special case start cell and exit cells
        initializeAllCells();
        initializeStartCell();
        initializeExitCell(); 
        
        // 2. create and initialize unvisited cell list
        ArrayList<Cell> unvisitedCells = new ArrayList();
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
                unvisitedCells.add(cells[i][j]);
        
        // 3. create visited cell list
        ArrayList<Cell> visitedCells = new ArrayList();
                     
        Random rand = new Random();
        Cell currentCell = cells[0][0]; // start generating from the top left corner
        Cell exitCell = cells[cells.length - 1][cells[cells.length - 1].length - 1];
        while(currentCell != exitCell)
        {
            unvisitedCells.remove(currentCell);
            visitedCells.add(currentCell);           

            int wallOrientationIndex = rand.nextInt(4);
            boolean breakOccurred = false;
            // 4. Break down wall
            for(int i = 0 ; i < currentCell.walls.length && !breakOccurred; i++)
            {
                if(!isCellViable(currentCell, wallOrientationIndex, unvisitedCells))
                    wallOrientationIndex = (wallOrientationIndex + 1) % 4;
                else
                {
                    breakWall(currentCell, wallOrientationIndex);
                    currentCell = currentCell.adjacentCells[wallOrientationIndex]; 
                    breakOccurred = true;
                }
            }          
            // 5. If no viable wall to break, go to new cell
            if(!breakOccurred)
                currentCell = visitedCells.get(rand.nextInt(visitedCells.size()));                           
        }       
    }
    
    /** Traverse Maze | Marko Sterbentz 4/19/2015
     *  This method will use a depth first search to find a path from 
     *  the first given Cell to the second given Cell.
     */
    public long traverseMaze(Cell startCell, Cell endCell, boolean drawPath)
    {
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
                cells[i][j].isVisited = false;
        
        ArrayList<Cell> cellPathToEnd = new ArrayList();
        ArrayList<Integer> pathToEnd; // this will store the directions from the startCell to the endCell
        Stack<Cell> stack = new Stack();
        Cell nextCell;
        Cell searchCell = startCell;
               
        long startTime = System.nanoTime();
        stack.push(searchCell);
        while(!stack.isEmpty())
        {
            searchCell = stack.peek();
            if(searchCell == endCell) // end cell has been found, return the path/directions
            {
                long endTime = System.nanoTime();
                //System.out.println((endTime - startTime)/1000 + " microseconds");
                while(!stack.isEmpty())               
                    cellPathToEnd.add(0, stack.pop());             
                pathToEnd = extractDirectionsFromCellPath(cellPathToEnd);
                if(drawPath)
                    StdDraw.drawPath(startCell, pathToEnd, StdDraw.RED);
                return ((endTime - startTime) / 1000);
            }                
            nextCell = getNextCellToVisit(searchCell);
            if(searchCell.isVisited)
            {
                if(nextCell != null)
                    stack.push(nextCell);
                else
                    stack.pop();
            }
            if(!searchCell.isVisited)
            {
                searchCell.isVisited = true;
                if(nextCell != null)
                    stack.push(nextCell); 
            }
        }
        return -1;
    }
      
    /** Get Next Cell To Visit | Marko Sterbentz 4/20/2015
     *  This method will return the next appropriate cell to visit.
     */
    private Cell getNextCellToVisit(Cell cell)
    {
        for(int i = 0; i < cell.adjacentCells.length; i++)
            if(cell.adjacentCells[i] != null &&!cell.adjacentCells[i].isVisited && !cell.walls[i])
                return cell.adjacentCells[i];
        return null;
    }
    
    /** Extract Directions From Cell Path | Marko Sterbentz 4/20/2015
     *  This method will return a list of directions that is dependent upon the 
     *  cells that are along the given cell path.
     */
    private ArrayList<Integer> extractDirectionsFromCellPath(ArrayList<Cell> cellPathToEnd)
    {
        ArrayList<Integer> directions = new ArrayList();     
        for(int i = 0; i < cellPathToEnd.size() - 1; i++)
            for(int j = 0; j < cellPathToEnd.get(i).adjacentCells.length; j++)
                if(cellPathToEnd.get(i).adjacentCells[j] == cellPathToEnd.get(i + 1))
                    directions.add(j);                      
        return directions;
    }
    
    /** Get Cells | Marko Sterbentz 4/17/2015
     *  This method will return the class variable cells.
     */
    public Cell[][] getCells()
    {
        return cells;
    }
}


