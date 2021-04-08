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
        int[] activeReaders = new int[numFiles];
        // Create file reader for each file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            readers[fNum] = new BufferedReader(new FileReader("./" + fName + (fNum+1))); // Assume file is in current directory
            activeReaders[fNum] = 1; // Give reader active flag
        }
        // Create output file for each file
        for (int fNum = 1; fNum <= numFiles; fNum++) {
            new BufferedWriter(new FileWriter("./" + fName + (numFiles + fNum)));
        }
        
        // Create merge minheap
        MyMergeMinHeap mergeMinHeap = new MyMergeMinHeap(numFiles);

        // Read intial values from each initial run into heap
        for (int rNum = 0; rNum < readers.length; rNum++) {
            // Insert line from run and filename into heap
            mergeMinHeap.insert(new String[] {readers[rNum].readLine(), fName + rNum});
        }

        // Get smallest value from heap
        String value = mergeMinHeap.peek();
        String source = mergeMinHeap.peekSource();
        int  fNum = Integer.parseInt(source.substring(source.length() - 1, source.length()));

        int currentRunFile = 0;

        // Get current output run file 
        BufferedWriter writer = new BufferedWriter(new FileWriter(fName + (currentRunFile + numFiles), true));
        while (true) {
            if (value == "{RUN}") {
                activeReaders[fNum] = 0; // Give reader inactive flag
            }
            
            if (activeReaders[fNum] == 1) {
                // Output the smallest value
                writer.write(value);

                // Raplce value in heap with next value from same file
                mergeMinHeap.replace(new String[] {readers[fNum].readLine(), fName + fNum});

                // Output smallest value to output next
                value = mergeMinHeap.peek();
                source = mergeMinHeap.peekSource();
                fNum = Integer.parseInt(source.substring(source.length() - 1, source.length()));
            }

            boolean done = true;
            for (int i = 0; i < activeReaders.length; i++) {
                if (activeReaders[i] == 1) {
                    done = false;
                }
            }
            if (done) {
                break;
            }
        }
        writer.flush();
        writer.close();
    }
}
