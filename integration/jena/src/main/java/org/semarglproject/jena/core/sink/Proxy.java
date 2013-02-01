package org.semarglproject.jena.core.sink;


public class Proxy {

        public static void setProxy() {

                System.getProperties().put("proxySet", "true");

                System.getProperties().put("proxyPort", "8080");

                System.getProperties().put("proxyHost", "proxystc");

        }

}