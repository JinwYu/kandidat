package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

//import org.jbox2d.common.Vec3;

/**
 * Created by Andreas on 2016-03-02.
 * As far as i can tell the server does not actually connect
 * with external units. What it does is wait for a packet of
 * information that can be sent by all units within the local
 * area network (Andreass phone in this case). When one is received
 * the server checks the packet for validity, and if correct, sends
 * a response packet to the corresponding unit by IP address.
 * The server loops in perpetuity, the join button only sends one
 * packet and then exits immediately. Question is, will this be viable
 * in the actual game later on?
 */

/**
 * Idea: Is it possible that we use this method in order to make the servers find each other,
 * and the packet being sent is a connection request? If the packet is valid, the requesting unit
 * will send network details to the server, connecting the sockets together, and using the TCP/IP
 * method of connecting units. This would actually connect the units with each other, and allow
 * information to be sent that way, while also removing the need for typing in the servers IP
 * manually. This might improve performance, assuming that i could actually get the connection to
 * work, as that's what the old network code attempted, and failed with. It worked in a regular
 * project, just not on a libGDX project.
 */

public class CreateServer extends Thread
{
    static final int SOCKETSERVERPORT = 8080;

    DatagramSocket dSocket;

    //Vector<User> userList;

    private String msg = "msg", error = "No Error", data;

    //ServerSocket serverSocket;

    //ServerInterface sInterface;

    private Boolean threadRun;

    @Override
    public void run()
    {
        //Set the loop to true, until disconnected from the outside.
        threadRun = true;
        try
        {
            //Create new socket handling incoming/outgoing packets.
            dSocket = new DatagramSocket(SOCKETSERVERPORT, InetAddress.getByName("0.0.0.0"));
            //Activate broadcast.
            dSocket.setBroadcast(true);
            //Engage server loop.
            while(threadRun)
            {
                //Sleep for one second, so that the server response can be read.
                try
                {
                    this.sleep(1000);
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                    error = "Exception: " + e.toString();
                }

                msg = getClass().getName() + ">>>Ready to receive broadcast packets!";
                //Create buffer for incoming packets.
                byte[] recvbuf = new byte[15000];
                DatagramPacket dPacket = new DatagramPacket(recvbuf, recvbuf.length);
                //Wait for incoming packet.
                dSocket.receive(dPacket);
                //Display packet information.
                msg = getClass().getName() + ">>>Discovery packet recieved from: " + dPacket.getAddress().getHostAddress() + "\n";
                msg += getClass().getName() + ">>>Packet received; data: " + new String(dPacket.getData()).trim() + "\n";
                //Get string data.
                String message = new String(dPacket.getData()).trim();
                //Check if the packet is valid.
                if(message.equals("DISCOVER_FUIFSERVER_REQUEST"))
                {
                    //Send response to unit.
                    byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dPacket.getAddress(), dPacket.getPort());
                    dSocket.send(sendPacket);
                    msg += getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress();
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            error = "Exception: " + e.toString();
        }

    }

    public String getMsg(){ return msg; }

    public String getError() { return error; }

    public void stopServer()
    {
        threadRun = false;
        dSocket.close();
    }

    public String getIpAddress()
    {
        String ip = "";
        Enumeration<NetworkInterface>enumNetworkInterfaces;
        try {

            enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {

                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {

                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    //Ancient server code, may come in handy later.
    /*public void run()
    {
        Socket socket = null;
        threadRun = true;
        try
        {
            serverSocket = new ServerSocket(SOCKETSERVERPORT);

            while(threadRun)
            {
                socket = serverSocket.accept();
                User user = new User();
                userList.add(user);
                ConnectThread connectThread = new ConnectThread(user, socket);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            error = "Exception: " + e.toString();
        }finally
        {
            if(socket != null)
            {
                try
                {
                    socket.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                    error = "Exception: " + e.toString();
                }
            }
           // if(!userList.isEmpty())
           //     userList.clear();
        }
    }

    public void stopServer()
    {
        threadRun = false;
        try
        {
            serverSocket.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    private class ConnectThread extends Thread
    {
        Socket socket;
        User user;
        String msg;

        ConnectThread(User usr, Socket socket)
        {
            this.socket = socket;
            user = usr;
            user.socket = socket;
            user.conThread = this;
        }

        public void run()
        {
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try
            {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                //user.name = ;

                while(true)
                {
                if(dataInputStream.available() > 0)
                {

                }
                if()
                {

                }
                }
            }catch(IOException e)
            {
                e.printStackTrace();
                error = "Exception: " + e.toString();
            } finally
            {
                if(dataInputStream != null)
                {
                    try
                    {
                        dataInputStream.close();
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                        error = "Exception: " + e.toString();
                    }
                }

                if(dataOutputStream != null)
                {
                    try
                    {
                        dataOutputStream.close();
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                        error = "Exception: " + e.toString();
                    }
                }

                userList.remove(user);

            }



        }

        private void sendData() {}

    }*/



    /*public class User
    {
        public String name;
        public Socket socket;
        public ConnectThread conThread;

    }*/
}