package cn.nukkit.utils.functional

import java.util.*
import java.util.function.Function

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the three-arity specialization of [Function].
 *
 *
 * This is a functional interface
 * whose functional method is [.apply].
 *
 * @param <F> the type of the first argument to the function
 * @param <S> the type of the second argument to the function
 * @param <T> the type of the third argument to the function
 * @param <R> the type of the result of the function
 * @see Function
 *
 * @since 1.4.0.0-PN
</R></T></S></F> */
fun interface TriFunction<F, S, T, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param f the first function argument
     * @param s the second function argument
     * @param t the third function argument
     * @return the function result
     */
    fun apply(f: F, s: S, t: T): R

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the `after` function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the `after` function, and of the
     * composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the `after` function
     * @throws NullPointerException if after is null
    </V> */
    fun <V> andThen(after: Function<in R, out V>): TriFunction<F, S, T, V> {
        Objects.requireNonNull(after)
        return TriFunction { f: F, s: S, t: T -> after.apply(apply(f, s, t)) }
    }
}
