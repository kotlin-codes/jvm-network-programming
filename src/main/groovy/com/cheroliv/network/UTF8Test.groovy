package com.cheroliv.network

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.charset.StandardCharsets

class UTF8Test {

    static void main(String[] args) throws UnsupportedEncodingException {
        Logger log = LoggerFactory.getLogger(UTF8Test.class)
        String pi = "\u03C0"
        byte[] data = pi.getBytes(StandardCharsets.UTF_8)
        for (byte x : data) {
            log.info(Integer.toHexString(x))
        }
    }

}
