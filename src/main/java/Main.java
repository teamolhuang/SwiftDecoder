import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args)
    {
        // get input file folder
        String folder = Config.get("inputFileFolder");
        System.out.println("讀取目錄 : " + folder);

        // get possible swift files
        List<File> swiftFiles = getSwiftFiles(folder);

        // split them into assigned formats
        workOnSwifts(swiftFiles);
    }

    private static List<File> getSwiftFiles(String folderPath)
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

    private static void workOnSwifts(List<File> files)
    {
        final String hashLinePatternString = "^#$";
        final Pattern hashLinePattern = Pattern.compile(hashLinePatternString, Pattern.MULTILINE);

        for (File file : files)
        {
            System.out.println("===========================================");
            System.out.println(file.getName());
            System.out.println("===========================================");
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

            splitSwift(content);
        }
        System.out.println("===========================================");
    }

    private static void splitSwift(String content)
    {
        // 如果是多次電文，則用split方式處理
        if (content.indexOf("\u0001", 1) != -1)
        {
            String[] splits = content.split("\u0003");
            for (int i = 0; i < splits.length; i++)
            {
                String split = splits[i];

                System.out.println("#### 子電文 " + (i+1));
                splitSwift(split); // recursion
            }
            return;
        }

        final int groupCount = Integer.parseInt(Config.get("swiftGroupCount"));

        final String blocksPatternString = "\\{1:(?<group1>.+?)}\\{2:(?<group2>.+?)}(\\{3:(?<group3>.+?)})?\\{4:(?<group4>.+?)}\\{5:(?<group5>.+)}";
        final Pattern blocksPattern = Pattern.compile(blocksPatternString, Pattern.DOTALL);

        final String columnPatternString = ":([\\dA-Za-z]+?):";
        final Pattern columnPattern = Pattern.compile(columnPatternString);

        Matcher matcher = blocksPattern.matcher(content);

        // group1: 找出銀行 BIC
        // group2: 找出電文別
        // group4: 依欄位分別輸出內容
        if (matcher.find())
        {
            for (int i = 1; i <= groupCount; i++)
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
                        if (blockContent.length() >= 4)
                            System.out.println("電文 : MT " + blockContent.substring(1,4));
                        break;
                    case 4:
                        Queue<String> columnNames = new LinkedList<>(); // 依序登錄電文中所有欄位
                        HashSet<String> columnSet = new HashSet<>(); // 用於辨別欄位是否已出現過的 hashset

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

                            // 當此欄位名已在電文中出現過時
                            if (columnSet.contains(thisColumnName))
                            {
                                System.out.println("######## 新一筆子資料");
                                columnSet.clear();
                            }

                            // 去掉最後的換行, 讓 println 好看一點
                            System.out.println("欄位 " + thisColumnName + " : " + split.replaceAll("\\n$", ""));
                            columnSet.add(thisColumnName);
                        }

                        break;
                    default:
                        break;
                }
            }
        }

    }
}
