package javax.servlet.descriptor;

import java.util.Collection;

public interface JspConfigDescriptor {
  Collection<TaglibDescriptor> getTaglibs();
  
  Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\descriptor\JspConfigDescriptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */