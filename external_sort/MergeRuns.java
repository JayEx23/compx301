//
// Names:   Jesse Reyneke-Barnard   ,   Eugene Chew
// IDs:     1351388                 ,   1351553
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

//
// This class should use temp files from DistributeRuns and create extra temp files
// to merge all runs into a single stream into standard output
//
public class MergeRuns {
    //
    // Program entry point, calls all other methods from here
    // This method takes in 'args' which are mapped to command line arguments
    //
    public static void main(String[] args) {
        if (args.length > 1) { // Check that the correct args are passed in
            System.out.println("Usage: java MergeRuns <number of files>");
            return;
        }
        try {
            // use DistributeRuns to distribute runs across specified amount of files
            int numFiles = Integer.parseInt(args[0]);
            DistributeRuns dr = new DistributeRuns(numFiles);
            dr.createFile();

            // Get filename format from DistributeRuns and begin file merge
            String fName = new String(dr.getFilePrefix());
            merge(numFiles, fName);
        } catch (NumberFormatException nfe) {
            System.err.println("Usage: java MergeRuns <number of files>");
        } catch (Exception x) {
            System.err.println(x);
            x.printStackTrace();
        }
    }

    //
    // Merge temporary run files into one single run in single file, and output to
    // standard output
    // Takes in numFiles: specified number of distributed files, fName: temp file
    // name standard
    //
    public static void merge(int numFiles, String fName) throws Exception {
        int[] activeReaders = new int[numFiles];

        // Create output file for each dsitributed file
        for (int fNum = 0; fNum < numFiles; fNum++) {
            activeReaders[fNum] = 1; // Give reader active flag
            // Create output file
            BufferedWriter bf = new BufferedWriter(new FileWriter("./" + fName + ((fNum + 1) + numFiles)));
            bf.close();
        }

        // Initialize required references for temp files
        int[][] fileNums = new int[2][numFiles];
        for (int i = 0; i < numFiles; i++) {
            fileNums[0][i] = (i + 1);
            fileNums[1][i] = (i + 1) + numFiles;
        }
        // Initialize required tempfile reference indexes
        int outputSetIndex = 1;
        int outputFileIndex = 0;
        int inputSetIndex = 0;

        // Merge runs from each file until until all runs merged into one file, thus a
        // single run
        int filesWithRunsRemaining = numFiles;
        while (filesWithRunsRemaining > 1) {
            // Initialize numFiles amount of readers for each input file
            BufferedReader[] readers = new BufferedReader[numFiles];
            for (int i = 0; i < numFiles; i++) {
                readers[i] = new BufferedReader(new FileReader("./" + fName + fileNums[inputSetIndex][i]));
            }
            boolean runPerFileMerged = false;
            filesWithRunsRemaining = 0;
            // Merge all runs from each file
            while (!runPerFileMerged) {
                // Create merge minheap
                MyMergeMinHeap mergeMinHeap = new MyMergeMinHeap(numFiles);
                // Read intial value from each file initial run into heap
                for (int rNum = 0; rNum < numFiles; rNum++) {
                    String value = readers[rNum].readLine();
                    String source = fName + fileNums[inputSetIndex][rNum];
                    if (value != null) {
                        // Insert line from run and filename into heap
                        mergeMinHeap.insert(new String[] { value, source });
                    }
                }

                // Get smallest value from heap
                String[] peek = mergeMinHeap.peek();
                String value = peek[0];
                String source = peek[1];
                if (value == null) {
                    runPerFileMerged = true;
                    break;
                }
                filesWithRunsRemaining++; // increment number of files used to store runs in
                int fNum = calcReadFileNum(source);

                // Get current output file to output merged runs into
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(fName + fileNums[outputSetIndex][outputFileIndex], true));
                // Merge one run from each file
                while (mergeMinHeap.getSize() > 0) {
                    int inputFileIndex = fNum - 1;
                    if (inputSetIndex == 1) {
                        // Adjust indexes accordingly when using output files as input
                        // e.g. when input files are run4, run5, run6 instead of run1, run2, run3
                        inputFileIndex = inputFileIndex - numFiles;
                    }
                    if (activeReaders[inputFileIndex] == 1) { // Check if reader still has values from the run
                        if (value == null) {
                            activeReaders[inputFileIndex] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                            runPerFileMerged = true;
                            break;
                        } else if (value.equals("{RUN}")) {
                            activeReaders[inputFileIndex] = 0; // Give reader inactive flag
                            mergeMinHeap.remove();
                        } else {
                            // Output the smallest value
                            writer.write(value + "\n");
                            // Raplce value in heap with next value from same origin run, thus the same file
                            mergeMinHeap.replace(new String[] { readers[inputFileIndex].readLine(), fName + fNum });
                        }

                        // Get the next smallest value from min heap for the next loop
                        peek = mergeMinHeap.peek();
                        value = peek[0];
                        source = peek[1];
                        fNum = calcReadFileNum(source);
                    } else {
                        break;
                    }
                }

                // Reset active readers
                for (int i = 0; i < activeReaders.length; i++) {
                    activeReaders[i] = 1;
                }

                writer.write("{RUN}" + "\n"); // Denote end of run (merged run)
                // Setup output file index for next run
                outputFileIndex++;
                if (outputFileIndex > numFiles - 1) {
                    outputFileIndex = 0;
                }

                writer.flush();
                writer.close();
            }

            // Close readers
            for (int i = 0; i < numFiles; i++) {
                readers[i].close();
            }

            // Clear 'already merged runs' files to be used as output for next loop
            for (int i = 0; i < numFiles; i++) {
                FileWriter fw = new FileWriter(fName + fileNums[inputSetIndex][i], false);
                fw.flush();
                fw.close();
            }

            // Toggle input and output set indexes between 0 and 1 for next set of files to
            // be used as input/output files
            outputSetIndex = 1 - outputSetIndex;
            inputSetIndex = 1 - inputSetIndex;

            outputFileIndex = 0; // Reset output file index
        }
        outputToStandardOutput(fileNums, inputSetIndex, fName);
        cleanTempFiles(numFiles, fName);
    }

    //
    // Output the ultimate merged run in file to standard output
    //
    public static void outputToStandardOutput(int[][] fileNums, int inputSetIndex, String fName) throws Exception {
        // Send final sorted lines to standard output
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader reader = new BufferedReader(new FileReader("./" + fName + fileNums[inputSetIndex][0]));

        String line = reader.readLine();
        while (line != null && !line.equals("{RUN}")) {
            writer.write(line + "\n"); // Keep reading and writing lines
            line = reader.readLine();
        }
        writer.flush();
        writer.close();
        reader.close();
    }

    //
    // Cleanup temp files
    // Takes in numFiles: number of specified temp files to distribute runs over
    // and fName: format of temp file names
    //
    public static void cleanTempFiles(int numFiles, String fName) {
        // Cleanup temp files
        for (int i = 1; i <= numFiles * 2; i++) {
            File file = new File("./" + fName + i);
            file.delete(); // Delete temp file
        }
    }

    //
    // Calculate and return read file Num
    // Takes in source: file destiation of input run value
    // Returns: The number as integer of the source file name
    //
    public static int calcReadFileNum(String source) {
        String num = source.replaceAll("[^0-9]", ""); // Filter only the number from the string
        int fNum = Integer.parseInt(num);
        return fNum;
    }
}
