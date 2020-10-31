package ru.geekbrains.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.geekbrains.common.command.Command;
import ru.geekbrains.common.services.FileTransferService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ServerOutboundHandler extends ChannelOutboundHandlerAdapter {
    private Channel curChannel;
    private final FileTransferService fileTransferService;

    public ServerOutboundHandler(FileTransferService fileTransferService) {
        this.fileTransferService = fileTransferService;
    }

    public void setChannel(Channel s) {
        curChannel = s;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String message = msg.toString();

        if (msg.getClass().equals(File.class)) {
            fileTransferService.transfer(Paths.get(message), null, ctx, channelFutureListener -> {
                if (!channelFutureListener.isSuccess()) {
                    channelFutureListener.cause().printStackTrace();
                }
                if (channelFutureListener.isSuccess()) {
                    if (message.length() > 3) {
                        String fileType = message.substring(message.length() - 3);
                        if (!fileType.equals("tmp")) {
                            transferString(Command.FDS.toString(), ctx);
                        }
                    }
                }
            });
        } else {
            transferString(message, ctx);
        }
    }

    private void transferString(String message, ChannelHandlerContext handlerContext) {
        transferBytes(message.getBytes(StandardCharsets.UTF_8), handlerContext);
    }

    private void transferBytes(byte[] bytes, ChannelHandlerContext handlerContext) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.length);
        buffer.writeBytes(bytes);
        handlerContext.writeAndFlush(buffer);
    }
}