package com.cheroliv.network

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@CompileStatic
class Weblog {

    static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(Weblog.class)
        try {
            FileInputStream fin = new FileInputStream(args[0])
            try {
                Reader in_ = new InputStreamReader(fin)
                try {
                    BufferedReader bin = new BufferedReader(in_)
                    try {
                        String entry = bin.readLine()
                        while (entry != null) {
                            // separate out the IP address
                            int index = entry.indexOf(' ')
                            String ip = entry.substring(0, index)
                            String theRest = entry.substring(index)
                            // Ask DNS for the hostname and print it out
                            try {
                                InetAddress address = InetAddress.getByName(ip)
                                log.info address.getHostName() + theRest
                            } catch (UnknownHostException ex) {
                                log.error(entry)
                            }
                            entry = bin.readLine()
                        }
                    } finally {
                        bin.close()
                    }
                } finally {
                    in_.close()
                }
            } finally {
                fin.close()
            }
        } catch (IOException ex) {
            log.info "Exception: " + ex
        }
    }
}
