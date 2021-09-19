package org.controlsfx.property.editor;

import javafx.scene.Node;

public interface PropertyEditor<T> {
  Node getEditor();
  
  T getValue();
  
  void setValue(T paramT);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\PropertyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */