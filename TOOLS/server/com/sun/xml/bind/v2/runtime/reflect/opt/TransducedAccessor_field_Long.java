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
/*    */ public final class TransducedAccessor_field_Long
/*    */   extends DefaultTransducedAccessor
/*    */ {
/*    */   public String print(Object o) {
/* 55 */     return DatatypeConverterImpl._printLong(((Bean)o).f_long);
/*    */   }
/*    */   
/*    */   public void parse(Object o, CharSequence lexical) {
/* 59 */     ((Bean)o).f_long = DatatypeConverterImpl._parseLong(lexical);
/*    */   }
/*    */   
/*    */   public boolean hasValue(Object o) {
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\TransducedAccessor_field_Long.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */