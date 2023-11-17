package com.github.akafasty.authenticator.internal.commands.child;

import com.github.akafasty.authenticator.internal.commands.context.CommandContext;

public interface ChildCommand {

    void execute(CommandContext context);

}
