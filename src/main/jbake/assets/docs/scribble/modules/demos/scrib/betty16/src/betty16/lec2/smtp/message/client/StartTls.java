package betty16.lec2.smtp.message.client;

import betty16.lec2.smtp.Smtp.Smtp.Smtp;
import betty16.lec2.smtp.message.SmtpMessage;

public class StartTls extends SmtpMessage
{
	private static final long serialVersionUID = 1L;

	public StartTls()
	{
		super(Smtp.StartTls);
	}
}
