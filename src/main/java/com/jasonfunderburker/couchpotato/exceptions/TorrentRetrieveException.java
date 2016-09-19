package com.jasonfunderburker.couchpotato.exceptions;

/**
 * Created by JasonFunderburker on 12.09.2016
 */
public class TorrentRetrieveException extends Exception {
    public TorrentRetrieveException() {
        super();
    }

    public TorrentRetrieveException(String message) {
        super(message);
    }

    public TorrentRetrieveException(String message, Throwable cause) {
        super(message, cause);
    }

    public TorrentRetrieveException(Throwable cause) {
        super(cause);
    }

    protected TorrentRetrieveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
