package sudoku.myself.xhc.com.sudoku.network.smack;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ankit on 10/3/2015.
 */
public class MyXMPP {

    private static final String DOMAIN = "www.xhc.com";
    private static final String HOST = "192.168.0.6";
    private static final int PORT = 5222;
    private String userName ="123";
    private String passWord = "123";
    AbstractXMPPConnection connection ;
    ChatManager chatmanager ;
    Chat newChat;
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();
    private boolean connected;
    private boolean isToasted;
    private boolean chat_created;
    private boolean loggedin;


    //Initialize
    public void init(String userId,String pwd ) {
        Log.i("XMPP", "Initializing!");
        this.userName = userId;
        this.passWord = pwd;
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setUsernameAndPassword(userName, passWord);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        configBuilder.setResource("Android");
        configBuilder.setServiceName(DOMAIN);
        configBuilder.setHost(HOST);
        configBuilder.setSendPresence(true);
        configBuilder.setPort(PORT);
        connection = new XMPPTCPConnection(configBuilder.build());
        connection.addConnectionListener(connectionListener);

    }

    // Disconnect Function
    public void disconnectConnection(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connectConnection()
    {
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... arg0) {

                // Create a connection
                try {
                    connection.connect();
                    connected = true;

                } catch (IOException e) {
                } catch (SmackException e) {

                } catch (XMPPException e) {
                                    }

                return null;
            }
        };
        connectionThread.execute();
    }


    public void sendMsg(String who , String msg) {
        if (connection.isConnected()== true) {
            // Assume we've created an XMPPConnection name "connection"._
            chatmanager = ChatManager.getInstanceFor(connection);
            newChat = chatmanager.createChat(who , new ChatMessageListener(){
                @Override
                public void processMessage(Chat chat, Message message) {
                    Log.e("xhc","收到的消息--> "+message.getBody());
                }
            });

            try {
                newChat.sendMessage(msg);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    public void login() {

        try {
            Log.e("xhc", "user Name " + userName + " password "+passWord);
            connection.login(userName, passWord);
            offlineMsg();
        } catch (XMPPException | SmackException | IOException e) {
            Log.e("xhc","exception "+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
        }

    }

    private void offlineMsg(){
        OfflineMessageManager offlineManager = new OfflineMessageManager(
                connection);
        try{
            List<Message> lsMsg =  offlineManager.getMessages();
            Log.e("xhc" , " lsmsg "+lsMsg+" "+(lsMsg == null ? 0 : lsMsg.size()));
            for(Message msg : lsMsg){
                Log.e("xhc","离线消息 "+msg.toString());
            }
        }catch (Exception e){
            Log.e("xhc", "离线exception "+e.getMessage());
        }
    }

    //Connection Listener to check connection state
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.e("xhc", "Connected! ");
            connected = true;

            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub


                    }
                });
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {

            Log.d("xmpp", "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {



                    }
                });
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub



                    }
                });
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

            chat_created = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub



                    }
                });
        }
    }


}
