package com.cheroliv.network

class CallbackDigestUserInterface {

    static void main(String[] args) {
        args = ["/usr/share/mime/packages/libreoffice.xml"]
        args.each { String filename ->
            // Calculate the digest
            new Thread(new CallbackDigest(filename: filename)).start()
        }
    }
}
