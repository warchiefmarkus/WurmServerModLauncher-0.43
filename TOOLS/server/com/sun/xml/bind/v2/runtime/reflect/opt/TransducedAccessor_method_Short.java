/*    */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.bind.DatatypeConverterImpl;
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public final class TransducedAccessor_method_Short
/*    */   extends DefaultTransducedAccessor
/*    */ {
/*    */   public String print(Object o) {
/* 55 */     return DatatypeConverterImpl._printShort(((Bean)o).get_short());
/*    */   }
/*    */   
/*    */   public void parse(Object o, CharSequence lexical) {
/* 59 */     ((Bean)o).set_short(DatatypeConverterImpl._parseShort(lexical));
/*    */   }
/*    */   
/*    */   public boolean hasValue(Object o) {
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\TransducedAccessor_method_Short.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */