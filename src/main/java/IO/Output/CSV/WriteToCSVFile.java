package IO.Output.CSV;

import Objects.Staff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteToCSVFile {
    private static final String FilePath = "src/main/resources/tests/";

    public static void Write(List list, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FilePath + fileName));

        for(Object object : list) {
            String out = object.toString();
            writer.write(out + "\n");
        }

        writer.close();
    }
}