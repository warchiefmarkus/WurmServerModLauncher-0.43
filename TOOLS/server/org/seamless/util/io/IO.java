/*      */ package org.seamless.util.io;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ public class IO {
/*      */   public static final String LINE_SEPARATOR;
/*      */   
/*      */   public static String makeRelativePath(String path, String base) {
/*   28 */     String p = "";
/*   29 */     if (path != null && path.length() > 0) {
/*   30 */       if (path.startsWith("/")) {
/*   31 */         if (path.startsWith(base)) {
/*   32 */           p = path.substring(base.length());
/*      */         } else {
/*   34 */           p = base + path;
/*      */         } 
/*      */       } else {
/*   37 */         p = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
/*      */       } 
/*      */ 
/*      */       
/*   41 */       if (p.startsWith("/")) p = p.substring(1); 
/*      */     } 
/*   43 */     return p;
/*      */   }
/*      */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*      */   public static void recursiveRename(File dir, String from, String to) {
/*   47 */     File[] subfiles = dir.listFiles();
/*   48 */     for (File file : subfiles) {
/*   49 */       if (file.isDirectory()) {
/*   50 */         recursiveRename(file, from, to);
/*   51 */         file.renameTo(new File(dir, file.getName().replace(from, to)));
/*      */       } else {
/*   53 */         file.renameTo(new File(dir, file.getName().replace(from, to)));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void findFiles(File file, FileFinder finder) {
/*   59 */     finder.found(file);
/*   60 */     File[] children = file.listFiles();
/*   61 */     if (children != null) {
/*   62 */       for (File child : children) {
/*   63 */         findFiles(child, finder);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean deleteFile(File path) {
/*   72 */     if (path.exists()) {
/*   73 */       File[] files = path.listFiles();
/*   74 */       if (files != null)
/*   75 */         for (File file : files) {
/*   76 */           if (file.isDirectory()) {
/*   77 */             deleteFile(file);
/*      */           } else {
/*   79 */             file.delete();
/*      */           } 
/*      */         }  
/*      */     } 
/*   83 */     return path.delete();
/*      */   }
/*      */   
/*      */   public static void copyFile(File sourceFile, File destFile) throws IOException {
/*   87 */     if (!destFile.exists()) {
/*   88 */       destFile.createNewFile();
/*      */     }
/*      */     
/*   91 */     FileChannel source = null;
/*   92 */     FileChannel destination = null;
/*      */     try {
/*   94 */       source = (new FileInputStream(sourceFile)).getChannel();
/*   95 */       destination = (new FileOutputStream(destFile)).getChannel();
/*   96 */       destination.transferFrom(source, 0L, source.size());
/*      */     } finally {
/*   98 */       if (source != null) {
/*   99 */         source.close();
/*      */       }
/*  101 */       if (destination != null) {
/*  102 */         destination.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] readBytes(InputStream is) throws IOException {
/*  110 */     return toByteArray(is);
/*      */   }
/*      */   
/*      */   public static byte[] readBytes(File file) throws IOException {
/*  114 */     InputStream is = new FileInputStream(file);
/*      */     try {
/*  116 */       return readBytes(is);
/*      */     } finally {
/*  118 */       is.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void writeBytes(OutputStream outputStream, byte[] data) throws IOException {
/*  123 */     write(data, outputStream);
/*      */   }
/*      */   
/*      */   public static void writeBytes(File file, byte[] data) throws IOException {
/*  127 */     if (file == null) {
/*  128 */       throw new IllegalArgumentException("File should not be null.");
/*      */     }
/*  130 */     if (!file.exists()) {
/*  131 */       throw new FileNotFoundException("File does not exist: " + file);
/*      */     }
/*  133 */     if (!file.isFile()) {
/*  134 */       throw new IllegalArgumentException("Should not be a directory: " + file);
/*      */     }
/*  136 */     if (!file.canWrite()) {
/*  137 */       throw new IllegalArgumentException("File cannot be written: " + file);
/*      */     }
/*      */     
/*  140 */     OutputStream os = new FileOutputStream(file);
/*      */     try {
/*  142 */       writeBytes(os, data);
/*  143 */       os.flush();
/*      */     } finally {
/*  145 */       os.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void writeUTF8(OutputStream outputStream, String data) throws IOException {
/*  150 */     write(data, outputStream, "UTF-8");
/*      */   }
/*      */   
/*      */   public static void writeUTF8(File file, String contents) throws IOException {
/*  154 */     if (file == null) {
/*  155 */       throw new IllegalArgumentException("File should not be null.");
/*      */     }
/*  157 */     if (!file.exists()) {
/*  158 */       throw new FileNotFoundException("File does not exist: " + file);
/*      */     }
/*  160 */     if (!file.isFile()) {
/*  161 */       throw new IllegalArgumentException("Should not be a directory: " + file);
/*      */     }
/*  163 */     if (!file.canWrite()) {
/*  164 */       throw new IllegalArgumentException("File cannot be written: " + file);
/*      */     }
/*      */     
/*  167 */     OutputStream os = new FileOutputStream(file);
/*      */     try {
/*  169 */       writeUTF8(os, contents);
/*  170 */       os.flush();
/*      */     } finally {
/*  172 */       os.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String readLines(InputStream is) throws IOException {
/*  177 */     if (is == null) throw new IllegalArgumentException("Inputstream was null");
/*      */ 
/*      */     
/*  180 */     BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
/*      */ 
/*      */ 
/*      */     
/*  184 */     StringBuilder input = new StringBuilder();
/*      */     String inputLine;
/*  186 */     while ((inputLine = inputReader.readLine()) != null) {
/*  187 */       input.append(inputLine).append(System.getProperty("line.separator"));
/*      */     }
/*      */     
/*  190 */     return (input.length() > 0) ? input.toString() : "";
/*      */   }
/*      */   
/*      */   public static String readLines(File file) throws IOException {
/*  194 */     InputStream is = new FileInputStream(file);
/*      */     try {
/*  196 */       return readLines(is);
/*      */     } finally {
/*  198 */       is.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String[] readLines(File file, boolean trimLines) throws IOException {
/*  203 */     return readLines(file, trimLines, null);
/*      */   }
/*      */   
/*      */   public static String[] readLines(File file, boolean trimLines, Character commentChar) throws IOException {
/*  207 */     return readLines(file, trimLines, commentChar, false);
/*      */   }
/*      */   
/*      */   public static String[] readLines(File file, boolean trimLines, Character commentChar, boolean skipEmptyLines) throws IOException {
/*  211 */     List<String> contents = new ArrayList<String>();
/*  212 */     BufferedReader input = new BufferedReader(new FileReader(file));
/*      */     try {
/*      */       String line;
/*  215 */       while ((line = input.readLine()) != null) {
/*  216 */         if (commentChar != null && line.matches("^\\s*" + commentChar + ".*"))
/*  217 */           continue;  String l = trimLines ? line.trim() : line;
/*  218 */         if (skipEmptyLines && l.length() == 0)
/*  219 */           continue;  contents.add(l);
/*      */       } 
/*      */     } finally {
/*  222 */       input.close();
/*      */     } 
/*  224 */     return contents.<String>toArray(new String[contents.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  290 */     StringWriter buf = new StringWriter(4);
/*  291 */     PrintWriter out = new PrintWriter(buf);
/*  292 */     out.println();
/*  293 */     LINE_SEPARATOR = buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Reader input) {
/*      */     try {
/*  313 */       if (input != null) {
/*  314 */         input.close();
/*      */       }
/*  316 */     } catch (IOException ioe) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Writer output) {
/*      */     try {
/*  331 */       if (output != null) {
/*  332 */         output.close();
/*      */       }
/*  334 */     } catch (IOException ioe) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(InputStream input) {
/*      */     try {
/*  349 */       if (input != null) {
/*  350 */         input.close();
/*      */       }
/*  352 */     } catch (IOException ioe) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(OutputStream output) {
/*      */     try {
/*  367 */       if (output != null) {
/*  368 */         output.close();
/*      */       }
/*  370 */     } catch (IOException ioe) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input) throws IOException {
/*  390 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  391 */     copy(input, output);
/*  392 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input) throws IOException {
/*  408 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  409 */     copy(input, output);
/*  410 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input, String encoding) throws IOException {
/*  432 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  433 */     copy(input, output, encoding);
/*  434 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(String input) throws IOException {
/*  450 */     return input.getBytes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is) throws IOException {
/*  470 */     CharArrayWriter output = new CharArrayWriter();
/*  471 */     copy(is, output);
/*  472 */     return output.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is, String encoding) throws IOException {
/*  494 */     CharArrayWriter output = new CharArrayWriter();
/*  495 */     copy(is, output, encoding);
/*  496 */     return output.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(Reader input) throws IOException {
/*  512 */     CharArrayWriter sw = new CharArrayWriter();
/*  513 */     copy(input, sw);
/*  514 */     return sw.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input) throws IOException {
/*  533 */     StringWriter sw = new StringWriter();
/*  534 */     copy(input, sw);
/*  535 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, String encoding) throws IOException {
/*  556 */     StringWriter sw = new StringWriter();
/*  557 */     copy(input, sw, encoding);
/*  558 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Reader input) throws IOException {
/*  573 */     StringWriter sw = new StringWriter();
/*  574 */     copy(input, sw);
/*  575 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] input) throws IOException {
/*  589 */     return new String(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] input, String encoding) throws IOException {
/*  608 */     if (encoding == null) {
/*  609 */       return new String(input);
/*      */     }
/*  611 */     return new String(input, encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input) {
/*  629 */     byte[] bytes = input.getBytes();
/*  630 */     return new ByteArrayInputStream(bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input, String encoding) throws IOException {
/*  647 */     byte[] bytes = (encoding != null) ? input.getBytes(encoding) : input.getBytes();
/*  648 */     return new ByteArrayInputStream(bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, OutputStream output) throws IOException {
/*  666 */     if (data != null) {
/*  667 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output) throws IOException {
/*  685 */     if (data != null) {
/*  686 */       output.write(new String(data));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output, String encoding) throws IOException {
/*  709 */     if (data != null) {
/*  710 */       if (encoding == null) {
/*  711 */         write(data, output);
/*      */       } else {
/*  713 */         output.write(new String(data, encoding));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, Writer output) throws IOException {
/*  733 */     if (data != null) {
/*  734 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output) throws IOException {
/*  754 */     if (data != null) {
/*  755 */       output.write((new String(data)).getBytes());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, String encoding) throws IOException {
/*  779 */     if (data != null) {
/*  780 */       if (encoding == null) {
/*  781 */         write(data, output);
/*      */       } else {
/*  783 */         output.write((new String(data)).getBytes(encoding));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, Writer output) throws IOException {
/*  801 */     if (data != null) {
/*  802 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output) throws IOException {
/*  821 */     if (data != null) {
/*  822 */       output.write(data.getBytes());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, String encoding) throws IOException {
/*  844 */     if (data != null) {
/*  845 */       if (encoding == null) {
/*  846 */         write(data, output);
/*      */       } else {
/*  848 */         output.write(data.getBytes(encoding));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(StringBuffer data, Writer output) throws IOException {
/*  867 */     if (data != null) {
/*  868 */       output.write(data.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(StringBuffer data, OutputStream output) throws IOException {
/*  887 */     if (data != null) {
/*  888 */       output.write(data.toString().getBytes());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
/*  910 */     if (data != null) {
/*  911 */       if (encoding == null) {
/*  912 */         write(data, output);
/*      */       } else {
/*  914 */         output.write(data.toString().getBytes(encoding));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(InputStream input, OutputStream output) throws IOException {
/*  943 */     long count = copyLarge(input, output);
/*  944 */     if (count > 2147483647L) {
/*  945 */       return -1;
/*      */     }
/*  947 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
/*  966 */     byte[] buffer = new byte[4096];
/*  967 */     long count = 0L;
/*  968 */     int n = 0;
/*  969 */     while (-1 != (n = input.read(buffer))) {
/*  970 */       output.write(buffer, 0, n);
/*  971 */       count += n;
/*      */     } 
/*  973 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output) throws IOException {
/*  993 */     InputStreamReader in = new InputStreamReader(input);
/*  994 */     copy(in, output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output, String encoding) throws IOException {
/* 1018 */     if (encoding == null) {
/* 1019 */       copy(input, output);
/*      */     } else {
/* 1021 */       InputStreamReader in = new InputStreamReader(input, encoding);
/* 1022 */       copy(in, output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(Reader input, Writer output) throws IOException {
/* 1049 */     long count = copyLarge(input, output);
/* 1050 */     if (count > 2147483647L) {
/* 1051 */       return -1;
/*      */     }
/* 1053 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output) throws IOException {
/* 1070 */     char[] buffer = new char[4096];
/* 1071 */     long count = 0L;
/* 1072 */     int n = 0;
/* 1073 */     while (-1 != (n = input.read(buffer))) {
/* 1074 */       output.write(buffer, 0, n);
/* 1075 */       count += n;
/*      */     } 
/* 1077 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output) throws IOException {
/* 1101 */     OutputStreamWriter out = new OutputStreamWriter(output);
/* 1102 */     copy(input, out);
/*      */ 
/*      */     
/* 1105 */     out.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
/* 1133 */     if (encoding == null) {
/* 1134 */       copy(input, output);
/*      */     } else {
/* 1136 */       OutputStreamWriter out = new OutputStreamWriter(output, encoding);
/* 1137 */       copy(input, out);
/*      */ 
/*      */       
/* 1140 */       out.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
/* 1163 */     if (!(input1 instanceof BufferedInputStream)) {
/* 1164 */       input1 = new BufferedInputStream(input1);
/*      */     }
/* 1166 */     if (!(input2 instanceof BufferedInputStream)) {
/* 1167 */       input2 = new BufferedInputStream(input2);
/*      */     }
/*      */     
/* 1170 */     int ch = input1.read();
/* 1171 */     while (-1 != ch) {
/* 1172 */       int i = input2.read();
/* 1173 */       if (ch != i) {
/* 1174 */         return false;
/*      */       }
/* 1176 */       ch = input1.read();
/*      */     } 
/*      */     
/* 1179 */     int ch2 = input2.read();
/* 1180 */     return (ch2 == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
/* 1200 */     if (!(input1 instanceof BufferedReader)) {
/* 1201 */       input1 = new BufferedReader(input1);
/*      */     }
/* 1203 */     if (!(input2 instanceof BufferedReader)) {
/* 1204 */       input2 = new BufferedReader(input2);
/*      */     }
/*      */     
/* 1207 */     int ch = input1.read();
/* 1208 */     while (-1 != ch) {
/* 1209 */       int i = input2.read();
/* 1210 */       if (ch != i) {
/* 1211 */         return false;
/*      */       }
/* 1213 */       ch = input1.read();
/*      */     } 
/*      */     
/* 1216 */     int ch2 = input2.read();
/* 1217 */     return (ch2 == -1);
/*      */   }
/*      */   
/*      */   public static interface FileFinder {
/*      */     void found(File param1File);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\io\IO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */