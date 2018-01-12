package org.scribble.ast.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.scribble.sesstype.name.ProtocolName;
import org.scribble.sesstype.name.Role;

// Mutable
// Used for two purposes: one to encapsulate Map structure and add method for ContextBuilder; second to allow overriding the generic types in ProtocolDeclContext (cf. nested Map generics)
public abstract class DependencyMap<N extends ProtocolName<?>>  // Maybe better to parameterise on Kind only?
{
	// self -> (proto -> target) -- the self role in this proto, depends on the proto, for the role param target
	private final Map<Role, Map<N, Set<Role>>> deps = new HashMap<>();  // All the potential dependencies from this protocol decl as the root

	public DependencyMap()
	{

	}

	protected DependencyMap(DependencyMap<N> deps)
	{
		for (Role r : deps.deps.keySet())  // FIXME: optimise
		{
			Map<N, Set<Role>> tmp = deps.deps.get(r);
			for (Entry<N, Set<Role>> e : tmp.entrySet())
			{
				for (Role rr : e.getValue())
				{
					addProtocolDependency(r, e.getKey(), rr);
				}
			}
		}
	}
	
	public abstract DependencyMap<N> clone();

	public void addProtocolDependency(Role self, N pn, Role target)
	{
		Map<N, Set<Role>> tmp1 = this.deps.get(self);
		if (tmp1 == null)
		{
			tmp1 = new HashMap<>();
			this.deps.put(self, tmp1);
		}
		
		Set<Role> tmp2 = tmp1.get(pn);
		if (tmp2 == null)
		{
			tmp2 = new HashSet<>();
			tmp1.put(pn, tmp2);
		}
		tmp2.add(target);
	}
	
	public Map<Role, Map<N, Set<Role>>> getDependencies()
	{
		return this.deps;
	}
}
