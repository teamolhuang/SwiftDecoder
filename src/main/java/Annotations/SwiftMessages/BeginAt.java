package Annotations.SwiftMessages;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BeginAt {
    int pos() default 0;
    int line() default 0;
}
