/*    */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.reader.Ring;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSSchemaSet;
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
/*    */ abstract class CTBuilder
/*    */ {
/* 73 */   protected final ComplexTypeFieldBuilder builder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/* 74 */   protected final ClassSelector selector = (ClassSelector)Ring.get(ClassSelector.class);
/* 75 */   protected final SimpleTypeBuilder simpleTypeBuilder = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 76 */   protected final ErrorReceiver errorReceiver = (ErrorReceiver)Ring.get(ErrorReceiver.class);
/* 77 */   protected final BindGreen green = (BindGreen)Ring.get(BindGreen.class);
/* 78 */   protected final XSSchemaSet schemas = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/* 79 */   protected final BGMBuilder bgmBuilder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*    */   
/*    */   abstract boolean isApplicable(XSComplexType paramXSComplexType);
/*    */   
/*    */   abstract void build(XSComplexType paramXSComplexType);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\CTBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */