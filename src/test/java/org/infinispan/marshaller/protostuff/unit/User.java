package org.infinispan.marshaller.protostuff.unit;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
class User {
   String name;

   User(String name) {
      this.name = name;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      User that = (User) o;

      return name.equals(that.name);
   }

   @Override
   public int hashCode() {
      return name.hashCode();
   }

   @Override
   public String toString() {
      return "User{" +
            "name='" + name + '\'' +
            '}';
   }
}
