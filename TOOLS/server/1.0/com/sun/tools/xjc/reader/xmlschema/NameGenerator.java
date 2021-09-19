/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*    */ import java.text.ParseException;
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
/*    */ public final class NameGenerator
/*    */ {
/*    */   public static String getName(BGMBuilder builder, XSModelGroup mg) throws ParseException {
/* 36 */     StringBuffer name = new StringBuffer();
/*    */     
/* 38 */     mg.visit((XSTermVisitor)new Object(name, builder));
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
/* 79 */     if (name.length() == 0) throw new ParseException("no element", -1);
/*    */     
/* 81 */     return name.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\NameGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */