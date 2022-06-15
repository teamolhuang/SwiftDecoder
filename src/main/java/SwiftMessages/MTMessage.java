package SwiftMessages;

import Annotations.*;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;

public class MTMessage {
    public Map<String, String> unknownColumns = new HashMap<>();

    @Getter
    protected Class<?> messageType = this.getClass();

    private Map<String, HashSet<Field>> idFieldMap = null;

    public void setColumn(String key, String value)
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
                    List<Object> fieldList = (List<Object>) field.get(this); // Unsafe reflection, 待Ad改

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

    public List<String> shapeValueByAnnotations(String value, Field field) throws Exception {
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
}
