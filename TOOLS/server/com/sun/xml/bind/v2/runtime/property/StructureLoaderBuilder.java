/*    */ package com.sun.xml.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*    */ import com.sun.xml.bind.v2.util.QNameMap;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface StructureLoaderBuilder
/*    */ {
/* 75 */   public static final QName TEXT_HANDLER = new QName("\000", "text");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 89 */   public static final QName CATCH_ALL = new QName("\000", "catchAll");
/*    */   
/*    */   void buildChildElementUnmarshallers(UnmarshallerChain paramUnmarshallerChain, QNameMap<ChildLoader> paramQNameMap);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\StructureLoaderBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */