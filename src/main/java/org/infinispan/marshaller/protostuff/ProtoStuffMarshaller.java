package org.infinispan.marshaller.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.commons.logging.Log;
import org.infinispan.commons.logging.LogFactory;
import org.infinispan.commons.marshall.AbstractMarshaller;
import org.infinispan.commons.marshall.StreamingMarshaller;
import org.infinispan.marshaller.protostuff.delegates.DelegateRegister;
import org.infinispan.marshaller.protostuff.schemas.ObjectWrapperSchema;
import org.infinispan.marshaller.protostuff.schemas.ReplicableCommandSchema;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ProtoStuffMarshaller extends AbstractMarshaller implements StreamingMarshaller {

   static {
      DelegateRegister.init();
      RuntimeSchema.register(ObjectWrapper.class, new ObjectWrapperSchema());
//      RuntimeSchema.register(ReplicableCommand.class, new ReplicableCommandSchema());
   }

   private static final Log log = LogFactory.getLog(ProtoStuffMarshaller.class);
   private static final boolean trace = log.isTraceEnabled();
   private static final Schema<ObjectWrapper> WRAPPER_SCHEMA = RuntimeSchema.getSchema(ObjectWrapper.class);

   @Override
   public Object objectFromByteBuffer(byte[] bytes, int offset, int length) throws IOException, ClassNotFoundException {
      ObjectWrapper wrapper = WRAPPER_SCHEMA.newMessage();
      ProtostuffIOUtil.mergeFrom(bytes, offset, length, wrapper, WRAPPER_SCHEMA);
      return wrapper.getObject();
   }

   @Override
   protected ByteBuffer objectToBuffer(Object obj, int estimatedSize) throws IOException, InterruptedException {
      ObjectWrapper wrapper = new ObjectWrapper(Test.class, obj);
      byte[] bytes = ProtostuffIOUtil.toByteArray(wrapper, WRAPPER_SCHEMA, LinkedBuffer.allocate());
      return new ByteBufferImpl(bytes, 0, bytes.length);
   }

   @Override
   public boolean isMarshallable(Object obj) throws Exception {
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

   // TODO update
   @Override
   public void objectToObjectStream(Object obj, ObjectOutput out) throws IOException {
      ProtostuffIOUtil.writeDelimitedTo(out, new ObjectWrapper(Test.class, obj), WRAPPER_SCHEMA);
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
         if (trace) log.trace("Unable to finish object input: " + e);
      }
   }

   @Override
   public Object objectFromObjectStream(ObjectInput in) throws IOException, ClassNotFoundException, InterruptedException {
      ObjectWrapper wrapper = WRAPPER_SCHEMA.newMessage();
      ProtostuffIOUtil.mergeDelimitedFrom(in, wrapper, WRAPPER_SCHEMA);
      return wrapper.getObject();
   }

   @Override
   public void stop() {
   }

   @Override
   public void start() {
   }
}
