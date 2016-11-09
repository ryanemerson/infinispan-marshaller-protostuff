package org.infinispan.marshaller.protostuff;

import static org.infinispan.statetransfer.StateRequestCommand.Type.GET_TRANSACTIONS;
import static org.infinispan.statetransfer.StateRequestCommand.Type.START_STATE_TRANSFER;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.marshaller.protostuff.schemas.ReplicableCommandSchema;
import org.infinispan.statetransfer.StateRequestCommand;
import org.infinispan.statetransfer.StateResponseCommand;
import org.infinispan.util.ByteString;

import io.protostuff.WireFormat;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class Driver {

   public static void main(String[] args) throws Exception {
      RuntimeSchema.register(Test.class, new ReplicableCommandSchema<>(Test.class));
      ProtoStuffMarshaller marshaller = new ProtoStuffMarshaller();

      ByteString cacheName = ByteString.fromString("cacheName");
      Set<Integer> segments = IntStream.range(0, 256).boxed().collect(Collectors.toSet());
      StateRequestCommand stateRequestCommand = new StateRequestCommand(cacheName, GET_TRANSACTIONS, null, 2, segments);
//      StateResponseCommand responseCommand = new StateResponseCommand(ByteString.fromString("Test"), null, 5, new ArrayList<>());

//      ByteBuffer buffer = marshaller.objectToBuffer(responseCommand);
//      ByteBuffer buffer = marshaller.objectToBuffer(stateRequestCommand);
//      System.out.println(buffer.getBuf().length);
//      System.out.println(marshaller.objectFromByteBuffer(buffer.getBuf()));

//      int tag = WireFormat.makeTag(4, WireFormat.WIRETYPE_TAIL_DELIMITER);
      int tag = WireFormat.makeTag(0, WireFormat.WIRETYPE_END_GROUP);
      tag = 8;
      tag = 12;
      int fieldNumber = 18 >>> 3;
      int tagTypeMask = (1 << 3) - 1;
      int result = (tag & tagTypeMask);
      System.out.println(fieldNumber);
      System.out.println(result);
      System.out.println(result == WireFormat.WIRETYPE_END_GROUP);

      Test t = new Test();
      t.msg = "THIS IS A TEST!!!!!";
      t.flag = true;
      t.flag2 = true;
      t.id = 5;
      t.test = -128;
      t.obj = new Test.EmbeddedObject();
      t.obj.test = 20;
//      t.segments = null;
//      t.segments = IntStream.range(0, 1).boxed().collect(Collectors.toSet());
      t.segments = IntStream.range(1, 3).boxed().collect(Collectors.toSet());

//      ByteBuffer buffer = marshaller.objectToBuffer((byte) 0x80);
      ByteBuffer buffer = marshaller.objectToBuffer(t);
      Object o = marshaller.objectFromByteBuffer(buffer.getBuf());
      System.out.println("Object: " + o);

   }
}
