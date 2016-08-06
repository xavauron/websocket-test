package com.xdassa.endpoint;

import javax.websocket.Session;

/**
 * Created by Xavier on 05/08/2016.
 */
public interface Endpoint {

    void openConnection(Session session);

    void sendMessage(int i);

    void onMessage(Session session, int i);
}
