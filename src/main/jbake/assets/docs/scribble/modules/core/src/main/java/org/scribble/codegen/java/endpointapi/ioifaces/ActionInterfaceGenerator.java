package org.scribble.codegen.java.endpointapi.ioifaces;

import org.scribble.codegen.java.endpointapi.ReceiveSocketGenerator;
import org.scribble.codegen.java.endpointapi.OutputSocketGenerator;
import org.scribble.codegen.java.endpointapi.SessionApiGenerator;
import org.scribble.codegen.java.endpointapi.StateChannelApiGenerator;
import org.scribble.codegen.java.util.AbstractMethodBuilder;
import org.scribble.codegen.java.util.InterfaceBuilder;
import org.scribble.codegen.java.util.JavaBuilder;
import org.scribble.main.ScribbleException;
import org.scribble.model.endpoint.EState;
import org.scribble.model.endpoint.actions.EAction;
import org.scribble.model.endpoint.actions.EReceive;
import org.scribble.sesstype.name.GProtocolName;
import org.scribble.sesstype.name.PayloadType;

public class ActionInterfaceGenerator extends IOInterfaceGenerator
{
	private final EAction a;
	private final InterfaceBuilder ib = new InterfaceBuilder();

	public ActionInterfaceGenerator(StateChannelApiGenerator apigen, EState curr, EAction a)
	{
		super(apigen, curr);
		this.a = a;
	}

	@Override
	public InterfaceBuilder generateType() throws ScribbleException
	{
		GProtocolName gpn = this.apigen.getGProtocolName();

		this.ib.setName(getActionInterfaceName(this.a));
		this.ib.setPackage(IOInterfacesGenerator.getIOInterfacePackageName(this.apigen.getGProtocolName(), this.apigen.getSelf()));
		this.ib.addImports("java.io.IOException");
		this.ib.addImports(SessionApiGenerator.getEndpointApiRootPackageName(gpn) + ".*");  // FIXME: factor out with ScribSocketGenerator
		this.ib.addImports(SessionApiGenerator.getRolesPackageName(gpn) + ".*");
		this.ib.addImports(SessionApiGenerator.getOpsPackageName(gpn) + ".*");
		this.ib.addModifiers(JavaBuilder.PUBLIC);
		this.ib.addParameters("__Succ extends " + SuccessorInterfaceGenerator.getSuccessorInterfaceName(this.a));
		AbstractMethodBuilder mb = this.ib.newAbstractMethod();  // FIXME: factor out with ReceiveSocketBuilder
		//AbstractMethodBuilder mb2 = null;
		if (this.a instanceof EReceive)
		{
			/*if (this.curr.getAcceptable().size() > 1)
			{
				//CaseSocketGenerator.setCaseReceiveHeaderWithoutReturnType(this.apigen, this.a, mb);
			}
			//else*/
			{
				ReceiveSocketGenerator.setReceiveHeaderWithoutReturnType(this.apigen, this.a, mb);
				/*if (this.curr.getAcceptable().size() == 1)  // FIXME: action interface should not depend on curr state -- should generate this method in the IO State I/f, not here
				{
					mb2 = this.ib.newAbstractMethod();
					ReceiveSocketGenerator.setAsyncDiscardHeaderWithoutReturnType(this.apigen, this.a, mb2, 
							InputFutureGenerator.getInputFutureName(this.apigen.getSocketClassName(this.curr)));
				}
				/*else
				{
					CaseSocketGenerator.setCaseReceiveDiscardHeaderWithoutReturnType(this.apigen, this.a, mb2);
				}*/
			}
		}
		else //if (this.a instanceof Send)
		{
			OutputSocketGenerator.setSendHeaderWithoutReturnType(this.apigen, this.a, mb);
		}
		/*EndpointState succ = this.curr.accept(this.a);
		if (succ.isTerminal())
		{
			ScribSocketGenerator.setNextSocketReturnType(this.apigen, mb, succ);
			/*if (this.a instanceof Receive)
			{
				ScribSocketGenerator.setNextSocketReturnType(this.apigen, mb2, succ);
			}* /
		}
		else*/
		{
			mb.setReturn("__Succ");
			//if (this.a instanceof Receive)
			/*if (this.a instanceof Receive && this.curr.getAcceptable().size() == 1)
			{
				mb2.setReturn("__Succ");
			}*/
		}
		return ib;
	}
	
	// FIXME: curr unnecessary
	public static String getActionInterfaceName(EAction a)
	{
		/*String name = (a instanceof Receive)
				? "In"
				: "Out";*/
		String name;
		//if (curr.getAcceptable().iterator().next() instanceof Receive)
		if (a instanceof EReceive)
		{
			/*if (curr.getAcceptable().size() > 1)
			{
				name = "Case";  // FIXME: make subtype of In?
			}
			//else*/
			{
				name = "In";
			}
		}
		else
		{
			name = "Out";
		}
		name += "_" + getActionString(a);
		return name;
	}

	public static String getActionString(EAction a)  // FIXME: peer not needed for inputs
	{
		//String name = a.peer + "$" + a.mid;
		String name = a.obj + "_" + a.mid;
		for (PayloadType<?> pay : a.payload.elems)
		{
			//name += "$" + pay;
			name += "_" + pay;
		}
		return name;
	}
}
