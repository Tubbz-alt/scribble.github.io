package bettybook.http.longv.message.server;

import bettybook.http.longv.HttpLong.Http.Http;
import bettybook.http.longv.message.HeaderField;

public class ETag extends HeaderField
{
	private static final long serialVersionUID = 1L;

	public ETag(String tag)
	{
		super(Http.ETag, tag);
	}
}
