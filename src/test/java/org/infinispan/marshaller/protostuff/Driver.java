package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.marshaller.protostuff.delegates.ReplicableCommandDelegate;
import org.infinispan.marshaller.protostuff.schemas.ReplicableCommandSchema;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class Driver {

   public static void main(String[] args) throws Exception {
      if (RuntimeEnv.ID_STRATEGY instanceof DefaultIdStrategy) {
//         ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY).registerDelegate(new ReplicableCommandDelegate(Test.class));
      }
      RuntimeSchema.register(Test.class, new ReplicableCommandSchema<>(Test.class));
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
      Object o = marshaller.objectFromByteBuffer(buffer.getBuf());
      System.out.println("Object: " + o);
   }
}
