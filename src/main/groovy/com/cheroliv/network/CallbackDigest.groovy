package com.cheroliv.network

import javax.xml.bind.DatatypeConverter
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class CallbackDigest implements Runnable {
    String filename

    static void receiveDigest(byte[] digest, String name) {
        println("$name : ${DatatypeConverter.printHexBinary(digest)}")
    }

    @Override
    void run() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256")
            DigestInputStream din = new DigestInputStream(new FileInputStream(filename), sha)
            while (din.read() != -1) {// read entire file
            }
            din.close()
            receiveDigest(sha.digest(), filename)
        } catch (IOException | NoSuchAlgorithmException ex) {
            System.err.println(ex)
        }
    }
}
