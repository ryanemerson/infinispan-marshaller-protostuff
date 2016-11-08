package org.infinispan.marshaller.protostuff.delegates;

import java.io.IOException;
import java.io.ObjectOutput;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.marshaller.protostuff.ProtostuffObjectInput;
import org.infinispan.marshaller.protostuff.ProtostuffObjectOutput;

import io.protostuff.ByteArrayInput;
import io.protostuff.CodedInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffOutput;
import io.protostuff.Schema;
import io.protostuff.WireFormat;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeSchema;


/**
 * @author Ryan Emerson
 * @since 9.0
 */
final public class ReplicableCommandDelegate implements Delegate<ReplicableCommand> {

   private final Class<? extends ReplicableCommand> implementation;
   private final Schema<? extends ReplicableCommand> schema;

   public ReplicableCommandDelegate(Class<? extends ReplicableCommand> implementation) {
      this.implementation = implementation;
      this.schema = RuntimeSchema.getSchema(implementation);
   }

   @Override
   public WireFormat.FieldType getFieldType() {
      System.out.println("getFieldType");
      return WireFormat.FieldType.MESSAGE;
   }

   @Override
   public ReplicableCommand readFrom(Input input) throws IOException {
      System.out.println("ReadFrom");
      ReplicableCommand replicableCommand = schema.newMessage();
      try {
         replicableCommand.readFrom(new ProtostuffObjectInput(input, schema));
//         ByteArrayInput byteArrayInput = (ByteArrayInput) input;
//         byteArrayInput.setBounds(byteArrayInput.currentOffset() + 1, byteArrayInput.currentLimit());
         return replicableCommand;
      } catch (ClassNotFoundException e) {
         throw new IOException("Unable to deserialize object: " + e);
      }
   }

   @Override
   public void writeTo(Output output, int i, ReplicableCommand replicableCommand, boolean b) throws IOException {
      System.out.println("In delegate writeTo | " + i);
//      ObjectOutput objectOutput = new ProtostuffObjectOutput(i, b, output);
//      replicableCommand.writeTo(objectOutput);
//      objectOutput.writeByte(WireFormat.WIRETYPE_TAIL_DELIMITER);
   }

   @Override
   public void transfer(Pipe pipe, Input input, Output output, int i, boolean b) throws IOException {
      throw new UnsupportedOperationException("Delegate.transfer not yet implemented");
   }

   @Override
   public Class<?> typeClass() {
      return implementation;
   }
}
