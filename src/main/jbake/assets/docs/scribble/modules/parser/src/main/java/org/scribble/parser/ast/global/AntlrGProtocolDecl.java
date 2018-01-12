package org.scribble.parser.ast.global;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.global.GProtocolDecl;
import org.scribble.ast.global.GProtocolDef;
import org.scribble.ast.global.GProtocolHeader;
import org.scribble.parser.ScribParser;
import org.scribble.util.ScribParserException;

public class AntlrGProtocolDecl
{
	public static final int HEADER_CHILD_INDEX = 0;
	public static final int BODY_CHILD_INDEX = 1;
	//public static final int EXPLICIT_CONNECTIONS_FLAG_INDEX = 2;
	public static final int MODIFIERS_CHILD_INDEX = 2;

	public static GProtocolDecl parseGPrototocolDecl(ScribParser parser, CommonTree ct) throws ScribParserException
	{
		GProtocolHeader header = (GProtocolHeader) parser.parse(getHeaderChild(ct));
		GProtocolDef def = (GProtocolDef) parser.parse(getBodyChild(ct));
		List<GProtocolDecl.Modifiers> modifiers = new LinkedList<>();
		/*if (isExplicitConnections(ct))
		{
			modifiers.add(GProtocolDecl.Modifiers.EXPLICIT);
		}*/
		if (hasModifiersChild(ct))
		{
			for (CommonTree mod :getModifierChildren(ct))
			{
				switch (mod.getText())
				{
					case "aux":      modifiers.add(GProtocolDecl.Modifiers.AUX); break;
					case "explicit": modifiers.add(GProtocolDecl.Modifiers.EXPLICIT); break;
					default: throw new RuntimeException("TODO: " + mod);
				}
			}
		}
		return AstFactoryImpl.FACTORY.GProtocolDecl(ct, modifiers, header, def);
	}

	public static CommonTree getHeaderChild(CommonTree ct)
	{
		return (CommonTree) ct.getChild(HEADER_CHILD_INDEX);
	}

	public static CommonTree getBodyChild(CommonTree ct)
	{
		return (CommonTree) ct.getChild(BODY_CHILD_INDEX);
	}

	public static boolean hasModifiersChild(CommonTree ct)
	{
		return ct.getChildCount() > MODIFIERS_CHILD_INDEX;
	}

	public static List<CommonTree> getModifierChildren(CommonTree ct)
	{
		//return (CommonTree) ct.getChild(MODIFIERS_CHILD_INDEX);
		return ((List<?>) ((CommonTree) ct.getChild(MODIFIERS_CHILD_INDEX)).getChildren()).stream()
				.map((c) -> (CommonTree) c).collect(Collectors.toList());
	}

	/*public static boolean isExplicitConnections(CommonTree ct)
	{
		return ct.getChildCount() > EXPLICIT_CONNECTIONS_FLAG_INDEX;
	}*/
}
