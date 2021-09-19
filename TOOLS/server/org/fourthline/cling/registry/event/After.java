package org.fourthline.cling.registry.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\event\After.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */