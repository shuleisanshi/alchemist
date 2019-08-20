if redis.call('GET', KEYS[1]) == ARGV[1] then
    return 1 == redis.call('DEL', KEYS[1])
else
    return false
end