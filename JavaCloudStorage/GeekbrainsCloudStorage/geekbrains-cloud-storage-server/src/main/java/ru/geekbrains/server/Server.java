package ru.geekbrains.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.services.*;
import ru.geekbrains.server.handlers.ServerInboundHandler;
import ru.geekbrains.server.handlers.ServerOutboundHandler;
import ru.geekbrains.server.services.AuthenticationServiceImpl;
import ru.geekbrains.server.services.CommandExecutorServiceImpl;
import ru.geekbrains.server.services.DataBaseServiceImpl;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final DataBaseService dataBaseService = new DataBaseServiceImpl();
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl(dataBaseService);
    private final FileTransferService fileTransferService = new FileTransferServiceImpl();
    private final CommandExecutorService commandExecutorService = new CommandExecutorServiceImpl(authenticationService);

    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (InterruptedException exception) {
            logger.error(exception);
            Thread.currentThread().interrupt();
        } catch (RuntimeException exception) {
            logger.error(exception);
        }
    }

    public void run() throws RuntimeException, InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            logger.info("Server started");
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws RuntimeException {
                            ch.pipeline().addLast(
                                    new ServerOutboundHandler(fileTransferService),
                                    new ServerInboundHandler(commandExecutorService)
                            );
                        }
                    });

            ChannelFuture future = bootstrap.bind(CommonConfigurations.SERVER_PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
