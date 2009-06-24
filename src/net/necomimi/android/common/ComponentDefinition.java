package net.necomimi.android.common;

import java.io.Serializable;

public class ComponentDefinition implements Serializable {
	private static final long serialVersionUID = 2380872956073592046L;
	private final String id;
	private final String interfaceName;
	private final String className;
	
	public ComponentDefinition(String id,
			                   String interfaceName,
			                   String className) {
		this.id = id;
		this.interfaceName = interfaceName;
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getClassName() {
		return className;
	}

}
