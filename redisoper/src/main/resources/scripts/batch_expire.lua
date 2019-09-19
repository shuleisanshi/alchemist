for i=1, ARGV[1], 1
do
    redis.call("EXPIRE", KEYS[i], ARGV[2]);
end
