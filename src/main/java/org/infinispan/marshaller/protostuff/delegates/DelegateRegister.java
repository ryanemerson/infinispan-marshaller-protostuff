package org.infinispan.marshaller.protostuff.delegates;

import java.lang.reflect.Modifier;
import java.util.Set;

import org.infinispan.commands.ReplicableCommand;
import org.infinispan.statetransfer.StateRequestCommand;
import org.infinispan.statetransfer.StateResponseCommand;
import org.reflections.Reflections;

import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;

/**
 * @author Ryan Emerson
 * @since 9.0
 */
final public class DelegateRegister {
   public static void init() {
      // Replicable Command Delegates
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.VersionedCommitCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.topology.CacheTopologyControlCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.SingleRpcCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.xsite.statetransfer.XSiteStatePushCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.statetransfer.StateRequestCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.KeySetCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.ClearCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.totalorder.TotalOrderNonVersionedPrepareCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.WriteOnlyKeyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.EvictCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.PutMapCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.InvalidateL1Command.class));
      register(new ReplicableCommandDelegate(org.infinispan.stream.impl.StreamResponseCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.totalorder.TotalOrderVersionedCommitCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.ClusteredGetAllCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.GetKeysInGroupCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.statetransfer.StateResponseCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.DistributedExecuteCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.recovery.GetInDoubtTransactionsCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.xsite.SingleXSiteRpcCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.PrepareCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.CommitCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.xsite.XSiteAdminCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.ClusteredGetCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.xsite.statetransfer.XSiteStateTransferControlCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.totalorder.TotalOrderVersionedPrepareCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.CancelCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.stream.impl.StreamSegmentResponseCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.InvalidateCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.RemoveExpiredCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.control.LockControlCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.recovery.TxCompletionNotificationCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.WriteOnlyKeyValueCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadWriteKeyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.SizeCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.manager.impl.ReplicableCommandManagerFunction.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadWriteManyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.RollbackCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.GetAllCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.totalorder.TotalOrderCommitCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.EntrySetCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.stream.impl.StreamRequestCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.totalorder.TotalOrderRollbackCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.CreateCacheCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.RemoveCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.tx.VersionedPrepareCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.WriteOnlyManyEntriesCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadOnlyKeyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.ApplyDeltaCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadOnlyManyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.recovery.GetInDoubtTxInfoCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.GetCacheEntryCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.RemoveCacheCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadWriteManyEntriesCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.marshaller.protostuff.FakeCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.WriteOnlyManyCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.PutKeyValueCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.manager.impl.ReplicableCommandRunnable.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.write.ReplaceCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.remote.recovery.CompleteTransactionCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.read.GetKeyValueCommand.class));
      register(new ReplicableCommandDelegate(org.infinispan.commands.functional.ReadWriteKeyValueCommand.class));
   }

   private static void register(Delegate delegate) {
      ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY).registerDelegate(delegate);
   }

   public static void initReplicableCommandDelegates() {

   }

   public static void main(String[] args) {
      Reflections reflections = new Reflections("org.infinispan");
      Set<Class<? extends ReplicableCommand>> classSet = reflections.getSubTypesOf(ReplicableCommand.class);
      classSet.stream()
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .forEach(clazz -> System.out.println(String.format("register(new ReplicableCommandDelegate(%s.class));", clazz.getName())));
   }
}
