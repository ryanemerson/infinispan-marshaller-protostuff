package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.infinispan.commands.ReplicableCommand;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class FakeCommand implements ReplicableCommand {

   int test = 5;

   @Override
   public byte getCommandId() {
      return 0;
   }

   @Override
   public boolean isReturnValueExpected() {
      return false;
   }

   @Override
   public boolean canBlock() {
      return false;
   }

   @Override
   public void writeTo(ObjectOutput output) throws IOException {
      System.out.println("WRITE!");
   }

   @Override
   public void readFrom(ObjectInput input) throws IOException, ClassNotFoundException {

   }
}
