package org.fourthline.cling.binding.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpnpServiceId {
  String namespace() default "upnp-org";
  
  String value();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\UpnpServiceId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */