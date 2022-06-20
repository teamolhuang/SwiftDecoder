package Utility;

import Annotations.SwiftMessages.*;
import Enums.SwiftMessages.PresenceType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationApplier {

    /**
     * 依據 SwiftDecoder.Annotations 格式化 value 。
     * @param value 參數內容的字串
     * @param field 對應的欄位
     * @return List of 格式化好的字串
     */
    public static List<String> shapeValueByAnnotations(String value, Field field) throws Exception {
        List<String> resultList = new ArrayList<>();

        if (value == null || value.isEmpty())
        {
            if (field.getAnnotation(Presence.class).value() == PresenceType.Mandatory)
                value = "";
            else
                return resultList;
        }

        // 行數範圍 LineRange
        if (value.contains("\n") && field.isAnnotationPresent(LineRange.class)) {
            LineRange lineRange = field.getAnnotation(LineRange.class);

            if (lineRange.bottom() >= lineRange.top()) {

                String[] splits = value.split("\\n");

                for (int i = lineRange.top(); i <= Math.min(splits.length - 1, lineRange.bottom()); i++) {
                    resultList.addAll(shapeValueByAnnotations(splits[i], field));
                }

                return resultList;
            }
        }

        // 起始位置 BeginAt

        if (field.isAnnotationPresent(BeginAt.class)) {
            BeginAt beginAt = field.getAnnotation(BeginAt.class);

            // 行處理
            if (beginAt.line() > 0) {
                int beginIndex = -1;
                for (int i = 0; i < beginAt.line(); i++) {
                    beginIndex = value.indexOf("\n", beginIndex + 1);
                }

                if (beginIndex != -1)
                    value = value.substring(beginIndex);
            }

            // 位置處理
            if (beginAt.pos() > 0) {
                value = value.length() > beginAt.pos() ? value.substring(beginAt.pos()) : "";
            }
        }

        // 欄位長度範圍 ColumnSizeRange
        // 欄位長度指定 ColumnSizeFixed

        if (field.isAnnotationPresent(ColumnSizeRange.class)) {
            int maxLength = field.getAnnotation(ColumnSizeRange.class).max();

            if (maxLength > 0)
                value = value.substring(0, Integer.min(value.length(), maxLength));
        } else if (field.isAnnotationPresent(ColumnSizeFixed.class)) {
            int fixedSize = field.getAnnotation(ColumnSizeFixed.class).value();

            if (value.length() < fixedSize)
                throw new Exception("欄位長度小於指定的長度 !!");

            value = value.substring(0, fixedSize);
        }

        // 正規條件 IfRegex

        if (field.isAnnotationPresent(IfRegex.class)) {
            String regex = field.getAnnotation(IfRegex.class).value();
            if (!value.matches(regex))
                return resultList;
        }

        // 指定字串之後 AfterLiteral

        if (field.isAnnotationPresent(AfterLiteral.class)) {
            String literal = field.getAnnotation(AfterLiteral.class).value();

            if (!value.isEmpty())
                value = value.substring(value.indexOf(literal) + 1);
        }

        // 直到指定字串 UntilLiteral

        if (field.isAnnotationPresent(UntilLiteral.class)) {
            String literal = field.getAnnotation(UntilLiteral.class).value();

            if (!value.isEmpty())
                value = value.substring(0, Math.max(0, value.indexOf(literal)));
        }

        resultList.add(value);

        return resultList;
    }
}
