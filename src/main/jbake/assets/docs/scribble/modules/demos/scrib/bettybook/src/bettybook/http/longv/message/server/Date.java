package bettybook.http.longv.message.server;

import bettybook.http.longv.HttpLong.Http.Http;
import bettybook.http.longv.message.HeaderField;

public class Date extends HeaderField
{
	private static final long serialVersionUID = 1L;

	public Date(String date)
	{
		super(Http.Date, date);
	}
}
