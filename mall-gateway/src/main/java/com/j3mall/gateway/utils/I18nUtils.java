package com.j3mall.gateway.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Locale;

public class I18nUtils {

    public static Locale getLocale(ServerRequest request) {
        String lang = request.queryParam("lang").filter(StrUtil::isNotBlank).orElse("zh-CN");
        return Locale.forLanguageTag(lang);
    }

}
