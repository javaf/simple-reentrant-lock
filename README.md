A lock is re-entrant if it can be acquired multiple
times by the same thread. Simple Reentrant Lock
uses owner thread ID and hold count fields to keep
track of the owning thread and the number of times
it holds the lock. A common lock is used for
ensuring field updates are atomic, and a condition
object is used for synchronization.

Acquiring the lock involves holding the common
lock, waiting until there is no other thread
holding it, updating owner thread ID (to current)
and incrementing hold count before releasing the
common lock.

Releasing the write lock involves holding the
common lock, decrementing hold count, and if
not holding anymore, signalling the others before
releasing the common lock.

Java already provides a ReentrantLock. This is
for educational purposes only.

```java
lock():
1. Acquire common lock.
2. Wait until there is no other holder.
3. Update owner, and increment hold count.
4. Release common lock.
```

```java
unlock():
1. Acquire common lock.
2. Throw expection, if we dont hold it.
3. Decrement hold count.
4. If not holding anymore, signal others.
5. Release common lock.
```

See [SimpleReentrantLock.java] for code, [Main.java] for test, and [repl.it] for output.

[SimpleReentrantLock.java]: https://repl.it/@wolfram77/simple-reentrant-lock#SimpleReentrantLock.java
[Main.java]: https://repl.it/@wolfram77/simple-reentrant-lock#Main.java
[repl.it]: https://simple-reentrant-lock.wolfram77.repl.run


### references

- [The Art of Multiprocessor Programming :: Maurice Herlihy, Nir Shavit](https://dl.acm.org/doi/book/10.5555/2385452)
