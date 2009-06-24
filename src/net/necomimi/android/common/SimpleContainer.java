package net.necomimi.android.common;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import net.necomimi.android.utut.R;

import android.content.Context;
import android.content.res.Resources;

public class SimpleContainer {
	private static String DELEMITER = ",";
	private static SimpleContainer container;
	private Map<String,ComponentDefinition> components;
	private Map<String,Object> instances;
	
	private SimpleContainer(Context ctx) {
		this.components = new HashMap<String,ComponentDefinition>();
		this.instances = new ConcurrentHashMap<String,Object>();
		Resources res = ctx.getResources();
		String[] comps = res.getStringArray(R.array.components);
		for (int i=0; i<comps.length; i++) {
			StringTokenizer st = new StringTokenizer(comps[i], DELEMITER);
			if (st.countTokens() == 3) {
				ComponentDefinition def = new ComponentDefinition(
						st.nextToken().trim(),
						st.nextToken().trim(),
						st.nextToken().trim());
				this.components.put(def.getId(), def);
			}
		}
	}
	
	public static SimpleContainer getContainer(Context ctx) {
		if (null == container) {
			return container = new SimpleContainer(ctx);
		}
		return container;
	}
	
	public Object getConponent(String id) throws ComponentInitializeException {
		Object comp = this.instances.get(id);
		if (null == comp) {
			return createNewInstance(id);
		}
		return comp;
	}
	
	private Object createNewInstance(String id) throws ComponentInitializeException {
		ComponentDefinition def = this.components.get(id);
		if (null == id) {
			throw new ComponentInitializeException();
		}
		
		try {
			Object instance = Class.forName(def.getClassName()).newInstance();
			this.instances.put(id, instance);
			
			return instance;
		} catch (IllegalAccessException e) {
			throw new ComponentInitializeException();
		} catch (InstantiationException e) {
			throw new ComponentInitializeException();
		} catch (ClassNotFoundException e) {
			throw new ComponentInitializeException();
		}		
	}
}
