package br.com.evd.store.cache;

import javax.cache.configuration.Configuration;

public interface CacheConfigurable {
	
	Configuration<Object, Object> getConfiguration();
	
}
