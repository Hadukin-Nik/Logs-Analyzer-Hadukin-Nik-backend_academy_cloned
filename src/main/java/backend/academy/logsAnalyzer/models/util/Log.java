package backend.academy.logsAnalyzer.models.util;

import java.time.OffsetDateTime;

@SuppressWarnings("RecordComponentNumber")
public record Log(String remoteAddr, String remoteUser, OffsetDateTime date, String httpMethod, String request,
                  String httpVersion, short status, long bodyBytesSent, String httpReferer, String userAgent) {
}
