package com.ela.ccvoice.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.ela.ccvoice.common.websocket.domain.vo.request.WsLoginRequest;
import com.ela.ccvoice.common.websocket.service.WebSocketService;
import com.ela.ccvoice.common.websocket.domain.enums.WSRequestTypeEnum;
import com.ela.ccvoice.common.websocket.domain.vo.request.WSBaseRequest;
import com.ela.ccvoice.common.websocket.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        userOffline(ctx.channel());
    }
    /**
     * 重写握手事件
     * 心跳检测
     *
     * @param ctx
     * @param evt 具体的事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if(StrUtil.isNotBlank(token)){
                webSocketService.authorization(ctx.channel(), token);
            }
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE){
                //如果是读空闲，则要求关闭连接
                System.out.println("读空闲，客户端已经60s没有响应，自动断开连接。");
                //应该对客户端进行下线操作
                userOffline(ctx.channel());
            }
        }
    }

    /**
     * 用户下线处理
     * @param channel
     */
    private void userOffline(Channel channel){
        webSocketService.remove(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();//获取ws 新消息
        //**可以透过该方法获取到从前端发来的所有消息 根据type的值来做不同操作
        System.out.println(text);
        WSBaseRequest wsBaseRequest = JSONUtil.toBean(text, WSBaseRequest.class);
        switch (WSRequestTypeEnum.of(wsBaseRequest.getType())) {
            case LOGIN:
                //连同认证也做在这里面，透过传进来的token是否为空进行判断
                WsLoginRequest wsLoginRequest = JSONUtil.toBean(text, WsLoginRequest.class);
                webSocketService.handleLoginReq(ctx.channel(),wsLoginRequest);
                break;
            case HEARTBEAT:
                break;
            default:
                log.info("unknown type");
        }
    }
}
