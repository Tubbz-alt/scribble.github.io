package org.scribble.codegen.java.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.scribble.main.RuntimeScribbleException;

public class EnumBuilder extends TypeBuilder
{
	private final List<String> vals = new LinkedList<String>();

	protected EnumBuilder()
	{
		
	}

	protected EnumBuilder(String name)
	{
		super(name);
	}
	
	public void addValues(String... vals)
	{
		this.vals.addAll(Arrays.asList(vals));
	}

	@Override
	public String build()
	{
		String enun = "";
		enun = buildSignature(enun);
		enun += " {\n";
		enun += "\t" + this.vals.stream().collect(Collectors.joining(", "));
		enun += "\n}";
		return enun;
	}
		
	@Override
	protected String buildSignature(String enun)
	{
		if (!this.mods.isEmpty())
		{
			enun += this.mods.stream().collect(Collectors.joining(" "));
			enun += " ";
		}
		enun += "enum " + this.name;
		if (!this.ifaces.isEmpty())
		{
			enun += " implements ";
			enun += this.ifaces.stream().collect(Collectors.joining(", "));
		}
		return enun;
	}
	
	// Following not necessarily invalid for Java, just for this tool set
	
	@Override
	public void setPackage(String packname)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}
	
	@Override
	public void addImports(String... imports)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}

	@Override
	public FieldBuilder newField(String name)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}
	
	@Override
	public AbstractMethodBuilder newAbstractMethod()
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}
	
	@Override
	public ClassBuilder newTopLevelClass()
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}

	@Override
	protected String buildFields(String clazz)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}

	@Override
	protected String buildMethods(String clazz)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}

	@Override
	protected String buildMemberTypes(String clazz)
	{
		throw new RuntimeScribbleException("Invalid for enums");
	}
}
