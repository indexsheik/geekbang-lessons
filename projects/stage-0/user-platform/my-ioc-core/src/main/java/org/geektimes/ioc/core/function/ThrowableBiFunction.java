package org.geektimes.ioc.core.function;

@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws Throwable;

    /**
     * Executes {@link ThrowableBiFunction}
     *
     * @param t the function argument
     * @return the function result
     * @throws RuntimeException wrappers {@link Throwable}
     */
    default R execute(T t, U u) throws RuntimeException {
        R result = null;
        try {
            result = apply(t, u);
        } catch (Throwable e) {
            throw new RuntimeException(e.getCause());
        }
        return result;
    }

    /**
     * Executes {@link ThrowableBiFunction}
     *
     * @param t        the function argument
     * @param function {@link ThrowableBiFunction}
     * @param <T>      the source type
     * @param <R>      the return type
     * @return the result after execution
     */
    static <T, U, R> R execute(T t, U u, ThrowableBiFunction<T, U, R> function) {
        return function.execute(t, u);
    }
}

