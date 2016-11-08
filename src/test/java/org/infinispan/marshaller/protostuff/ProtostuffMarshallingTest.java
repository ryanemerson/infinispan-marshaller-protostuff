package org.infinispan.marshaller.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.infinispan.commons.marshall.StreamingMarshaller;
import org.infinispan.commons.util.ImmutableListCopy;
import org.infinispan.test.AbstractInfinispanTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.protostuff.Input;
import io.protostuff.LinkedBuffer;
import io.protostuff.Output;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
@Test
public class ProtostuffMarshallingTest extends AbstractInfinispanTest {

   private final AtomicInteger mergeFromCount = new AtomicInteger();
   private final AtomicInteger writeToCount = new AtomicInteger();
   private final StreamingMarshaller marshaller = new ProtoStuffMarshaller();
   private final List<TestObject> testObjects = IntStream.range(1, 2).boxed().map(this::getTestObject).collect(Collectors.toList());
   private ByteArrayOutputStream outputStream;

   @BeforeTest
   public void setup() {
      RuntimeSchema.register(User.class, new UserSchema());
   }

   @BeforeMethod(alwaysRun =  true)
   public void init() {
      outputStream = new ByteArrayOutputStream(1024);
   }

   @AfterMethod(alwaysRun = true)
   public void reset() throws Exception {
      mergeFromCount.set(0);
      writeToCount.set(0);
      outputStream.close();
   }

   private TestObject getTestObject(int id) {
      TestObject testObject = new TestObject(id);
      Map<Integer, String> map = new HashMap<>();
      map.put(id, "Test" + id);
      testObject.map = map;
      testObject.list = IntStream.range(0, id).boxed().collect(Collectors.toList());
      testObject.user = new User("User" + id);
      return testObject;
   }

   public void testInputOutputStreams() throws Exception {
      try (ObjectOutput output = marshaller.startObjectOutput(outputStream, false, 0)) {
         for (TestObject object : testObjects) {
            marshaller.objectToObjectStream(object, output);
         }
         marshaller.finishObjectOutput(output);
      }

      byte[] bytes = outputStream.toByteArray();
      try (ObjectInput input = marshaller.startObjectInput(new ByteArrayInputStream(bytes), false)) {
         for (TestObject object : testObjects) {
            Object obj = marshaller.objectFromObjectStream(input);
            assert object.equals(obj);
         }
         marshaller.finishObjectInput(input);
      }
   }

   public void testRegisterSchemasAreUtilised() throws Exception {
      TestObject obj = testObjects.get(0);
      byte[] bytes = marshaller.objectToByteBuffer(obj);
      assert writeToCount.get() == 1;
      assert marshaller.objectFromByteBuffer(bytes).equals(obj);
      assert mergeFromCount.get() == 1;
   }

//   public void testImmutableCopyList() throws IOException, ClassNotFoundException, InterruptedException {
//      try (ObjectOutput output = marshaller.startObjectOutput(outputStream, false, 0)) {
//         ImmutableListCopy<TestObject> immutableListCopy = new ImmutableListCopy<>(testObjects);
//         marshaller.objectToObjectStream(immutableListCopy, output);
//         marshaller.finishObjectOutput(output);
//      }
//
//      byte[] bytes = outputStream.toByteArray();
//      try (ObjectInput input = marshaller.startObjectInput(new ByteArrayInputStream(bytes), false)) {
//         ImmutableListCopy<TestObject> newList = (ImmutableListCopy<TestObject>) marshaller.objectFromObjectStream(input);
//         marshaller.finishObjectInput(input);
//
//         for (int i = 0; i < testObjects.size(); i++) {
//            assert newList.get(0).equals(testObjects.get(0));
//         }
//      }
//   }

   private static class TestObject {
      int id;
      Map<Integer, String> map;
      List<Integer> list;
      User user;

      TestObject(int id) {
         this.id = id;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         TestObject that = (TestObject) o;

         if (id != that.id) return false;
         if (map != null ? !map.equals(that.map) : that.map != null) return false;
         if (list != null ? !list.equals(that.list) : that.list != null) return false;
         return user != null ? user.equals(that.user) : that.user == null;

      }

      @Override
      public int hashCode() {
         int result = id;
         result = 31 * result + (map != null ? map.hashCode() : 0);
         result = 31 * result + (list != null ? list.hashCode() : 0);
         result = 31 * result + (user != null ? user.hashCode() : 0);
         return result;
      }

      @Override
      public String toString() {
         return "TestObject{" +
               "id=" + id +
               ", map=" + map +
               ", list=" + list +
               ", user=" + user +
               '}';
      }
   }

   private static class User {
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

   private class UserSchema implements Schema<User> {
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
}
