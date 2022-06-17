package Program;

import SwiftMessages.MTMessage;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

public class Decoder {

    public static void workOnSwifts(List<File> files)
    {
        final String hashLinePatternString = "^#$";
        final Pattern hashLinePattern = Pattern.compile(hashLinePatternString, Pattern.MULTILINE);

        for (File file : files)
        {
            String content = "";

            // 取得 file 的完整內容
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                lines.removeIf(s -> hashLinePattern.matcher(s).matches()); // 把只包含#的行去掉

                content = String.join("\n", lines);
            } catch (Exception e)
            {
                System.out.println(file.getName() + " 讀取失敗");
                System.out.println(e.getMessage());
            }

            // 用split方式處理，方便處理同檔多電文的情況
            String[] splits = content.split("\u0003"); // swift 結束符號

            for (int i = 0; i < splits.length; i++)
            {
                MTMessage mt = MTMessage.parse(splits[i]);

                System.out.println("===========================================");
                System.out.println(file.getName()  + " 中的電文 " + (i+1));
                System.out.println("===========================================");

                mt.getBasicHeaderBlock().outputAsString();
                mt.getApplicationHeaderBlock().outputAsString();
                mt.getUserHeaderBlock().outputAsString();

                mt.outputFields();

                mt.getTrailerBlock().outputAsString();
            }
        }

        System.out.println("===========================================");
    }

}
