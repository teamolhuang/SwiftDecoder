package Program;

import SwiftMessages.MTMessage;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

public class Decoder {

    static final String hashLinePatternString = "^#$";
    static final Pattern hashLinePattern = Pattern.compile(hashLinePatternString, Pattern.MULTILINE);

    public static void workOnSwifts(List<File> files)
    {
        for (File file : files)
        {
            String content = "";

            try {
                List<String> lines = Files.readAllLines(file.toPath());
                lines.removeIf(s -> hashLinePattern.matcher(s).matches()); // 把只包含#的行去掉

                content = String.join("\n", lines);
            } catch (Exception e)
            {
                System.out.println(file.getName() + " failed!");
                System.out.println(e.getMessage());
            }

            // 如果是多次電文，則用split方式處理
            String[] splits = null;
            if (content.indexOf("\u0001", 1) != -1) // swift 開始符號
            {
                splits = content.split("\u0003"); // swift 結束符號
            }

            if (splits != null)
            {
                for (int i = 0; i < splits.length; i++)
                {
                    System.out.println("===========================================");
                    System.out.println(file.getName()  + " 子電文 " + (i+1));
                    System.out.println("===========================================");
                    MTMessage mt = MTMessage.parse(splits[i]);
                    if (mt != null)
                        mt.outputAsString();
                }
            } else {
                System.out.println("===========================================");
                System.out.println(file.getName());
                System.out.println("===========================================");
                MTMessage mt = MTMessage.parse(content);
                if (mt != null)
                    mt.outputAsString();
            }
        }
        System.out.println("===========================================");
    }

}
