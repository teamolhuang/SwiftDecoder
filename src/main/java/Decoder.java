import SwiftMessages.MT103;
import SwiftMessages.MTMessage;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decoder {

    static final String hashLinePatternString = "^#$";
    static final Pattern hashLinePattern = Pattern.compile(hashLinePatternString, Pattern.MULTILINE);

    static final String blocksPatternString = "\\{1:(?<group1>.+?)}\\{2:(?<group2>.+?)}(\\{3:(?<group3>.+?)})?\\{4:(?<group4>.+?)}\\{5:(?<group5>.+)}";
    static final Pattern blocksPattern = Pattern.compile(blocksPatternString, Pattern.DOTALL);

    static final String columnPatternString = ":([\\dA-Za-z]+?):";
    static final Pattern columnPattern = Pattern.compile(columnPatternString);

    static final int swiftGroupCount = Integer.parseInt(Config.get("swiftGroupCount"));

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
                    splitSwift(splits[i]);
                }
            } else {
                System.out.println("===========================================");
                System.out.println(file.getName());
                System.out.println("===========================================");
                splitSwift(content);
            }
        }
        System.out.println("===========================================");
    }

    private static void splitSwift(String content)
    {
        Matcher matcher = blocksPattern.matcher(content);

        // group1: 找出銀行 BIC
        // group2: 找出電文別
        // group4: 依欄位分別輸出內容
        if (matcher.find())
        {
            MTMessage mtMessage = null;

            for (int i = 1; i <= swiftGroupCount; i++)
            {
                String blockContent = matcher.group("group" + i);
                if (blockContent == null || blockContent.isEmpty()) // optional group 可能回傳 null
                    continue;

                switch (i)
                {
                    case 1:
                        if (blockContent.length() >= 15)
                            System.out.println("銀行 BIC : " + blockContent.substring(3, 15));
                        break;
                    case 2:
                        String mtCode = null;
                        if (blockContent.length() >= 4) {
                            mtCode = blockContent.substring(1, 4);
                            System.out.println("電文 : MT " + mtCode);
                        }

                        try {
                            mtMessage = (MTMessage) (Class.forName("SwiftMessages.MT" + mtCode).newInstance());
                        } catch (Exception e)
                        {
                            mtMessage = new MTMessage();
                        }
                        break;
                    case 4:

                        if (mtMessage == null)
                            return;

                        Queue<String> columnNames = new LinkedList<>(); // 依序登錄電文中所有欄位

                        // 去掉 block 4 最後一行的斜槓
                        blockContent = blockContent.replaceAll("\\n-$", "");

                        Matcher columnNameMatcher = columnPattern.matcher(blockContent);

                        // 第一次: 依序記錄所有欄位名稱
                        while (columnNameMatcher.find())
                        {
                            String found = columnNameMatcher.group(1);
                            columnNames.add(found);
                        }

                        // 第二次: 以欄位名為 separator, 找出所有欄位內容
                        // 並依順序和欄位名配對
                        for (String split : blockContent.split(columnPatternString))
                        {
                            if (split.replace("\n","").trim().isEmpty())
                                continue;

                            String thisColumnName = columnNames.remove();

                            split = split.replaceAll("\\n$", ""); // 去掉最後的換行, 讓 println 好看一點

                            mtMessage.setColumn(thisColumnName, split);
                        }

                        break;
                    default:
                        break;
                }
            }

            if (mtMessage != null)
                mtMessage.outputAsString();
        }

    }
}
