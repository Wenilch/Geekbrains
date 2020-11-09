package ru.geekbrains.common.services;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.geekbrains.common.command.Command;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTransferServiceImpl implements FileTransferService {

    public void transfer(Path path, Channel channel, ChannelHandlerContext handlerContext, ChannelFutureListener finishListener) throws IOException {

        byte[] commandToBytes = Command.F_T.toString().getBytes(StandardCharsets.UTF_8);
        transferBytes(commandToBytes, channel, handlerContext);

        byte[] fileNameToBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        transferInt(fileNameToBytes.length, channel, handlerContext);
        transferBytes(fileNameToBytes, channel, handlerContext);

        long fileSize = Files.size(path);
        transferLong(fileSize, channel, handlerContext);

        ChannelFuture transferFileFuture = transferObject(new DefaultFileRegion(path.toFile(), 0, fileSize), channel, handlerContext);
        if (finishListener != null) {
            transferFileFuture.addListener(finishListener);
        }
    }

    private void transferBytes(byte[] bytes, Channel channel, ChannelHandlerContext handlerContext) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(bytes.length);
        buffer.writeBytes(bytes);
        transferObject(buffer, channel, handlerContext);
    }

    private void transferInt(int value, Channel channel, ChannelHandlerContext handlerContext) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(Integer.BYTES);
        buffer.writeInt(value);
        transferObject(buffer, channel, handlerContext);
    }

    private void transferLong(long value, Channel channel, ChannelHandlerContext handlerContext) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(Long.BYTES);
        buffer.writeLong(value);
        transferObject(buffer, channel, handlerContext);
    }

    private ChannelFuture transferObject(Object objectForTransfer, Channel channel, ChannelHandlerContext handlerContext) {
        return channel != null ? channel.writeAndFlush(objectForTransfer) : handlerContext.writeAndFlush(objectForTransfer);
    }
}
