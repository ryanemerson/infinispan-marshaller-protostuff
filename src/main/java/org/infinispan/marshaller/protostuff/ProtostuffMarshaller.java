package org.infinispan.marshaller.protostuff;

import static io.protostuff.LinkedBuffer.MIN_BUFFER_SIZE;

import java.io.IOException;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.commons.marshall.AbstractMarshaller;
import org.infinispan.commons.marshall.Marshaller;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtostuffMarshaller extends AbstractMarshaller implements Marshaller {

   private static final Schema<Wrapper> WRAPPER_SCHEMA = RuntimeSchema.getSchema(Wrapper.class);

   @Override
   public Object objectFromByteBuffer(byte[] bytes, int offset, int length) throws IOException, ClassNotFoundException {
      Wrapper wrapper = WRAPPER_SCHEMA.newMessage();
      ProtostuffIOUtil.mergeFrom(bytes, offset, length, wrapper, WRAPPER_SCHEMA);
      return wrapper.object;
   }

   @Override
   protected ByteBuffer objectToBuffer(Object obj, int estimatedSize) throws IOException, InterruptedException {
      if (obj instanceof byte[]) {
         byte[] bytes = (byte[]) obj;
         return new ByteBufferImpl(bytes, 0, bytes.length);
      }

      LinkedBuffer buffer = estimatedSize < MIN_BUFFER_SIZE ? LinkedBuffer.allocate(MIN_BUFFER_SIZE) : LinkedBuffer.allocate(estimatedSize);
      byte[] bytes = ProtostuffIOUtil.toByteArray(new Wrapper(obj), WRAPPER_SCHEMA, buffer);
      return new ByteBufferImpl(bytes, 0, bytes.length);
   }

   @Override
   public boolean isMarshallable(Object obj) throws Exception {
      try {
         objectToBuffer(obj);
         return true;
      } catch (Throwable t) {
         return false;
      }
   }
}
