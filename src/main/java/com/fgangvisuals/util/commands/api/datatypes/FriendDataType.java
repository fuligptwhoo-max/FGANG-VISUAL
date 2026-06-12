package com.fgangvisuals.util.commands.api.datatypes;

import com.fgangvisuals.util.commands.api.exception.CommandException;
import com.fgangvisuals.util.commands.api.helpers.TabCompleteHelper;
import com.fgangvisuals.util.friend.Friend;
import com.fgangvisuals.util.friend.FriendRepository;

import java.util.List;
import java.util.stream.Stream;

public enum FriendDataType implements IDatatypeFor<Friend> {
    INSTANCE;

    @Override
    public Stream<String> tabComplete(IDatatypeContext datatypeContext) throws CommandException {
        Stream<String> friends = getFriends()
                .stream()
                .map(Friend::name);

        String context = datatypeContext
                .getConsumer()
                .getString();

        return new TabCompleteHelper()
                .append(friends)
                .filterPrefix(context)
                .sortAlphabetically()
                .stream();
    }

    @Override
    public Friend get(IDatatypeContext datatypeContext) throws CommandException {
        String username = datatypeContext
                .getConsumer()
                .getString();

        return getFriends().stream()
                .filter(s -> s.name().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    private List<? extends Friend> getFriends() {
        return FriendRepository.getFriends();
    }
}
