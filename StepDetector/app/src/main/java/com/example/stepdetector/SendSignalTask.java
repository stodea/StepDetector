package com.example.stepdetector;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendSignalTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String pcIpAddress = params[0];
        int pcPort = Integer.parseInt(params[1]);
        String info = params[2];

        try {
            Socket socket = new Socket(pcIpAddress, pcPort);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(info.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}