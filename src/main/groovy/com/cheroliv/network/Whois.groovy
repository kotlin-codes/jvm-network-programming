package com.cheroliv.network

class Whois {

    final static int DEFAULT_PORT = 43
    final static String DEFAULT_HOST = "whois.internic.net"

    int port = DEFAULT_PORT
    InetAddress host

    Whois(InetAddress host, int port) {
        this.host = host
        this.port = port
    }

    Whois(InetAddress host) {
        this(host, DEFAULT_PORT)
    }

    Whois(String hostname, int port)
            throws UnknownHostException {
        this(InetAddress.getByName(hostname), port)
    }

    Whois(String hostname) throws UnknownHostException {
        this(InetAddress.getByName(hostname), DEFAULT_PORT)
    }

    Whois() throws UnknownHostException {
        this(DEFAULT_HOST, DEFAULT_PORT)
    }

    // Items to search for
    enum SearchFor {
        ANY("Any"), NETWORK("Network"), PERSON("Person"), HOST("Host"),
        DOMAIN("Domain"), ORGANIZATION("Organization"), GROUP("Group"),
        GATEWAY("Gateway"), ASN("ASN");

        private String label

        private SearchFor(String label) {
            this.label = label
        }
    }

    // Categories to search in
    enum SearchIn {
        ALL(""), NAME("Name"), MAILBOX("Mailbox"), HANDLE("!");

        private String label

        private SearchIn(String label) {
            this.label = label
        }
    }

    String lookUpNames(String target, SearchFor category,
                       SearchIn group, boolean exactMatch) throws IOException {

        String suffix = ""
        if (!exactMatch) suffix = "."

        String prefix = category.label + " " + group.label
        String query = prefix + target + suffix

        Socket socket = new Socket()
        try {
            SocketAddress address = new InetSocketAddress(host, port)
            socket.connect(address)
            Writer out = new OutputStreamWriter(
                    socket.outputStream,
                    "ASCII")
            BufferedReader in_ = new BufferedReader(new
                    InputStreamReader(
                    socket.inputStream,
                    "ASCII"))
            out.write(query + "\r\n")
            out.flush()

            StringBuilder response = new StringBuilder()
            String theLine = null
            while ((theLine = in_.readLine()) != null) {
                response.append(theLine)
                response.append("\r\n")
            }
            return response.toString()
        } finally {
            socket.close()
        }
    }

    InetAddress getHost() {
        return this.host
    }

    void setHost(String host)
            throws UnknownHostException {
        this.host = InetAddress.getByName(host)
    }
}
