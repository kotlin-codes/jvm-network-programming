package com.cheroliv.network

class InetAddressVersion {
    /**
     * Version of Internet Protocol (IP) address
     * @param ia
     * @return
     */
    static int getVersion(InetAddress ia) {
        byte[] address = ia.getAddress()
        if (address.length == 4) return 4
        else if (address.length == 16) return 6
        else return -1
    }

    static void main(String... args) {
        println "IP version : ${getVersion(InetAddress.localHost)}"
    }
}
