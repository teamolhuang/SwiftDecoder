package Program.SwiftMessages;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

    public static List<String> getSwiftContents(Path path)
    {
        final String hashLinePatternString = "^#$";
        final Pattern hashLinePattern = Pattern.compile(hashLinePatternString, Pattern.MULTILINE);

        List<String> result = new ArrayList<>();

        // 取得 file 的完整內容
        try {
            List<String> lines = Files.readAllLines(path);
            lines.removeIf(s -> hashLinePattern.matcher(s).matches()); // 把只包含#的行去掉

            String content = String.join("\n", lines);

            // 用split方式處理，方便處理同檔多電文的情況
            String[] splits = content.split("\u0003"); // swift 結束符號
            result.addAll(Arrays.asList(splits));
        } catch (Exception e)
        {
            System.out.println(path.getFileName() + " 讀取失敗");
            System.out.println(e.getMessage());
        }

        return result;
    }

}
