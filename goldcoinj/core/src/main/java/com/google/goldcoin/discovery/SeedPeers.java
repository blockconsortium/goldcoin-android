/**
 * Copyright 2011 Micheal Swiggs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.goldcoin.discovery;

import com.google.goldcoin.core.NetworkParameters;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * SeedPeers stores a pre-determined list of goldcoin node addresses. These nodes are selected based on being
 * active on the network for a long period of time. The intention is to be a last resort way of finding a connection
 * to the network, in case IRC and DNS fail. The list comes from the goldcoin C++ source code.
 */
public class SeedPeers implements PeerDiscovery {
    private NetworkParameters params;
    private int pnseedIndex;

    public SeedPeers(NetworkParameters params) {
        this.params = params;
    }

    /**
     * Acts as an iterator, returning the address of each node in the list sequentially.
     * Once all the list has been iterated, null will be returned for each subsequent query.
     *
     * @return InetSocketAddress - The address/port of the next node.
     * @throws PeerDiscoveryException
     */
    public InetSocketAddress getPeer() throws PeerDiscoveryException {
        try {
            return nextPeer();
        } catch (UnknownHostException e) {
            throw new PeerDiscoveryException(e);
        }
    }

    private InetSocketAddress nextPeer() throws UnknownHostException {
        if (pnseedIndex >= seedAddrs.length) return null;
        return new InetSocketAddress(convertAddress(seedAddrs[pnseedIndex++]),
                params.port);
    }

    /**
     * Returns an array containing all the goldcoin nodes within the list.
     */
    public InetSocketAddress[] getPeers(long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
        try {
            return allPeers();
        } catch (UnknownHostException e) {
            throw new PeerDiscoveryException(e);
        }
    }
    public InetSocketAddress[] getPeers2(long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
        try {
            return allPeers2();
        } catch (UnknownHostException e) {
            throw new PeerDiscoveryException(e);
        }
    }
    private InetSocketAddress[] allPeers() throws UnknownHostException {
        InetSocketAddress[] addresses = new InetSocketAddress[seedAddrs.length];
        for (int i = 0; i < seedAddrs.length; ++i) {
            addresses[i] = new InetSocketAddress(convertAddress(seedAddrs[i]), params.port);
        }
        return addresses;
    }
    private InetSocketAddress[] allPeers2() throws UnknownHostException {
        InetSocketAddress[] addresses = new InetSocketAddress[seedAddrs2.length];
        for (int i = 0; i < seedAddrs2.length; ++i) {
            addresses[i] = new InetSocketAddress(seedAddrs2[i], params.port);
        }
        return addresses;
    }

    private InetAddress convertAddress(int seed) throws UnknownHostException {
        byte[] v4addr = new byte[4];
        v4addr[0] = (byte) (0xFF & (seed));
        v4addr[1] = (byte) (0xFF & (seed >> 8));
        v4addr[2] = (byte) (0xFF & (seed >> 16));
        v4addr[3] = (byte) (0xFF & (seed >> 24));
        return InetAddress.getByAddress(v4addr);
    }

    public static int[] seedAddrs =
            {
                    //0x2EFDCB71, 0xCC1B3AD6, 0xADA77149,
            };
    public static String[] seedAddrs2 =
            {
                    "162.243.57.246",
                    "84.200.17.249",
                    "174.53.218.230",
                    "72.78.100.7",
                    "217.31.181.40",
                    "81.2.95.132",
                    //"216.232.230.91",
                    //"84.74.106.242",
            //"82.30.110.220",
            //"95.96.252.18",
            //"67.253.99.90",
            //"82.227.217.20",
            //"93.136.92.72"
            };

    public void shutdown() {
    }
}
