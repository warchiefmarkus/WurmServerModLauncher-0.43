/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import javax.annotation.Nullable;
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
/*    */ final class StandardAttributeProviders
/*    */ {
/* 33 */   private static final ImmutableMap<String, AttributeProvider> PROVIDERS = (new ImmutableMap.Builder()).put("basic", new BasicAttributeProvider()).put("owner", new OwnerAttributeProvider()).put("posix", new PosixAttributeProvider()).put("dos", new DosAttributeProvider()).put("acl", new AclAttributeProvider()).put("user", new UserDefinedAttributeProvider()).build();
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
/*    */   @Nullable
/*    */   public static AttributeProvider get(String view) {
/* 49 */     AttributeProvider provider = (AttributeProvider)PROVIDERS.get(view);
/*    */     
/* 51 */     if (provider == null && view.equals("unix"))
/*    */     {
/*    */       
/* 54 */       return new UnixAttributeProvider();
/*    */     }
/*    */     
/* 57 */     return provider;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\StandardAttributeProviders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */