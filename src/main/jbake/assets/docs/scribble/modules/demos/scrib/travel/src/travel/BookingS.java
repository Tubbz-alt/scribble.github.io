package travel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.scribble.main.ScribbleRuntimeException;
import org.scribble.net.Buf;
import org.scribble.net.ObjectStreamFormatter;
import org.scribble.net.scribsock.ScribServerSocket;
import org.scribble.net.scribsock.SocketChannelServer;
import org.scribble.net.session.MPSTEndpoint;

import travel.Travel.Booking.Booking;
import travel.Travel.Booking.channels.S.Booking_S_1;
import travel.Travel.Booking.channels.S.Booking_S_1_Cases;
import travel.Travel.Booking.roles.S;

public class BookingS
{
	public static void main(String[] args) throws IOException, ScribbleRuntimeException, ExecutionException, InterruptedException
	{
		try (ScribServerSocket ss_C = new SocketChannelServer(8888);
				 ScribServerSocket ss_A = new SocketChannelServer(9999))
		{
			while (true)
			{
				Booking booking = new Booking();
				try (MPSTEndpoint<Booking, S> se = new MPSTEndpoint<>(booking, Booking.S, new ObjectStreamFormatter()))
				{
					//S.register(Booking.A, ss_A);
				
					se.accept(ss_C, Booking.C);
					se.accept(ss_A, Booking.A);
					Buf<String> payment = new Buf<>();

					Booking_S_1 s1 = new Booking_S_1(se);
					Booking_S_1_Cases s1cases;
					X: while (true)
					{
						s1cases = s1.branch(Booking.A);
						switch (s1cases.op)
						{
							case Dummy:
								s1 = s1cases.receive(Booking.Dummy);
								break;
							case No:
								s1cases.receive(Booking.No);
								break X;
							case Yes:
								System.out.println("Yes: ");
								s1cases.receive(Booking.Yes)
								  .receive(Booking.C, Booking.Payment, payment)
								  .send(Booking.C, Booking.Ack);
								break X;
						}
					}
					
					System.out.println("Done:");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
