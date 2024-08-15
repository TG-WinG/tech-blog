package kr.tgwing.tech.security.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class JwtBlackListService {
    private final Set<String> blackList = new ConcurrentSkipListSet<>();

    public void addToBlacklist(String token) {
        blackList.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blackList.contains(token);
    }}
