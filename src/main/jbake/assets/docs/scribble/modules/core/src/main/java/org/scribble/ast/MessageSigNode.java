package org.scribble.ast;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.name.simple.OpNode;
import org.scribble.del.ScribDel;
import org.scribble.main.ScribbleException;
import org.scribble.sesstype.MessageSig;
import org.scribble.visit.AstVisitor;

public class MessageSigNode extends ScribNodeBase implements MessageNode
{
	public final OpNode op;
	public final PayloadElemList payloads;

	public MessageSigNode(CommonTree source, OpNode op, PayloadElemList payload)
	{
		super(source);
		this.op = op;
		this.payloads = payload;
	}
	
	@Override
	public MessageNode project()  // Currently outside of visitor/env pattern
	{
		return AstFactoryImpl.FACTORY.MessageSigNode(this.source, this.op, this.payloads.project());  // Original del not retained by projection
	}

	@Override
	protected MessageSigNode copy()
	{
		return new MessageSigNode(this.source, this.op, this.payloads);
	}	

	@Override
	public MessageSigNode clone()
	{
		OpNode op = this.op.clone();
		PayloadElemList payload = this.payloads.clone();
		return AstFactoryImpl.FACTORY.MessageSigNode(this.source, op, payload);
	}
	
	public MessageSigNode reconstruct(OpNode op, PayloadElemList payload)
	{
		ScribDel del = del();	
		MessageSigNode msn = new MessageSigNode(this.source, op, payload);
		msn = (MessageSigNode) msn.del(del);
		return msn;
	}
	
	@Override
	public MessageSigNode visitChildren(AstVisitor nv) throws ScribbleException
	{
		OpNode op = (OpNode) visitChild(this.op, nv);
		PayloadElemList payload = (PayloadElemList) visitChild(this.payloads, nv);
		return reconstruct(op, payload);
	}
	

	@Override
	public boolean isMessageSigNode()
	{
		return true;
	}

	// Make a direct scoped version? (taking scope as argument)
	@Override
	public MessageSig toArg()
	{
		return new MessageSig(this.op.toName(), this.payloads.toPayload());
	}

	@Override
	public MessageSig toMessage()
	{
		return toArg();
	}

	@Override
	public String toString()
	{
		return this.op.toString() + this.payloads.toString();
	}
}
