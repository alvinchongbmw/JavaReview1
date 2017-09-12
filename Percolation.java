/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *  Dependencies: WeightedQuickUnionUF
 *
 *  This program take arg from constructor
 * 
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte BLOCK = 0;
    private static final byte OPEN = 1;    
    private static final byte BOTTOM = 2;
    private static final byte FULL = 3;
    private static final byte BOTTOMFULL = 4;
    private byte[] grid;
    private final int size;
    private int numOpen = 0;
    private boolean isPercolation = false;
    
    private final WeightedQuickUnionUF connectionMap;

    /**
     *    Constructor for class Percolation
     *    
     *    Create the grid and initialize the variables
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Illegal Args in Percolation()");
        grid = new byte[n * n];
        connectionMap = new WeightedQuickUnionUF(n * n);
        size = n;
    }
    
    /**
     *    Open the given site and increment number of open by one.
     *    
     *    Call method to connect the newly open site with its neightbor
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            setOpenAndConnectNeightbor(row, col);
            numOpen++;
        }
    }
    
    /**
     *    Check if the given site is Open or not
     * 
     *    ALL status "FULL, OPEN, BOTTOM, BOTTOMFULL" are Open
     *    except "BLOCK"
     */
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("Illegal Argument isOpen()");
        int index = getConnectionIndex(row, col);
        return (grid[index] > BLOCK);
    }

    /**
     *    Check if the given site is connected to the top
     * 
     *    Can just return the root status, but instead
     *    everytime we call and get the root status,
     *    we also update the current site, so next time
     *    when its call, it can retrive faster
     */    
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) 
            throw new IllegalArgumentException("Illegal Argument isFull()");
        int index = getConnectionIndex(row, col);
        int rootIndex = connectionMap.find(index);
        if (grid[index] == FULL || grid[index] == BOTTOMFULL)
            return true;
        else {
            if (grid[rootIndex] == FULL || grid[rootIndex] == BOTTOMFULL)
                grid[index] = grid[rootIndex];
        }        
        return grid[index] == FULL || grid[index] == BOTTOMFULL;
    }
    
    /**
     *    Return number or site that's already open
     */
    public int numberOfOpenSites() {
        return numOpen;
    }
    
    /**
     *    Determinte if it is Percolates
     *    The variable isPercolation is set inside
     *    the "setOpenAndConnectNeightbor" method
     *    if it is connected to top and bottom, it will set to true
     */
    public boolean percolates() {
        return isPercolation;
    }
    
    /**
     *    Validate the input make sure it is within range
     *    Throw exception otherwise
     */
    private boolean isValid(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size)
            return false;
        return true;            
    }
    
    /**
     *    Check the neighbor site
     *    union them and set the corresponding status.
     *    each status is given a Percedence
     *    save it to the hightest percednce when unioning the neightbor
     *    also check if it is percolate or not by checking if 
     *    we unioning FULL to BOTTOM
     */
    private void setOpenAndConnectNeightbor(int row, int col) {
        int index = getConnectionIndex(row, col);
        int tempIndex = 0;        // index for grid and union map
        int rootIndex = 0;        // index for root of the neightbor
        byte status;              // status of the current site
        byte rootStatus;          // status of the root site of its neightbor
        int[] x = {-1, 1, 0, 0};  // neightbor in x[] so we can use for loop
        int[] y = {0, 0, -1, 1};  // neightbor in y[] so we can use for loop
                
        if (row == 1 && row == size)
            status = BOTTOMFULL;
        else if (row == 1) 
            status = FULL;
        else if (row == size)
            status = BOTTOM;
        else 
            status = OPEN;
        grid[index] = status;        
        // check all neightbor        
        for (int i = 0; i < x.length; i++) {
            if (row+x[i] >= 1 && row+x[i] <= size &&
                col+y[i] >= 1 && col+y[i] <= size) {
                if (isOpen(row + x[i], col + y[i])) { 
                    tempIndex = getConnectionIndex(row + x[i], col + y[i]); 
                    rootIndex = connectionMap.find(tempIndex);
                    rootStatus = grid[rootIndex];
                    if ((status == BOTTOM || rootStatus == BOTTOM) &&
                        (status == FULL || rootStatus == FULL))
                        status = BOTTOMFULL;
                    if (rootStatus > status)
                        status = rootStatus;
                    connectionMap.union(index, tempIndex);                   
                }
            }
        }
        if (status == BOTTOMFULL)
            isPercolation = true;       
        grid[connectionMap.find(index)] = status;
    }
        
    /**
     *   Convert 2D indexing to 1D indexing.
     *   Notices there are no out of bound check.
     *   function that call "getConnectionIndex" must use 
     *   isValid to check for valid input before calling this function
     */
    private int getConnectionIndex(int row, int col) {
        int index = (row-1) * size + col-1;
        return index;
    }
        
    public static void main(String[] args) {
        int size = 4;
        Percolation test = new Percolation(size);
        test.open(4, 1);
        test.open(3, 1);
        test.open(2, 1);
        test.open(1, 1);
        test.open(1, 4);
        test.open(2, 4);
        test.open(4, 4);
        System.out.println(test.isFull(4, 4));  
    }
}