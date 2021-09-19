package javax.jnlp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface JNLPRandomAccessFile extends DataInput, DataOutput {
  void close() throws IOException;
  
  long length() throws IOException;
  
  long getFilePointer() throws IOException;
  
  int read() throws IOException;
  
  int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  int read(byte[] paramArrayOfbyte) throws IOException;
  
  void readFully(byte[] paramArrayOfbyte) throws IOException;
  
  void readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  int skipBytes(int paramInt) throws IOException;
  
  boolean readBoolean() throws IOException;
  
  byte readByte() throws IOException;
  
  int readUnsignedByte() throws IOException;
  
  short readShort() throws IOException;
  
  int readUnsignedShort() throws IOException;
  
  char readChar() throws IOException;
  
  int readInt() throws IOException;
  
  long readLong() throws IOException;
  
  float readFloat() throws IOException;
  
  double readDouble() throws IOException;
  
  String readLine() throws IOException;
  
  String readUTF() throws IOException;
  
  void seek(long paramLong) throws IOException;
  
  void setLength(long paramLong) throws IOException;
  
  void write(int paramInt) throws IOException;
  
  void write(byte[] paramArrayOfbyte) throws IOException;
  
  void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  void writeBoolean(boolean paramBoolean) throws IOException;
  
  void writeByte(int paramInt) throws IOException;
  
  void writeShort(int paramInt) throws IOException;
  
  void writeChar(int paramInt) throws IOException;
  
  void writeInt(int paramInt) throws IOException;
  
  void writeLong(long paramLong) throws IOException;
  
  void writeFloat(float paramFloat) throws IOException;
  
  void writeDouble(double paramDouble) throws IOException;
  
  void writeBytes(String paramString) throws IOException;
  
  void writeChars(String paramString) throws IOException;
  
  void writeUTF(String paramString) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\JNLPRandomAccessFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */