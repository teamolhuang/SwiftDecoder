package Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IO {

    public static List<File> getSwiftFiles(String folderPath)
    {
        List<File> resultList = new ArrayList<>();
        try
        {
            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles == null)
                throw new Exception("listOfFiles is null!");

            for (File file : listOfFiles)
            {
                if (file.isFile() && file.getAbsolutePath().toLowerCase().endsWith(".swi"))
                {
                    resultList.add(file);
                }
            }
        } catch (Exception e)
        {
            System.out.println("getSwiftFiles failed!");
            System.out.println(e.getMessage());
        }

        return resultList;
    }

}
