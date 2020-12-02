package top.chengdongqing.common.auth;

import lombok.Builder;
import lombok.Data;

/**
 * @author Luyao
 */
public interface ThirdPartyAuth {

    /**
     * 构建授权地址
     *
     * @return
     */
    String buildGrantUrl();

    /**
     * 根据认证码请求访问令牌
     *
     * @param authCode 认证码
     * @return 包含令牌和用户对外id的响应实体
     */
    AccessTokenResponse requestAccessToken(String authCode);

    /**
     * 请求用户开放信息
     *
     * @param accessToken 访问令牌
     * @return 用户开放信息
     */
    UserOpenInfo requestUserInfo(String accessToken);

    @Data
    @Builder
    class AccessTokenResponse {

        private String token;
        private String userId;
    }

    @Data
    @Builder
    class UserOpenInfo {

        private String id;
        private String nickname;
        private String avatar;
        private Integer gender;
        private String address;
    }
}
