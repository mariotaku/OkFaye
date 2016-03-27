package org.mariotaku.okfaye;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ws.WebSocketCall;

import java.util.Scanner;

/**
 * Created by mariotaku on 16/3/27.
 */
public class Sample {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:18080").build();
        WebSocketCall call = WebSocketCall.create(client, request);
        Faye faye = Faye.create(client, call);

        Scanner scanner = new Scanner(System.in);
        while (faye.getState() != Faye.DISCONNECTED && scanner.hasNext()) {
            final String line = scanner.nextLine();
            if (line.startsWith("/subscribe")) {
                final String[] split = line.split(" ", 2);
                if (split.length < 2) {
                    System.err.println("Usage: /subscribe [channel]");
                    break;
                }
                faye.subscribe(split[1], System.out::println);
            } else if (line.startsWith("/unsubscribe")) {
                final String[] split = line.split(" ", 2);
                if (split.length < 2) {
                    System.err.println("Usage: /unsubscribe [channel]");
                    break;
                }
                faye.unsubscribe(split[1]);
            } else if (line.startsWith("/disconnect")) {
                faye.disconnect();
            } else if (line.startsWith("/send")) {
                final String[] split = line.split(" ", 3);
                if (split.length < 3) {
                    System.err.println("Send message: /send [channel] [text]");
                    break;
                }
                faye.publish(split[1], split[2], System.out::println);
            } else {
                System.out.println("Subscribe to channel: /subscribe [channel]");
                System.out.println("Unsubscribe from channel: /unsubscribe [channel]");
                System.out.println("Disconnect: /disconnect");
                System.out.println("Show usage: /help");
                System.out.println("Send message: /send [channel] [text]");
            }
        }
        System.out.println("Disconnected");
    }

}
