package com.jasonfunderburker.couchpotato.exceptions;

/**
 * Created by JasonFunderburker on 30.03.17.
 */
public class TorrentDownloadException extends TorrentRetrieveException {
    public TorrentDownloadException() {
        super();
    }

    public TorrentDownloadException(String message) {
        super(message);
    }

    public TorrentDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public TorrentDownloadException(Throwable cause) {
        super(cause);
    }

    protected TorrentDownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
