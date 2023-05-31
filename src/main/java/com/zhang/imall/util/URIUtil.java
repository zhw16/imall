package com.zhang.imall.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : UIRIUtil
 * @description : [描述说明该类的功能]
 * @createTime : [2022/10/18 13:15]
 */
/**
 * 工具方法：获取图片完整地址中的，URI：
 * 即通过【"http://127.0.0.1:8083/images/bfe5d66e8c0741328a.png"】得到【"http://127.0.0.1:8083/"】
 */
public class URIUtil {
    public static URI getHost(URI uri) {
        URI effectiveURI;
        try {
            //
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
                    uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }
}
