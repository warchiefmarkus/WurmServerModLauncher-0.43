/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import java.util.Comparator;
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
/*    */ public final class OrderComparator
/*    */   implements Comparator
/*    */ {
/* 20 */   public static final Comparator theInstance = new com.sun.tools.xjc.generator.unmarshaller.automaton.OrderComparator();
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Object o1, Object o2) {
/* 25 */     Alphabet a1 = (Alphabet)o1;
/* 26 */     Alphabet a2 = (Alphabet)o2;
/*    */     
/* 28 */     return a2.order - a1.order;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\OrderComparator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */