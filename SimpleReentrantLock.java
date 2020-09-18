import java.util.concurrent.locks.*;

// FIFO Read-write Lock uses a counter and a
// boolean flag to keep track of multiple readers
// and waiting writer, but does not prioritize writers.
// A common lock is used to ensure internal
// updates happen atomically and a common
// condition is used for indicating either "no
// reader" or "no writer".
// 
// Acquiring the read lock involves holding the
// common lock, waiting until there is no writer,
// and finally incrementing the readers count.
// Releasing the read lock involves holding the
// common lock, decrementing the reader count, and
// signalling any writer/readers.
// 
// Acquiring the write lock involves holding the
// common lock, waiting until there are no writers
// and readers, and finally indicating presence of
// a writer. Releasing the write lock involves
// involves holding the common lock, indicating
// absence of writer, and signalling any
// writer/readers.
// 
// Even though the algorithm is correct, it is not
// quite satisfactory. If readers are much more
// frequent than writers, as is usually the case,
// the writers could be locked out for a long
// period of time by a continual stream of readers.
// Due to this lack of writer prioritization, this
// type of lock is generally only suitable for
// educational purposes.

class SimpleReentrantLock extends AbstractLock {
  Lock lock;
  Condition noHolder;
  long owner, holdCount;
  // lock: common lock
  // condition: indicates "no holder"
  // readers: number of readers accessing
  // writer: indicates if writer is accessing

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
  // 4. Signal others that no one is holding.
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
