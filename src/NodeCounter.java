/*
 * @Author Andrew Bergey
 * 
 * 
 * This class implements a simple counter that is used to keep track of how many recursive calls each search method makes 
 * 
 */


public class NodeCounter {
    
    private int PC2NodeCount;
    private int MRVNodeCount;

    //Constructor
    public NodeCounter(){
        PC2NodeCount = 0;
        MRVNodeCount = 0;
    }

    //incrementes the node counter for the PC2 search
    public void incrementPC2Count(){
        PC2NodeCount++;
    }
    
    //increments the node counter for the MRV search
    public void MRVNodeCount(){
        MRVNodeCount++;
    }

    public int getPC2Count(){
        return PC2NodeCount;
    }

    public int getMRVCount(){
        return MRVNodeCount;
    }


}
