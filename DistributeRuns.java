import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

public class DistributeRuns {
    int _fileNum;

    // Constructor for DistributeRuns
    public DistributeRuns(int fileNum) {
        if(fileNum <= 1) {
            _fileNum = 2;
        } else {
            _fileNum = fileNum;
        }
    }

    public void createFile() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File file = null;
        FileWriter fw = null;
        String fileName = "run";
        int fileFlag = 1;
        try{
            reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();

            // create num of files specified
            for(int i = 1; i <= _fileNum; i++) {
                file = new File(fileName + i);
                if(!file.exists()) {
                    file.createNewFile();
                }
            }

            // while there are still lines to read
            while(line != null) {
                // check if we are not at the end of a run
                if(!line.equals("{RUN}")) {
                    // check if we are in the right file
                    if(fileFlag <= _fileNum) {
                        fw = new FileWriter(file);
                        writer = new BufferedWriter(fw);
                    } 
                    else { // reset fileFlag back to start
                        fileFlag = _fileNum;
                    }

                } 
                else if(line.equals("{RUNS}")) {fileFlag++;}
            }

        } catch (Exception e) {
            System.out.println(e);
        }



    }
}
