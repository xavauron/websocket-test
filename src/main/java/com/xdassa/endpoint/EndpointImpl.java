package com.xdassa.endpoint;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Xavier on 05/08/2016.
 */
@ServerEndpoint(value = "/compteur")
public class EndpointImpl implements Endpoint {

    static Queue<Session> sessions = new ConcurrentLinkedQueue<Session>();
    private int compteur;

    public EndpointImpl() {

        compteur = 0;

        Thread incrementsCompteurThread = new Thread(new IncrementCompteurRunnbale());
        incrementsCompteurThread.start();
    }

    @OnOpen
    public void openConnection(Session session) {
        sessions.add(session);
    }

    public void sendMessage(int i) {
        sessions.stream().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendObject(compteur);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnMessage
    public void onMessage(Session session, int i) {

    }

    private class IncrementCompteurRunnbale implements Runnable {

        public void run() {

            for (int i = 0; i < 10000000; i++) {

                compteur++;
                sendMessage(compteur);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized int getCompteur() {
        return compteur;
    }
}
