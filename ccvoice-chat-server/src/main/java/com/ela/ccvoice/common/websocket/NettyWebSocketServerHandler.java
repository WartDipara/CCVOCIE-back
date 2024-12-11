package com.ela.ccvoice.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.ela.ccvoice.common.websocket.service.WebSocketService;
import com.ela.ccvoice.common.websocket.domain.enums.WSRequestTypeEnum;
import com.ela.ccvoice.common.websocket.domain.vo.request.WSBaseRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
            System.out.println("握手成功");
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE){
                //如果是读空闲，则要求关闭连接
                System.out.println("读空闲，客户端已经30s没有响应，自动断开连接。");
                //应该对客户端进行下线操作
                //TODO 用户下线逻辑 目前只做了简单的断开连接
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();//获取ws 新消息
        //**可以透过该方法获取到从前端发来的所有消息 根据type的值来做不同操作
        System.out.println(text);
        WSBaseRequest wsBaseRequest = JSONUtil.toBean(text, WSBaseRequest.class);
        switch (WSRequestTypeEnum.of(wsBaseRequest.getType())) {
            case LOGIN:
                webSocketService.handleLoginReq(channelHandlerContext.channel());
//                System.out.println("登录test");
//                //类型必须是TextWebsocketFrame，这是 ws 协议要求的。
//                channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("测试channel返回"));
            case HEARTBEAT:
                break;
            case AUTHORIZE:
                break;
        }
    }
}
