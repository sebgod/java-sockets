package example;

import java.io.IOException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;

import com.savarese.rocksaw.net.RawSocket;

public class AddressResolver {
    protected final RawSocket _socket;
    protected final NetworkInterface _networkInterface;

    protected AddressResolver(NetworkInterface networkInterface)
        throws IOException
    {
        _socket = new RawSocket();
        _networkInterface = networkInterface;
        _socket.open(RawSocket.PF_PACKET, 0x806);
    }

    public void sendBroadcast()
        throws IOException
    {
        ARPPacket broadcast = new ARPPacket();
        broadcast.setOperation(ARPPacket.OPER_REQ);
        broadcast.setSender(_networkInterface);
        broadcast.setTargetMAC(ARPPacket.MAC_BROADCAST);
        broadcast.setTargetIP(ARPPacket.IP4_EMPTY);
    }
    
    /**
     * Closes the raw socket opened by the constructor.  After calling
     * this method, the object cannot be used.
     *
     * @throws IOException if the underlying socket cannot be closed
     */
    public void close() throws IOException {
        if (_socket.isOpen()) {
            _socket.close();
        }
    }

    public static final void main(String[] args) throws Exception {
        AddressResolver addressResolver = null;
        try {
            final Enumeration<NetworkInterface> ifaceEnumerator =
                NetworkInterface.getNetworkInterfaces();

            if (ifaceEnumerator == null) {
                throw new UnsupportedOperationException(
                        "cannot find any network interfaces");
            }

            List<NetworkInterface> usableIfaces =
                new ArrayList<NetworkInterface>(8);
            while (ifaceEnumerator.hasMoreElements()) {
                NetworkInterface iface = ifaceEnumerator.nextElement();
                if (!iface.isVirtual() && !iface.isPointToPoint() &&
                        !iface.isLoopback() && iface.isUp()) {
                    usableIfaces.add(iface);
                }
            }

            if (usableIfaces.isEmpty()) {
                throw new UnsupportedOperationException(
                        "cannot find any suitable network interface");
            }

            NetworkInterface firstNetworkInterface =
                    usableIfaces.get(0);
            
            System.out.println("using interface: " +
                    firstNetworkInterface);
            
            addressResolver = new AddressResolver(firstNetworkInterface);
            addressResolver.sendBroadcast();
        } finally {
            try {
                if (addressResolver != null) {
                    addressResolver.close();
                }
            } catch (Exception ex) {
                addressResolver = null;
                throw ex;
            }
        }
    }
}
