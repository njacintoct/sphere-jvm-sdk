package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

import java.io.Closeable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

abstract class Actor extends Base implements Closeable {
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);//kind of mailbox

    public final void tell(final Object message) {
        executor.execute(() -> receive(message));
    }

    @Override
    public final void close() {
        closeInternal();
        executor.shutdown();
    }

    protected final ReceiveBuilder receiveBuilder(final Object message) {
        return new ReceiveBuilder(message);
    }

    protected static class ReceiveBuilder {
        private final Object message;
        private boolean done = false;

        public ReceiveBuilder(final Object message) {
            this.message = message;
        }

        @SuppressWarnings("unchecked")
        protected <T> ReceiveBuilder when(final Class<T> type, final Consumer<T> consumer) {
            if (!done && type.isAssignableFrom(message.getClass())) {
                consumer.accept((T) message);
            }
            return this;
        }
    }

    protected abstract void closeInternal();
    protected abstract void receive(final Object message);
}
