package com.ela.ccvoice.common.websocket.service;

import cn.hutool.core.net.url.UrlBuilder;
import com.ela.ccvoice.common.websocket.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.codec.http.HttpRequest;

import java.util.Optional;

/**
 * 自定义处理器
 */
public class MyWsHeaderCollectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            Optional<String> token = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(kv -> kv.get("token"))
                    .map(CharSequence::toString);
            //如果有token，保存起来
            token.ifPresent(s -> NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, s));
            request.setUri(urlBuilder.getPath().toString());
        }
        ctx.fireChannelRead(msg);
    }
}
