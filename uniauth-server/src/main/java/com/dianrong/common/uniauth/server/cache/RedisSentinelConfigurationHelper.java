package com.dianrong.common.uniauth.server.cache;

import java.util.Set;

import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;
import static org.springframework.util.StringUtils.split;

public class RedisSentinelConfigurationHelper extends RedisSentinelConfiguration {

    public RedisSentinelConfigurationHelper(String master, String sentinels) {
        notNull(master, "master must not be null!");
        notNull(sentinels, "sentinels must not be null!");
        this.setMaster(master);
        this.appendSentinels(commaDelimitedListToSet(sentinels));
    }

    private void appendSentinels(Set<String> hostAndPorts) {
        for (String hostAndPort : hostAndPorts) {
            addSentinel(readHostAndPortFromString(hostAndPort));
        }
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = split(hostAndPort, ":");
        notNull(args, "HostAndPort need to be seperated by  ':'.");
        isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]).intValue());
    }
}
