package io.sphere.sdk.utils;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;


public class SetUtilsTest {
    @Test
    public void asSet() throws Exception {
        final Set<Integer> actual = SetUtils.asSet(1, 2, 3);
        assertThat(actual).isEqualTo(new HashSet<>(asList(1, 2, 3)));
    }
}