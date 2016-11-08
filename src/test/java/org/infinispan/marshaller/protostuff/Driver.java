package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.marshaller.protostuff.delegates.ReplicableCommandDelegate;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeEnv;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class Driver {

   public static void main(String[] args) throws Exception {
      if (RuntimeEnv.ID_STRATEGY instanceof DefaultIdStrategy) {
//         ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY).registerDelegate(new ReplicableCommandDelegate(Test.class));
      }
      ProtoStuffMarshaller marshaller = new ProtoStuffMarshaller();

//      System.out.println("Driver: " + RuntimeEnv.ID_STRATEGY.isDelegateRegistered(StateRequestCommand.class));
//
//      ByteString cacheName = ByteString.fromString("cacheName");
//      Set<Integer> segments = IntStream.range(0, 256).boxed().collect(Collectors.toSet());
//      StateRequestCommand stateRequestCommand = new StateRequestCommand(cacheName, START_STATE_TRANSFER, null, 2, segments);
//      StateResponseCommand responseCommand = new StateResponseCommand(ByteString.fromString("Test"));
//      marshaller.objectToBuffer(responseCommand);
//
//      ByteBuffer buffer = marshaller.objectToBuffer(stateRequestCommand);
//      System.out.println(buffer.getBuf().length);
//
//      StateRequestCommand cmd = (StateRequestCommand) marshaller.objectFromByteBuffer(buffer.getBuf());
//      System.out.println(cmd);

      int x = (1 << 3) - 1;
      int tag = (1 << 3) | 4;
      int fn =  12 >>> 3;
      System.out.println(x);
      System.out.println(15 & x);
      System.out.println(tag);
      System.out.println(fn);

      Test t = new Test();
//      t.msg = "THIS IS A TEST!!!!!";
      t.flag = true;
      t.flag2 = true;
      t.id = 5;
      t.test = 0x11;
      ByteBuffer buffer = marshaller.objectToBuffer(t);
      System.out.println(marshaller.objectFromByteBuffer(buffer.getBuf()));
   }

   static class Test implements ReplicableCommand {
//      String msg;
      boolean flag;
      boolean flag2;
      int id;
      byte test;

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
//         MarshallUtil.marshallString(msg, output);
//         output.writeBoolean(flag);
//         output.writeBoolean(flag2);
//         output.writeInt(id);
         output.writeByte(test);
      }

      @Override
      public void readFrom(ObjectInput input) throws IOException, ClassNotFoundException {
         System.out.println("In Test readFrom");
//         msg = MarshallUtil.unmarshallString(input);
//         flag = input.readBoolean();
//         flag2 = input.readBoolean();
//         id = input.readInt();
         test = input.readByte();
      }

      @Override
      public String toString() {
         return "Test{" +
               "flag=" + flag +
               ", flag2=" + flag2 +
               ", id=" + id +
               ", test=" + test +
               '}';
      }
   }

   public static class TestSchema implements Schema<Test> {
      @Override
      public String getFieldName(int number) {
         return null;
      }

      @Override
      public int getFieldNumber(String name) {
         return 0;
      }

      @Override
      public boolean isInitialized(Test message) {
         return false;
      }

      @Override
      public Test newMessage() {
         return null;
      }

      @Override
      public String messageName() {
         return null;
      }

      @Override
      public String messageFullName() {
         return null;
      }

      @Override
      public Class<? super Test> typeClass() {
         return null;
      }

      @Override
      public void mergeFrom(Input input, Test message) throws IOException {
         System.out.println("mergeFrom in schema");
      }

      @Override
      public void writeTo(Output output, Test message) throws IOException {
         System.out.println("WriteTo in schema");
      }
   }
}
