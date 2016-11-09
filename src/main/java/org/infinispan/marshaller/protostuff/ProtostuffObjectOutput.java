package org.infinispan.marshaller.protostuff;

import static io.protostuff.WireFormat.WIRETYPE_END_GROUP;
import static io.protostuff.WireFormat.WIRETYPE_START_GROUP;
import static io.protostuff.WireFormat.WIRETYPE_TAIL_DELIMITER;

import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import javax.annotation.Nonnull;

import io.protostuff.ByteString;
import io.protostuff.LinkedBuffer;
import io.protostuff.Output;
import io.protostuff.ProtostuffOutput;
import io.protostuff.Schema;
import io.protostuff.WireFormat;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtostuffObjectOutput implements ObjectOutput {

   private int fieldNumber;
   private final boolean repeatable;
   private final Output output;

   public ProtostuffObjectOutput(Output output) throws IOException, SecurityException {
      this(output, 1, false);
   }

   public ProtostuffObjectOutput(Output output, int fieldNumber, boolean repeatable) throws IOException, SecurityException {
      this.output = output;
      this.fieldNumber = fieldNumber;
      this.repeatable = repeatable;
   }

   @Override
   public void writeObject(Object obj) throws IOException {
      writeRawByte((byte) WireFormat.makeTag(fieldNumber, WIRETYPE_START_GROUP));
      if (obj == null) {
         writeRawByte((byte) WireFormat.makeTag(fieldNumber, WIRETYPE_END_GROUP));
         return;
      }
      Class clazz = obj.getClass();
      System.out.println("CLazzz field number: " + fieldNumber);
      output.writeString(fieldNumber, clazz.getName(), false);
      RuntimeSchema.getSchema(clazz).writeTo(output, obj);
      System.out.println("END_GROUP field number: " + fieldNumber);
      writeRawByte((byte) WireFormat.makeTag(fieldNumber, WIRETYPE_END_GROUP));
      fieldNumber++;
   }

   private void writeRawByte(byte v) throws IOException {
      ProtostuffOutput protostuffOutput = (ProtostuffOutput) output;
      protostuffOutput.sink.writeByte(v, protostuffOutput, protostuffOutput.head);

   }

   @Override
   public void write(int b) throws IOException {
      output.writeInt32(fieldNumber++, (byte) b, repeatable);
   }

   @Override
   public void write(byte[] b) throws IOException {
      output.writeByteArray(fieldNumber++, b, repeatable);
   }

   @Override
   public void write(byte[] b, int off, int len) throws IOException {
      output.writeByteRange(false, fieldNumber++, b, off, len, repeatable);
   }

   @Override
   public void flush() throws IOException {
      // TODO do we need to implement this?
//      output.flushHandler.flush(output, 0, ?);
   }

   @Override
   public void close() throws IOException {
      if (output instanceof ProtostuffOutput)
         ((ProtostuffOutput) output).clear();
   }

   @Override
   public void writeBoolean(boolean v) throws IOException {
      output.writeBool(fieldNumber++, v, repeatable);
   }

   @Override
   public void writeByte(int v) throws IOException {
      output.writeInt32(fieldNumber++, (byte) v, repeatable);
   }

   @Override
   public void writeShort(int v) throws IOException {
      output.writeInt32(fieldNumber++, (short) v, repeatable);
   }

   @Override
   public void writeChar(int v) throws IOException {
      output.writeInt32(fieldNumber++, (char) v, repeatable);
   }

   @Override
   public void writeInt(int v) throws IOException {
      output.writeInt32(fieldNumber++, v, repeatable);
   }

   @Override
   public void writeLong(long v) throws IOException {
      output.writeInt64(fieldNumber++, v, repeatable);
   }

   @Override
   public void writeFloat(float v) throws IOException {
      output.writeFloat(fieldNumber++, v, repeatable);
   }

   @Override
   public void writeDouble(double v) throws IOException {
      output.writeDouble(fieldNumber++, v, repeatable);
   }

   @Override
   public void writeBytes(@Nonnull String s) throws IOException {
      Objects.requireNonNull(s);
      ByteString byteString = ByteString.copyFrom(s, "UTF-16");
      output.writeBytes(fieldNumber++, byteString, repeatable);
   }

   @Override
   public void writeChars(String s) throws IOException {
      throw new UnsupportedOperationException("writeChars not yet implemented");
   }

   @Override
   public void writeUTF(@Nonnull String s) throws IOException {
      Objects.requireNonNull(s);
      ByteBuffer buffer = Charset.forName("UTF-8").encode(s);
      output.writeByteRange(true, fieldNumber++, buffer.array(), 0, buffer.limit(), repeatable);
   }
}
