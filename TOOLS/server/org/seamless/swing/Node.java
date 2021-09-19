package org.seamless.swing;

import java.util.List;

public interface Node<T> {
  Long getId();
  
  T getParent();
  
  List<T> getChildren();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\Node.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */