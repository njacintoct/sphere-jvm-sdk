package io.sphere.sdk.customergroups.commands.updateactions;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customergroups.CustomerGroup;


/**
 Changes the name of the customer group.

 {@include.example io.sphere.sdk.customergroups.commands.CustomerGroupUpdateCommandTest#changeName()}

 */
public class ChangeName extends UpdateAction<CustomerGroup> {
    private final String name;

    private ChangeName(final String name) {
        super("changeName");
        this.name = name;
    }

    public static ChangeName of(final String name) {
        return new ChangeName(name);
    }

    public String getName() {
        return name;
    }
}
