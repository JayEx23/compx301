import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

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
            // String fName = new String("run");
            merge(numFiles, fName);
        } catch (Exception x) {
            System.out.println(x);
            x.printStackTrace();
        }
    }
    
    // Merge temporary run files
    public static void merge(int numFiles, String fName) throws Exception {
        int[] activeReaders = new int[numFiles];

        // Create output file for each file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            activeReaders[fNum] = 1; // Give reader active flag

            BufferedWriter bf = new BufferedWriter(new FileWriter("./" + fName + ((fNum+1)+numFiles)));
            bf.close();
        }

        // Initialize required references for temp files
        int[][] fileNums = new int[2][numFiles];
        for (int i = 0; i < numFiles; i++) {
            fileNums[0][i] = (i+1);
            fileNums[1][i] = (i+1)+numFiles;
        }

        int outputSetIndex = 1;
        int outputFileIndex = 0;

        int inputSetIndex = 0;

        // Merge runs from each file until until all runs merged into one
        int mergedFiles = numFiles;
        while (mergedFiles > 1) {
            BufferedReader[] readers = new BufferedReader[numFiles];
            for (int i = 0; i < numFiles; i++) {
                readers[i] = new BufferedReader(new FileReader("./" + fName + fileNums[inputSetIndex][i]));
            }
            // Process a run from each file together
            boolean allRunsMerged = false;
            mergedFiles = 0;
            // Merge all runs from each file
            while (!allRunsMerged) {
                // Create merge minheap
                MyMergeMinHeap mergeMinHeap = new MyMergeMinHeap(numFiles);

                // Read intial values from each initial run into heap
                for (int rNum = 0; rNum < numFiles; rNum++) {
                    // Insert line from run and filename into heap
                    String value = readers[rNum].readLine();
                    String source = fName + fileNums[inputSetIndex][rNum];
                    if (value != null) {
                        mergeMinHeap.insert(new String[] {value, source});
                    }
                }

                // Get smallest value from heap
                String value = mergeMinHeap.peek();
                String source = mergeMinHeap.peekSource();
                if (value == null) {
                    allRunsMerged = true;
                    break;
                }
                mergedFiles ++;
                int fNum = calcReadFileNum(source);

                // Get current output run file 
                BufferedWriter writer = new BufferedWriter(new FileWriter(fName + fileNums[outputSetIndex][outputFileIndex], true));
                // Merge one run from each file
                while (mergeMinHeap.getSize() > 0) {
                    int inputFileIndex = fNum-1;
                    if (inputSetIndex == 1) {
                        inputFileIndex = inputFileIndex - numFiles;
                    }
                    if (activeReaders[inputFileIndex] == 1) {
                        if (value == null) {
                            activeReaders[inputFileIndex] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                            allRunsMerged = true;
                            break;
                        } else if (value.equals("{RUN}")) {
                            activeReaders[inputFileIndex] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                        } else {
                            // Output the smallest value
                            writer.write(value + "\n");
                            // Raplce value in heap with next value from same file
                            mergeMinHeap.replace(new String[] {readers[inputFileIndex].readLine(), fName + fNum});
                        }

                        // Output smallest value to output next
                        value = mergeMinHeap.peek();
                        source = mergeMinHeap.peekSource();
                        fNum = calcReadFileNum(source);
                    } else {
                        break;
                    }
                }

                // Reset active readers
                for (int i = 0; i < activeReaders.length; i++) {
                    activeReaders[i] = 1;
                }

                // Setup output file index for next run
                writer.write("{RUN}" + "\n");
                outputFileIndex ++;
                if (outputFileIndex > numFiles-1) {
                    outputFileIndex = 0;
                }

                writer.flush();
                writer.close();
            }

            // Close readers
            for (int i = 0; i < numFiles; i++) {
                readers[i].close();
            }

            // Clear merged files to be used as output for next loop
            for (int i = 0; i < numFiles; i++) {
                FileWriter fw = new FileWriter(fName + fileNums[inputSetIndex][i], false);
                fw.flush();
                fw.close();
            }

            // Toggle input and output set indices between 0 and 1 for next set of files to be used as input/output files
            outputSetIndex = 1 - outputSetIndex;
            inputSetIndex = 1 - inputSetIndex;

            outputFileIndex = 0; // Reset output file index
        }

        // Send final sorted lines to standard output
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader reader = new BufferedReader(new FileReader("./" + fName + fileNums[inputSetIndex][0]));

        String line = reader.readLine();
        while (line != null && !line.equals("{RUN}")) {
            writer.write(line + "\n");
            line = reader.readLine();
        }
        writer.flush();
        writer.close();
        reader.close();

        // Cleanup files
        for (int i = 1; i <= numFiles*2; i++) {
            File file = new File("./" + fName + i);
            file.delete();
        }
    }

    //
    // Calculate and return read file Num
    //
    public static int calcReadFileNum(String source) {
        String num = source.replaceAll("[^0-9]", "");
        int fNum = Integer.parseInt(num);
        return fNum;
    }
}
