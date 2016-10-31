package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.commons.logging.Log;
import org.infinispan.commons.logging.LogFactory;
import org.infinispan.commons.marshall.AbstractMarshaller;
import org.infinispan.commons.marshall.StreamingMarshaller;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtoStuffMarshaller extends AbstractMarshaller implements StreamingMarshaller {

   private static final Log log = LogFactory.getLog(ProtoStuffMarshaller.class);
   private static final boolean trace = log.isTraceEnabled();
   private static final Schema<ObjectWrapper> WRAPPER_SCHEMA = RuntimeSchema.getSchema(ObjectWrapper.class);

   @Override
   public Object objectFromByteBuffer(byte[] bytes, int offset, int length) throws IOException, ClassNotFoundException {
      ObjectWrapper wrapper = WRAPPER_SCHEMA.newMessage();
      ProtostuffIOUtil.mergeFrom(bytes, offset, length, wrapper, WRAPPER_SCHEMA);

      Class clazz = Class.forName(wrapper.clazz);
      Schema schema = RuntimeSchema.getSchema(clazz);
      Object object = schema.newMessage();
      ProtostuffIOUtil.mergeFrom(wrapper.bytes, object, schema);
      return object;
   }

   @Override
   protected ByteBuffer objectToBuffer(Object o, int estimatedSize) throws IOException, InterruptedException {
      Schema schema = RuntimeSchema.getSchema(o.getClass());
      String className = o.getClass().getName();
      estimatedSize += className.length();

      LinkedBuffer linkedBuffer = LinkedBuffer.allocate(estimatedSize);
      byte[] bytes = ProtostuffIOUtil.toByteArray(o, schema, linkedBuffer);
      linkedBuffer.clear();

      bytes = ProtostuffIOUtil.toByteArray(new ObjectWrapper(className, bytes), WRAPPER_SCHEMA, linkedBuffer);
      return new ByteBufferImpl(bytes, 0, bytes.length);
   }

   @Override
   public boolean isMarshallable(Object o) throws Exception {
      return true;
   }

   @Override
   public ObjectOutput startObjectOutput(OutputStream os, boolean isReentrant, int estimatedSize) throws IOException {
      return new ObjectOutputStream(os);
   }

   @Override
   public void finishObjectOutput(ObjectOutput out) {
      try {
         out.flush();
         out.close();
      } catch (IOException e) {
         if (trace) log.trace("Unable to finish object output: " + e);
      }
   }

   @Override
   public void objectToObjectStream(Object obj, ObjectOutput out) throws IOException {
      try {
         out.write(objectToByteBuffer(obj));
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         if (trace) log.trace("Interrupted while writing to output stream");
      }
   }

   @Override
   public ObjectInput startObjectInput(InputStream is, boolean isReentrant) throws IOException {
      return new ObjectInputStream(is);
   }

   @Override
   public void finishObjectInput(ObjectInput in) {
      try {
         in.close();
      } catch (IOException e) {
         if (trace) log.trace("Unable to finish object output: " + e);
      }
   }

   @Override
   public Object objectFromObjectStream(ObjectInput in) throws IOException, ClassNotFoundException, InterruptedException {
      int len = in.available();
      int bufferSize = len > 0 ? Math.min(len, 1024) : 1024;
      byte[] bytes = new byte[bufferSize];
      in.read(bytes);
      return this.objectFromByteBuffer(bytes, 0, bufferSize);
   }

   @Override
   public void stop() {
   }

   @Override
   public void start() {
   }

   private class ObjectWrapper {
      private String clazz;
      private byte[] bytes;

      ObjectWrapper(String clazz, byte[] bytes) {
         this.clazz = clazz;
         this.bytes = bytes;
      }
   }
}
