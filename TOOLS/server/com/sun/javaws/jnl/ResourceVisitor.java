package com.sun.javaws.jnl;

public interface ResourceVisitor {
  void visitJARDesc(JARDesc paramJARDesc);
  
  void visitPropertyDesc(PropertyDesc paramPropertyDesc);
  
  void visitPackageDesc(PackageDesc paramPackageDesc);
  
  void visitExtensionDesc(ExtensionDesc paramExtensionDesc);
  
  void visitJREDesc(JREDesc paramJREDesc);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ResourceVisitor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */