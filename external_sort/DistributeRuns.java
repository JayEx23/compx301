//
// Names:   Jesse Reyneke-Barnard   ,   Eugene Chew
// IDs:     1351388                 ,   1351553
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;

//
//  This class should read standard input from CreateRuns and output the number of files specified
//
public class DistributeRuns {
    int _fileNum;

    // Constructor for DistributeRuns
    public DistributeRuns(int fileNum) {
        if (fileNum <= 1) {
            _fileNum = 2;
        } else {
            _fileNum = fileNum;
        }
    }

    public String getFilePrefix() {
        return "run";
    }

    //
    // A method that creates the file specified
    //
    public void createFile() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        FileWriter fw = null;
        String fileName = "run";
        int fileFlag = 1;

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();

            // create num of files specified
            for (int i = 1; i <= _fileNum; i++) {
                writer = new BufferedWriter(new FileWriter(fileName + i));
                writer.close();
            }
            fw = new FileWriter(fileName + fileFlag);
            writer = new BufferedWriter(fw);
            // while there are still lines to read
            while (line != null) {

                // check if we are not at the end of a run
                if (!line.equals("{RUN}")) {
                    // check if we are in the right file
                    writer.write(line + "\n");
                    writer.flush();
                } else { // if we are at the end of the run
                    writer.write("{RUN}" + "\n");
                    writer.flush();
                    if (fileFlag < _fileNum) { // check if the fileflag is less than the number of file specified
                        fileFlag++;
                    } else { // reset fileFlag back to start
                        fileFlag = 1;
                    }
                    fw = new FileWriter(fileName + fileFlag, true);
                    writer = new BufferedWriter(fw);

                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
