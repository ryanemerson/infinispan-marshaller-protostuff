package org.infinispan.marshaller.protostuff;

import io.protostuff.Tag;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ObjectWrapper {
   @Tag(1)
   private Object object;

   ObjectWrapper(Object object) {
      this.object = object;
   }

   public void setObject(Object object) {
      this.object = object;
   }

   public Object getObject() {
      return object;
   }
}
