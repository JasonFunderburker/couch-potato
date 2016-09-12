package com.jasonfunderburker.couchpotato.exceptions;

/**
 * Created by JasonFunderburker on 12.09.2016
 */
public class TorrentStateRetrieveException extends Exception {
    public TorrentStateRetrieveException() {
        super();
    }

    public TorrentStateRetrieveException(String message) {
        super(message);
    }

    public TorrentStateRetrieveException(String message, Throwable cause) {
        super(message, cause);
    }

    public TorrentStateRetrieveException(Throwable cause) {
        super(cause);
    }

    protected TorrentStateRetrieveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
