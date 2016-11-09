package org.infinispan.marshaller.protostuff.schemas;

import java.io.IOException;

import org.infinispan.marshaller.protostuff.ObjectWrapper;
import org.infinispan.marshaller.protostuff.ProtostuffObjectInput;
import org.infinispan.marshaller.protostuff.ProtostuffObjectOutput;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ObjectWrapperSchema implements Schema<ObjectWrapper> {
   @Override
   public String getFieldName(int number) {
      return null;
   }

   @Override
   public int getFieldNumber(String name) {
      return 0;
   }

   @Override
   public boolean isInitialized(ObjectWrapper message) {
      return message.getObject() != null;
   }

   @Override
   public ObjectWrapper newMessage() {
      return new ObjectWrapper();
   }

   @Override
   public String messageName() {
      return ObjectWrapper.class.getSimpleName();
   }

   @Override
   public String messageFullName() {
      return ObjectWrapper.class.getName();
   }

   @Override
   public Class<? super ObjectWrapper> typeClass() {
      return ObjectWrapper.class;
   }

   @Override
   public void mergeFrom(Input input, ObjectWrapper message) throws IOException {
      try {
         input.readFieldNumber(this); // Must read between each subsequent read on input
         Class clazz = Class.forName(input.readString());
         Schema schema = RuntimeSchema.getSchema(clazz);
         input.readFieldNumber(this);
         Object object = input.mergeObject(schema.newMessage(), schema);
         message.setInterfaceClazz(clazz);
         message.setObject(object);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeTo(Output output, ObjectWrapper message) throws IOException {
      Class clazz = message.getInterfaceClazz();
      output.writeString(1, clazz.getName(), false);
      Schema schema = RuntimeSchema.getSchema(clazz);
      output.writeObject(2, message.getObject(), schema, false);
   }
}
