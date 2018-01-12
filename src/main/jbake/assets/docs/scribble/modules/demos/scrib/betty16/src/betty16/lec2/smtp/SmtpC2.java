package betty16.lec2.smtp;

import static betty16.lec2.smtp.Smtp.Smtp.Smtp.C;
import static betty16.lec2.smtp.Smtp.Smtp.Smtp.S;
import static betty16.lec2.smtp.Smtp.Smtp.Smtp._220;
import static betty16.lec2.smtp.Smtp.Smtp.Smtp._250;
import static betty16.lec2.smtp.Smtp.Smtp.Smtp._250d;

import org.scribble.net.Buf;
import org.scribble.net.scribsock.LinearSocket;
import org.scribble.net.session.SSLSocketChannelWrapper;
import org.scribble.net.session.MPSTEndpoint;
import org.scribble.net.session.SocketChannelEndpoint;

import betty16.lec2.smtp.Smtp.Smtp.Smtp;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.EndSocket;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.Smtp_C_1;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Branch_C_S_250__S_250d;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Case_C_S_250__S_250d;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Receive_C_S_220;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_Ehlo;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_Quit;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Select_C_S_StartTls;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Succ_In_S_220;
import betty16.lec2.smtp.Smtp.Smtp.channels.C.ioifaces.Succ_In_S_250;
import betty16.lec2.smtp.Smtp.Smtp.roles.C;
import betty16.lec2.smtp.message.SmtpMessageFormatter;
import betty16.lec2.smtp.message.client.Ehlo;
import betty16.lec2.smtp.message.client.Quit;
import betty16.lec2.smtp.message.client.StartTls;

public class SmtpC2 {

	public static void main(String[] args) throws Exception {
		String host = "smtp.cc.ic.ac.uk";
		int port = 25;

		Smtp smtp = new Smtp();
		try (MPSTEndpoint<Smtp, C> client = new MPSTEndpoint<>(smtp, C, new SmtpMessageFormatter())) {
			client.connect(S, SocketChannelEndpoint::new, host, port);
			new SmtpC2().run(new Smtp_C_1(client));
		}
	}

	private EndSocket run(Smtp_C_1 c1) throws Exception {
		return
			doInit(
					doStartTls(
							doInit(c1.async(S, _220)).to(Select_C_S_StartTls.cast)
					).to(Select_C_S_Ehlo.cast)
			)
			.to(Select_C_S_Quit.cast)
			.send(S, new Quit())
			.to(EndSocket.cast);
	}

	private Succ_In_S_250 doInit(Select_C_S_Ehlo<?> c) throws Exception {
		Branch_C_S_250__S_250d<?, ?> b =
				c.send(S, new Ehlo("test")).to(Branch_C_S_250__S_250d.cast);
		Buf<Object> buf = new Buf<>();
		for (Case_C_S_250__S_250d<?, ?> cases = b.branch(S); true; cases = b.branch(S)) {
			switch (cases.getOp()) {
				case _250:  return printlnBuf(cases.receive(S, _250, buf), buf);
				case _250d: b = cases.receive(S, _250d, buf).to(Branch_C_S_250__S_250d.cast); System.out.print(buf.val); break;
			}
		}
	}

	private Succ_In_S_220 doStartTls(Select_C_S_StartTls<?> c) throws Exception {
		return
				LinearSocket.wrapClient(
						c.send(S, new StartTls()).to(Receive_C_S_220.cast)
							.async(S, _220)
				, S, SSLSocketChannelWrapper::new);
	}

	private static <S, B extends Buf<?>> S printlnBuf(S s, B b) {
		System.out.println(b.val);
		return s;
	}

	/*private static <S, B extends Buf<?>> S printBuf(S s, B b) {
		System.out.print(b.val);
		return s;
	}*/
}
