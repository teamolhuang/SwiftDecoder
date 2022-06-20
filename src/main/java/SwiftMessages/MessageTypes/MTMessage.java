package SwiftMessages.MessageTypes;

import SwiftMessages.Annotations.ColumnId;
import SwiftMessages.Annotations.ListItemType;
import SwiftMessages.Blocks.*;
import SwiftMessages.Utilities.AnnotationApplier;
import Utility.GenericConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("CanBeFinal")
public class MTMessage {

    @Getter
    protected BasicHeaderBlock basicHeaderBlock = null;
    @Getter
    protected ApplicationHeaderBlock applicationHeaderBlock = null;
    @Getter
    protected UserHeaderBlock userHeaderBlock = null;
    @Getter
    protected TextBlock textBlock = null;
    @Getter
    protected TrailerBlock trailerBlock = null;

    protected Map<String, String> unknownColumns = new HashMap<>();

    @Getter
    protected Class<?> messageType = MTMessage.class;

    private Map<String, HashSet<Field>> idFieldMap = null;

    public void outputFields() {

        if (this.messageType != MTMessage.class) {
            System.out.println("電文 : " + this.messageType.getSimpleName());
            for (Field field : this.messageType.getDeclaredFields()) {
                try {
                    System.out.println(field.getName() + " : " + field.get(this));
                } catch (Exception e) {
                    System.out.println("嘗試輸出 " + field.getName() + " 時失敗");
                }
            }
        }

        if (!unknownColumns.isEmpty()) {
            System.out.println("未定義欄位如下：");
            for (String key : unknownColumns.keySet()) {
                System.out.println(key + " : " + unknownColumns.get(key));
            }
        }
    }

    /**
     * 讀取 SWIFT 電文字串，輸出 MTMessage 物件。
     *
     * @param content SWIFT 電文字串
     * @return 成功轉換時：對應的電文物件<br>否則：一個基本的 MTMessage 物件
     */
    public static MTMessage parse(String content) {

        MTMessage mtMessage = parseBlocks(content);
        mtMessage.parseColumns();
        return mtMessage;
    }

    /**
     * 分別登錄電文 Blocks
     *
     * @param content 完整的電文文字內容
     * @return 分好 Headers 的電文
     */
    private static MTMessage parseBlocks(String content) {
        MTMessage mtMessage = null; // 先看電文種類之後，才決定 parse 成什麼物件

        final String blocksPatternString = "\\{1:(?<group1>.+?)}\\{2:(?<group2>.+?)}(\\{3:(?<group3>.+?)})?\\{4:(?<group4>.+?)}\\{5:(?<group5>.+)}";
        final Pattern blocksPattern = Pattern.compile(blocksPatternString, Pattern.DOTALL);

        Matcher matcher = blocksPattern.matcher(content);

        if (matcher.find()) {
            String mtCode = matcher.group("group2").substring(1, 4);

            mtMessage = getMTMessage("MT" + mtCode);

            mtMessage.basicHeaderBlock = (BasicHeaderBlock) MTBlock.parse(BasicHeaderBlock.class, matcher.group("group1"));
            mtMessage.applicationHeaderBlock = (ApplicationHeaderBlock) MTBlock.parse(ApplicationHeaderBlock.class, matcher.group("group2"));
            mtMessage.userHeaderBlock = (UserHeaderBlock) MTBlock.parse(UserHeaderBlock.class, matcher.group("group3"));
            mtMessage.textBlock = (TextBlock) MTBlock.parse(TextBlock.class, matcher.group("group4"));
            mtMessage.trailerBlock = (TrailerBlock) MTBlock.parse(TrailerBlock.class, matcher.group("group5"));
        }

        return mtMessage != null ? mtMessage : new MTMessage();
    }

    private static MTMessage getMTMessage(String mtName) {
        try {
            return (MTMessage) Class.forName(MTMessage.class.getPackage().getName() + "." + mtName).newInstance();
        } catch (Exception e) {
            System.out.println("getMTMessage 收到未知的電文：" + mtName);
            return new MTMessage();
        }
    }

    private void parseColumns() {
        final String columnPatternString = ":([\\dA-Za-z]+?):";
        final Pattern columnPattern = Pattern.compile(columnPatternString);

        String content = this.textBlock.getContent();

        Queue<String> columnNames = new LinkedList<>();

        // 去掉 block 4 最後一行的斜槓
        content = content.replaceAll("\\n-$", "");

        Matcher columnNameMatcher = columnPattern.matcher(content);

        // 第一次: 依序記錄所有欄位名稱
        while (columnNameMatcher.find()) {
            String found = columnNameMatcher.group(1);
            columnNames.add(found);
        }

        // 第二次: 以欄位名為 separator, 找出所有欄位內容
        // 並依順序和欄位名配對
        for (String split : content.split(columnPatternString)) {
            if (split.replace("\n", "").trim().isEmpty())
                continue;

            String thisColumnName = columnNames.remove();

            split = split.replaceAll("\\n$", ""); // 去掉最後的換行, 讓 println 好看一點

            this.setColumn(thisColumnName, split);
        }
    }

    private void setColumn(String key, String value) {
        if (this.idFieldMap == null) {
            // 建立此物件的 columnId-field map

            this.idFieldMap = new HashMap<>();

            for (Field field : this.getMessageType().getDeclaredFields()) {
                if (field.isAnnotationPresent(ColumnId.class)) {
                    String columnId = field.getAnnotation(ColumnId.class).value();
                    idFieldMap.putIfAbsent(columnId, new HashSet<>());
                    idFieldMap.get(columnId).add(field);
                }
            }
        }

        try {
            for (Field field : idFieldMap.get(key)) {
                List<String> thisFieldValues = AnnotationApplier.shapeValueByAnnotations(value, field); // 根據 field 的 annotations 格式化 values

                // 如果是 List, foreach constructed 之後放入 list
                if (field.getType().equals(List.class)) {
                    @SuppressWarnings("unchecked")
                    List<Object> fieldList = (List<Object>) field.get(this); // Unsafe reflection, 待改

                    // List 應該要有 ListItemType annotation 來表示此 List 裡面裝什麼
                    Class<?> listItemType = field.getAnnotation(ListItemType.class).value();

                    for (String v : thisFieldValues) {
                        if (listItemType.equals(String.class)) {
                            fieldList.add(v);
                        } else {
                            fieldList.add(GenericConstructor.getInstanceByField(field, v));
                        }
                    }

                } else {
                    // 如果 field 不是 list, 固定取第一個結果放進去
                    if (!thisFieldValues.isEmpty()) {
                        if (field.getType().equals(String.class)) {
                            field.set(this, thisFieldValues.get(0));
                        } else {
                            field.set(this, GenericConstructor.getInstanceByField(field, thisFieldValues.get(0)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 途中發生任何錯誤時，丟在 unknown Columns
            unknownColumns.put(key, value);
        }
    }
}