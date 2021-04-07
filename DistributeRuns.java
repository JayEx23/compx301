import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;

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
        
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
            
        } catch (Exception e) {
            System.out.println(e);
        }



    }
}
