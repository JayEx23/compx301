import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

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
            x.printStackTrace();
        }
    }
    
    // Merge temporary run files
    public static void merge(int numFiles, String fName) throws Exception {
        BufferedReader[][] readers = new BufferedReader[2][numFiles];
        int[] activeReaders = new int[numFiles];

        // Create output file for each file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            new BufferedWriter(new FileWriter("./" + fName + ((fNum+1)+numFiles)));

            activeReaders[fNum] = 1; // Give reader active flag

            // Create file reader for each file
            readers[0][fNum] = new BufferedReader(new FileReader("./" + fName + (fNum+1))); // Assume file is in current directory
            readers[1][fNum] = new BufferedReader(new FileReader("./" + fName + ((fNum+1)+numFiles))); // Assume file is in current directory
        }

        // Initialize required references for temp files
        int[][] fileNums = new int[2][numFiles];
        for (int i = 0; i < fileNums[0].length; i++) {
            fileNums[0][i] = (i+1);
        }
        for (int i = 0; i < fileNums[1].length; i++) {
            fileNums[1][i] = (i+1)+numFiles;
        }

        int outputSetIndex = 1;
        int outputFileIndex = 0;

        int inputSetIndex = 0;

        // Merge all runs
        int filesLeft = 0;
        while (filesLeft != 1) {
            // Process a run from each file together
            boolean allFilesDone = false;
            filesLeft = 0;
            while (!allFilesDone) {
                // Create merge minheap
                MyMergeMinHeap mergeMinHeap = new MyMergeMinHeap(numFiles);

                // Read intial values from each initial run into heap
                for (int rNum = 0; rNum < readers[inputSetIndex].length; rNum++) {
                    // Insert line from run and filename into heap
                    String value = readers[inputSetIndex][rNum].readLine();
                    String source = fName + fileNums[inputSetIndex][rNum];
                    if (value != null) {
                        mergeMinHeap.insert(new String[] {value, source});
                    }
                }

                // Get smallest value from heap
                String value = mergeMinHeap.peek();
                String source = mergeMinHeap.peekSource();
                if (value == null) {
                    break;
                }
                int fNum = calcReadFileNum(source);

                // Get current output run file 
                BufferedWriter writer = new BufferedWriter(new FileWriter(fName + fileNums[outputSetIndex][outputFileIndex], true));
                while (mergeMinHeap.getSize() > 0) {        
                    if (activeReaders[fNum-1] == 1) {
                        if (value == null) {
                            activeReaders[fNum-1] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                        } else if (value.equals("{RUN}")) {
                            activeReaders[fNum-1] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                        } else {
                            // Output the smallest value
                            System.out.println(value);
                            writer.write(value + "\n");
                            // Raplce value in heap with next value from same file
                            mergeMinHeap.replace(new String[] {readers[inputSetIndex][fNum-1].readLine(), fName + fNum});
                        }

                        // Output smallest value to output next
                        value = mergeMinHeap.peek();
                        source = mergeMinHeap.peekSource();
                        fNum = calcReadFileNum(source);
                    } else {
                        break;
                    }
                }

                // Setup output file index for next run
                writer.write("{RUN}" + "\n");
                outputFileIndex ++;
                if (outputFileIndex > numFiles-1) {
                    outputFileIndex = 0;
                }

                writer.flush();
                writer.close();

                // Check if this run is done
                allFilesDone = true;
                for (int i = 0; i < activeReaders.length; i++) {
                    if (activeReaders[i] == 0) {
                        allFilesDone = false;
                    }
                }
                filesLeft ++;
            }
            // Reset active readers
            for (int i = 0; i < activeReaders.length; i++) {
                activeReaders[i] = 1;
            }

            // Toggle input and output set indices between 0 and 1 for next run
            outputSetIndex = 1 - outputSetIndex;
            inputSetIndex = 1 - inputSetIndex;
        }
    }

    //
    // Calculate and return read file Num
    //
    public static int calcReadFileNum(String source) {
        int fNum = Integer.parseInt(source.substring(source.length() - 1));
        return fNum;
    }
}
