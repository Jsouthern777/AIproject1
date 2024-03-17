public class NodeCounter {
    
    private int PC2NodeCount;
    private int MRVNodeCount;

    //Constructor
    public NodeCounter(){
        PC2NodeCount = 0;
        MRVNodeCount = 0;
    }


    public void incrementPC2Count(){
        PC2NodeCount++;
    }
    
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
