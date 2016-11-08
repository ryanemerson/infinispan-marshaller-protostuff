package org.infinispan.marshaller.protostuff.delegates;

import java.io.IOException;

import org.infinispan.commands.ReplicableCommand;
import org.jboss.marshalling.Externalize;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.WireFormat;
import io.protostuff.runtime.Delegate;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ExternalizerDelegate implements Delegate<Externalize> {

   private final Class<? extends Externalize> implementation;

   public ExternalizerDelegate(Class<? extends Externalize> implementation) {
      this.implementation = implementation;
   }

   @Override
   public WireFormat.FieldType getFieldType() {
      return WireFormat.FieldType.MESSAGE;
   }

   @Override
   public Externalize readFrom(Input input) throws IOException {
      return null;
   }

   @Override
   public void writeTo(Output output, int i, Externalize externalize, boolean b) throws IOException {

   }

   @Override
   public void transfer(Pipe pipe, Input input, Output output, int i, boolean b) throws IOException {

   }

   @Override
   public Class<?> typeClass() {
      return null;
   }
}
