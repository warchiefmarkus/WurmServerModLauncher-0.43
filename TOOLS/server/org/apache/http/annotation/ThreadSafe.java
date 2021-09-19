package org.apache.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\annotation\ThreadSafe.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */