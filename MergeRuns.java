import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class MergeRuns {

    public static void main(String[] args) {
        if(args.length > 1) {
            System.out.println("Usage: java MergeRuns <number of files>");
            return;
        }
        try {
            int numFiles = Integer.parseInt(args[0]);
            DistributeRuns dr = new DistributeRuns(numFiles);
            dr.createFile();

            String fName = new String(dr.getFilePrefix());
            merge(numFiles, fName);
        } catch (Exception x) {
            System.out.println(x);
        }
    }
    
    // Merge temporary run files
    public static void merge(int numFiles, String fName) throws Exception {
        BufferedReader[] readers = new BufferedReader[numFiles];
        // Create file reader for each file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            readers[fNum] = new BufferedReader(new FileReader("./" + fName + fNum)); // Assume file is in current directory
        }
        // Create output file for each file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            new BufferedWriter(new FileWriter("./" + fName + (numFiles + fNum)));
        }
        
        // Create merge minheap
        MyMergeMinHeap mergeMinHeap = new MyMergeMinHeap(numFiles);

        int currentRunFile = 0;
        while (true) {
            // Read lines from files
            for (int rNum = 0; rNum < readers.length; rNum++) {
                // Insert line from run and filename into heap
                mergeMinHeap.insert(new String[] {readers[rNum].readLine(), fName + rNum});
            }

            // Get current output run file 
            BufferedWriter writer = new BufferedWriter(new FileWriter(fName + (currentRunFile + numFiles), true));

            // Output smallest value into output run file
            mergeMinHeap.peek();
        }
    }
}
