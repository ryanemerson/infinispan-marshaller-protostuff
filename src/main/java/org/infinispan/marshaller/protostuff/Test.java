package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commons.marshall.MarshallUtil;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class Test implements ReplicableCommand {
   public String msg;
   public boolean flag;
   public boolean flag2;
   public int id;
   public byte test;
   public EmbeddedObject obj;
   public Collection<Integer> segments;

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
      System.out.println("In Test writeTo");
//      MarshallUtil.marshallString(msg, output);
//      output.writeBoolean(flag);
//      output.writeBoolean(flag2);
//      output.writeInt(id);
//      output.writeByte(test);
//      output.writeObject(obj);
//      output.writeObject(obj);
      MarshallUtil.marshallCollection(segments, output);
   }

   @Override
   public void readFrom(ObjectInput input) throws IOException, ClassNotFoundException {
      System.out.println("In Test readFrom");
//      msg = MarshallUtil.unmarshallString(input);
//      flag = input.readBoolean();
//      flag2 = input.readBoolean();
//      id = input.readInt();
//      test = input.readByte();
//      obj = (EmbeddedObject) input.readObject();
//      obj = (EmbeddedObject) input.readObject();
      segments = MarshallUtil.unmarshallCollectionUnbounded(input, HashSet::new);
   }

   @Override
   public String toString() {
      return "Test{" +
            "msg='" + msg + '\'' +
            ", flag=" + flag +
            ", flag2=" + flag2 +
            ", id=" + id +
            ", test=" + test +
            ", obj=" + obj +
            ", segments=" + segments +
            '}';
   }

   public static class EmbeddedObject {
      public int test;

      @Override
      public String toString() {
         return "EmbeddedObject{" +
               "test=" + test +
               '}';
      }
   }
}