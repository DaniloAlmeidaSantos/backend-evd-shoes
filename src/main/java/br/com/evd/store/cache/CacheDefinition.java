package br.com.evd.store.cache;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;

import java.time.Duration;

import javax.cache.configuration.Configuration;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;

public enum CacheDefinition implements CacheConfigurable {
	SERVICE_CACHE(SERVICE_ON_MEMORY_CACHE){
		@Override
		public Configuration<Object, Object> getConfiguration() {
			return CacheFactory.createOnMemoryCache(128, 900000, 1028, 10);
		}
	};

	private final String cacheName;
	
	CacheDefinition(String cacheName) {
		this.cacheName = cacheName;
	}
	
	public String getCacheName() {
		return cacheName;
	}
	
	private static class CacheFactory {
		static Configuration<Object, Object> createOnMemoryCache(
			long memorySizeInMb, long durationInMs, long sizeObjectGraph, long sizeObjectSizeInMb){
			
			CacheConfiguration<Object, Object> configuration =
					CacheConfigurationBuilder
							.newCacheConfigurationBuilder(Object.class, Object.class,
									ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(memorySizeInMb,
											MemoryUnit.MB))
						.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(durationInMs)))
						.withSizeOfMaxObjectGraph(sizeObjectGraph)
						.withSizeOfMaxObjectSize(sizeObjectSizeInMb, MemoryUnit.MB)
						.withKeySerializingCopier()
						.withValueSerializingCopier()
						.build();
			
			return Eh107Configuration.fromEhcacheCacheConfiguration(configuration);
		}
	}
}
