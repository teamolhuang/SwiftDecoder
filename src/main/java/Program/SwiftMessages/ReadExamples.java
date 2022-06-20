package Program.SwiftMessages;

import java.io.File;
import java.util.List;

public class ReadExamples {
    private static void swiftMessage()
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
