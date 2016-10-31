package org.infinispan.marshaller.protostuff.unit;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
public class UserSchema implements Schema<User> {

   public static final AtomicInteger mergeFromCount = new AtomicInteger();
   public static final AtomicInteger writeToCount = new AtomicInteger();

   @Override
   public String getFieldName(int number) {
      return number == 1 ? "name" : null;
   }

   @Override
   public int getFieldNumber(String name) {
      return name.equals("name") ? 1 : 0;
   }

   @Override
   public boolean isInitialized(User user) {
      return user.name != null;
   }

   @Override
   public User newMessage() {
      return new User(null);
   }

   @Override
   public String messageName() {
      return User.class.getSimpleName();
   }

   @Override
   public String messageFullName() {
      return User.class.getName();
   }

   @Override
   public Class<? super User> typeClass() {
      return User.class;
   }

   @Override
   public void mergeFrom(Input input, User user) throws IOException {
      while (true) {
         int fieldNumber = input.readFieldNumber(this);
         switch (fieldNumber) {
            case 0:
               return;
            case 1:
               mergeFromCount.incrementAndGet();
               user.name = input.readString();
               break;
            default:
               input.handleUnknownField(fieldNumber, this);
         }
      }
   }

   @Override
   public void writeTo(Output output, User user) throws IOException {
      if (!this.isInitialized(user))
         throw new UninitializedMessageException(user, this);

      output.writeString(1, user.name, false);
      writeToCount.incrementAndGet();
   }
}
