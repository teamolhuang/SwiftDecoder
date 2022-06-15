package SwiftMessages;

import Annotations.*;
import Program.Config;
import lombok.Getter;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MTMessage {
    protected Map<String, String> unknownColumns = new HashMap<>();

    @Getter
    protected Class<?> messageType = this.getClass();

    private Map<String, HashSet<Field>> idFieldMap = null;

    private void setColumn(String key, String value)
    {
        if (this.idFieldMap == null)
        {
            // 建立此物件的 columnId-field map

            this.idFieldMap = new HashMap<>();

            for (Field field : this.getMessageType().getDeclaredFields())
            {
                if (field.isAnnotationPresent(ColumnId.class)) {
                    String columnId = field.getAnnotation(ColumnId.class).value();
                    idFieldMap.putIfAbsent(columnId, new HashSet<>());
                    idFieldMap.get(columnId).add(field);
                }
            }
        }

        try {
            for (Field field : idFieldMap.get(key)) {
                List<String> thisFieldValues = shapeValueByAnnotations(value, field); // 根據 field 的 annotations 格式化 values

                // 如果是 List, foreach constructed 之後放入 list
                if (field.getType().equals(List.class))
                {
                    @SuppressWarnings("unchecked")
                    List<Object> fieldList = (List<Object>) field.get(this); // Unsafe reflection, 待改

                    // List 應該要有 ListItemType annotation 來表示此 List 裡面裝什麼
                    Class<?> listItemType = field.getAnnotation(ListItemType.class).value();

                    for (String v : thisFieldValues)
                    {
                        if (listItemType.equals(String.class)) {
                            fieldList.add(v);
                        } else {
                            fieldList.add(field.getType().getConstructor(String.class).newInstance(v));
                        }
                    }

                } else {
                    // 如果 field 不是 list, 固定取第一個結果放進去
                    if (!thisFieldValues.isEmpty()) {
                        if (field.getType().equals(String.class)) {
                            field.set(this, thisFieldValues.get(0));
                        } else {
                            field.set(this, field.getType().getConstructor(String.class).newInstance(thisFieldValues.get(0)));
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            // 途中發生任何錯誤時，丟在 unknown Columns
            System.out.println("設定 " + key + " 欄位時失敗，請確認程式碼");
            unknownColumns.put(key, value);
        }
    }

    private List<String> shapeValueByAnnotations(String value, Field field) throws Exception {
        List<String> resultList = new ArrayList<>();

        // 行數範圍 LineRange
        if (value.contains("\n") && field.isAnnotationPresent(LineRange.class))
        {
            LineRange lineRange = field.getAnnotation(LineRange.class);

            if (lineRange.bottom() >= lineRange.top()) {

                String[] splits = value.split("\\n");

                for (int i = lineRange.top(); i <= Math.min(splits.length-1, lineRange.bottom()); i++)
                {
                    resultList.addAll(shapeValueByAnnotations(splits[i], field));
                }

                return resultList;
            }
        }

        // 起始行 BeginAt

        if (field.isAnnotationPresent(BeginAt.class))
        {
            BeginAt beginAt = field.getAnnotation(BeginAt.class);

            if (beginAt.line() > 0)
            {
                int beginIndex = -1;
                for (int i = 0; i < beginAt.line(); i++)
                {
                    beginIndex = value.indexOf("\n", beginIndex+1);
                }

                if (beginIndex != -1)
                    value = value.substring(beginIndex);
            }

            if (beginAt.pos() > 0)
            {
                value = value.length() > beginAt.pos() ? value.substring(beginAt.pos()) : "";
            }
        }

        // 欄位長度範圍 ColumnSizeRange
        // 欄位長度指定 ColumnSizeFixed

        if (field.isAnnotationPresent(ColumnSizeRange.class))
        {
            int maxLength = field.getAnnotation(ColumnSizeRange.class).max();

            if (maxLength > 0)
                value = value.substring(0, Integer.min(value.length(), maxLength));
        } else if (field.isAnnotationPresent(ColumnSizeFixed.class))
        {
            int fixedSize = field.getAnnotation(ColumnSizeFixed.class).value();

            if (value.length() < fixedSize)
                throw new Exception("欄位長度小於指定的長度 !!");

            value = value.substring(0, fixedSize);
        }

        // 正規條件 IfRegex

        if (field.isAnnotationPresent(IfRegex.class))
        {
            String regex = field.getAnnotation(IfRegex.class).value();
            if (!value.matches(regex))
                return resultList;
        }

        resultList.add(value);

        return resultList;
    }

    public void outputAsString()
    {
        for (Field field : this.messageType.getDeclaredFields())
        {
            try {
                System.out.println(field.getName() + " : " + field.get(this));
            } catch (Exception e)
            {
                System.out.println("嘗試輸出 " + field.getName() + " 時失敗");
            }
        }
    }

    /**
     * 讀取 SWIFT 電文字串，輸出 MTMessage 物件。
     * @param content SWIFT 電文字串
     * @return 成功轉換時：MTMessage<br>否則：null
     */
    public static MTMessage parse(String content)
    {
        final String blocksPatternString = "\\{1:(?<group1>.+?)}\\{2:(?<group2>.+?)}(\\{3:(?<group3>.+?)})?\\{4:(?<group4>.+?)}\\{5:(?<group5>.+)}";
        final Pattern blocksPattern = Pattern.compile(blocksPatternString, Pattern.DOTALL);

        final String columnPatternString = ":([\\dA-Za-z]+?):";
        final Pattern columnPattern = Pattern.compile(columnPatternString);

        final int swiftGroupCount = Integer.parseInt(Config.get("swiftGroupCount"));

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
                            mtMessage = new MTMessage();

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

            return mtMessage;
        }

        return null;
    }
}
