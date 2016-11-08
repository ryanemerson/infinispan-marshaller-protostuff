package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.ObjectInput;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.protostuff.ByteArrayInput;
import io.protostuff.ByteBufferInput;
import io.protostuff.GraphByteArrayInput;
import io.protostuff.Input;
import io.protostuff.Schema;
import io.protostuff.runtime.PolymorphicSchemaFactories;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtostuffObjectInput implements ObjectInput {

   private final Input input;
   private final Schema schema;

   public ProtostuffObjectInput(Input input, Schema schema) {
      this.input = input;
      this.schema = schema;
   }

   @Override
   public Object readObject() throws ClassNotFoundException, IOException {
      return input.mergeObject(schema.newMessage(), schema);
   }

   @Override
   public int read() throws IOException {
      return (byte) input.readInt32();
   }

   @Override
   public int read(byte[] b) throws IOException {
      return read(b, 0, b.length);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      byte[] bytes =  input.readByteArray();
      System.arraycopy(bytes, off, b, 0, len);
      resetInputBuffer(bytes.length);
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
      return input.readBool();
   }

   @Override
   public byte readByte() throws IOException {
      return (byte) input.readInt32();
   }

   @Override
   public int readUnsignedByte() throws IOException {
      return (byte) input.readUInt32();
   }

   @Override
   public short readShort() throws IOException {
      return (short) input.readInt32();
   }

   @Override
   public int readUnsignedShort() throws IOException {
      return (short) input.readInt32();
   }

   @Override
   public char readChar() throws IOException {
      return (char) input.readInt32();
   }

   @Override
   public int readInt() throws IOException {
      return input.readInt32();
   }

   @Override
   public long readLong() throws IOException {
      return input.readInt64();
   }

   @Override
   public float readFloat() throws IOException {
      return input.readFloat();
   }

   @Override
   public double readDouble() throws IOException {
      return input.readDouble();
   }

   @Override
   public String readLine() throws IOException {
      return input.readString();
   }

   @Override
   public String readUTF() throws IOException {
      ByteBuffer byteBuffer = input.readByteBuffer();
      return Charset.forName("UTF-8").decode(byteBuffer).toString();
   }

   private void resetInputBuffer(int off) {
      if (input instanceof ByteArrayInput) {
         ByteArrayInput byteArrayInput = (ByteArrayInput) input;
         int limit = byteArrayInput.currentLimit();
         byteArrayInput.setBounds(off, limit);
      }
   }
}
