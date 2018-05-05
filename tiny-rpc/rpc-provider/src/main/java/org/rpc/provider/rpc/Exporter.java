package org.rpc.provider.rpc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author luoliang
 * @date 2018/5/3
 **/
public interface Exporter {
    void export(final Object service, int port) throws IOException, Exception;
}
