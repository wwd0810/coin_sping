/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.laon.cashlink.common.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class OutputStreamWrapper extends ServletOutputStream {

    private OutputStream outputStream;
    private ByteArrayOutputStream copy;

    public OutputStreamWrapper(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }

    /**
     * Checks if a non-blocking write will succeed. If this returns
     * <code>false</code>, it will cause a callback to
     * {@link WriteListener#onWritePossible()} when the buffer has emptied. If this method returns
     * <code>false</code> no further data must be written until the contain calls {@link
     * WriteListener#onWritePossible()}.
     *
     * @return <code>true</code> if data can be written, else <code>false</code>
     * @since Servlet 3.1
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Sets the {@link WriteListener} for this {@link ServletOutputStream} and thereby switches to
     * non-blocking IO. It is only valid to switch to non-blocking IO within async processing or
     * HTTP upgrade processing.
     *
     * @param listener The non-blocking IO write listener
     * @throws IllegalStateException If this method is called if neither async nor HTTP upgrade is
     * in progress or if the {@link WriteListener} has already been set
     * @throws NullPointerException If listener is null
     * @since Servlet 3.1
     */
    @Override
    public void setWriteListener(WriteListener listener) {

    }

}