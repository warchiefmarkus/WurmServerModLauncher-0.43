/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Base
/*    */ {
/*    */   protected AnnotationsHost cast(Annotations ann) {
/* 13 */     if (ann == null) {
/* 14 */       return nullAnnotations;
/*    */     }
/* 16 */     return (AnnotationsHost)ann;
/*    */   }
/*    */   
/*    */   protected LocationHost cast(Location loc) {
/* 20 */     if (loc == null) {
/* 21 */       return nullLocation;
/*    */     }
/* 23 */     return (LocationHost)loc;
/*    */   }
/*    */   
/* 26 */   private static final AnnotationsHost nullAnnotations = new AnnotationsHost(null, null);
/* 27 */   private static final LocationHost nullLocation = new LocationHost(null, null);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\Base.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */