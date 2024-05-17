## Micronaut 4.4.2 Documentation

Issue with Micronaut's transactional annotation using Kotlin suspend functions when throwing an exception that extends `kotlin.Throwable`

## Versions

- Micronaut 4.4.2
- Kotlin 1.9.23
- Kotlin coroutines 1.8.0
- Java 21

## Running locally

* Start the PostgreSQL server locally
    * Under the root directory of this git repo, execute `docker compose up -d`
* Run the test to reproduce the issue in another console `./gradlew -i test --rerun-tasks --tests "com.example.DemoTest.hikariIssue"`

## Possible root cause

Throwing an exception that extends `kotlin.Throwable` in a suspend function causes the transaction to be rolled back, but the connection is not released back to the pool.


We cannot reach the following part:
```kotlin
io.micronaut.transaction.async.AsyncUsingSyncTransactionOperations.withTransaction

result.whenComplete((o, throwable) -> {
    if (throwable == null) {
        synchronousTransactionManager.commit(status);
        newResult.complete(o);
    } else {
        try {
            synchronousTransactionManager.rollback(status);
        } catch (Exception e) {
            // Ignore rethrow
        }
        newResult.completeExceptionally(throwable);
    }
});
```
