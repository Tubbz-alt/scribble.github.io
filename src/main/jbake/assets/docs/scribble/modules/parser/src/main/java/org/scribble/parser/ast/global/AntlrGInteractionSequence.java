package org.scribble.parser.ast.global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;
import org.scribble.ast.AstFactoryImpl;
import org.scribble.ast.global.GInteractionNode;
import org.scribble.ast.global.GInteractionSeq;
import org.scribble.parser.ScribParser;
import org.scribble.parser.util.ScribParserUtil;
import org.scribble.util.ScribParserException;

public class AntlrGInteractionSequence
{
	public static GInteractionSeq parseGInteractionSequence(ScribParser parser, CommonTree ct) throws ScribParserException
	{
		/*List<GInteractionNode> gis =
				getInteractionChildren(ct).stream().map((gi) -> (GInteractionNode) parser.parse(gi)).collect(Collectors.toList());*/
		List<GInteractionNode> gis = new LinkedList<>();
		for (CommonTree gi : getInteractionChildren(ct))
		{
			gis.add((GInteractionNode) parser.parse(gi));
		}
		return AstFactoryImpl.FACTORY.GInteractionSeq(ct, gis);
	}

	public static List<CommonTree> getInteractionChildren(CommonTree ct)
	{
		return (ct.getChildCount() == 0)
				? Collections.emptyList()
				: ScribParserUtil.toCommonTreeList(ct.getChildren());
	}
}
