package io.sphere.sdk.client;

import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

final class AsyncUtils {
    private AsyncUtils() {
    }

    public static <T> CompletableFuture<T> successful(final T object) {
        return CompletableFuture.completedFuture(object);
    }

    public static <T> Throwable blockForFailure(final CompletionStage<T> future) {
        try {
            future.toCompletableFuture().join();
            throw new NoSuchElementException(future + " did not complete exceptionally.");
        } catch (final CompletionException e1) {
            return e1.getCause();
        }
    }

    public static <T> CompletableFuture<T> failed(final Throwable e) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }

    public static <T> void transferResult(final CompletionStage<T> source,
                                          final CompletableFuture<T> target) {
        source.whenComplete((result, throwable) -> {
            final boolean isSuccessful = throwable == null;
            if (isSuccessful) {
                target.complete(result);
            } else {
                target.completeExceptionally(throwable);
            }
        });
    }

    public static <T> CompletionStage<T> onFailure(final CompletionStage<T> future, final Consumer<Throwable> consumer) {
        return future.whenCompleteAsync((value, throwable) -> {
            if (throwable != null) {
                consumer.accept(throwable);
            }
        });
    }

    public static <T> CompletionStage<T> onSuccess(final CompletionStage<T> future, final Consumer<T> consumer) {
        return future.whenCompleteAsync((value, throwable) -> {
            if (throwable == null) {
                consumer.accept(value);
            }
        });
    }

    public static <T> CompletionStage<T> recover(final CompletionStage<T> future, final Function<Throwable, T> f) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        future.whenComplete((value, e) -> {
            if (e == null) {
                result.complete(value);
            } else {
                final T recoveredResult = f.apply(e);
                result.complete(recoveredResult);
            }
        });
        return result;
    }

    public static <T> CompletionStage<T> recoverWith(final CompletionStage<T> future, final Function<Throwable, CompletionStage<T>> f) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        final BiConsumer<T, Throwable> action = (value, error) -> {
            if (value != null) {
                result.complete(value);
            } else {
                final CompletionStage<T> alternative = f.apply(error);
                alternative.whenComplete((alternativeValue, alternativeError) -> {
                    if (alternativeValue != null) {
                        result.complete(alternativeValue);
                    } else {
                        result.completeExceptionally(alternativeError);
                    }
                });
            }
        };
        future.whenComplete(action);
        return result;
    }

    public static <T, U> CompletionStage<U> flatMap(final CompletionStage<T> future, final Function<T, CompletionStage<U>> f) {
        return future.thenCompose(f);
    }
}