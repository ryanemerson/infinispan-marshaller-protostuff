package org.infinispan.marshaller.protostuff.schemas;

import java.io.IOException;
import java.io.ObjectInput;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.marshaller.protostuff.ProtostuffObjectInput;
import org.infinispan.marshaller.protostuff.ProtostuffObjectOutput;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeEnv;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ReplicableCommandSchema<T extends ReplicableCommand> implements Schema<T> {

   private final Class<T> implementation;

   public ReplicableCommandSchema(Class<T> implementation) {
      this.implementation = implementation;
   }

   @Override
   public String getFieldName(int number) {
      System.out.println("getFieldName");
      return null;
   }

   @Override
   public int getFieldNumber(String name) {
      System.out.println("getFieldNumber");
      return 0;
   }

   @Override
   public boolean isInitialized(ReplicableCommand message) {
      return true;
   }

   @Override
   public T newMessage() {
      try {
         return RuntimeEnv.newInstantiator(implementation).newInstance();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public String messageName() {
      return implementation.getSimpleName();
   }

   @Override
   public String messageFullName() {
      return implementation.getName();
   }

   @Override
   public Class typeClass() {
      return implementation;
   }

   @Override
   public void mergeFrom(Input input, ReplicableCommand message) throws IOException {
      System.out.println("mergeFrom in RC schema");

      try (ObjectInput objectInput = new ProtostuffObjectInput(input)) {
         message.readFrom(objectInput);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeTo(Output output, ReplicableCommand message) throws IOException {
      System.out.println("writeTo in RC schema");
      ProtostuffObjectOutput objectOutput = new ProtostuffObjectOutput(output, 1, false);
      message.writeTo(objectOutput);
   }
}
