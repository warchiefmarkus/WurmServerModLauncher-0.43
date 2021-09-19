/*     */ package 1.0.com.sun.tools.xjc.reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface NameConverter
/*     */ {
/*  69 */   public static final com.sun.tools.xjc.reader.NameConverter standard = (com.sun.tools.xjc.reader.NameConverter)new Standard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final com.sun.tools.xjc.reader.NameConverter jaxrpcCompatible = (com.sun.tools.xjc.reader.NameConverter)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static final com.sun.tools.xjc.reader.NameConverter smart = (com.sun.tools.xjc.reader.NameConverter)new Object();
/*     */   
/*     */   String toClassName(String paramString);
/*     */   
/*     */   String toInterfaceName(String paramString);
/*     */   
/*     */   String toPropertyName(String paramString);
/*     */   
/*     */   String toConstantName(String paramString);
/*     */   
/*     */   String toVariableName(String paramString);
/*     */   
/*     */   String toPackageName(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\NameConverter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */