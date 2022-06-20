package Annotations.SwiftMessages;

import Enums.SwiftMessages.PresenceType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Presence {
    PresenceType value();
}
