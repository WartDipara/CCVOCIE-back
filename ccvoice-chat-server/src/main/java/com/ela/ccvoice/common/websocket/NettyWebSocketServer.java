package com.ela.ccvoice.common.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class NettyWebSocketServer {
    //Websocket指定端口 ， 访问websocket必须指定出一个新的端口来接收
    public static final int WEB_SOCKET_PORT = 8090;
    //建立线程池执行器
    //BossGroup是监听端口，处理新连接的线程组，默认线程数为1
    //WorkerGroup是处理I/O操作的线程组。
    private EventLoopGroup boosGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    /**
     * 启动websocket服务器
     * 项目创建初始化时调用
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException{
        run();
    }
    public void run() throws InterruptedException{
        // 服务器启动引导对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LoggingHandler(LogLevel.INFO)) //boss线程日志
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws  Exception{
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //3os里没有客户端向服务器发送心跳则关闭链接
                        //心跳包的存在是必要的，因为客户端可能没有来得及挥手直接下线了（断电情况），造成服务器一直在维护这个客户端链接，浪费资源
                        //三个参数，（读空闲，写空闲，全局空闲） ，读参数一般是服务器用的，用来判断能否接受到客户端的信息，写参数一般客户端用的，用来判断是否发送了心跳包，全局参数一般不用，
                        pipeline.addLast(new IdleStateHandler(60, 0, 0));
                        // http协议的encode和decode
                        pipeline.addLast(new HttpServerCodec());
                        // 以块的方式写，添加chunkedWriterHandler 处理器
                        pipeline.addLast(new ChunkedWriteHandler());
                        /**
                         * 聚合多个HttpObject为单个 FullHttpRequest/Response
                         * http的传输是分段传输
                         * HttpObjectAggregator就是做这个的
                         */
                        pipeline.addLast(new HttpObjectAggregator(8196));
                        //websocket请求，在该路径下的请求都会被升级成websocket模式
                        //ws升级只会在第一次请求的时候执行一次，之后会在pipeline里销毁自己，详情见源码
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        // 自定义handler
                        pipeline.addLast(new NettyWebSocketServerHandler());
                    }
                });
        //绑定了端口8090
        bootstrap.bind(WEB_SOCKET_PORT).sync();
    }
    /*
    * 销毁websocket服务器
     */
    @PreDestroy
    public void destroy() throws InterruptedException{
        Future<?> future1 = boosGroup.shutdownGracefully();
        Future<?> future2 = workGroup.shutdownGracefully();
        future1.syncUninterruptibly();
        future2.syncUninterruptibly();
        log.info("成功关闭websocket server");
    }

}
