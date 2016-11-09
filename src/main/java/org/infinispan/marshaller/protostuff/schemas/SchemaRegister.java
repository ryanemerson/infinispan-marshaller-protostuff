package org.infinispan.marshaller.protostuff.schemas;

import java.lang.reflect.Modifier;
import java.util.Set;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.marshaller.protostuff.Test;
import org.infinispan.statetransfer.StateRequestCommand;
import org.infinispan.statetransfer.StateResponseCommand;
import org.reflections.Reflections;

import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
final public class SchemaRegister {
   public static void init() {
      // Replicable Command Delegates
      register(org.infinispan.commands.tx.totalorder.TotalOrderCommitCommand.class);
      register(org.infinispan.topology.CacheTopologyControlCommand.class);
      register(org.infinispan.stream.impl.StreamSegmentResponseCommand.class);
      register(org.infinispan.xsite.SingleXSiteRpcCommand.class);
      register(org.infinispan.commands.read.DistributedExecuteCommand.class);
      register(org.infinispan.commands.CreateCacheCommand.class);
      register(org.infinispan.commands.remote.GetKeysInGroupCommand.class);
      register(org.infinispan.commands.write.PutMapCommand.class);
      register(org.infinispan.commands.CancelCommand.class);
      register(org.infinispan.commands.functional.ReadOnlyManyCommand.class);
      register(org.infinispan.commands.write.ApplyDeltaCommand.class);
      register(org.infinispan.commands.tx.CommitCommand.class);
      register(org.infinispan.commands.functional.ReadWriteKeyValueCommand.class);
      register(org.infinispan.commands.write.ClearCommand.class);
      register(org.infinispan.statetransfer.StateRequestCommand.class);
      register(org.infinispan.commands.tx.VersionedCommitCommand.class);
      register(org.infinispan.commands.remote.SingleRpcCommand.class);
      register(org.infinispan.commands.read.SizeCommand.class);
      register(org.infinispan.stream.impl.StreamRequestCommand.class);
      register(org.infinispan.manager.impl.ReplicableCommandManagerFunction.class);
      register(org.infinispan.commands.remote.recovery.TxCompletionNotificationCommand.class);
      register(org.infinispan.marshaller.protostuff.FakeCommand.class);
      register(org.infinispan.commands.control.LockControlCommand.class);
      register(org.infinispan.stream.impl.StreamResponseCommand.class);
      register(org.infinispan.commands.remote.ClusteredGetAllCommand.class);
      register(org.infinispan.xsite.statetransfer.XSiteStatePushCommand.class);
      register(org.infinispan.commands.remote.ClusteredGetCommand.class);
      register(org.infinispan.commands.tx.VersionedPrepareCommand.class);
      register(org.infinispan.xsite.XSiteAdminCommand.class);
      register(org.infinispan.commands.remote.recovery.CompleteTransactionCommand.class);
      register(org.infinispan.commands.write.EvictCommand.class);
      register(org.infinispan.commands.tx.totalorder.TotalOrderVersionedCommitCommand.class);
      register(org.infinispan.statetransfer.StateResponseCommand.class);
      register(org.infinispan.commands.tx.RollbackCommand.class);
      register(org.infinispan.xsite.statetransfer.XSiteStateTransferControlCommand.class);
      register(org.infinispan.commands.functional.ReadWriteKeyCommand.class);
      register(org.infinispan.commands.write.RemoveExpiredCommand.class);
      register(org.infinispan.commands.read.EntrySetCommand.class);
      register(org.infinispan.commands.functional.WriteOnlyManyCommand.class);
      register(org.infinispan.commands.write.InvalidateCommand.class);
      register(org.infinispan.commands.tx.PrepareCommand.class);
      register(org.infinispan.commands.tx.totalorder.TotalOrderRollbackCommand.class);
      register(org.infinispan.commands.functional.WriteOnlyKeyCommand.class);
      register(org.infinispan.commands.tx.totalorder.TotalOrderVersionedPrepareCommand.class);
      register(org.infinispan.commands.RemoveCacheCommand.class);
      register(org.infinispan.manager.impl.ReplicableCommandRunnable.class);
      register(org.infinispan.commands.tx.totalorder.TotalOrderNonVersionedPrepareCommand.class);
      register(org.infinispan.commands.functional.ReadWriteManyCommand.class);
      register(org.infinispan.commands.read.GetCacheEntryCommand.class);
      register(org.infinispan.commands.read.KeySetCommand.class);
      register(org.infinispan.commands.write.ReplaceCommand.class);
      register(org.infinispan.commands.read.GetAllCommand.class);
      register(org.infinispan.commands.remote.recovery.GetInDoubtTransactionsCommand.class);
      register(org.infinispan.commands.read.GetKeyValueCommand.class);
      register(org.infinispan.commands.write.InvalidateL1Command.class);
      register(org.infinispan.commands.functional.ReadOnlyKeyCommand.class);
      register(org.infinispan.commands.functional.ReadWriteManyEntriesCommand.class);
      register(org.infinispan.commands.write.RemoveCommand.class);
      register(org.infinispan.marshaller.protostuff.Test.class);
      register(org.infinispan.commands.write.PutKeyValueCommand.class);
      register(org.infinispan.commands.remote.recovery.GetInDoubtTxInfoCommand.class);
      register(org.infinispan.commands.functional.WriteOnlyManyEntriesCommand.class);
      register(org.infinispan.commands.functional.WriteOnlyKeyValueCommand.class);
   }

   private static void register(Class<? extends ReplicableCommand> clazz) {
      RuntimeSchema.register(clazz, new ReplicableCommandSchema(clazz));
   }

   public static void main(String[] args) {
      Reflections reflections = new Reflections("org.infinispan");
      Set<Class<? extends ReplicableCommand>> classSet = reflections.getSubTypesOf(ReplicableCommand.class);
      classSet.stream()
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .forEach(clazz -> System.out.println(String.format("register(%s.class);", clazz.getName())));
   }
}
