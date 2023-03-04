package br.com.evd.store.cache;

import java.util.Arrays;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {

	private final MutableCacheEntryListenerConfiguration<Object, Object> entryListenerConfiguration = 
			new MutableCacheEntryListenerConfiguration<>(
					FactoryBuilder.factoryOf(CacheEntryLogger.class), null, true, false);
	
	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cacheManager -> Arrays.stream(CacheDefinition.values()).forEach(it -> createCache(cacheManager, it));
	}
	
	private void createCache(CacheManager cacheManager, CacheDefinition cacheDefinition) {
		Cache<Object, Object> cache = cacheManager.getCache(cacheDefinition.getCacheName());
		
		if (cache == null) { 
			cacheManager
				.createCache(cacheDefinition.getCacheName(), cacheDefinition.getConfiguration())
				.registerCacheEntryListener(entryListenerConfiguration);
			
			cacheManager.enableStatistics(cacheDefinition.getCacheName(), true);
		}
	}
	
}
