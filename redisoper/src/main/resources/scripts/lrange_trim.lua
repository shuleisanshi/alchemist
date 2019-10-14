local endIndex = ARGV[2] + 1
local temp = redis.call("LRANGE", KEYS[1], ARGV[1], ARGV[2])
redis.call("LTRIM", KEYS[1], endIndex, -1)
return temp