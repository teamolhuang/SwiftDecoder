package SwiftMessages.Annotations;

import SwiftMessages.Enums.PresenceType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Presence {
    PresenceType value();
}
