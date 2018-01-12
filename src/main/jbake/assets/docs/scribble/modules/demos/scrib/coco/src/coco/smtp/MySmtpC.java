//$ java -cp modules/cli/target/classes/';'modules/core/target/classes';'modules/parser/target/classes';'modules/demos/target/classes/ coco.smtp.Client

package coco.smtp;

import static coco.smtp.Smtp.Smtp.Smtp.C;
import static coco.smtp.Smtp.Smtp.Smtp.S;
import static coco.smtp.Smtp.Smtp.Smtp._220;
import static coco.smtp.Smtp.Smtp.Smtp._250;
import static coco.smtp.Smtp.Smtp.Smtp._250d;

import org.scribble.net.Buf;
import org.scribble.net.scribsock.LinearSocket;
import org.scribble.net.session.SSLSocketChannelWrapper;
import org.scribble.net.session.MPSTEndpoint;
import org.scribble.net.session.SocketChannelEndpoint;

import coco.smtp.Smtp.Smtp.Smtp;
import coco.smtp.Smtp.Smtp.channels.C.Smtp_C_1;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Branch_C_S_250__S_250d;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Case_C_S_250__S_250d;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Out_S_Ehlo;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Receive_C_S_220;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_Ehlo;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_Quit;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_StartTls;
import coco.smtp.Smtp.Smtp.channels.C.ioifaces.Succ_In_S_250;
import coco.smtp.Smtp.Smtp.roles.C;
import coco.smtp.message.SmtpMessageFormatter;
import coco.smtp.message.client.Ehlo;
import coco.smtp.message.client.Quit;
import coco.smtp.message.client.StartTls;
import coco.smtp.message.server._250;
import coco.smtp.message.server._250d;

public class MySmtpC
{
	public MySmtpC() throws Exception
	{
		run();
	}

	public static void main(String[] args) throws Exception
	{
		new MySmtpC();
	}

	public void run() throws Exception
	{
		String host = "smtp.cc.ic.ac.uk";
		int port = 25;

		Smtp smtp = new Smtp();
		try (MPSTEndpoint<Smtp, C> se = new MPSTEndpoint<>(smtp, C, new SmtpMessageFormatter()))
		{
			se.connect(S, SocketChannelEndpoint::new, host, port);

			Smtp_C_1 s1 = new Smtp_C_1(se);
			doInit(
				LinearSocket.wrapClient(
					doInit(s1.receive(S, _220, new Buf<>()))
						.to(Select_C_S_StartTls.cast)  // Run-time cast
						.send(S, new StartTls())
						.to(Receive_C_S_220.cast)  // Safe cast
						.receive(S, _220, new Buf<>())
						.to(Select_C_S_Ehlo.cast)  // Safe cast
				, S, SSLSocketChannelWrapper::new)
			)
			.to(Select_C_S_Quit.cast)  // Run-time cast
			.send(S, new Quit());
		}
	}

	//*
	private Succ_In_S_250 doInit(Out_S_Ehlo<?> s) throws Exception
	{
		// TODO: ...
		return null;
	}
	/*/
	private Succ_In_S_250 doInit(Select_C_S_Ehlo<?> s) throws Exception
	{
		Branch_C_S_250__S_250d<?, ?> b =
				s.send(S, new Ehlo("test"))
				 .to(Branch_C_S_250__S_250d.cast);  // Safe cast
		Buf<_250> b1 = new Buf<>();
		Buf<_250d> b2 = new Buf<>();
		while (true)
		{
			Case_C_S_250__S_250d<?, ?> c = b.branch(S);
			switch (c.getOp())
			{
				case _250:
					return Client1.printlnBuf(c.receive(S, _250, b1), b1);
				case _250d:
					b = Client1.printBuf(
								c.receive(S, _250d, b2)
								 .to(Branch_C_S_250__S_250d.cast)  // Safe cast
							, b2);
					break;
			}
		}
	}
	//*/
}
