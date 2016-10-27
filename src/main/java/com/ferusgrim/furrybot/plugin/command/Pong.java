package com.ferusgrim.furrybot.plugin.command;

import com.ferusgrim.furrybot.util.DiscordUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Pong extends FurryCommand {

    public Pong(final IUser user, final IChannel channel, final String[] content) {
        super(user, channel, content);
    }

    @Override
    public void execute() {
        DiscordUtil.sendMessage(this.getChannel(), this.getUser().mention(true) + " Pong!");
    }
}
