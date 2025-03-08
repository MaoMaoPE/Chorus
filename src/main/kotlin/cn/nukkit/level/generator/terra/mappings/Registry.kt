package cn.nukkit.level.generator.terra.mappings

import java.util.function.Consumer

/**
 * A wrapper around a value which is loaded based on the output from the provided
 * [RegistryLoader]. This class is primarily designed to hold a registration
 * of some kind, however no limits are set on what it can hold, as long as the
 * specified RegistryLoader returns the same value type that is specified in the
 * generic.
 *
 *
 *
 * Below, a RegistryLoader is taken in the constructor. RegistryLoaders have two
 * generic types: the input, and the output. The input is what it takes in, whether
 * it be a string which references to a file, or nothing more than an integer. The
 * output is what it generates based on the input, and should be the same type as
 * the `CONTENT` generic specified in the registry.
 *
 *
 *
 * Registries can be very simple to create. Here is an example that simply parses a
 * number given a string:
 *
 * <pre>`public static final SimpleRegistry<Integer> STRING_TO_INT = SimpleRegistry.create("5", Integer::parseInt);`</pre>
 *
 *
 *
 * This is a simple example which really wouldn't have much of a practical use,
 * however it demonstrates a fairly basic use case of how this system works. Typically
 * though, the first parameter would be a location of some sort, such as a file path
 * where the loader will load the mappings from.
 *
 *
 * Allay Project 2023/3/18
 *
 * @param <CONTENT> the type of the value which is being held by the registry
 * @author GeyserMC | daoge_cmd
</CONTENT> */
interface Registry<CONTENT> {
    /**
     * Gets the underlying value held by this registry.
     *
     * @return the underlying value held by this registry.
     */
    /**
     * Sets the underlying value held by this registry.
     * Clears any existing data associated with the previous
     * value.
     *
     * @param content the underlying value held by this registry
     */
    var content: CONTENT

    /**
     * Registers what is specified in the given
     * [Consumer] into the underlying value.
     *
     * @param consumer the consumer
     */
    fun register(consumer: Consumer<CONTENT>) {
        consumer.accept(content)
    }
}
