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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.geekbrains.common.configuration.CommonConfigurations;
import ru.geekbrains.common.services.CommandExecutorService;
import ru.geekbrains.common.services.FileTransferService;
import ru.geekbrains.server.configuration.SpringContextConfiguration;
import ru.geekbrains.server.handlers.ServerInboundHandler;
import ru.geekbrains.server.handlers.ServerOutboundHandler;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private FileTransferService fileTransferService;

    private CommandExecutorService commandExecutorService;

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
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringContextConfiguration.class);
        ApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("spring-context.xml");

        fileTransferService = context.getBean(FileTransferService.class);
        commandExecutorService = xmlApplicationContext.getBean(CommandExecutorService.class);

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
