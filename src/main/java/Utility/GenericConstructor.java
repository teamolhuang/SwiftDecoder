package Utility;

import java.lang.reflect.Field;

public class GenericConstructor {

    /**
     * 依據欄位型態，從字串建立對應的物件。
     * @param field 參考型態的欄位
     * @param s 字串
     * @return 依據欄位型態建成的物件
     */
    public static Object getInstance(Field field, String s)
    {
        boolean isInt = field.getType().equals(int.class);
        Object result = isInt ? 0 : null;

        if (s == null)
            return result;

        try {
            if (isInt)
                result = Integer.parseInt(s);
            else {
                result = field.getType().getConstructor(String.class).newInstance(s);
            }
        } catch (Exception e) {
            System.out.println("Generic Constructor for " + field.getName() + " failed:  " + s);
        }

        return result;
    }
}
