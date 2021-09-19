package com.sun.tools.xjc.generator.bean.field;

import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.FieldOutline;

public interface FieldRenderer {
  FieldOutline generate(ClassOutlineImpl paramClassOutlineImpl, CPropertyInfo paramCPropertyInfo);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\FieldRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */