package SwiftMessages.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LineRange {
    int top() default 0;
    int bottom();
}
