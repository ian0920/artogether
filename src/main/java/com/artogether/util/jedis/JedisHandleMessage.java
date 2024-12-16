package com.artogether.util.jedis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHandleMessage {
    private static JedisPool pool = JedisPoolUtil.getJedisPool();

    /**
     * 取得歷史聊天記錄
     * @param currentUserRole 當前用戶角色（"m" 或 "bm"）
     * @param currentUserId 當前用戶 ID
     * @param peerUserRole 對方用戶角色（"m" 或 "bm"）
     * @param peerUserId 對方用戶 ID
     * @return 雙方的歷史聊天記錄
     */
    public static List<String> getHistoryMsg(String currentUserRole, String currentUserId, String peerUserRole, String peerUserId) {
        String key = generateKey(currentUserRole, currentUserId, peerUserRole, peerUserId);
        try (Jedis jedis = pool.getResource()) {
            return jedis.lrange(key, 0, -1); // 獲取歷史消息
        }
    }

    /**
     * 儲存聊天訊息
     * @param currentUserRole 當前用戶角色（"m" 或 "bm"）
     * @param currentUserId 當前用戶 ID
     * @param peerUserRole 對方用戶角色（"m" 或 "bm"）
     * @param peerUserId 對方用戶 ID
     * @param message 聊天訊息
     */
    public static void saveChatMessage(String currentUserRole, String currentUserId, String peerUserRole, String peerUserId, String message) {
        String currentUserKey = generateKey(currentUserRole, currentUserId, peerUserRole, peerUserId);
        String peerUserKey = generateKey(peerUserRole, peerUserId, currentUserRole, currentUserId);

        try (Jedis jedis = pool.getResource()) {
            jedis.rpush(currentUserKey, message); // 儲存到當前用戶的 Key
            jedis.rpush(peerUserKey, message);    // 儲存到對方用戶的 Key
        }
    }

    /**
     * 產生 Redis Key，格式：角色_ID:角色_ID
     * @param currentUserRole 當前用戶角色（"m" 或 "bm"）
     * @param currentUserId 當前用戶 ID
     * @param peerUserRole 對方用戶角色（"m" 或 "bm"）
     * @param peerUserId 對方用戶 ID
     * @return 組成的 Key
     */
    private static String generateKey(String currentUserRole, String currentUserId, String peerUserRole, String peerUserId) {
        return new StringBuilder()
                .append(currentUserRole).append("_").append(currentUserId)
                .append(":")
                .append(peerUserRole).append("_").append(peerUserId)
                .toString();
    }
}
