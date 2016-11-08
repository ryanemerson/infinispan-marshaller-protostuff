package org.infinispan.marshaller.protostuff;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class ObjectWrapper {

   private Class interfaceClazz;
   private Object object;


   public ObjectWrapper() {}

   public ObjectWrapper(Class interfaceClazz, Object object) {
//      this.interfaceClazz = object.getClass().getName();
      this.interfaceClazz = interfaceClazz;
      this.object = object;
   }

   public Class getInterfaceClazz() {
      return interfaceClazz;
   }

   public void setInterfaceClazz(Class interfaceClazz) {
      this.interfaceClazz = interfaceClazz;
   }

   public void setObject(Object object) {
      this.object = object;
   }

   public Object getObject() {
      return object;
   }
}
