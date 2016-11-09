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
   public EmbeddedObject obj2;
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
//      MarshallUtil.marshallString(msg, output);
//      output.writeUTF(msg);
//      output.writeBoolean(flag);
//      output.writeBoolean(flag2);
//      output.writeInt(id);
//      output.writeObject(obj);
//      output.writeByte(test);
//      EmbeddedObject e = new EmbeddedObject();
//      e.test = 3;
//      output.writeObject(e);
      MarshallUtil.marshallCollection(segments, output);
//      output.writeObject(new Integer(2));
//      output.writeObject(null);
   }

   @Override
   public void readFrom(ObjectInput input) throws IOException, ClassNotFoundException {
//      msg = MarshallUtil.unmarshallString(input);
//      msg = input.readUTF();
//      flag = input.readBoolean();
//      flag2 = input.readBoolean();
//      id = input.readInt();
//      test = input.readByte();
//      obj = (EmbeddedObject) input.readObject();
//      obj2 = (EmbeddedObject) input.readObject();
      segments = MarshallUtil.unmarshallCollectionUnbounded(input, HashSet::new);
//      id = (Integer) input.readObject();
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
            ", obj2=" + obj2 +
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