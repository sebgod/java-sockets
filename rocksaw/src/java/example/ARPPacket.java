package example;

import java.io.IOException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;

public class ARPPacket {
    public static final int PACKET_SIZE = 28;

    public static final int HTYPE = 0;
    public static final byte HTYPE_SIZE = 2;
    public static final byte MAC_SIZE = 6;
    public static final short HTYPE_ETHER = 1;

    public static final byte[] MAC_EMPTY =
        new byte[]{(byte)0x0, (byte)0x0, (byte)0x0,
                   (byte)0x0, (byte)0x0, (byte)0x0};
    
    public static final byte[] MAC_BROADCAST =
        new byte[]{(byte)0xff, (byte)0xff, (byte)0xff,
                   (byte)0xff, (byte)0xff, (byte)0xff};
    
    public static final int PTYPE = 2;
    public static final byte PTYPE_SIZE = 2;
    public static final byte IP4_SIZE = 4;
    public static final short PTYPE_IP4 = 0x800;
    
    public static final byte[] IP4_EMPTY =
        new byte[]{(byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0};
    

    public static final int HLEN = 4;
    public static final byte HLEN_SIZE = 1;

    public static final int PLEN = 5;
    public static final byte PLEN_SIZE = 1;

    public static final int OPER = 6;
    public static final byte OPER_SIZE = 2;
    public static final short OPER_REQ = 1;
    public static final short OPER_REPL = 2;

    public static final int SHA = 8;
    public static final byte SHA_SIZE = 6;

    public static final int SPA = 14;
    public static final byte SPA_SIZE = 4;

    public static final int THA = 18;
    public static final byte THA_SIZE = 6;

    public static final int TPA = 24;
    public static final byte TPA_SIZE = 4;

    private final byte[] data;

    public ARPPacket() {
        data = new byte[PACKET_SIZE];
        setHardwareAndProtocolType(HTYPE_ETHER, PTYPE_IP4);
    }

    public final void setOperation(short operation) {
        setShortField(OPER, operation);
    }

    public final void setSenderMAC(NetworkInterface networkInterface)
        throws SocketException
    {
        setSenderMAC(networkInterface.getHardwareAddress());
    }

    public final void setSenderMAC(byte[] address) {
        setMACField(SHA, address);
    }

    public final void setTargetMAC(NetworkInterface networkInterface)
        throws SocketException
    {
        setTargetMAC(networkInterface.getHardwareAddress());
    }

    public final void setTargetMAC(byte[] address) {
        setMACField(THA, address);
    }

    public final void setSender(NetworkInterface networkInterface)
        throws SocketException
    {
        setSenderIP(networkInterface);
        setSenderMAC(networkInterface);
    }

    public final void setSenderIP(NetworkInterface networkInterface) {
        setSenderIP(InetAddressHelper.getFirstInetAddress(networkInterface));
    }

    public final void setSenderIP(InetAddress address) {
        setSenderIP(address.getAddress());
    }

    public final void setSenderIP(byte[] address) {
        setIP4Field(SPA, address);
    }
    
    public final void setTarget(NetworkInterface networkInterface)
        throws SocketException
    {
        setTargetIP(networkInterface);
        setTargetMAC(networkInterface);
    }

    public final void setTargetIP(NetworkInterface networkInterface) {
        setTargetIP(InetAddressHelper.getFirstInetAddress(networkInterface));
    }

    public final void setTargetIP(InetAddress address) {
        setTargetIP(address.getAddress());
    }

    public final void setTargetIP(byte[] address) {
        setIP4Field(TPA, address);
    }

    public void setHardwareAndProtocolType(short hardwareType,
            short protocolType) {
        switch (hardwareType) {
            case HTYPE_ETHER:
                setByteField(HLEN, MAC_SIZE);
                switch (protocolType) {
                    case PTYPE_IP4:
                        setByteField(PLEN, IP4_SIZE);
                        break;

                    default:
                        throw new UnsupportedOperationException(
                                "Protocol type: " + protocolType + " is " +
                                "not supported");
                }
                break;

            default:
                throw new UnsupportedOperationException("Hardware type: " +
                        hardwareType + " is not supported");
        }

        setShortField(HTYPE, hardwareType);
        setShortField(PTYPE, protocolType);
    }

    private final void setByteField(int field, byte value) {
        data[field] = value;
    }

    private final void setShortField(int field, short value) {
        data[field]     = (byte)((value >> 8) & 0xff);
        data[field + 1] = (byte)(value & 0xff);
    }

    private final void setIP4Field(int field, byte[] address) {
        System.arraycopy(data, field, address, 0, IP4_SIZE);
    }

    private final void setMACField(int field, byte[] address) {
        System.arraycopy(data, field, address, 0, MAC_SIZE);
    }
}

class InetAddressHelper {
    public static InetAddress getFirstInetAddress(
            NetworkInterface networkInterface)
    {
        Enumeration<InetAddress> inetAddressEnumerator =
            networkInterface.getInetAddresses();
        if (!inetAddressEnumerator.hasMoreElements()) {
            throw new IllegalArgumentException(
                    "network interface has no IP address");
        }
        return inetAddressEnumerator.nextElement();
    }
}
