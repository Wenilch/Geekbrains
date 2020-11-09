package ru.geekbrains.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.client.gui.LoginController;
import ru.geekbrains.client.gui.MainController;
import ru.geekbrains.client.handlers.InboundHandler;
import ru.geekbrains.common.configuration.CommonConfigurations;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class NettyNetwork {
    private Channel currentChannel;

    private static final Logger logger = LogManager.getLogger(NettyNetwork.class);

    private static final NettyNetwork thisNetwork = new NettyNetwork();

    private NettyNetwork() {

    }

    public static NettyNetwork getInstance() {
        return thisNetwork;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void start(CountDownLatch countDownLatch, LoginController loginController, MainController mainController, Scene mainScene, ClientExecutorService clientExecutorService) throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", CommonConfigurations.SERVER_PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new InboundHandler(loginController, mainController, mainScene, clientExecutorService));
                            currentChannel = socketChannel;
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            countDownLatch.countDown();
            channelFuture.channel().closeFuture().sync();

        } finally {
            try {
                clientGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Connection closing failure - " + e.getMessage());
            }
        }
    }

    public void stop() {
        currentChannel.close();
    }
}
