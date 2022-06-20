package SwiftMessages.Blocks;

import SwiftMessages.Utilities.AnnotationApplier;
import Utility.GenericConstructor;

import java.lang.reflect.Field;
import java.util.List;

public class MTBlock {

    public Class<?> blockType = null;

    public void outputAsString() {
        if (blockType == null)
            return;

        System.out.println("##### " + blockType.getSimpleName() + " #####");

        for (Field field : this.blockType.getDeclaredFields()) {
            try {
                System.out.println(field.getName() + " : " + field.get(this));
            } catch (Exception e) {
                System.out.println("嘗試輸出 " + field.getName() + " 時失敗");
            }
        }
    }

    /**
     * 指定 MTBlock 的子 class，將 content parse 進去。
     * @param clazz MTBlock 的子 class
     * @param content 內容字串
     * @return 產出的 MTBlock
     */
    public static MTBlock parse(Class<? extends MTBlock> clazz, String content) {

        MTBlock result = null;

        try {
            result = clazz.newInstance();

            result.blockType = clazz;

            result.parse(content);
        } catch (Exception e)
        {
            System.out.println("MTBlock parse failed: " + e.getMessage());
        }

        return result;
    }

    private void parse(String content)
    {
        if (this.blockType == null || content == null || content.isEmpty())
            return;

        for (Field field : this.blockType.getDeclaredFields())
        {
            List<String> result;
            try {
                result = AnnotationApplier.shapeValueByAnnotations(content, field);

                Object value = GenericConstructor.getInstanceByField(field, result.get(0));
                field.set(this, value);

            } catch (Exception e)
            {
                System.out.println(field.getName() + " parse failed: " + e.getMessage());
            }
        }
    }
}
