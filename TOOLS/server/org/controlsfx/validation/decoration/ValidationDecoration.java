package org.controlsfx.validation.decoration;

import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationMessage;

public interface ValidationDecoration {
  void removeDecorations(Control paramControl);
  
  void applyValidationDecoration(ValidationMessage paramValidationMessage);
  
  void applyRequiredDecoration(Control paramControl);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\decoration\ValidationDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */