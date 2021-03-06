package com.ferusgrim.furrybot.plugin.command;

import com.ferusgrim.furrybot.FurryBot;
import com.ferusgrim.furrybot.util.GSearch;
import com.ferusgrim.furrybot.util.ParseUtil;
import com.google.common.collect.Lists;
import ninja.leaping.configurate.ConfigurationNode;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import java.util.ArrayList;
import java.util.List;

public class Gif extends FurryCommand {

    private static final List<String> ACCEPTED_IMAGE_EXTENSIONS = Lists.newArrayList();

    static {
        ACCEPTED_IMAGE_EXTENSIONS.add(".gif");
        ACCEPTED_IMAGE_EXTENSIONS.add(".gifv");
    }

    private final String apiToken;
    private final String engineToken;
    private final List<String> sfwChannels;
    private final List<String> nsfwChannels;

    public Gif(final CommandManager manager,
               final IDiscordClient bot,
               final ConfigurationNode rawConfig) {
        super(manager, bot, rawConfig);

        this.apiToken = rawConfig.getNode("api-token").getString("");
        this.engineToken = rawConfig.getNode("engine-token").getString("");

        if (this.apiToken.isEmpty()) {
            FurryBot.LOGGER.warn("\"gif.api-token\" isn't configured! Disabling 'Gif' command...");
        }

        if (this.engineToken.isEmpty()) {
            FurryBot.LOGGER.warn("\"gif.engine-token\" isn't configured! Disabling 'Gif' command...");
        }

        this.sfwChannels = ParseUtil.getList(rawConfig.getNode("sfw-channelId-whitelist"));
        this.nsfwChannels = ParseUtil.getList(rawConfig.getNode("nsfw-channelId-whitelist"));
    }

    @Override
    public boolean isActive() {
        return this.getRawConfig().getNode("use").getBoolean(false)
                && !(this.apiToken.isEmpty()
                || this.engineToken.isEmpty());
    }

    @Override
    public List<String> isAllowedInChannel(final IChannel channel) {
        final List<String> ids = new ArrayList<>(this.sfwChannels);
        ids.addAll(this.nsfwChannels);

        if (ids.contains(channel.getID())) {
            return Lists.newArrayList();
        }

        return ids;
    }

    @Override
    public String getName() {
        return "gif";
    }

    @Override
    public String getDescription() {
        return "Send me out into the wilderness to collect your gifs!";
    }

    @Override
    public String getSyntax() {
        return "gif <keyword> [keyword ... ]";
    }

    @Override
    public String execute(final IChannel channel, final IUser user, final String[] args) {
        return GSearch.getImage(
                this.apiToken,
                this.engineToken,
                args,
                ACCEPTED_IMAGE_EXTENSIONS,
                this.nsfwChannels.contains(channel.getID()),
                user.mention(true)
        );
    }
}
