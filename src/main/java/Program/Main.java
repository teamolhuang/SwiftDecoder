package Program;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        // get input file folder
        String folder = Config.get("inputFileFolder");
        System.out.println("讀取目錄 : " + folder);

        // get possible swift files
        List<File> swiftFiles = IO.getSwiftFiles(folder);

        // split them into assigned formats
        Decoder.workOnSwifts(swiftFiles);
    }
}
