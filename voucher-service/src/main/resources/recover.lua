-- 1.参数列表
-- 1.1.优惠券id
local voucherId = ARGV[1]
-- 1.2.用户id
local userId = ARGV[2]

-- 2.数据key
-- 2.1.库存key
local stockKey = 'seckill:stock:' .. voucherId
-- 2.2.订单key
local orderKey = 'seckill:order:' .. voucherId

-- 3.脚本业务
-- 3.1.增加库存 INCRBY stockKey 1
redis.call('incrby', stockKey, 1)

-- 仅允许同一个用户下一单 及时订单取消了也不能再次下单
-- 3.2.判断用户是否在订单集合中 SISMEMBER orderKey userId
--if (redis.call('sismember', orderKey, userId) == 1) then
--    -- 3.3.用户存在，移除用户下单记录 SREM orderKey userId
--    redis.call('srem', orderKey, userId)
--end

-- 3.4.操作成功，返回0
return 0
