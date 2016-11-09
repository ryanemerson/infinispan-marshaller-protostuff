package org.infinispan.marshaller.protostuff;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.protostuff.ByteArrayInput;
import io.protostuff.ByteBufferInput;
import io.protostuff.CodedInput;
import io.protostuff.GraphByteArrayInput;
import io.protostuff.Input;
import io.protostuff.ProtobufException;
import io.protostuff.Schema;
import io.protostuff.WireFormat;
import io.protostuff.runtime.PolymorphicSchemaFactories;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtostuffObjectInput implements ObjectInput, Closeable {

   private final Input input;

   public ProtostuffObjectInput(Input input) {
      this.input = input;
   }

//   @Override
//   public Object readObject() throws ClassNotFoundException, IOException {
////      ignoreFieldNumber();
//      System.out.println(input.readFieldNumber(null));
//      // TODO ArrayIndexOutOfBound due to repeated calls on readObject and fieldNumber
//      System.out.println(input.readInt32());
//      System.out.println(input.readInt32());
//      String className = input.readString();
//      if (className.isEmpty()) {
//         return null;
//      }
//      System.out.println(className);
//      Class clazz = Class.forName(className);
//      Schema schema = RuntimeSchema.getSchema(clazz);
//      Object object = schema.newMessage();
//      schema.mergeFrom(input, object);
//      ignoreFieldNumber();
////      System.out.println(input.readFieldNumber(null));
//      return object;
//   }

   @Override
   public Object readObject() throws ClassNotFoundException, IOException {
      checkValidObjectGroup();

      int tag = getTagFromFieldNumber();
      if (tag == WireFormat.WIRETYPE_END_GROUP)
         return null;

      String className = input.readString();
      if (className.isEmpty()) {
         return null;
      }

      Class clazz = Class.forName(className);
      Schema schema = RuntimeSchema.getSchema(clazz);
      Object object = schema.newMessage();
      schema.mergeFrom(input, object);
      System.out.println("Read Obj: " + object);
      return object;
   }

   private void checkValidObjectGroup() throws IOException {
      int tag = getTagFromFieldNumber();
//      if (tag == WireFormat.WIRETYPE_START_GROUP)
//         throw new IOException("Expected Object to start with bytes " + WireFormat.WIRETYPE_START_GROUP);
   }

   private int getTagFromFieldNumber() throws IOException {
      return input.readFieldNumber(null) & ((1 << 3) - 1);
   }

   @Override
   public int read() throws IOException {
      ignoreFieldNumber();
      return (byte) input.readInt32();
   }

   @Override
   public int read(byte[] b) throws IOException {
      ignoreFieldNumber();
      return read(b, 0, b.length);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      ignoreFieldNumber();
      byte[] bytes = input.readByteArray();
      System.arraycopy(bytes, off, b, 0, len);
      return b.length;
   }

   @Override
   public long skip(long n) throws IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public int available() throws IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void close() throws IOException {
      if (input.readFieldNumber(null) != 0) {
         throw new ProtobufException("Unexpected field number. Ensure that you're attempting to write/read the same number of fields");
      }
   }

   @Override
   public void readFully(byte[] b) throws IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void readFully(byte[] b, int off, int len) throws IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public int skipBytes(int n) throws IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public boolean readBoolean() throws IOException {
      ignoreFieldNumber();
      return input.readBool();
   }

   @Override
   public byte readByte() throws IOException {
      ignoreFieldNumber();
      return (byte) input.readInt32();
   }

   @Override
   public int readUnsignedByte() throws IOException {
      ignoreFieldNumber();
      return (byte) input.readUInt32();
   }

   @Override
   public short readShort() throws IOException {
      ignoreFieldNumber();
      return (short) input.readInt32();
   }

   @Override
   public int readUnsignedShort() throws IOException {
      ignoreFieldNumber();
      return (short) input.readInt32();
   }

   @Override
   public char readChar() throws IOException {
      ignoreFieldNumber();
      return (char) input.readInt32();
   }

   @Override
   public int readInt() throws IOException {
      ignoreFieldNumber();
      return input.readInt32();
   }

   @Override
   public long readLong() throws IOException {
      ignoreFieldNumber();
      return input.readInt64();
   }

   @Override
   public float readFloat() throws IOException {
      ignoreFieldNumber();
      return input.readFloat();
   }

   @Override
   public double readDouble() throws IOException {
      ignoreFieldNumber();
      ;
      return input.readDouble();
   }

   @Override
   public String readLine() throws IOException {
      ignoreFieldNumber();
      ;
      return input.readString();
   }

   @Override
   public String readUTF() throws IOException {
      ignoreFieldNumber();
      ByteBuffer byteBuffer = input.readByteBuffer();
      return Charset.forName("UTF-8").decode(byteBuffer).toString();
   }

   /**
    * Protostuff reads field numbers for each value stored in a buffer, however even though we don't use these we need
    * to ensure that they are still read to ensure that the underlying input buffer has the correct offset.
    *
    * @throws IOException
    */
   private void ignoreFieldNumber() throws IOException {
      input.readFieldNumber(null);
   }
}
