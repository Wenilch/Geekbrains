package ru.geekbrains.common.services;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Path;

public interface FileTransferService {
    void transfer(Path filePath, Channel channel, ChannelHandlerContext handlerContext, ChannelFutureListener completeListener) throws IOException;
}
