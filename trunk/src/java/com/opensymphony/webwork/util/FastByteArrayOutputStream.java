/*
 * WebWork, Web Application Framework
 *
 * Distributable under Apache license.
 * See terms of license at opensource.org
 */
package com.opensymphony.webwork.util;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A speedy implementation of ByteArrayOutputStream. It's not synchronized, and it
 * does not copy buffers when it's expanded. There's also no copying of the internal buffer
 * if it's contents is extracted with the writeTo(stream) method.
 *
 * @author Rickard �berg
 * @version $Revision$
 */
public class FastByteArrayOutputStream
        extends OutputStream {
    // Static --------------------------------------------------------
    private static final int DEFAULT_BLOCK_SIZE = 8192;

    // Attributes ----------------------------------------------------
    // internal buffer
    private byte[] buffer;
    private LinkedList buffers;
    private int index;
    private int size;
    private int blockSize;
    // is the stream closed?
    private boolean closed;

    // Constructors --------------------------------------------------
    public FastByteArrayOutputStream() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public FastByteArrayOutputStream(int aSize) {
        blockSize = aSize;
        buffer = new byte[blockSize];
    }

    // Public
    public void writeTo(OutputStream out) throws IOException {
        // Check if we have a list of buffers
        if (buffers != null) {
            Iterator enum = buffers.iterator();
            while (enum.hasNext()) {
                byte[] bytes = (byte[]) enum.next();
                out.write(bytes, 0, blockSize);
            }
        }

        // write the internal buffer directly
        out.write(buffer, 0, index);
    }

    public void writeTo(RandomAccessFile out) throws IOException {
        // Check if we have a list of buffers
        if (buffers != null) {
            Iterator enum = buffers.iterator();
            while (enum.hasNext()) {
                byte[] bytes = (byte[]) enum.next();
                out.write(bytes, 0, blockSize);
            }
        }

        // write the internal buffer directly
        out.write(buffer, 0, index);
    }

    public void writeTo(JspWriter out, String encoding) throws IOException {
        // Check if we have a list of buffers
        if (buffers != null) {
            Iterator enum = buffers.iterator();
            while (enum.hasNext()) {
                byte[] bytes = (byte[]) enum.next();

                if (encoding != null)
                    out.write(new String(bytes, encoding));
                else
                    out.write(new String(bytes));
            }
        }

        // write the internal buffer directly
        if (encoding != null)
            out.write(new String(buffer, 0, index, encoding));
        else
            out.write(new String(buffer, 0, index));
    }

    public int getSize() {
        return size + index;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[getSize()];

        // Check if we have a list of buffers
        int pos = 0;
        if (buffers != null) {
            Iterator enum = buffers.iterator();
            while (enum.hasNext()) {
                byte[] bytes = (byte[]) enum.next();
                System.arraycopy(bytes, 0, data, pos, blockSize);
                pos += blockSize;
            }
        }

        // write the internal buffer directly
        System.arraycopy(buffer, 0, data, pos, index);

        return data;
    }

    public String toString() {
        return new String(toByteArray());
    }

    // OutputStream overrides ----------------------------------------
    public void write(int datum) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (index == blockSize) {
                // Create new buffer and store current in linked list
                if (buffers == null)
                    buffers = new LinkedList();

                buffers.addLast(buffer);

                buffer = new byte[blockSize];
                size += index;
                index = 0;
            }

            // store the byte
            buffer[index++] = (byte) datum;
        }
    }

    public void write(byte[] data, int offset, int length)
            throws IOException {
        if (data == null) {
            throw new NullPointerException();
        } else if ((offset < 0) || (offset + length > data.length)
                || (length < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (closed) {
            throw new IOException("Stream closed");
        } else {
            if (index + length >= blockSize) {
                // Write byte by byte
                // FIXME optimize this to use arraycopy's instead
                for (int i = 0; i < length; i++)
                    write(data[offset + i]);
            } else {
                // Copy in the subarray
                System.arraycopy(data, offset, buffer, index, length);
                index += length;
            }
        }
    }

    public void close() {
        closed = true;
    }
}

