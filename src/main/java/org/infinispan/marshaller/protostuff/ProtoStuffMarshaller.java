package org.infinispan.marshaller.protostuff;

import java.io.IOException;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.marshall.BufferSizePredictor;
import org.infinispan.commons.marshall.Marshaller;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtoStuffMarshaller implements Marshaller {

   @Override
   public byte[] objectToByteBuffer(Object o, int i) throws IOException, InterruptedException {
      return new byte[0];
   }

   @Override
   public byte[] objectToByteBuffer(Object o) throws IOException, InterruptedException {
      return new byte[0];
   }

   @Override
   public Object objectFromByteBuffer(byte[] bytes) throws IOException, ClassNotFoundException {
      return null;
   }

   @Override
   public Object objectFromByteBuffer(byte[] bytes, int i, int i1) throws IOException, ClassNotFoundException {
      return null;
   }

   @Override
   public ByteBuffer objectToBuffer(Object o) throws IOException, InterruptedException {
      return null;
   }

   @Override
   public boolean isMarshallable(Object o) throws Exception {
      return false;
   }

   @Override
   public BufferSizePredictor getBufferSizePredictor(Object o) {
      return null;
   }
}
