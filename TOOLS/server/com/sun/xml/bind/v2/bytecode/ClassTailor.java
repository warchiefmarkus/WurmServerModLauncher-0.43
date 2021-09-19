/*     */ package com.sun.xml.bind.v2.bytecode;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class ClassTailor
/*     */ {
/*  58 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toVMClassName(Class c) {
/*  64 */     assert !c.isPrimitive();
/*  65 */     if (c.isArray())
/*     */     {
/*  67 */       return toVMTypeName(c); } 
/*  68 */     return c.getName().replace('.', '/');
/*     */   }
/*     */   
/*     */   public static String toVMTypeName(Class<boolean> c) {
/*  72 */     if (c.isArray())
/*     */     {
/*  74 */       return '[' + toVMTypeName(c.getComponentType());
/*     */     }
/*  76 */     if (c.isPrimitive()) {
/*  77 */       if (c == boolean.class) return "Z"; 
/*  78 */       if (c == char.class) return "C"; 
/*  79 */       if (c == byte.class) return "B"; 
/*  80 */       if (c == double.class) return "D"; 
/*  81 */       if (c == float.class) return "F"; 
/*  82 */       if (c == int.class) return "I"; 
/*  83 */       if (c == long.class) return "J"; 
/*  84 */       if (c == short.class) return "S";
/*     */       
/*  86 */       throw new IllegalArgumentException(c.getName());
/*     */     } 
/*  88 */     return 'L' + c.getName().replace('.', '/') + ';';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] tailor(Class templateClass, String newClassName, String... replacements) {
/*  94 */     String vmname = toVMClassName(templateClass);
/*  95 */     return tailor(templateClass.getClassLoader().getResourceAsStream(vmname + ".class"), vmname, newClassName, replacements);
/*     */   }
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
/*     */   public static byte[] tailor(InputStream image, String templateClassName, String newClassName, String... replacements) {
/* 114 */     DataInputStream in = new DataInputStream(image);
/*     */     
/*     */     try {
/* 117 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 118 */       DataOutputStream out = new DataOutputStream(baos);
/*     */ 
/*     */       
/* 121 */       long l = in.readLong();
/* 122 */       out.writeLong(l);
/*     */ 
/*     */       
/* 125 */       short count = in.readShort();
/* 126 */       out.writeShort(count);
/*     */ 
/*     */       
/* 129 */       for (int i = 0; i < count; i++) {
/* 130 */         String value; byte tag = in.readByte();
/* 131 */         out.writeByte(tag);
/* 132 */         switch (tag) {
/*     */           case 0:
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/* 142 */             value = in.readUTF();
/* 143 */             if (value.equals(templateClassName)) {
/* 144 */               value = newClassName;
/*     */             } else {
/* 146 */               for (int j = 0; j < replacements.length; j += 2) {
/* 147 */                 if (value.equals(replacements[j])) {
/* 148 */                   value = replacements[j + 1]; break;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 152 */             out.writeUTF(value);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 3:
/*     */           case 4:
/* 158 */             out.writeInt(in.readInt());
/*     */             break;
/*     */           
/*     */           case 5:
/*     */           case 6:
/* 163 */             i++;
/* 164 */             out.writeLong(in.readLong());
/*     */             break;
/*     */           
/*     */           case 7:
/*     */           case 8:
/* 169 */             out.writeShort(in.readShort());
/*     */             break;
/*     */           
/*     */           case 9:
/*     */           case 10:
/*     */           case 11:
/*     */           case 12:
/* 176 */             out.writeInt(in.readInt());
/*     */             break;
/*     */           
/*     */           default:
/* 180 */             throw new IllegalArgumentException("Unknown constant type " + tag);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 185 */       byte[] buf = new byte[512];
/*     */       int len;
/* 187 */       while ((len = in.read(buf)) > 0) {
/* 188 */         out.write(buf, 0, len);
/*     */       }
/* 190 */       in.close();
/* 191 */       out.close();
/*     */ 
/*     */       
/* 194 */       return baos.toByteArray();
/*     */     }
/* 196 */     catch (IOException e) {
/*     */       
/* 198 */       logger.log(Level.WARNING, "failed to tailor", e);
/* 199 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\bytecode\ClassTailor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */