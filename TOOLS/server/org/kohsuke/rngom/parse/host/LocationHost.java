/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LocationHost
/*    */   implements Location
/*    */ {
/*    */   final Location lhs;
/*    */   final Location rhs;
/*    */   
/*    */   LocationHost(Location lhs, Location rhs) {
/* 15 */     this.lhs = lhs;
/* 16 */     this.rhs = rhs;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\LocationHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */