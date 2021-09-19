package com.sun.xml.bind.v2.model.core;

public interface ValuePropertyInfo<T, C> extends PropertyInfo<T, C>, NonElementRef<T, C> {
  Adapter<T, C> getAdapter();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\ValuePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */