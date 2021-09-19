/*    */ package coffee.keenan.network.helpers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ public abstract class ErrorTracking
/*    */ {
/* 11 */   private final HashMap<String, List<Exception>> exceptions = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addException(@NotNull String key, @NotNull Exception ex) {
/* 21 */     if (!this.exceptions.containsKey(key))
/* 22 */       this.exceptions.put(key, new ArrayList<>()); 
/* 23 */     ((List<Exception>)this.exceptions.get(key)).add(ex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getExceptions() {
/* 33 */     List<String> ret = new ArrayList<>();
/* 34 */     this.exceptions.forEach((key, list) -> list.forEach(()));
/* 35 */     return ret.<String>toArray(new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\helpers\ErrorTracking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */