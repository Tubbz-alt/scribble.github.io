package http.longvers.message.server;

import http.longvers.message.HttpLongMessage;

@Deprecated
public class CRLF extends HttpLongMessage
{
	private static final long serialVersionUID = 1L;
	
	public CRLF()
	{
		//super(Http.CRLF, HttpMessage.CRLF);  // No: HttpMessage already appends the CRLF
		//super(Http.CRLF, "");
		super(null, "");
	}
}
