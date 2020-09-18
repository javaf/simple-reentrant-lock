import java.util.concurrent.locks.*;

// A lock is re-entrant if it can be acquired multiple
// times by the same thread. Simple Reentrant Lock
// uses owner thread ID and hold count fields to keep
// track of the owning thread and the number of times
// it holds the lock. A common lock is used for
// ensuring field updates are atomic, and a condition
// object is used for synchronization.
// 
// Acquiring the lock involves holding the common
// lock, waiting until there is no other thread
// holding it, updating owner thread ID (to current)
// and incrementing hold count before releasing the
// common lock.
// 
// Releasing the write lock involves holding the
// common lock, decrementing hold count, and if
// not holding anymore, signalling the others before
// releasing the common lock.
// 
// Java already provides a ReentrantLock. This is
// for educational purposes only.

class SimpleReentrantLock extends AbstractLock {
  Lock lock;
  Condition noHolder;
  long owner, holdCount;
  // lock: common lock
  // condition: indicates "no holder"
  // owner: thread ID of holding thread
  // holdCount: times lock was acquired by owner

  // 1. Acquire common lock.
  // 2. Wait until there is no other holder.
  // 3. Update owner, and increment hold count.
  // 4. Release common lock.
  @Override
  public void lock() {
    long id = Thread.currentThread().getId();
    lock.lock(); // 1
    try {
      while (owner != id && holdCount > 0) // 2
        noHolder.await();                  // 2
      owner = id;  // 3
      holdCount++; // 3
    }
    catch (InterruptedException e) {}
    finally { lock.unlock(); } // 4
  }

  // 1. Acquire common lock.
  // 2. Throw expection, if we dont hold it.
  // 3. Decrement hold count.
  // 4. If not holding anymore, signal others.
  // 5. Release common lock.
  @Override
  public void unlock() {
    long id = Thread.currentThread().getId();
    lock.lock(); // 1
    if (owner != id || holdCount == 0)          // 2
      throw new IllegalMonitorStateException(); // 2
    if(--holdCount == 0) noHolder.signal(); // 3, 4
    lock.unlock(); // 5
  }
}
